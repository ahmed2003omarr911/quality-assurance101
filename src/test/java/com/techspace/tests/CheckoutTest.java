package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * Test Suite: Checkout Process
 */
public class CheckoutTest extends TestBase {

    /*
     * Data Provider for Login with valid credentials
     * Returns test valid data for login
     */
    @DataProvider(name = "loginWithValidCredentialsWithAddressAndSuccessMessage")
    public Object[][] getLoginValidCredentials() {
        return new Object[][]{
                // email, password, address, orderSuccessMessage
                {TestData.USER7_EMAIL, TestData.USER7_PASSWORD, TestData.DELIVERY_ADDRESS, TestData.ORDER_SUCCESS_MESSAGE},
        };
    }

    /*
     * TC-CHECK-001: Verify user can complete checkout process
     * Precondition: User must be logged in and have items in cart
     * Expected Result: Order is placed successfully with confirmation message
     */
    @Test(priority = 1, dataProvider = "loginWithValidCredentialsWithAddressAndSuccessMessage")
    public void testCompleteCheckout(String email, String password, String address, String orderSuccessMessage) throws InterruptedException {
        System.out.println("\n▶ Starting Checkout Test...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in");

        homePage.addProductToCart();
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        waitForPageToLoad();
        System.out.println("✓ Navigated to cart page");

        // ============================================
        // STEP 2: PROCEED TO CHECKOUT
        // ============================================
        cartPage.clickCheckoutButton();
        waitForPageToLoad();
        System.out.println("✓ Proceeded to checkout page");

        // ============================================
        // STEP 3: COMPLETE CHECKOUT
        // ============================================
        checkoutPage.completeCheckout(address);
        System.out.println("✓ Checkout completed with address: " + address);

        // ============================================
        // STEP 4: VERIFY ORDER SUCCESS
        // ============================================
        String actualMessage = orderSuccessPage.getSuccessMessage();
        Assert.assertEquals(actualMessage, orderSuccessMessage, "Order checkout failed - Wrong message displayed!");
        System.out.println("✓ Test Passed - Order placed successfully!");
    }
}
