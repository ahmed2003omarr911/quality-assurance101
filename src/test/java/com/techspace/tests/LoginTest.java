package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * Test Suite: User Login Functionality
 */
public class LoginTest extends TestBase {

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
     * TC-AUTH-005: Verify successful login with valid credentials
     * Expected Result: User is logged in and email is displayed in navbar
     */
    @Test(priority = 1, dataProvider = "loginWithValidCredentials")
    public void testValidLogin(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-005: Testing Valid Login...");

        // ============================================
        // STEP 1: NAVIGATE TO LOGIN PAGE
        // ============================================
        homePage.clickLoginButton();
        System.out.println("✓ Navigated to login page");

        // ============================================
        // STEP 2: PERFORM LOGIN
        // ============================================
        loginPage.login(email, password);
        Thread.sleep(3000);
        System.out.println("✓ Login credentials submitted");

        // ============================================
        // STEP 3: VERIFY SUCCESSFUL LOGIN
        // ============================================
        String actualEmail = homePage.getUserEmail();
        Assert.assertEquals(actualEmail, email, "Login failed!");
        System.out.println("✓ Test Passed - User logged in successfully!");
    }

    /*
     * TC-AUTH-006: Verify user remains logged in after page refresh
     * Precondition: User must be logged in
     * Expected Results:
     * 1. Token persists in localStorage after refresh
     * 2. User remains authenticated
     * 3. User email still displayed in navbar
     */
    @Test(priority = 2, dataProvider = "loginWithValidCredentials")
    public void testUserRemainsLoggedInAfterRefresh(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-006: Testing User Remains Logged In After Page Refresh...");

        // ============================================
        // PRECONDITION: LOGIN
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in successfully");

        // ============================================
        // STEP 1: VERIFY USER IS LOGGED IN BEFORE REFRESH
        // ============================================
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in!");
        String emailBeforeRefresh = homePage.getUserEmail();
        Assert.assertEquals(emailBeforeRefresh, email, "User email should be displayed!");
        System.out.println("✓ Verified user is logged in: " + emailBeforeRefresh);

        // ============================================
        // STEP 2: GET TOKEN FROM LOCAL STORAGE BEFORE REFRESH
        // ============================================
        String tokenBeforeRefresh = getLocalStorageToken();
        Assert.assertFalse(tokenBeforeRefresh.isEmpty(), "Token should exist before refresh!");
        System.out.println("✓ Token found before refresh: " + tokenBeforeRefresh.substring(0, Math.min(20, tokenBeforeRefresh.length())) + "...");

        // ============================================
        // STEP 3: REFRESH THE PAGE
        // ============================================
        driver.navigate().refresh();
        Thread.sleep(3000); // Wait for page to reload
        System.out.println("✓ Page refreshed");

        // ============================================
        // STEP 4: VERIFY TOKEN PERSISTS IN LOCAL STORAGE
        // ============================================
        String tokenAfterRefresh = getLocalStorageToken();
        Assert.assertFalse(tokenAfterRefresh.isEmpty(), "Token should persist after refresh!");
        Assert.assertEquals(tokenAfterRefresh, tokenBeforeRefresh, "Token should be the same after refresh!");
        System.out.println("✓ Token persists after refresh: " + tokenAfterRefresh.substring(0, Math.min(20, tokenAfterRefresh.length())) + "...");

        // ============================================
        // STEP 5: VERIFY USER REMAINS LOGGED IN
        // ============================================
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should remain logged in after refresh!");
        String emailAfterRefresh = homePage.getUserEmail();
        Assert.assertEquals(emailAfterRefresh, email, "User email should still be displayed!");
        System.out.println("✓ User remains logged in: " + emailAfterRefresh);

        // ============================================
        // STEP 6: VERIFY USER MENU IS STILL DISPLAYED
        // ============================================
        Assert.assertTrue(homePage.isUserMenuDisplayed(), "User menu should still be displayed!");
        System.out.println("✓ User menu is displayed");

        System.out.println("✓ Test Passed - User remains logged in after page refresh!");
    }

    /*
     * Data Provider for incorrect password test
     * Returns valid emails with wrong passwords
     */
    @DataProvider(name = "incorrectPasswordData")
    public Object[][] getIncorrectPasswordData() {
        return new Object[][]{
                // email, password
                {TestData.USER1_EMAIL, "wrongpassword"},           // Wrong password
                {TestData.USER2_EMAIL, "12345"},                   // Different wrong password
        };
    }

