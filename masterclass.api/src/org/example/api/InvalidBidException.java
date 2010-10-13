package org.example.api;

import java.text.MessageFormat;

public class InvalidBidException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final AuctionItem item;
	private final long price;
	
	public InvalidBidException(AuctionItem item, long price) {
		this.item = item;
		this.price = price;
	}
	
	@Override
	public String getMessage() {
		return MessageFormat.format("Invalid bid at price {0} for item ID {1} (\"{2}\").", price, item.getId(), item.getDescription());
	}

}
