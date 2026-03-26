package pageobjects;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js     = (JavascriptExecutor) driver;
    }

    By firstNameField = By.xpath(
        "(//input[not(@type='email') and not(@readonly) and not(@disabled)])[1]"
    );
    By lastNameField = By.xpath(
        "(//input[not(@type='email') and not(@readonly) and not(@disabled)])[2]"
    );
    By zipCodeField = By.xpath(
        "(//input[not(@type='email') and not(@readonly) and not(@disabled)])[3]"
    );

    private void jsClickByText(String buttonText, String waitForUrl) {
        js.executeScript(
            "var els = document.querySelectorAll('button, a');" +
            "for(var i=0;i<els.length;i++){" +
            "  var t=(els[i].innerText||'').trim().toLowerCase();" +
            "  if(t.indexOf('" + buttonText.toLowerCase() + "') !== -1){" +
            "    els[i].scrollIntoView({block:'center'});" +
            "    els[i].click(); return;" +
            "  }" +
            "}"
        );
        if (waitForUrl != null) {
            wait.until(ExpectedConditions.urlContains(waitForUrl));
        }
    }

    public void enterDetails(String firstName, String lastName, String zip) {
        wait.until(ExpectedConditions.urlContains("checkout-info"));

        WebElement fn = wait.until(
            ExpectedConditions.visibilityOfElementLocated(firstNameField)
        );
        fn.clear();
        fn.sendKeys(firstName);

        WebElement ln = driver.findElement(lastNameField);
        ln.clear();
        ln.sendKeys(lastName);

        WebElement zc = driver.findElement(zipCodeField);
        zc.clear();
        zc.sendKeys(zip);
    }

    public void clickContinue() {
        jsClickByText("Continue", "checkout-overview");
    }

    public void placeOrder() {
        jsClickByText("Finish", "checkout-complete");
    }

    public boolean isOrderSuccess() {
        try {
            boolean urlOk = driver.getCurrentUrl().contains("checkout-complete");
            String text = (String) js.executeScript(
                "var els = document.querySelectorAll('*');" +
                "for(var i=0;i<els.length;i++){" +
                "  var t=(els[i].innerText||'').trim();" +
                "  if(t.toLowerCase().indexOf('thank you') !== -1){" +
                "    return t;" +
                "  }" +
                "}" +
                "return null;"
            );
            return urlOk && text != null;
        } catch (Exception e) {
            return false;
        }
    }
}