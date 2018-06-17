package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

    String itemUrl;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void openRandomProduct() {
        // TODO implement logic to open random product before purchase

        Random random = new Random();
        List<WebElement> products = driver.findElements(By.
                xpath("//div[@class='product-description']/h1/a"));
        products.get(random.nextInt(products.size())).click();
    }

    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        itemUrl = driver.getCurrentUrl();
        driver.findElement(By.xpath("//a[@href='#product-details']")).click();

        waitForDetailsTabLoad();
        String name = driver.findElement(By.xpath("//h1[@itemprop='name']")).getText();
        String qty = driver.findElement(By.xpath("//div[@class='product-quantities']/span")).
                getText();
        String strPrice = driver.findElement(By.xpath("//div[@class='current-price']/span")).
                getText();
        return new ProductData(name.toLowerCase(), DataConverter.parseStockValue(qty),
                DataConverter.parsePriceValue(strPrice));
    }


    public String emailGenerator() {
        int ran = 100 + (int) (Math.random() * ((10000 - 100) + 1));
        return "testmail" + ran + "@test.com";
    }

    public void waitForContentLoad() {
        wait.until(ExpectedConditions.elementToBeClickable(By.
                id("wrapper")));
    }

    public void waitForModalLoad() {
        wait.until(ExpectedConditions.elementToBeClickable(By.
                xpath("//a[contains(@class, 'btn') and contains(@class, 'btn-primary')]")));
    }

    public void waitForDetailsTabLoad(){
        wait.until(ExpectedConditions.elementToBeClickable(By.
                xpath("//h1[@itemprop='name']")));
    }
    public String getItemUrl(){
        return itemUrl;
    }

}
