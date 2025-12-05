package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * Test Suite: Checkout Process
 */
public class CheckoutTest extends TestBase{

    /*
     * Test Case: Verify user can complete checkout process
     * Precondition: User must be logged in and have items in cart
     * Expected Result: Order is placed successfully with confirmation message
     */
    @Test
    public void testCompleteCheckout() throws InterruptedException {
        System.out.println("\n▶ Starting Checkout Test...");

        // ============================================
        // PRECONDITION: LOGIN
        // ============================================
        performLogin(TestData.NEW_USER_EMAIL, TestData.NEW_USER_PASSWORD);

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        Thread.sleep(5000);
        System.out.println("✓ Navigated to cart page");

        // ============================================
        // STEP 2: PROCEED TO CHECKOUT
        // ============================================
        cartPage.clickCheckoutButton();
        Thread.sleep(5000);
        System.out.println("✓ Proceeded to checkout page");

        // ============================================
        // STEP 3: COMPLETE CHECKOUT
        // ============================================
        checkoutPage.completeCheckout(TestData.DELIVERY_ADDRESS);
        Thread.sleep(5000);
        System.out.println("✓ Checkout completed with address: " + TestData.DELIVERY_ADDRESS);

        // ============================================
        // STEP 4: VERIFY ORDER SUCCESS
        // ============================================
        String actualMessage = orderSuccessPage.getSuccessMessage();
        Assert.assertEquals(actualMessage, TestData.ORDER_SUCCESS_MESSAGE, "Order checkout failed - Wrong message displayed!");
        System.out.println("✓ Test Passed - Order placed successfully!");
    }
}
