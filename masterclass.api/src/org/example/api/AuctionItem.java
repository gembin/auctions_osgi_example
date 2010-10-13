package org.example.api;

import java.io.Serializable;
import java.util.Date;

/**
 * An item offered for sale at auction.
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
public class AuctionItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String description;
	private long startPrice;
	private Date expiry;

	public AuctionItem() {
	}

	public AuctionItem(long id, String description, long startPrice, Date expiry) {
		this.id = id;
		this.description = description;
		this.startPrice = startPrice;
		this.expiry = expiry;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(long startPrice) {
		this.startPrice = startPrice;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

}
