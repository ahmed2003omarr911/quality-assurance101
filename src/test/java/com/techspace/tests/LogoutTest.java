package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * Test Suite: User Logout Functionality
 * Verifies that users can successfully log out and session is cleared
 */
public class LogoutTest extends TestBase {

    /*
     * Data Provider for Login with valid credentials
     * Returns test valid data
     */
    @DataProvider(name = "loginWithValidCredentials")
    public Object[][] getLoginValidCredentials() {
        return new Object[][]{
                // email, password
                {TestData.USER1_EMAIL, TestData.USER1_PASSWORD},
                {TestData.USER2_EMAIL, TestData.USER2_PASSWORD},
        };
    }

    /*
     * TC-AUTH-010: Verify user can log out successfully
     * Precondition: User must be logged in
     * Expected Results:
     * 1. Token removed from localStorage
     * 2. User redirected to home page
     * 3. Navbar shows "Login" button instead of user menu
     */
    @Test(priority = 1, dataProvider = "loginWithValidCredentials")
    public void testSuccessfulLogout(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-010: Testing Successful Logout...");

        // ============================================
        // PRECONDITION: LOGIN
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in successfully");

        // ============================================
        // STEP 1: VERIFY USER IS LOGGED IN
        // ============================================
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in before logout!");
        Assert.assertTrue(homePage.isUserMenuDisplayed(), "User menu should be displayed!");
        System.out.println("✓ Verified user is logged in");

        // ============================================
        // STEP 2: CHECK TOKEN EXISTS IN LOCAL STORAGE (BEFORE LOGOUT)
        // ============================================
        String tokenBeforeLogout = getLocalStorageToken();
        Assert.assertFalse(tokenBeforeLogout.isEmpty(), "Token should exist before logout!");
        System.out.println("✓ Token found in localStorage before logout: " + tokenBeforeLogout.substring(0, Math.min(20, tokenBeforeLogout.length())) + "...");

        // ============================================
        // STEP 3: PERFORM LOGOUT
        // ============================================
        homePage.clickUserMenu();
        System.out.println("✓ User menu opened");

        homePage.clickLogoutButton();
        System.out.println("✓ Logout button clicked");

        // ============================================
        // STEP 4: VERIFY TOKEN REMOVED FROM LOCAL STORAGE
        // ============================================
        String tokenAfterLogout = getLocalStorageToken();
        Assert.assertTrue(tokenAfterLogout.isEmpty(),
                "Token should be removed from localStorage after logout!");
        System.out.println("✓ Token removed from localStorage");

        // ============================================
        // STEP 5: VERIFY LOGIN BUTTON IS DISPLAYED
        // ============================================
        Assert.assertTrue(homePage.isLoginButtonDisplayed(), "Login button should be displayed after logout!");
        System.out.println("✓ Login button is displayed in navbar");

        System.out.println("✓ Test Passed - User logged out successfully!");
    }

    /*
     * TC-AUTH-011: Verify user cannot access protected pages after logout
     * Tests that logout prevents access to pages requiring authentication
     */
    @Test(priority = 2, dataProvider = "loginWithValidCredentials")
    public void testProtectedPagesAfterLogout(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-011: Testing Protected Pages After Logout...");

        // LOGIN
        performLogin(email, password);
        System.out.println("✓ User logged in");

        // LOGOUT
        homePage.clickUserMenu();
        homePage.clickLogoutButton();
        System.out.println("✓ User logged out");

        // TRY TO ACCESS CART
        homePage.clickCartIcon();

        // VERIFY REDIRECT TO LOGIN PAGE
        String currentUrl = driver.getCurrentUrl();
        boolean redirectedToLogin = currentUrl.contains("/login");

        Assert.assertTrue(
                redirectedToLogin,
                "Should not access cart after logout! Current URL: " + currentUrl
        );

        System.out.println("✓ Test Passed - Cannot access protected pages after logout!");
    }
}
