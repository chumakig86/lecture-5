package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PlaceOrderTest extends BaseTest {


    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version
        Assert.assertFalse(isMobileTesting);
    }

    @Test
    public void createNewOrder() throws InterruptedException {
        // TODO implement order creation test

        WebDriverWait wait = actions.getDriverWait();
        driver.get(Properties.getBaseUrl());
        WebElement allProducts = driver.
                findElement(By.xpath("//*[@id='content']/section/a"));
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
        actions.waitForContentLoad();

        List<WebElement> cartItems = driver.findElements(By.xpath("//li[@class='cart-item']"));
        String actualName = driver.findElement(By.xpath("//div[@class='product-line-info']/a")).
                getText().toLowerCase();
        String actualPrice = driver.findElement(By.xpath("//span[@class='product-price']")).getText();

        Assert.assertEquals(cartItems.size(), 1);
        Assert.assertEquals(actualName, productData.getName());
        Assert.assertEquals(DataConverter.parsePriceValue(actualPrice), productData.getPrice());
        actions.waitForContentLoad();

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
        actions.waitForContentLoad();

        driver.findElement(By.xpath("//section[@id='checkout-addresses-step']")).click();
        actions.waitForContentLoad();
        driver.findElement(By.xpath("//input[@name='address1']")).
                sendKeys("Test Address, 1");
        driver.findElement(By.xpath("//input[@name='postcode']")).
                sendKeys("49000");
        driver.findElement(By.xpath("//input[@name='city']")).
                sendKeys("Test City");
        driver.findElement(By.xpath("//button[@name='confirm-addresses']")).
                click();

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='js-delivery']/button"))));
        driver.findElement(By.xpath("//*[@id='js-delivery']/button")).click();

        Thread.sleep(100);
        actions.waitForContentLoad();

        driver.findElement(By.xpath("//*[@id='payment-option-1']")).click();

        driver.findElement(By.xpath("//*[@id='conditions_to_approve[terms-and-conditions]']")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='payment-confirmation']//button[@type='submit']")));
        driver.findElement(By.xpath("//div[@id='payment-confirmation']//button[@type='submit']"))
                .click();
        actions.waitForContentLoad();

        // place new order and validate order summary
        String actualMessage = driver.findElement(By.xpath("//h3[contains(@class, 'h1') and contains(@class, 'card-title')]")).
                getText().toUpperCase();
        actualMessage = actualMessage.substring(1, actualMessage.length());
        String actOrderName = driver.findElement(By.
                xpath("//div[@class='order-line row']//div[2]/span")).getText();
        actOrderName = actOrderName.substring(0, actOrderName.indexOf(" - Size"));

        Assert.assertEquals(actualMessage, "ВАШ ЗАКАЗ ПОДТВЕРЖДЁН");
        Assert.assertEquals(actOrderName.toLowerCase(), productData.getName().toLowerCase());

        // check updated In Stock value
        driver.get(actions.getItemUrl());
        actions.waitForElementClickable("//a[@href='#product-details']");
        driver.findElement(By.xpath("//a[@href='#product-details']")).click();
        actions.waitForDetailsTabLoad();
        actions.waitForContentLoad();
        String actQty = driver.findElement(By.xpath("//div[@class='product-quantities']/span")).
                getText();
        Assert.assertEquals(DataConverter.parseStockValue(actQty), productData.getQty() - 1);
    }

}