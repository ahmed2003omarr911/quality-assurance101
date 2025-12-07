package com.techspace.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * Test Suite: Cart Stock Validation
 * Tests cart functionality for stock limits
 */
public class CartTest extends TestBase {

    /*
     * Data Provider for Login with valid credentials
     * Returns test valid data for login
     */
    @DataProvider(name = "loginWithValidCredentials")
    public Object[][] getLoginValidCredentials() {
        return new Object[][]{
                // email, password
                {TestData.USER1_EMAIL, TestData.USER1_PASSWORD},
        };
    }

    /*
     * TC-CART-003: Verify cannot add product exceeding stock
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
    @Test(priority = 1, dataProvider = "loginWithValidCredentials")
    public void testCannotExceedProductStock(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-CART-003: Testing Cannot Exceed Product Stock...");
        System.out.println("⚠️  NOTE: This test is EXPECTED TO FAIL - Documents a UI bug");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in");

        homePage.addProductToCart();
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        waitForPageToLoad();
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
                System.out.println("Could not click increase button (attempt " + (i + 1) + ")");
                break;
            }
        }

        System.out.println("✓ Completed clicking increase quantity button");

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
            System.out.println("No error message found in UI (as expected - this is the bug)");
            errorMessage = "(No error message displayed in UI - error only in browser console)";
        }

        // ============================================
        // STEP 4: VERIFY ERROR MESSAGE IS DISPLAYED
        // ============================================
        Assert.assertTrue(
                errorDisplayed && errorMessage.toLowerCase().contains("stock"),
                "   BUG CONFIRMED: No error message displayed when trying to exceed stock!\n" +
                        "   Expected: Visible error message containing 'Low stock for item!' or similar\n" +
                        "   Actual: " + errorMessage + "\n" +
                        "   Location Checked: Multiple common error message locations in DOM\n" +
                        "   Fix Required: Display user-friendly error message in UI when stock limit is reached\n" +
                        "   Suggestion: Add a toast/snackbar notification or inline error message"
        );

        System.out.println("✓ Test Passed - Error message displayed for low stock");
    }

    /*
     * Data Provider for Login with valid credentials
     * Returns test valid data for login
     */
    @DataProvider(name = "loginWithValidCredentials1")
    public Object[][] getLoginValidCredentials1() {
        return new Object[][]{
                // email, password
                {TestData.USER2_EMAIL, TestData.USER2_PASSWORD},
        };
    }

