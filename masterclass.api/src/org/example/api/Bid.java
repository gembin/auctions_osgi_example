package org.example.api;

import java.io.Serializable;
import java.util.Date;

/**
 * A bid on an auction item.
 * 
 * This class is designed to support being passed by value over a wire.
 * Different distribution technologies place different requirements. For
 * example:
 * <ul>
 * <li>Apache CXF's SOAP mapping requires a zero-arg constructor and
 * JavaBeans-style getters and setters.</li>
 * <li>ECF's "Generic" provider uses Java serialization, so requires the
 * {@link Serializable} interface to be implemented.</li>
 * </ul>
 * 
 * @author Neil Bartlett
 */
public class Bid implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private AuctionItem item;
	private long price;
	private String user;
	private Date date;
	
	public Bid() {}

	public Bid(AuctionItem item, long price, String user, Date date) {
		this.item = item;
		this.price = price;
		this.user = user;
		this.date = date;
	}

	public AuctionItem getItem() {
		return item;
	}

	public void setItem(AuctionItem item) {
		this.item = item;
	}
	
	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}


}
