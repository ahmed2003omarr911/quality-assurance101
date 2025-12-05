package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * Test Suite: Orders Page Functionality
 */
public class OrdersTest extends TestBase {

    /*
     * Test Case: Verify user can navigate to Orders page
     * Precondition: User must be logged in
     * Expected Result: Orders page is displayed with correct heading
     */
    @Test
    public void testNavigateToOrdersPage() throws InterruptedException {
        System.out.println("\n▶ Starting Orders Page Test...");

        // ============================================
        // PRECONDITION: LOGIN
        // ============================================
        performLogin(TestData.NEW_USER_EMAIL, TestData.NEW_USER_PASSWORD);

        // ============================================
        // STEP 1: OPEN USER MENU
        // ============================================
        homePage.clickUserMenu();
        Thread.sleep(3000);
        System.out.println("✓ User menu opened");

        // ============================================
        // STEP 2: NAVIGATE TO ORDERS PAGE
        // ============================================
        homePage.navToMyOrdersPage();
        Thread.sleep(3000);
        System.out.println("✓ Navigated to orders page");

        // ============================================
        // STEP 3: VERIFY ORDERS PAGE DISPLAYED
        // ============================================
        String actualHeading = ordersPage.getPageHeading();
        Assert.assertEquals(actualHeading, TestData.ORDERS_PAGE_HEADING, "Orders page navigation failed - Wrong page displayed!");
        System.out.println("✓ Test Passed - Orders page verified with heading: " + actualHeading);
    }

    /*
     * Test Case: Verify orders are displayed on Orders page
     * Precondition: User must be logged in and have previous orders
     * Expected Result: At least one order is displayed
     */
    @Test
    public void testOrdersAreDisplayed() throws InterruptedException {
        System.out.println("\n▶ Starting Orders Display Test...");

        // PRECONDITION: LOGIN
        performLogin(TestData.NEW_USER_EMAIL, TestData.NEW_USER_PASSWORD);

        // NAVIGATE TO ORDERS PAGE
        homePage.clickUserMenu();
        Thread.sleep(5000);
        homePage.navToMyOrdersPage();
        Thread.sleep(5000);

        // VERIFY ORDERS ARE DISPLAYED
        int numberOfOrders = ordersPage.calculateNumberOfOrders();
        Assert.assertTrue(numberOfOrders > 0, "No orders found on the page!");
        System.out.println("✓ Test Passed - Found " + numberOfOrders + " orders!");
    }
}
