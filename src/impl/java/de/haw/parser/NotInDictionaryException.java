package de.haw.parser;

public class NotInDictionaryException extends Exception {

	private static final long serialVersionUID = 9172656023803663995L;

	public NotInDictionaryException(String msg) {
		super(msg);
	}
	
}
