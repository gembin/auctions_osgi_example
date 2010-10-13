package org.example.tests;

import java.util.Collection;

import org.example.api.AuctionItem;
import org.example.api.AuctionService;

import bndtools.runtime.junit.OSGiTestCase;
import bndtools.runtime.junit.Operation;

public class OSGiTest extends OSGiTestCase {

	public void testOSGi() throws Exception {
		assertSvcAvail(AuctionService.class, null);
		
		Collection<AuctionItem> items = withService(AuctionService.class, null, new Operation<AuctionService, Collection<AuctionItem>>() {

			@Override
			public Collection<AuctionItem> perform(AuctionService service) throws Exception {
				return service.listItems();
			}
			
		});
		
		assertEquals(3, items.size());
	}
	
}