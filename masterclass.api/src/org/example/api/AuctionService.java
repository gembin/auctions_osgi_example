package org.example.api;

import java.util.Collection;
import java.util.Date;

public interface AuctionService {
	
	/**
	 * Create a new Item listing.
	 * 
	 * @param description
	 * @param startPrice
	 * @param expiry
	 */
	AuctionItem createItemListing(String description, long startPrice, Date expiry);
	
	/**
	 * Get all items.
	 */
	Collection<AuctionItem> listItems();

	/**
	 * Bid on an Item
	 * 
	 * @param item
	 * @param price
	 * @param user
	 * 
	 * @throws InvalidBidException If the bid is invalid, e.g. not greater than an existing bid. 
	 */
	Bid bid(AuctionItem item, long price, String user) throws InvalidBidException;

	/**
	 * Get the bids related to the specified Item. An iterator over the
	 * resulting collection must return the bids in descending price, so the
	 * current highest bid &ndash; assuming there have been more than zero bids
	 * &ndash; can be obtained by <code>result.iterator().next()</code>.
	 * 
	 * @param item
	 */
	Collection<Bid> listBids(AuctionItem item);
}
