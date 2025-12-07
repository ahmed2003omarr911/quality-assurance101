package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/*
 * Page Object Model for Checkout Page
 */
public class CheckoutPage {
    WebDriver driver;
    WebDriverWait wait;

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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Enter delivery address
     */
    public void enterAddress(String address) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressField)).sendKeys(address);
    }

    /*
     * Click pay button to complete order
     */
    public void clickPayButton() {
        wait.until(ExpectedConditions.elementToBeClickable(payButton)).click();
        wait.until(ExpectedConditions.urlContains("/order-success"));
    }

    /*
     * Complete checkout with address
     */
    public void completeCheckout(String address) {
        enterAddress(address);
        clickPayButton();
    }
}
