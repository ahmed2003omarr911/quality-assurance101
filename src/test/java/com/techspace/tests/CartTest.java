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
                System.out.println("⚠️  Could not click increase button (attempt " + (i + 1) + ")");
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

    /*
     * TC-CART-005: Verify user can increase product quantity
     * Input: Click "+" button on cart item
     * Expected: Quantity increases, total amount updates
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     */
    @Test
    public void testIncreaseProductQuantity() throws InterruptedException {
        System.out.println("\n▶ TC-CART-005: Testing Increase Product Quantity...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(TestData.LOGIN_EMAIL, TestData.LOGIN_PASSWORD);
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
        // STEP 2: GET INITIAL QUANTITY AND TOTAL
        // ============================================
        int initialQuantity = cartPage.getQuantity();
        double initialTotal = cartPage.getTotalAmount();
        double unitPrice = cartPage.getUnitPrice();

        System.out.println("✓ Initial quantity: " + initialQuantity);
        System.out.println("✓ Initial total: " + initialTotal);
        System.out.println("✓ Unit price: " + unitPrice);

        // ============================================
        // STEP 3: INCREASE QUANTITY
        // ============================================
        cartPage.clickIncreaseQuantityButton();
        Thread.sleep(2000); // Wait for update
        System.out.println("✓ Clicked increase quantity button");

        // ============================================
        // STEP 4: VERIFY QUANTITY INCREASED
        // ============================================
        int newQuantity = cartPage.getQuantity();
        Assert.assertEquals(newQuantity, initialQuantity + 1,
                "Quantity should increase by 1!");
        System.out.println("✓ New quantity: " + newQuantity);

        // ============================================
        // STEP 5: VERIFY TOTAL AMOUNT UPDATED
        // ============================================
        double newTotal = cartPage.getTotalAmount();
        double expectedTotal = cartPage.calculateExpectedTotal(newQuantity, unitPrice);

        Assert.assertEquals(newTotal, expectedTotal,
                "Total amount should update correctly!");
        System.out.println("✓ New total: " + newTotal);
        System.out.println("✓ Test Passed - Quantity increased and total updated correctly!");
    }

    /*
     * TC-CART-006: Verify user can decrease product quantity
     * Input: Click "-" button on cart item
     * Expected: Quantity decreases, total amount updates
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     * - Quantity must be greater than 1
     */
    @Test
    public void testDecreaseProductQuantity() throws InterruptedException {
        System.out.println("\n▶ TC-CART-006: Testing Decrease Product Quantity...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(TestData.LOGIN_EMAIL, TestData.LOGIN_PASSWORD);
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
        // STEP 2: INCREASE QUANTITY TO 3
        // ============================================
        cartPage.clickIncreaseQuantityButton();
        Thread.sleep(1500);
        cartPage.clickIncreaseQuantityButton();
        Thread.sleep(1500);
        System.out.println("✓ Increased quantity to 3");

        // ============================================
        // STEP 3: GET CURRENT QUANTITY AND TOTAL
        // ============================================
        int currentQuantity = cartPage.getQuantity();
        double currentTotal = cartPage.getTotalAmount();
        double unitPrice = cartPage.getUnitPrice();

        System.out.println("✓ Current quantity: " + currentQuantity);
        System.out.println("✓ Current total: " + currentTotal);
        System.out.println("✓ Unit price: " + unitPrice);

        // ============================================
        // STEP 4: DECREASE QUANTITY
        // ============================================
        cartPage.clickDecreaseQuantityButton();
        Thread.sleep(2000); // Wait for update
        System.out.println("✓ Clicked decrease quantity button");

        // ============================================
        // STEP 5: VERIFY QUANTITY DECREASED
        // ============================================
        int newQuantity = cartPage.getQuantity();
        Assert.assertEquals(newQuantity, currentQuantity - 1,
                "Quantity should decrease by 1!");
        System.out.println("✓ New quantity: " + newQuantity);

        // ============================================
        // STEP 6: VERIFY TOTAL AMOUNT UPDATED
        // ============================================
        double newTotal = cartPage.getTotalAmount();
        double expectedTotal = cartPage.calculateExpectedTotal(newQuantity, unitPrice);

        Assert.assertEquals(newTotal, expectedTotal,
                "Total amount should update correctly!");
        System.out.println("✓ New total: " + newTotal);
        System.out.println("✓ Test Passed - Quantity decreased and total updated correctly!");
    }

    /*
     * TC-CART-007: Verify quantity cannot be decreased below 1
     * Input: Click "-" when quantity is 1
     * Expected: Quantity remains 1 (no action)
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     * - Quantity must be 1
     */
    @Test
    public void testCannotDecreaseQuantityBelowOne() throws InterruptedException {
        System.out.println("\n▶ TC-CART-007: Testing Cannot Decrease Quantity Below 1...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(TestData.LOGIN_EMAIL, TestData.LOGIN_PASSWORD);
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
        // STEP 2: VERIFY INITIAL QUANTITY IS 1
        // ============================================
        int initialQuantity = cartPage.getQuantity();
        Assert.assertEquals(initialQuantity, 1,
                "Initial quantity should be 1!");
        System.out.println("✓ Initial quantity confirmed: " + initialQuantity);

        // ============================================
        // STEP 3: ATTEMPT TO DECREASE QUANTITY
        // ============================================
        cartPage.clickDecreaseQuantityButton();
        Thread.sleep(2000); // Wait for any update
        System.out.println("✓ Clicked decrease quantity button");

        // ============================================
        // STEP 4: VERIFY QUANTITY REMAINS 1
        // ============================================
        int newQuantity = cartPage.getQuantity();
        Assert.assertEquals(newQuantity, 1,
                "Quantity should remain 1 when trying to decrease below minimum!");
        System.out.println("✓ Quantity remains: " + newQuantity);
        System.out.println("✓ Test Passed - Cannot decrease quantity below 1!");
    }

    /*
     * TC-CART-008: Verify total amount calculates correctly
     * Input: Multiple items with different quantities
     * Expected: Total = sum of (quantity × unitPrice) for all items
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     * - Test multiple quantity scenarios
     */
    @Test
    public void testTotalAmountCalculation() throws InterruptedException {
        System.out.println("\n▶ TC-CART-008: Testing Total Amount Calculation...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(TestData.LOGIN_EMAIL, TestData.LOGIN_PASSWORD);
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
        // STEP 2: GET UNIT PRICE
        // ============================================
        double unitPrice = cartPage.getUnitPrice();
        System.out.println("✓ Unit price: " + unitPrice);

        // ============================================
        // STEP 3: TEST MULTIPLE QUANTITIES
        // ============================================
        int[] testQuantities = {1, 2, 3, 4, 5};

        for (int targetQuantity : testQuantities) {
            System.out.println("\n--- Testing quantity: " + targetQuantity + " ---");

            // Set quantity to target
            int currentQuantity = cartPage.getQuantity();
            while (currentQuantity < targetQuantity) {
                cartPage.clickIncreaseQuantityButton();
                Thread.sleep(1500);
                currentQuantity = cartPage.getQuantity();
            }
            while (currentQuantity > targetQuantity) {
                cartPage.clickDecreaseQuantityButton();
                Thread.sleep(1500);
                currentQuantity = cartPage.getQuantity();
            }

            // Verify quantity
            int actualQuantity = cartPage.getQuantity();
            Assert.assertEquals(actualQuantity, targetQuantity,
                    "Quantity should be " + targetQuantity);
            System.out.println("✓ Current quantity: " + actualQuantity);

            // Verify total calculation
            double actualTotal = cartPage.getTotalAmount();
            double expectedTotal = cartPage.calculateExpectedTotal(targetQuantity, unitPrice);

            Assert.assertEquals(actualTotal, expectedTotal,
                    "Total should be " + expectedTotal + " for quantity " + targetQuantity);
            System.out.println("✓ Expected total: " + expectedTotal);
            System.out.println("✓ Actual total: " + actualTotal);
            System.out.println("✓ Calculation correct for quantity " + targetQuantity);
        }

        System.out.println("\n✓ Test Passed - Total amount calculates correctly for all quantities!");
    }
}
