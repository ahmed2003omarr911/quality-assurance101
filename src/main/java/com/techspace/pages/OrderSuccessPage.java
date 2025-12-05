package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 * Page Object Model for Order Success Page
 */
public class OrderSuccessPage {
    WebDriver driver;

    // ============================================
    // LOCATORS
    // ============================================
    By successMessage = By.xpath("//*[@id=\"root\"]/div/h4");

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public OrderSuccessPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Get order success message
     */
    public String getSuccessMessage() {
        return driver.findElement(successMessage).getText();
    }
}
