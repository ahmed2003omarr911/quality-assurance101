package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/*
 * Page Object Model for Home Page
 */
public class HomePage {
    WebDriver driver;
    WebDriverWait wait;

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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Get the displayed user email from navbar
     */
    public String getUserEmail() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(userEmailDisplay)).getText();
    }

    /*
      Navigate to Login Page
     */
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginPageNavButton)).click();
        wait.until(ExpectedConditions.urlContains("/login"));
    }

    /*
     * Navigate to Cart Page
     */
    public void clickCartIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIconButton)).click();
//        wait.until(ExpectedConditions.urlContains("/cart"));
    }

    /*
     * Add a product to cart
     */
    public void addProductToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
    }

    /*
     * Click user menu to open dropdown
     */
    public void clickUserMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(userMenuButton)).click();
        // Wait for dropdown menu to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(myOrdersPageNavButton));
    }

    /*
     * Navigate to My Orders Page
     */
    public void navToMyOrdersPage() {
        wait.until(ExpectedConditions.elementToBeClickable(myOrdersPageNavButton)).click();
        wait.until(ExpectedConditions.urlContains("/my-orders"));
    }

    /*
     * Click Logout Button
     */
    public void clickLogoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
        // Wait for redirect to home page after logout
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginPageNavButton));
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /*
     * Check if user is logged in (user email is displayed)
     */
    public boolean isUserLoggedIn() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(userEmailDisplay)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Check if Login button is displayed in navbar (user is logged out)
     */
    public boolean isLoginButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginPageNavButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Check if user menu is displayed (user is logged in)
     */
    public boolean isUserMenuDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(userMenuButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
