package pageobjects;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js     = (JavascriptExecutor) driver;
    }

    By cartIcon  = By.xpath(
        "//div[contains(@class,'profile')]" +
        "//span[contains(@class,'cursor-pointer') and contains(@class,'relative')]"
    );
    By cartBadge = By.xpath("//span[contains(@class,'bg-qa-clr')]");

    public void addAllProducts() {
        wait.until(ExpectedConditions.jsReturnsValue(
            "return document.readyState === 'complete';"
        ));

        // Scroll to bottom to ensure all products are loaded
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        js.executeScript("window.scrollTo(0, 0);");
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        int targetCount = 6;
        int added = 0;

        while (added < targetCount) {
            // Only get VISIBLE "Add to cart" buttons
            List<WebElement> btns = driver.findElements(
                By.xpath("//button[text()='Add to cart']")
            );

            // Filter to only visible ones
            List<WebElement> visibleBtns = btns.stream()
                .filter(WebElement::isDisplayed)
                .collect(java.util.stream.Collectors.toList());

            System.out.println("Visible 'Add to cart' buttons: " + visibleBtns.size());

            if (visibleBtns.isEmpty()) break;

            WebElement btn = visibleBtns.get(0);
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
            try { Thread.sleep(600); } catch (InterruptedException ignored) {}
            added++;
            System.out.println(" Added product " + added);

            // Scroll down to find more products
            js.executeScript("window.scrollBy(0, 300);");
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }

        // Verify cart badge
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
        String badgeCount = driver.findElement(cartBadge).getText().trim();
        System.out.println(" Cart badge count: " + badgeCount);
    }

    public void clickCart() {
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        WebElement cart = wait.until(
            ExpectedConditions.elementToBeClickable(cartIcon)
        );
        cart.click();
        wait.until(ExpectedConditions.urlToBe(
            "https://practice.qabrains.com/ecommerce/cart"
        ));
        System.out.println(" Navigated to cart page");
    }
}