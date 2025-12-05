package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * Test Suite: User Login Functionality
 */
public class LoginTest extends TestBase {

    /*
     * TC-AUTH-005: Verify successful login with valid credentials
     * Expected Result: User is logged in and email is displayed in navbar
     */
    @Test(priority = 1)
    public void testValidLogin() throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-005: Testing Valid Login...");

        // ============================================
        // STEP 1: NAVIGATE TO LOGIN PAGE
        // ============================================
        homePage.clickLoginButton();
        System.out.println("✓ Navigated to login page");

        // ============================================
        // STEP 2: PERFORM LOGIN
        // ============================================
        loginPage.login(TestData.LOGIN_EMAIL, TestData.LOGIN_PASSWORD);
        Thread.sleep(3000);
        System.out.println("✓ Login credentials submitted");

        // ============================================
        // STEP 3: VERIFY SUCCESSFUL LOGIN
        // ============================================
        String actualEmail = homePage.getUserEmail();
        Assert.assertEquals(actualEmail, TestData.LOGIN_EMAIL, "Login failed!");
        System.out.println("✓ Test Passed - User logged in successfully!");
    }

    /*
     * Data Provider for incorrect password test
     * Returns valid emails with wrong passwords
     */
    @DataProvider(name = "incorrectPasswordData")
    public Object[][] getIncorrectPasswordData() {
        return new Object[][] {
                // email, password
                {TestData.LOGIN_EMAIL, "wrongpassword"},           // Wrong password
                {TestData.LOGIN_EMAIL, "12345"},                   // Different wrong password
        };
    }

    /*
     * TC-AUTH-006: Verify login fails with incorrect password
     * Expected Result: Error message "Incorrect Email or Password!"
     */
    @Test(priority = 2, dataProvider = "incorrectPasswordData")
    public void testLoginWithIncorrectPassword(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-006: Testing Login with Incorrect Password...");
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
        return new Object[][] {
                // email, password
                {"nonexistent@example.com", "123"},                // Email doesn't exist
                {"random123@yahoo.com", "123"},                    // Random email
        };
    }

    /*
     * TC-AUTH-007: Verify login fails with non-existent email
     * Expected Result: Error message "Incorrect Email or Password!"
     */
    @Test(priority = 3, dataProvider = "nonExistentEmailData")
    public void testLoginWithNonExistentEmail(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-007: Testing Login with Non-Existent Email...");
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
        return new Object[][] {
                // email, password, emptyField
                {"", "123", "Email"},                              // Empty email
                {TestData.LOGIN_EMAIL, "", "Password"},            // Empty password
                {"", "", "Both Email and Password"},               // Both empty
        };
    }

    /*
     * TC-AUTH-008: Verify login fails with empty credentials
     * Expected Result: Error message displayed
     */
    @Test(priority = 4, dataProvider = "emptyCredentialsData")
    public void testLoginWithEmptyCredentials(String email, String password, String emptyField) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-008: Testing Login with Empty " + emptyField);

        // STEP 1: NAVIGATE TO LOGIN PAGE
        homePage.clickLoginButton();
        System.out.println("✓ Navigated to login page");

        // STEP 2: ATTEMPT LOGIN WITH EMPTY CREDENTIALS
        loginPage.login(email, password);
        Thread.sleep(2000);
        System.out.println("✓ Attempted login with empty " + emptyField);

        // STEP 3: VERIFY ERROR MESSAGE
        String errorMessage = loginPage.getErrorMessage();

        Assert.assertEquals(errorMessage,TestData.ERROR_EMPTY_CREDENTIALS,
                "Expected error message not displayed for empty " + emptyField
        );

        // STEP 4: VERIFY USER IS STILL ON LOGIN PAGE
        Assert.assertTrue(loginPage.isOnLoginPage(), "User should not be logged in!");
    }
}
