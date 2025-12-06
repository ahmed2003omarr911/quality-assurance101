package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 * Page Object Model for Cart Page
 */
public class CartPage {
    WebDriver driver;

    // ============================================
    // LOCATORS
    // ============================================
    By productTitle = By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[1]/div/h6");
    By checkoutButton = By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/button");
    By increaseQuantityButton = By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]/button[2]");
    By decreaseQuantityButton = By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]/button[1]");

    By quantityAndPriceDisplay = By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[1]/div/p");
    By totalAmountDisplay = By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/h5");

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Get product title from cart
     */
    public String getProductTitle() {
        return driver.findElement(productTitle).getText();
    }

    /*
     * Click checkout button
     */
    public void clickCheckoutButton() {
        driver.findElement(checkoutButton).click();
    }

    /*
     * Click increase quantity button
     */
    public void clickIncreaseQuantityButton() {
        driver.findElement(increaseQuantityButton).click();
    }

    /*
     * Click decrease quantity button
     */
    public void clickDecreaseQuantityButton() {
        driver.findElement(decreaseQuantityButton).click();
    }

    /*
     * Get current quantity displayed in cart
     * Extracts quantity from combined text like "1 × 1200$" or "2 × 1200$"
     */
    public int getQuantity() {
        String combinedText = driver.findElement(quantityAndPriceDisplay).getText();
        // Extract the first number before "×" or "x"
        // Example: "1 × 1200$" -> "1"
        String quantityStr = combinedText.split("[×x]")[0].trim();
        return Integer.parseInt(quantityStr);
    }

    /*
     * Get unit price of product
     * Extracts unit price from combined text like "1 × 1200$" or "2 × 1200$"
     */
    public double getUnitPrice() {
        String combinedText = driver.findElement(quantityAndPriceDisplay).getText();
        // Extract the number after "×" or "x"
        // Example: "1 × 1200$" -> "1200$" -> 1200.0
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
        String totalText = driver.findElement(totalAmountDisplay).getText();
        // Extract numeric value from text (e.g., "Total Amount: 1200$" -> 1200.0)
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
