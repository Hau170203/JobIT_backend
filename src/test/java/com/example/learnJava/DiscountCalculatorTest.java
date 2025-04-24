package com.example.learnJava;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DiscountCalculatorTest {
    
    @Test
    public void testNoDiscount(){
        DiscountCalculator discountObj = new DiscountCalculator();
        double total = discountObj.calculateDiscount(50);
        assertEquals(0, total);
    }

    @Test
    public void testTenPercentDiscount(){
        DiscountCalculator discountObj = new DiscountCalculator();
        double total = discountObj.calculateDiscount(200);
        assertEquals(20, total);
    }

    @Test
    public void testTwentyPercentDiscount(){
        DiscountCalculator discountObj = new DiscountCalculator();
        double total = discountObj.calculateDiscount(600);
        assertEquals(120, total);
    }
}
