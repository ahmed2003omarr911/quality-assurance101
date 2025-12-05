package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 * Page Object Model for Home Page
 */
public class HomePage {
    WebDriver driver;

    // ============================================
    // LOCATORS
    // ============================================
    By userEmailDisplay = By.xpath("//*[@id=\"root\"]/header/div/div/div/div/div/div[1]/p");
    By loginPageNavButton = By.xpath("//*[@id=\"root\"]/header/div/div/div/div/button[2]");
    By cartIconButton = By.xpath("//*[@id=\"root\"]/header/div/div/div/div/button");
    By addToCartButton = By.xpath("//*[@id=\"root\"]/div/div/div[1]/div/div[3]/button");
    By userMenuButton = By.xpath("//*[@id=\"root\"]/header/div/div/div/div/div/div[2]/button");
    By myOrdersPageNavButton = By.xpath("//*[@id=\"menu-appbar\"]/div[3]/ul/li[1]");

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Get the displayed user email from navbar
     */
    public String getUserEmail() {
        return driver.findElement(userEmailDisplay).getText();
    }

    /*
      Navigate to Login Page
     */
    public void clickLoginButton() throws InterruptedException {
        driver.findElement(loginPageNavButton).click();
        Thread.sleep(1000);
    }

    /*
     * Navigate to Cart Page
     */
    public void clickCartIcon() {
        driver.findElement(cartIconButton).click();
    }

    /*
     * Add first product to cart
     */
    public void addProductToCart() {
        driver.findElement(addToCartButton).click();
    }

    /*
     * Click user menu to open dropdown
     */
    public void clickUserMenu() {
        driver.findElement(userMenuButton).click();
    }

    /*
     * Navigate to My Orders Page
     */
    public void navToMyOrdersPage() {
        driver.findElement(myOrdersPageNavButton).click();
    }
}
