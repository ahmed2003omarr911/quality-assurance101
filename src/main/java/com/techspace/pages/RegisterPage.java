package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 * Page Object Model for Registration Page
 */
public class RegisterPage {
    WebDriver driver;

    // ============================================
    // LOCATORS
    // ============================================
    By firstNameField = By.name("firstName");
    By lastNameField = By.name("lastName");
    By emailField = By.name("email");
    By passwordField = By.name("password");
    By registerButton = By.xpath("//*[@id=\"root\"]/div/div/div/button");

    // Error message locator
    By errorMessage = By.xpath("//*[@id=\"root\"]/div/div/div/p[1]");

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Enter first name
     */
    public void enterFirstName(String firstName) {
        driver.findElement(firstNameField).sendKeys(firstName);
    }

    /*
     * Enter last name
     */
    public void enterLastName(String lastName) {
        driver.findElement(lastNameField).sendKeys(lastName);
    }

    /*
     * Enter email address
     */
    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    /*
     * Enter password
     */
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    /*
     * Click register button
     */
    public void clickRegisterButton() {
        driver.findElement(registerButton).click();
    }

    /*
     * Complete registration with all details
     */
    public void register(String firstName, String lastName, String email, String password) {
        if (firstName != null && !firstName.isEmpty()) {
            enterFirstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            enterLastName(lastName);
        }
        if (email != null && !email.isEmpty()) {
            enterEmail(email);
        }
        if (password != null && !password.isEmpty()) {
            enterPassword(password);
        }
        clickRegisterButton();
    }

    // ============================================
    // VERIFICATION METHODS (NEW)
    // ============================================

    /*
     * Get error message displayed on the page
     */
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    /*
     * Check if error message is displayed
     */
    public boolean isErrorDisplayed() {
        return driver.findElement(errorMessage).isDisplayed();
    }
}
