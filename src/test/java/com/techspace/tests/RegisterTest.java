package com.techspace.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * Test Suite: User Registration Functionality
 */
public class RegisterTest extends TestBase {

    /*
     * Data Provider for registration with valid credentials
     * Returns test valid data
     */
    @DataProvider(name = "registrationValidCredentials")
    public Object[][] getRegistrationValidCredentials() {
        return new Object[][]{
                // firstName, lastName, email, password
                {TestData.NEW_USER_FIRST_NAME, TestData.NEW_USER_LAST_NAME, TestData.NEW_USER_EMAIL, TestData.NEW_USER_PASSWORD},
                {TestData.NEW_USER2_FIRST_NAME, TestData.NEW_USER2_LAST_NAME, TestData.NEW_USER2_EMAIL, TestData.NEW_USER2_PASSWORD},
        };
    }

    /*
     * TC-AUTH-001: Verify successful user registration with valid credentials
     * Expected Result: User is registered and automatically logged in
     */
    @Test(priority = 1, dataProvider = "registrationValidCredentials")
    public void testValidRegistration(String firstName, String lastName, String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-001: Testing Valid Registration...");

        // ============================================
        // STEP 1: NAVIGATE TO REGISTRATION PAGE
        // ============================================
        homePage.clickLoginButton();
        System.out.println("✓ Navigated to login page");

        loginPage.clickRegisterLink();
        System.out.println("✓ Navigated to registration page");

        // ============================================
        // STEP 2: FILL REGISTRATION FORM
        // ============================================
        registerPage.register(
                firstName,
                lastName,
                email,
                password
        );
        Thread.sleep(3000);
        System.out.println("✓ Registration form submitted");

        // ============================================
        // STEP 3: VERIFY SUCCESSFUL REGISTRATION
        // ============================================
        String actualEmail = homePage.getUserEmail();
        Assert.assertEquals(actualEmail, email, "Registration failed!");
        System.out.println("✓ Test Passed - User registered successfully!");
    }

    /*
     * Data Provider for existing email test
     * Returns test data with emails that already exist in the database
     */
    @DataProvider(name = "existingEmailData")
    public Object[][] getExistingEmailData() {
        return new Object[][]{
                // firstName, lastName, email, password
                {TestData.USER1_FIRST_NAME, TestData.USER1_LAST_NAME, TestData.USER1_EMAIL, TestData.USER1_PASSWORD},
                {TestData.USER2_FIRST_NAME, TestData.USER2_LAST_NAME, TestData.USER2_EMAIL, TestData.USER2_PASSWORD},
        };
    }

    /*
     * TC-AUTH-002: Verify registration fails with existing email
     *
     * BUG REPORT:
     * Status depends on test execution - if test FAILS, this documents the bug.
     *
     * Expected Behavior: Display error "User already exists!" when registering with existing email
     * Actual Behavior (if failing): No error message displayed OR incorrect error message shown
     *
     * Test Data:
     * - "ahmed@gmail.com" (existing user)
     * - "amr@gmail.com" (existing user)
     *
     * Priority: HIGH
     * Impact: Users may be confused why registration fails, or worse, might overwrite existing accounts
     * Fix Required (if failing): Ensure backend properly returns "User already exists!" error
     *   and frontend displays it correctly to the user
     *
     * Note: This test should PASS if the backend and frontend properly handle duplicate emails.
     *       If it FAILS, it indicates the error message is not being displayed correctly.
     */
    @Test(priority = 2, dataProvider = "existingEmailData")
    public void testRegistrationWithExistingEmail(String firstName, String lastName, String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-002: Testing Registration with Existing Email...");

        // STEP 1: NAVIGATE TO REGISTRATION PAGE
        homePage.clickLoginButton();
        loginPage.clickRegisterLink();
        System.out.println("✓ Navigated to registration page");

        // STEP 2: ATTEMPT REGISTRATION WITH EXISTING EMAIL
        registerPage.register(firstName, lastName, email, password);
        Thread.sleep(3000);
        System.out.println("✓ Attempted registration with existing email: " + email);

        // STEP 3: VERIFY ERROR MESSAGE
        String errorMessage = registerPage.getErrorMessage();

        Assert.assertTrue(
                errorMessage.contains(TestData.ERROR_USER_EXISTS) || errorMessage.contains("already exists"),
                "Expected error message not displayed. Actual: " + errorMessage
        );
    }

