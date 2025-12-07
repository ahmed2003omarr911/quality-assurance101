package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * Test Suite: Orders Page Functionality
 */
public class OrdersTest extends TestBase {

    /*
     * Data Provider for Login with valid credentials
     * Returns test valid data for login
     */
    @DataProvider(name = "loginWithValidCredentialsWithOrdersPageHeading")
    public Object[][] getLoginValidCredentials() {
        return new Object[][]{
                // email, password, ordersPageHeading
                {TestData.USER5_EMAIL, TestData.USER5_PASSWORD, TestData.ORDERS_PAGE_HEADING},
        };
    }

    /*
     * TC-ORDERS-001: Verify user can navigate to Orders page
     * Precondition: User must be logged in
     * Expected Result: Orders page is displayed with correct heading
     */
    @Test(priority = 1, dataProvider = "loginWithValidCredentialsWithOrdersPageHeading")
    public void testNavigateToOrdersPage(String email, String password, String ordersPageHeading) throws InterruptedException {
        System.out.println("\n▶ Starting Orders Page Test...");

        // ============================================
        // PRECONDITION: LOGIN
        // ============================================
        performLogin(email, password);

        // ============================================
        // STEP 1: OPEN USER MENU
        // ============================================
        homePage.clickUserMenu();
        System.out.println("✓ User menu opened");

        // ============================================
        // STEP 2: NAVIGATE TO ORDERS PAGE
        // ============================================
        homePage.navToMyOrdersPage();
        waitForPageToLoad();
        System.out.println("✓ Navigated to orders page");

        // ============================================
        // STEP 3: VERIFY ORDERS PAGE DISPLAYED
        // ============================================
        String actualHeading = ordersPage.getPageHeading();
        Assert.assertEquals(actualHeading, ordersPageHeading, "Orders page navigation failed - Wrong page displayed!");
        System.out.println("✓ Test Passed - Orders page verified with heading: " + actualHeading);
    }

    /*
     * TC-ORDERS-002: Verify orders are displayed on Orders page
     * Precondition: User must be logged in and have previous orders
     * Expected Result: At least one order is displayed
     */
    @Test(priority = 2, dataProvider = "loginWithValidCredentialsWithOrdersPageHeading")
    public void testOrdersAreDisplayed(String email, String password, String ordersPageHeading) throws InterruptedException {
        System.out.println("\n▶ Starting Orders Display Test...");

        // PRECONDITION: LOGIN
        performLogin(email, password);

        // NAVIGATE TO ORDERS PAGE
        homePage.clickUserMenu();
        homePage.navToMyOrdersPage();

        // VERIFY ORDERS ARE DISPLAYED
        int numberOfOrders = ordersPage.calculateNumberOfOrders();
        Assert.assertTrue(numberOfOrders > 0, "No orders found on the page!");
        System.out.println("✓ Test Passed - Found " + numberOfOrders + " orders!");
    }
}
