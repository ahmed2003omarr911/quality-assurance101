package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/*
 * Page Object Model for Cart Page
 */
public class CartPage {
    WebDriver driver;
    WebDriverWait wait;

    // ============================================
    // LOCATORS
    // ============================================
    By productTitle = By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[1]/div/h6");
    By checkoutButton = By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/button");
    By increaseQuantityButton = By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]/button[2]");
    By decreaseQuantityButton = By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]/button[1]");
    By removeItemButton = By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[1]/div/button");
    By clearCartButton = By.xpath("//*[@id=\"root\"]/div/div[1]/button");
    By emptyCartMessage = By.tagName("h2");

    By quantityAndPriceDisplay = By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[1]/div/p");
    By totalAmountDisplay = By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/h5");

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Get product title from cart
     */
    public String getProductTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle)).getText();
    }

    /*
     * Get empty cart message
     */
    public String getEmptyCartMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage)).getText();
    }

    /*
     * Click checkout button
     */
    public void clickCheckoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }

    /*
     * Click increase quantity button
     */
    public void clickIncreaseQuantityButton() {
//        wait.until(ExpectedConditions.elementToBeClickable(increaseQuantityButton)).click();
        driver.findElement(increaseQuantityButton).click();
    }

    /*
     * Click decrease quantity button
     */
    public void clickDecreaseQuantityButton() {
//        wait.until(ExpectedConditions.elementToBeClickable(decreaseQuantityButton)).click();
        driver.findElement(decreaseQuantityButton).click();
    }

    /*
     * Click remove item button
     */
    public void clickRemoveItemButton() {
        wait.until(ExpectedConditions.elementToBeClickable(removeItemButton)).click();
    }

    /*
     * Click clear cart button
     */
    public void clickClearCartButton() {
        wait.until(ExpectedConditions.elementToBeClickable(clearCartButton)).click();
        // Wait for empty cart message
        wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage));
    }

    /*
     * Get current quantity displayed in cart
     * Extracts quantity from combined text like "1 × 1200$" or "2 × 1200$"
     */
    public int getQuantity() {
        String combinedText = wait.until(
                ExpectedConditions.visibilityOfElementLocated(quantityAndPriceDisplay)
        ).getText();
        String quantityStr = combinedText.split("[×x]")[0].trim();
        return Integer.parseInt(quantityStr);
    }

    /*
     * Get unit price of product
     * Extracts unit price from combined text like "1 × 1200$" or "2 × 1200$"
     */
    public double getUnitPrice() {
        String combinedText = wait.until(
                ExpectedConditions.visibilityOfElementLocated(quantityAndPriceDisplay)
        ).getText();
        String priceStr = combinedText.split("[×x]")[1].trim();
        // Remove all non-numeric characters except decimal point
        String numericValue = priceStr.replaceAll("[^0-9.]", "");
        return Double.parseDouble(numericValue);
    }

    /*
     * Get total amount displayed in cart
     * Extracts amount from text like "Total Amount: 1200$"
     */
    public double getTotalAmount() {
        String totalText = wait.until(
                ExpectedConditions.visibilityOfElementLocated(totalAmountDisplay)
        ).getText();
        // Remove all non-numeric characters except decimal point
        String numericValue = totalText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(numericValue);
    }

    /*
     * Calculate expected total based on quantity and unit price
     */
    public double calculateExpectedTotal(int quantity, double unitPrice) {
        return quantity * unitPrice;
    }
}