    /*
     * TC-CART-004: Verify user can increase product quantity
     * Input: Click "+" button on cart item
     * Expected: Quantity increases, total amount updates
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     */
    @Test(priority = 2, dataProvider = "loginWithValidCredentials1")
    public void testIncreaseProductQuantity(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-CART-004: Testing Increase Product Quantity...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in");

        homePage.addProductToCart();
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        waitForPageToLoad();
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
        Thread.sleep(1500);
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
     * Data Provider for Login with valid credentials
     * Returns test valid data for login
     */
    @DataProvider(name = "loginWithValidCredentials2")
    public Object[][] getLoginValidCredentials2() {
        return new Object[][]{
                // email, password
                {TestData.USER3_EMAIL, TestData.USER3_PASSWORD},
        };
    }

    /*
     * TC-CART-005: Verify user can decrease product quantity
     * Input: Click "-" button on cart item
     * Expected: Quantity decreases, total amount updates
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     * - Quantity must be greater than 1
     */
    @Test(priority = 3, dataProvider = "loginWithValidCredentials2")
    public void testDecreaseProductQuantity(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-CART-005: Testing Decrease Product Quantity...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in");

        homePage.addProductToCart();
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        waitForPageToLoad();
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
        Thread.sleep(1500); // Wait for update
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
     * Data Provider for Login with valid credentials
     * Returns test valid data for login
     */
    @DataProvider(name = "loginWithValidCredentials3")
    public Object[][] getLoginValidCredentials3() {
        return new Object[][]{
                // email, password
                {TestData.USER4_EMAIL, TestData.USER4_PASSWORD},
        };
    }

    /*
     * TC-CART-006: Verify quantity cannot be decreased below 1
     * Input: Click "-" when quantity is 1
     * Expected: Quantity remains 1 (no action)
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     * - Quantity must be 1
     */
    @Test(priority = 4, dataProvider = "loginWithValidCredentials3")
    public void testCannotDecreaseQuantityBelowOne(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-CART-006: Testing Cannot Decrease Quantity Below 1...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in");

        homePage.addProductToCart();
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        waitForPageToLoad();
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
        Thread.sleep(1500); // Wait for any update
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
     * Data Provider for Login with valid credentials
     * Returns test valid data for login
     */
    @DataProvider(name = "loginWithValidCredentials4")
    public Object[][] getLoginValidCredentials4() {
        return new Object[][]{
                // email, password
                {TestData.USER5_EMAIL, TestData.USER5_PASSWORD},
        };
    }

    /*
     * TC-CART-007: Verify total amount calculates correctly
     * Input: Multiple items with different quantities
     * Expected: Total = sum of (quantity × unitPrice) for all items
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     * - Test multiple quantity scenarios
     */
    @Test(priority = 5, dataProvider = "loginWithValidCredentials4")
    public void testTotalAmountCalculation(String email, String password) throws InterruptedException {
        System.out.println("\n▶ TC-CART-007: Testing Total Amount Calculation...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in");

        homePage.addProductToCart();
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        waitForPageToLoad();
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

    /*
     * Data Provider for Login with valid credentials
     * Returns test valid data for login
     */
    @DataProvider(name = "loginWithValidCredentialsAndProductName")
    public Object[][] getLoginValidCredentialsWithProductName() {
        return new Object[][]{
                // email, password, productName
                {TestData.USER1_EMAIL, TestData.USER1_PASSWORD, TestData.PRODUCT_NAME},

        };
    }

    /*
     * TC-CART-008: Verify user can remove item from cart
     * Input: Click "Remove Item" button
     * Expected: Item removed, cart shows empty state
     *
     * Prerequisites:
     * - User must be logged in
     * - Product must be in cart
     */
    @Test(priority = 6, dataProvider = "loginWithValidCredentialsAndProductName")
    public void testRemoveItemFromCart(String email, String password, String productName) throws InterruptedException {
        System.out.println("\n▶ TC-CART-008: Testing Remove Item From Cart...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in");

        homePage.addProductToCart();
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        waitForPageToLoad();
        System.out.println("✓ Navigated to cart page");

        // ============================================
        // STEP 2: VERIFY PRODUCT IS IN CART
        // ============================================
        String productBeforeRemoval = cartPage.getProductTitle();
        Assert.assertEquals(productBeforeRemoval, productName,
                "Product should be in cart before removal!");
        System.out.println("✓ Product in cart before removal: " + productBeforeRemoval);

        double totalBeforeRemoval = cartPage.getTotalAmount();
        Assert.assertTrue(totalBeforeRemoval > 0,
                "Total should be greater than 0 before removal!");
        System.out.println("✓ Total before removal: " + totalBeforeRemoval);

        // ============================================
        // STEP 3: CLICK REMOVE ITEM BUTTON
        // ============================================
        cartPage.clickRemoveItemButton();
        System.out.println("✓ Clicked remove item button");

        // ============================================
        // STEP 4: VERIFY CART IS EMPTY
        // ============================================
        String emptyCartMessage = cartPage.getEmptyCartMessage();
        Assert.assertTrue(
                emptyCartMessage.contains("Cart is Empty!"),
                "Empty cart message should be displayed!\n" +
                        "Expected: Message containing 'Cart is Empty' or 'empty'\n" +
                        "Actual: " + emptyCartMessage
        );
        System.out.println("✓ Empty cart message displayed: " + emptyCartMessage);

        System.out.println("✓ Test Passed - Item removed successfully and cart is empty!");
    }

    /*
     * TC-CART-009: Verify user can clear entire cart
     * Input: Click "Clear Cart" button
     * Expected: All items removed, cart shows "Cart is Empty!"
     *
     * Prerequisites:
     * - User must be logged in
     * - Multiple products in cart (or at least one)
     */
    @Test(priority = 7, dataProvider = "loginWithValidCredentialsAndProductName")
    public void testClearEntireCart(String email, String password, String productName) throws InterruptedException {
        System.out.println("\n▶ TC-CART-009: Testing Clear Entire Cart...");

        // ============================================
        // PRECONDITION: LOGIN AND ADD PRODUCT TO CART
        // ============================================
        performLogin(email, password);
        System.out.println("✓ User logged in");

        homePage.addProductToCart();
        System.out.println("✓ Product added to cart");

        // ============================================
        // STEP 1: NAVIGATE TO CART PAGE
        // ============================================
        homePage.clickCartIcon();
        waitForPageToLoad();
        System.out.println("✓ Navigated to cart page");

        // ============================================
        // STEP 2: VERIFY CART HAS ITEMS
        // ============================================
        String productBeforeClear = cartPage.getProductTitle();
        Assert.assertEquals(productBeforeClear, productName,
                "Product should be in cart before clearing!");
        System.out.println("✓ Product in cart before clear: " + productBeforeClear);

        double totalBeforeClear = cartPage.getTotalAmount();
        Assert.assertTrue(totalBeforeClear > 0,
                "Total should be greater than 0 before clearing!");
        System.out.println("✓ Total before clear: " + totalBeforeClear);

        // ============================================
        // STEP 3: INCREASE QUANTITY TO MAKE CART MORE FULL
        // ============================================
        cartPage.clickIncreaseQuantityButton();
        Thread.sleep(1500);
        cartPage.clickIncreaseQuantityButton();
        Thread.sleep(1500);
        int quantityBeforeClear = cartPage.getQuantity();
        System.out.println("✓ Increased quantity to: " + quantityBeforeClear);

        double totalAfterIncrease = cartPage.getTotalAmount();
        System.out.println("✓ Total after increasing quantity: " + totalAfterIncrease);
        Assert.assertTrue(totalAfterIncrease > totalBeforeClear,
                "Total should increase after adding more quantity!");

        // ============================================
        // STEP 4: CLICK CLEAR CART BUTTON
        // ============================================
        cartPage.clickClearCartButton();
        System.out.println("✓ Clicked clear cart button");

        // ============================================
        // STEP 5: VERIFY CART IS COMPLETELY EMPTY
        // ============================================
        String emptyCartMessage = cartPage.getEmptyCartMessage();
        Assert.assertTrue(
                emptyCartMessage.contains("Cart is Empty!"),
                "Empty cart message should be displayed!\n" +
                        "Expected: Message containing 'Cart is Empty' or 'empty'\n" +
                        "Actual: " + emptyCartMessage
        );
        System.out.println("✓ Empty cart message displayed: " + emptyCartMessage);

        System.out.println("✓ Test Passed - Cart cleared successfully and all items removed!");
    }
}
