package pageobjects;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    By emailField    = By.xpath("//input[@type='email']");
    By passwordField = By.xpath("//input[@type='password']");
    By loginBtn      = By.xpath("//button[@type='submit']");

    By errorMsg = By.xpath(
        "//*[contains(@class,'error') or contains(@class,'alert') or " +
        "contains(@class,'invalid') or contains(@class,'text-red')] | " +
        "//*[contains(text(),'invalid') or contains(text(),'Invalid') or " +
        "contains(text(),'incorrect') or contains(text(),'Incorrect') or " +
        "contains(text(),'wrong') or contains(text(),'Wrong') or " +
        "contains(text(),'failed') or contains(text(),'Failed') or " +
        "contains(text(),'exist') or contains(text(),'credentials')]"
    );

    public void login(String email, String password) {
        // Wait for page to load
        wait.until(ExpectedConditions.urlContains("/login"));

        WebElement emailEl = wait.until(
            ExpectedConditions.visibilityOfElementLocated(emailField)
        );
        emailEl.clear();

        // Only type if value is non-empty
        if (email != null && !email.isEmpty()) {
            emailEl.sendKeys(email);
        }

        WebElement passEl = driver.findElement(passwordField);
        passEl.clear();

        if (password != null && !password.isEmpty()) {
            passEl.sendKeys(password);
        }

        // Click login — browser HTML5 validation blocks submit if fields empty
        driver.findElement(loginBtn).click();

        // Small wait for response
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.urlToBe(
                "https://practice.qabrains.com/ecommerce"
            ));
            System.out.println(" Login successful — URL: " + driver.getCurrentUrl());
            return true;
        } catch (Exception e) {
            System.out.println(" Login did not redirect to products page");
            return false;
        }
    }

    public boolean isErrorDisplayed() {
        try {
            WebElement err = wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMsg)
            );
            System.out.println("Error message shown: " + err.getText());
            return true;
        } catch (Exception e) {
            boolean stillOnLogin = driver.getCurrentUrl().contains("/login");
            System.out.println(stillOnLogin
                ? " Still on login page — login rejected"
                : " Unexpected state: " + driver.getCurrentUrl()
            );
            return stillOnLogin;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}