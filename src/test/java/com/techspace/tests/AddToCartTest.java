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

    /*
     * TC-CART-003: Verify adding product requires authentication
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
    @Test
    public void testAddToCartRequiresAuthentication() throws InterruptedException {
        System.out.println("\n▶ TC-CART-003: Testing Add to Cart Requires Authentication...");
        System.out.println("⚠️  NOTE: This test is EXPECTED TO FAIL - Documents a UI bug");

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
                "🐛 BUG CONFIRMED: Should redirect to login page when unauthenticated user tries to add to cart!\n" +
                        "   Expected: URL should contain '/login'\n" +
                        "   Actual URL: " + urlAfterClick + "\n" +
                        "   Previous URL: " + urlBeforeClick + "\n" +
                        "   Fix Required: Implement redirect or show error message in UI"
        );

        System.out.println("✓ Test Passed - User redirected to login page");
    }
}
