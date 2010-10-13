package org.example.service;

import java.util.Comparator;

import org.example.api.Bid;

class BidPriceComparator implements Comparator<Bid> {
	
	private final boolean ascending;

	BidPriceComparator(boolean ascending) {
		this.ascending = ascending;
	}
	
	@Override
	public int compare(Bid o1, Bid o2) {
		long diff = ascending ? o1.getPrice() - o2.getPrice() : o2.getPrice() - o1.getPrice();
		return (int) diff;
	}

}
