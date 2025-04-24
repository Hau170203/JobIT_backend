package com.example.learnJava;

public class DiscountCalculator {
    public double calculateDiscount(double totalAmount) {
        if (totalAmount < 100) {
            return 0;
        } else if (totalAmount < 500 ) {
            return totalAmount * 0.1; // 10% discount
        } else {
            return totalAmount * 0.2; // 30% discount
        }
    }
}
