package com.gesdoc.grudexample.exception;

public class UsernameOrIdNotFound extends Exception {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7431067336177434281L;

	public UsernameOrIdNotFound () {
		super("Usuario o id no encontrado");
	}

	public UsernameOrIdNotFound (String message) {
		super(message);
	}
}
