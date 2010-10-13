package org.example.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import junit.framework.TestCase;

import org.example.api.Bid;
import org.example.api.InvalidBidException;
import org.example.api.AuctionItem;

public class BasicAuctionServiceTest extends TestCase {
	
	private Date createDate(int year, int month, int date, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, hour, minute, second);
		return cal.getTime();
	}
	
	public void testItemListing() {
		BasicAuctionService auction = new BasicAuctionService();
		
		auction.createItemListing("Fly Fishing by J.R. Hartley (1st Edition)", 1000, createDate(2012, 12, 31, 23, 59, 59));
		
		Collection<AuctionItem> items = auction.listItems();
		assertFalse(items.isEmpty());
		assertEquals("Fly Fishing by J.R. Hartley (first edition)", items.iterator().next().getDescription());
	}
	
	public void testBidding() throws InvalidBidException {
		BasicAuctionService auction = new BasicAuctionService();
		
		AuctionItem item = auction.createItemListing("Fly Fishing by J.R. Hartley (1st Edition)", 1000, createDate(2012, 12, 31, 23, 59, 59));
		auction.bid(item, 1001, "user1");
		auction.bid(item, 1002, "user2");
		
		Collection<Bid> bids = auction.listBids(item);
		assertFalse(bids.isEmpty());
		assertEquals(1002, bids.iterator().next().getPrice());
	}
	
	public void testInvalidBidding() {
		BasicAuctionService auction = new BasicAuctionService();
		
		AuctionItem item = auction.createItemListing("Fly Fishing by J.R. Hartley (1st Edition)", 1000, createDate(2012, 12, 31, 23, 59, 59));
		try {
			auction.bid(item, 1000, "user");
			fail("Should throw InvalidBidException");
		} catch (InvalidBidException e) {
		}
		
		try {
			auction.bid(item, 1001, "user");
		} catch (InvalidBidException e1) {
			fail("Should NOT throw InvalidBidException");
		}
		
		try {
			auction.bid(item, 1001, "user");
			fail("Should throw InvalidBidException");
		} catch (InvalidBidException e) {
		}
	}
}
