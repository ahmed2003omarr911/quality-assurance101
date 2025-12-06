package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * Test Suite: Shopping Cart Functionality
 */
public class AddToCartTest extends TestBase {

    /*
     * Data Provider for Adding a product to the cart
     * Returns test valid data for login and product name
     */
    @DataProvider(name = "addingProductToCart")
    public Object[][] getLoginValidCredentials() {
        return new Object[][]{
                // email, password
                {TestData.USER1_EMAIL, TestData.USER1_PASSWORD, TestData.PRODUCT_NAME},
                {TestData.USER2_EMAIL, TestData.USER2_PASSWORD, TestData.PRODUCT_NAME},
        };
    }

    /*
     * TC-CART-001: Verify user can add a product to their cart
     * Precondition: User must be logged in
     * Expected Result: Product appears in cart with correct name
     */
    @Test(priority = 1, dataProvider = "addingProductToCart")
    public void testAddProductToCartTest(String email, String password, String productName) throws InterruptedException {
        System.out.println("\n▶ TC-CART-001: Testing Add to Cart...");

        // ============================================
        // PRECONDITION: LOGIN
        // ============================================
        performLogin(email, password);

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
        Assert.assertEquals(actualProductName, productName, "Wrong product in cart!");
        System.out.println("✓ Test Passed - Product verified in cart: " + actualProductName);
    }

    /*
     * TC-CART-002: Verify adding a product requires authentication
     *
     * BUG REPORT:
     * Currently FAILING - Application does not redirect unauthenticated users to login page.
     * Instead, it silently fails with console error only.
     *
     * Expected Behavior: Unauthenticated user should be redirected to /login when clicking "Add to Cart"
     * Actual Behavior: Nothing happens in UI, only console error appears
     *
     * Priority: HIGH
     * Impact: Users don't understand why they can't add items to cart
     * Fix Required: Add UI redirect or error message for unauthenticated add-to-cart attempts
     */
    @Test(priority = 2)
    public void testAddToCartRequiresAuthentication() throws InterruptedException {
        System.out.println("\n▶ TC-CART-002: Testing Add to Cart Requires Authentication...");
        System.out.println("NOTE: This test is EXPECTED TO FAIL - Documents a UI bug");

        // ============================================
        // STEP 1: VERIFY USER IS NOT LOGGED IN
        // ============================================
        Assert.assertTrue(homePage.isLoginButtonDisplayed(),
                "User should not be logged in at start!");
        System.out.println("✓ Verified user is not authenticated");

        // ============================================
        // STEP 2: GET CURRENT URL BEFORE CLICKING ADD TO CART
        // ============================================
        String urlBeforeClick = driver.getCurrentUrl();
        System.out.println("✓ Current URL before action: " + urlBeforeClick);

        // ============================================
        // STEP 3: ATTEMPT TO ADD PRODUCT TO CART WITHOUT LOGIN
        // ============================================
        homePage.addProductToCart();
        Thread.sleep(3000); // Wait for any response/redirect
        System.out.println("✓ Clicked 'Add to Cart' button while unauthenticated");

        // ============================================
        // STEP 4: VERIFY REDIRECT TO LOGIN PAGE
        // ============================================
        String urlAfterClick = driver.getCurrentUrl();
        System.out.println("✓ Current URL after action: " + urlAfterClick);

        boolean redirectedToLogin = urlAfterClick.contains("/login");

        Assert.assertTrue(
                redirectedToLogin,
                "\nBUG CONFIRMED: Should redirect to login page when unauthenticated user tries to add to cart!\n" +
                        " Expected: URL should contain '/login'\n" +
                        " Actual URL: " + urlAfterClick + "\n" +
                        " Previous URL: " + urlBeforeClick + "\n" +
                        " Fix Required: Implement redirect or show error message in UI"
        );

        System.out.println("✓ Test Passed - User redirected to login page");
    }
}
