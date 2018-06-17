package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

    private String itemUrl;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 45);
    }

    public void openRandomProduct() {
        // TODO implement logic to open random product before purchase
        Random random = new Random();
        List<WebElement> products = driver.findElements(By.
                xpath("//*[@id='js-product-list']/div[1]/article/div/div[@class='product-description']"));
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
        waitForContentLoad();
        driver.findElement(By.xpath("//*[@id='main']/div[1]/div[2]/div[2]/div[3]/ul/li[2]/a")).
                click();
        waitForDetailsTabLoad();
        waitForContentLoad();
        String name = driver.findElement(By.xpath("//h1[@itemprop='name']")).getText();
        String qty = driver.findElement(By.xpath("//div[@class='product-quantities']/span")).
                getText();
        if (qty.equals("")) {
            qty = "0";
        }
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
        wait.until(ExpectedConditions.elementToBeClickable(By.id("wrapper")));
    }

    public void waitForElementClickable(String xpath) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    }

    public void waitForModalLoad() {
        wait.until(ExpectedConditions.elementToBeClickable(By.
                xpath("//a[contains(@class, 'btn') and contains(@class, 'btn-primary')]")));
    }

    public void waitForDetailsTabLoad() {
        wait.until(ExpectedConditions.elementToBeClickable(By.
                xpath("//h1[@itemprop='name']")));
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public WebDriverWait getDriverWait() {
        return wait;
    }

}
