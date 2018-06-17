package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void openRandomProduct() {
        // TODO implement logic to open random product before purchase
        throw new UnsupportedOperationException();
    }

    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        // TODO extract data from opened page
        throw new UnsupportedOperationException();
    }
}
