package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 * Page Object Model for Checkout Page
 */
public class CheckoutPage {
    WebDriver driver;

    // ============================================
    // LOCATORS
    // ============================================
    By addressField = By.name("address");
    By payButton = By.xpath("//*[@id=\"root\"]/div/button");

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Enter delivery address
     */
    public void enterAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    /*
     * Click pay button to complete order
     */
    public void clickPayButton() {
        driver.findElement(payButton).click();
    }

    /*
     * Complete checkout with address
     */
    public void completeCheckout(String address) {
        enterAddress(address);
        clickPayButton();
    }
}
