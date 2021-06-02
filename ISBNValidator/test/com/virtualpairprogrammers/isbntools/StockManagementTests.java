package com.virtualpairprogrammers.isbntools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

class StockManagementTests {

	ExternalISBNDataService testWebService;
	ExternalISBNDataService testDatabaseService;
	StockManager stockManager;
	
	@BeforeEach
	public void setup() {
		testWebService = Mockito.mock(ExternalISBNDataService.class);
		testDatabaseService = Mockito.mock(ExternalISBNDataService.class);
		stockManager = new StockManager();
		stockManager.setWebService(testWebService);
		stockManager.setDatabaseService(testDatabaseService);
	}
	
	@Test
	void testCanGetACorrectLocatorCode() {
		
		/* Without using Mockito -- Current implementation below using Mockito
		 * Note: You can uncomment this block and comment out the block where
		 * the stub implementation is using mock() to have the stub implementation
		 * without using Mockito. Also note that appropriate changes need to be
		 * made in setup() method as well. */
		
		/*
		ExternalISBNDataService testWebService = new ExternalISBNDataService() {

			@Override
			public Book lookup(String isbn) {
				return new Book(isbn, "Of Mice And Men", "J. Steinbeck");
			}
			
		};
		
		ExternalISBNDataService testDatabaseService = new ExternalISBNDataService() {

			@Override
			public Book lookup(String isbn) {
				return null;
			}
			
		};
		*/
		
		/* Stub implementation using Mockito -- begins here */
		
		Mockito.when(testDatabaseService.lookup(Mockito.anyString())).thenReturn(null);
		Mockito.when(testWebService.lookup("0140177396")).thenReturn(new Book("0140177396", "Of Mice And Men", "J. Steinbeck"));
		
		/* Stub implementation using Mockito -- ends here */
		
		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		assertEquals("7396J4", locatorCode);
	}
	
	@Test
	void databaseIsUsedIfDataIsPresent() {
		
		Mockito.when(testDatabaseService.lookup("0140177396")).thenReturn(new Book("0140177396", "abc", "abc"));
		
		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		
		Mockito.verify(testDatabaseService).lookup("0140177396");
		Mockito.verify(testWebService, Mockito.never()).lookup(Mockito.anyString());
	}

	@Test
	void webServiceIsUsedIfDataIsNotPresentInDatabase() {
		
		Mockito.when(testDatabaseService.lookup("0140177396")).thenReturn(null);
		Mockito.when(testWebService.lookup("0140177396")).thenReturn(new Book("0140177396", "abc", "abc"));
		
		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		
		Mockito.verify(testDatabaseService).lookup("0140177396"); // Mockito.verify(testDatabaseService, Mockito.times(1)).lookup("0140177396");
		Mockito.verify(testWebService).lookup("0140177396");
	}
}
