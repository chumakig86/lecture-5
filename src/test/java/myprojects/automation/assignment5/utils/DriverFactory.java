package myprojects.automation.assignment5.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.SkipException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {
    /**
     * @param browser Driver type to use in tests.
     * @return New instance of {@link WebDriver} object.
     */
    public static WebDriver initDriver(String browser) {
        switch (browser) {
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        new File(DriverFactory.class.getResource("/geckodriver.exe").getFile()).getPath());
                return new FirefoxDriver();
            case "ie":
            case "internet explorer":
                System.setProperty(
                        "webdriver.ie.driver",
                        new File(DriverFactory.class.getResource("/IEDriverServer.exe").getFile()).getPath());
                DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
                capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
                capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                return new InternetExplorerDriver(capabilities);
            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(DriverFactory.class.getResource("/chromedriver.exe").getFile()).getPath());
                return new ChromeDriver();
        }
    }

    /**
     * @param browser Remote driver type to use in tests.
     * @param gridUrl URL to Grid.
     * @return New instance of {@link RemoteWebDriver} object.
     */

    public static WebDriver initDriver(String browser, String gridUrl) throws MalformedURLException {
        // TODO prepare capabilities for required browser and return RemoteWebDriver instance
        DesiredCapabilities capabilities;
        try {
            switch (browser) {
                case "firefox":
                    capabilities = DesiredCapabilities.firefox();
                    break;
                case "ie":
                case "internet explorer":
                    capabilities = DesiredCapabilities.internetExplorer();
                    break;
                case "phantomjs":
                    capabilities = DesiredCapabilities.phantomjs();
                    break;
                case "opera":
                    capabilities = DesiredCapabilities.operaBlink();
                    break;
                case "android":
                    capabilities = DesiredCapabilities.android();
                    break;
                default:
                case "chrome":
                    capabilities = DesiredCapabilities.chrome();
                    break;
            }
            return new RemoteWebDriver(new URL(gridUrl), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SkipException("Unable to create RemoteWebDriver instance");
        }
    }
}