    /*
     * Data Provider for missing fields test
     * Returns test data with one field missing at a time
     */
    @DataProvider(name = "missingFieldsData")
    public Object[][] getMissingFieldsData() {
        return new Object[][]{
                // firstName, lastName, email, password, missingField
                {"", "Ali", "test@test.com", "123", "First Name"},           // Missing first name
                {"Ahmed", "", "test@test.com", "123", "Last Name"},          // Missing last name
                {"Ahmed", "Ali", "", "123", "Email"},                        // Missing email
                {"Ahmed", "Ali", "test@test.com", "", "Password"},           // Missing password
        };
    }

    /*
     * TC-AUTH-003: Verify registration fails with missing required fields
     * Expected Result: Error message "Check Submitted Data!"
     */
    @Test(priority = 3, dataProvider = "missingFieldsData")
    public void testRegistrationWithMissingFields(String firstName, String lastName, String email, String password, String missingField) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-003: Testing Registration with Missing Field: " + missingField);

        // STEP 1: NAVIGATE TO REGISTRATION PAGE
        homePage.clickLoginButton();
        loginPage.clickRegisterLink();
        System.out.println("✓ Navigated to registration page");

        // STEP 2: ATTEMPT REGISTRATION WITH MISSING FIELD
        registerPage.register(firstName, lastName, email, password);
        Thread.sleep(2000);
        System.out.println("✓ Attempted registration with missing: " + missingField);

        // STEP 3: VERIFY ERROR MESSAGE
        String errorMessage = registerPage.getErrorMessage();

        Assert.assertTrue(
                errorMessage.contains(TestData.ERROR_CHECK_DATA),
                "Expected error message not displayed for missing: " + missingField
        );
    }

    /*
     * Data Provider for invalid email format test
     * Returns test data with various invalid email formats
     */
    @DataProvider(name = "invalidEmailData")
    public Object[][] getInvalidEmailData() {
        return new Object[][]{
                // firstName, lastName, email, password
                {"Ahmed", "Ali", "notanemail", "123"},                       // No @ symbol
                {"Ahmed", "Ali", "invalid@", "123"},                         // Missing domain
                {"Ahmed", "Ali", "@invalid.com", "123"},                     // Missing local part
                {"Ahmed", "Ali", "invalid@.com", "123"},                     // Invalid domain
                {"Ahmed", "Ali", "invalid@domain", "123"},                   // Missing TLD
        };
    }

    /*
     * TC-AUTH-004: Verify registration fails with invalid email format
     *
     * BUG REPORT:
     * Currently FAILING - Application does not validate email format on client side.
     * Invalid emails are accepted without any validation error.
     *
     * Expected Behavior: Display validation error when user enters invalid email format
     * Actual Behavior: Form accepts invalid emails (no @, missing domain, etc.) without showing error
     *
     * Test Data:
     * - "notanemail" (no @ symbol)
     * - "invalid@" (missing domain)
     * - "@invalid.com" (missing local part)
     * - "invalid@.com" (invalid domain)
     * - "invalid@domain" (missing TLD)
     *
     * Priority: HIGH
     * Impact: Users can register with invalid emails, causing issues with:
     *   - Account recovery
     *   - Email notifications
     *   - Database data quality
     * Fix Required: Add client-side email validation with error message display
     * Suggestion: Use HTML5 email validation or JavaScript regex pattern validation
     */
    @Test(priority = 4, dataProvider = "invalidEmailData")
    public void testRegistrationWithInvalidEmail(String firstName, String lastName, String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-AUTH-004: Testing Registration with Invalid Email: " + email);

        // STEP 1: NAVIGATE TO REGISTRATION PAGE
        homePage.clickLoginButton();
        loginPage.clickRegisterLink();
        System.out.println("✓ Navigated to registration page");

        // STEP 2: ATTEMPT REGISTRATION WITH INVALID EMAIL
        registerPage.register(firstName, lastName, email, password);
        Thread.sleep(2000);
        System.out.println("✓ Attempted registration with invalid email: " + email);

        // STEP 3: VERIFY VALIDATION ERROR
        String errorMessage = registerPage.getErrorMessage();

        Assert.assertTrue(
                !errorMessage.isEmpty() || registerPage.isErrorDisplayed(),
                "Expected validation error not displayed for invalid email: " + email
        );
    }
}
