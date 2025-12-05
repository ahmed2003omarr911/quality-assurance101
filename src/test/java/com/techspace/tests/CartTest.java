package com.techspace.tests;

import com.techspace.pages.CartPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * Test Suite: Cart Stock Validation
 * Tests cart functionality for stock limits
 */
public class CartTest extends TestBase {

    /*
     * TC-CART-004: Verify cannot add product exceeding stock
     *
     * BUG REPORT:
     * Currently FAILING - Application does not show error message when stock limit is exceeded.
     * Instead, it silently fails with console error only.
     *
     * Expected Behavior: Error message "Low stock for item!" should display in UI
     * Actual Behavior: Plus button stops working, only console error appears
     *
     * Priority: HIGH
     * Impact: Users don't understand why they can't increase quantity
     * Fix Required: Add visible error message when stock limit is reached
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     * - Must reach maximum stock limit
     */
    @Test
    public void testCannotExceedProductStock() throws InterruptedException {
        System.out.println("\n▶ TC-CART-004: Testing Cannot Exceed Product Stock...");
        System.out.println("⚠️  NOTE: This test is EXPECTED TO FAIL - Documents a UI bug");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(TestData.USER2_EMAIL, TestData.USER2_PASSWORD);
        System.out.println("✓ User logged in");

        homePage.addProductToCart();
        Thread.sleep(2000);
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        Thread.sleep(3000);
        System.out.println("✓ Navigated to cart page");

        // ============================================
        // STEP 2: ATTEMPT TO EXCEED STOCK LIMIT
        // ============================================
        // Click the increase quantity button multiple times to reach stock limit
        int maxAttempts = 10; // Try clicking plus button 20 times (should hit limit)
        System.out.println("✓ Attempting to increase quantity " + maxAttempts + " times to exceed stock...");

        for (int i = 0; i < maxAttempts; i++) {
            try {
                cartPage.clickIncreaseQuantityButton();
                Thread.sleep(1500);
            } catch (Exception e) {
                System.out.println("⚠️  Could not click increase button (attempt " + (i+1) + ")");
                break;
            }
        }

        System.out.println("✓ Completed clicking increase quantity button");
        Thread.sleep(2000); // Wait for any error message to appear

        // ============================================
        // STEP 3: TRY TO FIND ERROR MESSAGE IN UI
        // ============================================
        boolean errorDisplayed = false;
        String errorMessage = "";

        // Common error message locators to try
        By[] possibleErrorLocators = {
                By.xpath("//*[contains(text(), 'Low stock')]"),
                By.xpath("//*[contains(text(), 'stock')]"),
                By.xpath("//*[contains(@class, 'error')]"),
                By.xpath("//*[contains(@class, 'alert')]"),
                By.xpath("//div[@role='alert']"),
                By.cssSelector(".error-message"),
                By.cssSelector(".alert"),
                By.cssSelector("[class*='error']")
        };

        System.out.println("✓ Searching for error message in UI...");

        for (By locator : possibleErrorLocators) {
            try {
                errorMessage = driver.findElement(locator).getText();
                if (!errorMessage.isEmpty()) {
                    errorDisplayed = true;
                    System.out.println("✓ Found error message: " + errorMessage);
                    break;
                }
            } catch (NoSuchElementException e) {
                // Continue trying other locators
            }
        }

        if (!errorDisplayed) {
            System.out.println("⚠️  No error message found in UI (as expected - this is the bug)");
            errorMessage = "(No error message displayed in UI - error only in browser console)";
        }

        // ============================================
        // STEP 4: VERIFY ERROR MESSAGE IS DISPLAYED
        // ============================================
        Assert.assertTrue(
                errorDisplayed && errorMessage.toLowerCase().contains("stock"),
                "🐛 BUG CONFIRMED: No error message displayed when trying to exceed stock!\n" +
                        "   Expected: Visible error message containing 'Low stock for item!' or similar\n" +
                        "   Actual: " + errorMessage + "\n" +
                        "   Location Checked: Multiple common error message locations in DOM\n" +
                        "   Fix Required: Display user-friendly error message in UI when stock limit is reached\n" +
                        "   Suggestion: Add a toast/snackbar notification or inline error message"
        );

        System.out.println("✓ Test Passed - Error message displayed for low stock");
    }


}
