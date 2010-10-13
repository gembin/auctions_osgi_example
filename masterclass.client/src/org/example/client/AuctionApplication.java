package org.example.client;

import java.util.Map;

import org.example.api.AuctionItem;
import org.example.api.AuctionService;
import org.example.api.Bid;

import aQute.bnd.annotation.component.Reference;

import com.vaadin.Application;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AuctionApplication extends Application {
	
	private static final long serialVersionUID = 1L;
	
	private static final String PID_NAME = "name";
	private static final String PID_AUCTION_REF = "auctionRef";
	
	private final Container auctions;
	private final BeanItemContainer<AuctionItem> items = new BeanItemContainer<AuctionItem>(AuctionItem.class);
	private final BeanItemContainer<Bid> bids = new BeanItemContainer<Bid>(Bid.class);
	
	private Table auctionsTable;
	private Table itemsTable;
	private Table bidsTable;
	private TextField bidEntryField;
	private Button bidButton;

	private AuctionService selectedAuctionService = null;
	private AuctionItem selectedItem = null;
	
	public AuctionApplication() {
		auctions = new IndexedContainer();
		auctions.addContainerProperty(PID_NAME, String.class, null);
		auctions.addContainerProperty(PID_AUCTION_REF, AuctionService.class, null);
	}
	
	@Override
	public synchronized void init() {
		Window window = new Window("Auction Demo Application");
		setMainWindow(window);
		
		// Create Controls
		auctionsTable = new Table("Auctions", auctions);
		auctionsTable.setVisibleColumns(new Object[] { PID_NAME });
		auctionsTable.setColumnHeader(PID_NAME, "Auction");
		auctionsTable.setSelectable(true);
		auctionsTable.setImmediate(true);
		
		ProgressIndicator progress = new ProgressIndicator();
		progress.setCaption("Polling...");
		progress.setIndeterminate(true);
		progress.setPollingInterval(3000);
		
		itemsTable = new Table("Listed Items", items);
		itemsTable.setSelectable(true);
		itemsTable.setImmediate(true);
		
		bidsTable = new Table("Bids", bids);
		bidsTable.setVisibleColumns(new Object[] { "price", "user", "date" });
		
		bidEntryField = new TextField();
		bidEntryField.addValidator(new IntegerValidator("Bid value must be a number!"));
		bidEntryField.setEnabled(false);
		bidButton = new Button("Bid!");
		bidButton.setEnabled(false);
		
		// Listeners
		auctionsTable.addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Object itemId = auctionsTable.getValue();
				Item selection = auctionsTable.getItem(itemId);
				setSelectedAuctionService(selection != null
					? (AuctionService) selection.getItemProperty(PID_AUCTION_REF).getValue()
					: null);
			}
		});
		itemsTable.addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent arg0) {
				BeanItem<AuctionItem> beanItem = items.getItem(itemsTable.getValue());
				setSelectedItem(beanItem != null ? beanItem.getBean() : null);
			}
		});
		bidButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent ev) {
				Object value = bidEntryField.getValue();
				System.out.println("value: " + value);
			}
		});
		
		// Layout
		HorizontalLayout root = new HorizontalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		window.setContent(root);
		
		VerticalLayout auctionListPanel = new VerticalLayout();
		auctionListPanel.setSpacing(true);
		auctionListPanel.addComponent(auctionsTable);
		auctionListPanel.addComponent(progress);
		
		VerticalLayout bidPanel = new VerticalLayout();
		bidPanel.setSpacing(true);
		bidPanel.addComponent(bidsTable);
		bidsTable.setSizeFull();
		
		HorizontalLayout bidEntryPanel = new HorizontalLayout();
		bidEntryPanel.setSpacing(true);
		bidEntryPanel.addComponent(bidEntryField);
		bidEntryPanel.addComponent(bidButton);
		bidPanel.addComponent(bidEntryPanel);
		
		root.addComponent(auctionListPanel);
		root.addComponent(itemsTable);
		root.addComponent(bidPanel);
	}
	
	protected void setSelectedAuctionService(AuctionService newAuction) {
		AuctionService old = selectedAuctionService;
		selectedAuctionService = newAuction;
		
		if (old != selectedAuctionService) {
			items.removeAllItems();
			if(selectedAuctionService != null) {
				for (AuctionItem auctionItem : selectedAuctionService.listItems()) {
					items.addBean(auctionItem);
				}
			}
			setSelectedItem(null);
		}
	}

	protected void setSelectedItem(AuctionItem newItem) {
		AuctionItem old = selectedItem;
		selectedItem = newItem;
		
		if (old != selectedItem) {
			bidsTable.removeAllItems();
			if(selectedItem != null) {
				for (Bid bid : selectedAuctionService.listBids(selectedItem)) {
					bids.addBean(bid);
				}
			}
			
			bidEntryField.setEnabled(selectedItem != null);
			bidButton.setEnabled(selectedItem != null);
		}
	}

	protected void addAuctionService(AuctionService auction, Map<String, Object> properties) {
		String label = "UNKNOWN";
		
		Object labelObj = properties.get("label");
		if (labelObj != null && labelObj instanceof String)
			label = (String) labelObj;
		
		synchronized (this) {
			Item item = auctions.addItem(System.identityHashCode(auction));
			item.getItemProperty(PID_AUCTION_REF).setValue(auction);
			item.getItemProperty(PID_NAME).setValue(label);
		}
	}
	
	protected synchronized void removeAuctionService(AuctionService auction) {
		int itemId = System.identityHashCode(auction);
		auctions.removeItem(itemId);
		
		Object value = auctionsTable.getValue();
		if (value != null && itemId == (Integer) value) {
			auctionsTable.setValue(null);
		}
	}
	
}