    /*
     * TC-AUTH-007: Verify login fails with incorrect password
     *
     * BUG REPORT:
     * Status depends on test execution - if test FAILS, this documents the bug.
     *
     * Expected Behavior: Display error "Incorrect Email or Password!" when user enters wrong password
     * Actual Behavior (if failing): No error message displayed OR incorrect error message shown
     *
     * Test Data:
     * - Valid email with "wrongpassword"
     * - Valid email with "12345"
     *
     * Priority: HIGH
     * Impact: Users cannot understand why login failed, poor user experience
     * Fix Required (if failing): Ensure backend properly returns error message and frontend displays it
     *
     * Security Note: Error message correctly doesn't distinguish between wrong email vs wrong password
     *                (prevents account enumeration attacks)
     *
     * Note: This test should PASS if the backend and frontend properly handle incorrect credentials.
     *       If it FAILS, it indicates the error message is not being displayed correctly.
     */
    @Test(priority = 3, dataProvider = "incorrectPasswordData")
    public void testLoginWithIncorrectPassword(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-007: Testing Login with Incorrect Password...");
        System.out.println("   Email: " + email + " | Password: " + password);

        // STEP 1: NAVIGATE TO LOGIN PAGE
        homePage.clickLoginButton();
        System.out.println("✓ Navigated to login page");

        // STEP 2: ATTEMPT LOGIN WITH WRONG PASSWORD
        loginPage.login(email, password);
        Thread.sleep(3000);
        System.out.println("✓ Attempted login with incorrect password");

        // STEP 3: VERIFY ERROR MESSAGE
        String errorMessage = loginPage.getErrorMessage();

        Assert.assertTrue(
                errorMessage.contains(TestData.ERROR_INCORRECT_CREDENTIALS),
                "Expected error message not displayed. Actual: " + errorMessage
        );

        // STEP 4: VERIFY USER IS STILL ON LOGIN PAGE
        Assert.assertTrue(loginPage.isOnLoginPage(), "User should not be logged in!");
    }

    /*
     * Data Provider for non-existent email test
     * Returns emails that don't exist in the database
     */
    @DataProvider(name = "nonExistentEmailData")
    public Object[][] getNonExistentEmailData() {
        return new Object[][]{
                // email, password
                {"nonexistent@example.com", "123"},                // Email doesn't exist
        };
    }

    /*
     * TC-AUTH-008: Verify login fails with non-existent email
     *
     * BUG REPORT:
     * Status depends on test execution - if test FAILS, this documents the bug.
     *
     * Expected Behavior: Display error "Incorrect Email or Password!" when user tries to login with non-existent email
     * Actual Behavior (if failing): No error message displayed OR incorrect error message shown
     *
     * Test Data:
     * - "nonexistent@example.com" (email doesn't exist in database)
     * - "random123@yahoo.com" (random email)
     *
     * Priority: HIGH
     * Impact: Users who mistype their email or don't have an account get no feedback
     * Fix Required (if failing): Ensure backend properly returns error message and frontend displays it
     *
     * Security Note: Error message correctly doesn't distinguish between wrong email vs wrong password
     *                (prevents account enumeration attacks - attackers can't discover valid emails)
     *
     * Note: This test should PASS if the backend and frontend properly handle non-existent accounts.
     *       If it FAILS, it indicates the error message is not being displayed correctly.
     */
    @Test(priority = 4, dataProvider = "nonExistentEmailData")
    public void testLoginWithNonExistentEmail(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-008: Testing Login with Non-Existent Email...");
        System.out.println("   Email: " + email);

        // STEP 1: NAVIGATE TO LOGIN PAGE
        homePage.clickLoginButton();
        System.out.println("✓ Navigated to login page");

        // STEP 2: ATTEMPT LOGIN WITH NON-EXISTENT EMAIL
        loginPage.login(email, password);
        Thread.sleep(3000);
        System.out.println("✓ Attempted login with non-existent email");

        // STEP 3: VERIFY ERROR MESSAGE
        String errorMessage = loginPage.getErrorMessage();

        Assert.assertTrue(
                errorMessage.contains(TestData.ERROR_INCORRECT_CREDENTIALS),
                "Expected error message not displayed. Actual: " + errorMessage
        );

        // STEP 4: VERIFY USER IS STILL ON LOGIN PAGE
        Assert.assertTrue(loginPage.isOnLoginPage(), "User should not be logged in!");
    }

    /*
     * Data Provider for empty credentials test
     * Returns test data with empty email or password
     */
    @DataProvider(name = "emptyCredentialsData")
    public Object[][] getEmptyCredentialsData() {
        return new Object[][]{
                // email, password, emptyField
                {"", "123", "Email"},                              // Empty email
                {TestData.USER1_EMAIL, "", "Password"},            // Empty password
                {"", "", "Both Email and Password"},               // Both empty
        };
    }

    /*
     * TC-AUTH-009: Verify login fails with empty credentials
     * Expected Result: Error message displayed
     */
    @Test(priority = 5, dataProvider = "emptyCredentialsData")
    public void testLoginWithEmptyCredentials(String email, String password, String emptyField) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-009: Testing Login with Empty " + emptyField);

        // STEP 1: NAVIGATE TO LOGIN PAGE
        homePage.clickLoginButton();
        System.out.println("✓ Navigated to login page");

        // STEP 2: ATTEMPT LOGIN WITH EMPTY CREDENTIALS
        loginPage.login(email, password);
        Thread.sleep(2000);
        System.out.println("✓ Attempted login with empty " + emptyField);

        // STEP 3: VERIFY ERROR MESSAGE
        String errorMessage = loginPage.getErrorMessage();

        Assert.assertEquals(errorMessage, TestData.ERROR_EMPTY_CREDENTIALS,
                "Expected error message not displayed for empty " + emptyField
        );

        // STEP 4: VERIFY USER IS STILL ON LOGIN PAGE
        Assert.assertTrue(loginPage.isOnLoginPage(), "User should not be logged in!");
    }
}
