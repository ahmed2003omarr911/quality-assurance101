package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * Test Suite: Shopping Cart Functionality
 */
public class AddToCartTest extends TestBase {

    /*
     * Test Case: Verify user can add a product to  their cart
     * Precondition: User must be logged in
     * Expected Result: Product appears in cart with correct name
     */
    @Test
    public void testAddProductToCartTest() throws InterruptedException {
        System.out.println("\n▶ Starting Add to Cart Test...");

        // ============================================
        // PRECONDITION: LOGIN
        // ============================================
        performLogin(TestData.NEW_USER_EMAIL, TestData.NEW_USER_PASSWORD);

        // ============================================
        // STEP 1: ADD PRODUCT TO CART
        // ============================================
        homePage.addProductToCart();
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 2: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        Thread.sleep(3000);
        System.out.println("✓ Navigated to cart page");

        // ============================================
        // STEP 3: VERIFY PRODUCT IN CART
        // ============================================
        String actualProductName = cartPage.getProductTitle();
        Assert.assertEquals(actualProductName, TestData.PRODUCT_NAME, "Wrong product in cart!");
        System.out.println("✓ Test Passed - Product verified in cart: " + actualProductName);
    }
}
