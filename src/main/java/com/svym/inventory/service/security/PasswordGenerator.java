package com.svym.inventory.service.security;

import java.security.SecureRandom;

public class PasswordGenerator {
	private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
	private static final int LENGTH = 10;

	public static String generateTempPassword() {
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < LENGTH; i++) {
			int index = random.nextInt(CHAR_SET.length());
			password.append(CHAR_SET.charAt(index));
		}
		//return password.toString();
        return "password"; // For testing purpose only
	}
}
