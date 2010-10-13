package org.example.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.csv.CSVParser;
import org.example.api.AuctionItem;
import org.example.api.AuctionService;
import org.example.api.Bid;
import org.example.api.InvalidBidException;

public class BasicAuctionService implements AuctionService {
	
	final AtomicLong itemCounter = new AtomicLong(0);
	final Map<Long, AuctionItem> itemMap = new ConcurrentHashMap<Long, AuctionItem>();
	final Map<Long, SortedSet<Bid>> itemBidMap = new HashMap<Long, SortedSet<Bid>>();
	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public BasicAuctionService() {
		loadInitialData();
	}
	
	private void loadInitialData() {
		URL url = getClass().getResource("items.csv");
		if (url != null) {
			InputStream stream = null;
			try {
				stream = url.openStream();
				InputStreamReader reader = new InputStreamReader(stream);
				CSVParser parser = new CSVParser(reader);
				String[] line;
				while((line = parser.getLine()) != null) {
					if (line.length >= 3) {
						long startPrice = Long.parseLong(line[1]);
						AuctionItem item = new AuctionItem(itemCounter.getAndIncrement(), line[0], startPrice, dateFormat.parse(line[2]));
						itemMap.put(item.getId(), item);
						bid(item, startPrice + 1, "user");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try { if (stream != null) stream.close(); } catch (IOException e) {}
			}
		}
	}
	
	@Override
	public AuctionItem createItemListing(String description, long startPrice, Date expiry) {
		long id = itemCounter.getAndIncrement();
		
		AuctionItem item = new AuctionItem(id, description, startPrice, expiry);
		itemMap.put(id, item);
		
		return item;
	}
	
	@Override
	public Collection<AuctionItem> listItems() {
		List<AuctionItem> list = new ArrayList<AuctionItem>(itemMap.size());
		list.addAll(itemMap.values());
		return list;
	}
	
	@Override
	public synchronized Bid bid(AuctionItem item, long price, String user) throws InvalidBidException {
		if (price <= item.getStartPrice())
			throw new InvalidBidException(item, price);
		
		Bid bid = new Bid(item, price, user, new Date());
		
		synchronized (itemBidMap) {
			SortedSet<Bid> bidSet = itemBidMap.get(item.getId());
			if(bidSet == null) {
				bidSet = new TreeSet<Bid>(new BidPriceComparator(false));
				itemBidMap.put(item.getId(), bidSet);
			}
			if(!bidSet.isEmpty() && price <= bidSet.first().getPrice())
				throw new InvalidBidException(item, price);
			bidSet.add(bid);
		}
		
		return bid;
	}

	@Override
	public Collection<Bid> listBids(AuctionItem item) {
		Collection<Bid> result = Collections.emptyList();
		synchronized (itemBidMap) {
			Collection<Bid> temp = itemBidMap.get(item.getId());
			if(temp != null) {
				result = new ArrayList<Bid>(temp.size());
				result.addAll(temp);
			}
		}
		return result;
	}

}
