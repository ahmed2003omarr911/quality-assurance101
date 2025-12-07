package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/*
 * Page Object Model for Orders Page
 */
public class OrdersPage {
    WebDriver driver;
    WebDriverWait wait;

    // ============================================
    // LOCATORS
    // ============================================
    By pageHeading = By.cssSelector("#root > div > p");
    By orderLocator = By.className("css-1jhqtcx");

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public OrdersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Get page heading text
     */
    public String getPageHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).getText();
    }

    public int calculateNumberOfOrders() {
        int numberOfOrders = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(orderLocator)).size();;
        if (numberOfOrders > 0) {
            System.out.println("Number of orders: " + numberOfOrders);
        } else {
            System.out.println("No orders found on the page!");
        }
        return numberOfOrders;
    }
}
