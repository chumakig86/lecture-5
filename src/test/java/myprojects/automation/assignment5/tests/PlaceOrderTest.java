package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.GeneralActions;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PlaceOrderTest extends BaseTest {

    @Test

    public void checkSiteVersion() {
        // TODO open main page and validate website version
        driver.get(Properties.getBaseUrl());

    }

    @Test
    public void createNewOrder() {
        // TODO implement order creation test
        driver.get(Properties.getBaseUrl());
        WebElement allProducts = driver.
                findElement(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']"));
        allProducts.click();

        // open random product
        actions.openRandomProduct();
        actions.waitForContentLoad();

        // save product parameters
        ProductData productData = actions.getOpenedProductInfo();

        // add product to Cart and validate product information in the Cart
        driver.findElement(By.xpath("//button[@data-button-action='add-to-cart']")).click();
        actions.waitForModalLoad();
        driver.findElement(By.xpath("//a[contains(@class, 'btn') and contains(@class, 'btn-primary')]")).
                click();

        List<WebElement> cartItems = driver.findElements(By.xpath("//li[@class='cart-item']"));
        Assert.assertEquals(cartItems.size(), 1);

        String actualName = driver.findElement(By.xpath("//div[@class='product-line-info']/a")).
                getText().toLowerCase();
        Assert.assertEquals(actualName, productData.getName());

        String actualPrice = driver.findElement(By.xpath("//span[@class='product-price']")).getText();
        Assert.assertEquals(DataConverter.parsePriceValue(actualPrice), productData.getPrice());

        // proceed to order creation, fill required information
        driver.findElement(By.xpath("//a[contains(@class, 'btn') and contains(@class, 'btn-primary')]")).
                click();
        actions.waitForContentLoad();
        driver.findElement(By.xpath("//input[@type='radio'][@value=1]")).click();
        driver.findElement(By.name("firstname")).sendKeys("Test FN");
        driver.findElement(By.name("lastname")).sendKeys("Test LN");
        driver.findElement(By.name("email")).sendKeys(actions.emailGenerator());
        driver.findElement(By.xpath("//button[@data-link-action='register-new-customer']"))
                .click();

        driver.findElement(By.xpath("//section[@id='checkout-addresses-step']")).click();
        driver.findElement(By.xpath("//input[@name='address1']")).
                sendKeys("Test Address, 1");
        driver.findElement(By.xpath("//input[@name='postcode']")).
                sendKeys("49000");
        driver.findElement(By.xpath("//input[@name='city']")).
                sendKeys("Test City");
        driver.findElement(By.xpath("//button[@name='confirm-addresses']")).
                click();
        actions.waitForContentLoad();
        driver.findElement(By.xpath("//button[@name='confirmDeliveryOption']")).
                click();
        actions.waitForContentLoad();
        driver.findElement(By.id("payment-option-1")).click();
        driver.findElement(By.id("conditions_to_approve[terms-and-conditions]")).click();

        driver.findElement(By.xpath("//div[@id='payment-confirmation']//button[@type='submit']"))
                .click();
        actions.waitForContentLoad();

        // place new order and validate order summary
        String actualMessage = driver.findElement(By.xpath("//h3[contains(@class, 'h1') and contains(@class, 'card-title')]")).
                getText().toUpperCase();
        actualMessage = actualMessage.substring(1, actualMessage.length());
        Assert.assertEquals(actualMessage, "ВАШ ЗАКАЗ ПОДТВЕРЖДЁН");

        String actOrderName = driver.findElement(By.
                xpath("//div[@class='order-line row']//div[2]/span")).getText();
        actOrderName = actOrderName.substring(0, actOrderName.indexOf(" -"));
        Assert.assertEquals(actOrderName.toLowerCase(), productData.getName());

        // check updated In Stock value
        driver.get(actions.getItemUrl());
        actions.waitForContentLoad();

        driver.findElement(By.xpath("//a[@href='#product-details']")).click();
        actions.waitForDetailsTabLoad();

        String actQty = driver.findElement(By.xpath("//div[@class='product-quantities']/span")).
                getText();

        Assert.assertEquals(DataConverter.parseStockValue(actQty), productData.getQty() - 1);
    }

}
