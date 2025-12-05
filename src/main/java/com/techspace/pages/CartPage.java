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

    public void clickIncreaseQuantityButton() {
        driver.findElement(increaseQuantityButton).click();
    }
}
