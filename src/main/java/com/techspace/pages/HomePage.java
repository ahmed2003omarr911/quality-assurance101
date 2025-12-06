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
    By addToCartButton = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div/div[3]/button");
    By userMenuButton = By.xpath("//*[@id=\"root\"]/header/div/div/div/div/div/div[2]/button");
    By myOrdersPageNavButton = By.xpath("//*[@id=\"menu-appbar\"]/div[3]/ul/li[1]");
    By logoutButton = By.xpath("//*[@id=\"menu-appbar\"]/div[3]/ul/li[2]/p");

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
    public void clickCartIcon() throws InterruptedException {
        driver.findElement(cartIconButton).click();
        Thread.sleep(1000);
    }

    /*
     * Add first product to cart
     */
    public void addProductToCart() throws InterruptedException {
        Thread.sleep(5000);
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

    /*
     * Click Logout Button
     */
    public void clickLogoutButton() {
        driver.findElement(logoutButton).click();
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /*
     * Check if user is logged in (user email is displayed)
     */
    public boolean isUserLoggedIn() {
        return driver.findElement(userEmailDisplay).isDisplayed();
    }

    /*
     * Check if Login button is displayed in navbar (user is logged out)
     */
    public boolean isLoginButtonDisplayed() {
        return driver.findElement(loginPageNavButton).isDisplayed();
    }

    /*
     * Check if user menu is displayed (user is logged in)
     */
    public boolean isUserMenuDisplayed() {
        return driver.findElement(userMenuButton).isDisplayed();
    }
}
