package com.virtualpairprogrammers.isbntools;

public class ValidateISBN {

	private static final int LONG_ISBN_MULTIPLIER = 10;
	private static final int SHORT_ISBN_MULTIPLIER = 11;
	private static final int LONG_ISBN_LENGTH = 13;
	private static final int SHORT_ISBN_LENGTH = 10;

	public boolean checkISBN(String isbn) {
		int total = 0;
		if(isbn.length() == SHORT_ISBN_LENGTH) {
			return isThisAValidShortISBN(isbn, total);
		} else if (isbn.length() == LONG_ISBN_LENGTH) {
			return isThisAValidLongISBN(isbn, total);
		} else {
			throw new NumberFormatException("ISBN numbers must be 10 or 13 digits long");
		}
	}

	private boolean isThisAValidShortISBN(String isbn, int total) {
		for(int i = 0; i < SHORT_ISBN_LENGTH; i++) {
			if(!Character.isDigit(isbn.charAt(i))) {
				if(i == 9 && isbn.charAt(i) == 'X') {
					total += 10;
				} else {
					throw new NumberFormatException("10-digit ISBN numbers can only contain numeric digits, or end with X as the only non-numeric digit");
				}
			} else
				total += (isbn.charAt(i) - '0') * (SHORT_ISBN_LENGTH - i);
		}
		return (total % SHORT_ISBN_MULTIPLIER == 0);
	}

	private boolean isThisAValidLongISBN(String isbn, int total) {
		for(int i = 0; i < LONG_ISBN_LENGTH; i++) {
			if(!Character.isDigit(isbn.charAt(i))) {
				throw new NumberFormatException("13-digit ISBN numbers can only contain numeric digits");
			}
			total += (isbn.charAt(i) - '0') * ((i % 2 == 0) ? 1 : 3);
		}
		return (total % LONG_ISBN_MULTIPLIER == 0);
	}

}
