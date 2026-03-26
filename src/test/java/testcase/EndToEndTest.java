package testcase;

import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseUtilis;
import pageobjects.CartPage;
import pageobjects.CheckoutPage;
import pageobjects.LoginPage;
import pageobjects.ProductPage;

public class EndToEndTest extends BaseUtilis {

    @Test
    public void completeFlowTest() {

        // Step 1: Login
        LoginPage lp = new LoginPage(driver);
        lp.login("test@qabrains.com", "Password123");

        // Step 2: Add ALL 6 products to cart
        ProductPage pp = new ProductPage(driver);
        pp.addAllProducts();

        // Step 3: Navigate to cart
        pp.clickCart();

        // Step 4: Remove 3 items from cart
        CartPage cp = new CartPage(driver);
        cp.removeItems(3);

        // Step 5: Validate remaining cart total > 0
        double sum = cp.getSumOfProducts();
        Assert.assertTrue(sum > 0,
            "Cart sum after removal should be > 0, got: $" + sum
        );

        // Step 6: Checkout
        cp.clickCheckout();

        // Step 7: Fill details
        CheckoutPage ch = new CheckoutPage(driver);
        ch.enterDetails("Navya", "Test", "560001");

        // Step 8: Continue to overview
        ch.clickContinue();

        // Step 9: Place order
        ch.placeOrder();

        // Step 10: Verify success
        Assert.assertTrue(ch.isOrderSuccess(),
            "Order failed — checkout-complete page not reached!"
        );

        System.out.println(" END-TO-END TEST PASSED!");
    }
}