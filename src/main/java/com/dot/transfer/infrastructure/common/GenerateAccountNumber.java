package com.dot.transfer.infrastructure.common;

import java.util.Random;

public class GenerateAccountNumber {

    public static  String generateRandomNumberWithSuffix(String suffix) {
        int totalLength = 10;
        int randomNumberLength = totalLength - suffix.length();

        if (randomNumberLength <= 0) {
            throw new IllegalArgumentException("Suffix length is greater than or equal to the total length");
        }

        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder();

        for (int i = 0; i < randomNumberLength; i++) {
            randomNumber.append(random.nextInt(10)); // Generate a random digit (0-9)
        }

        return randomNumber.toString() + suffix;
    }
}
