package core;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;

public class Base {

    private ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
    private ThreadLocal<String> sessionId = new ThreadLocal<>();

    public WebDriver getDriver(){
        return driver.get();
    }

    public String getSessionId(){
        return sessionId.get();
    }

    protected  void createDriver(String browser, String version, String os, String methodName)
            throws MalformedURLException, UnexpectedException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        //Set desired capabilities for the new driver (Capabilities are from the browser you want to use)
        desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        desiredCapabilities.setCapability(CapabilityType.VERSION, version);
        desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, os);
        desiredCapabilities.setCapability("name", methodName);

        //Launch remote browser and set it as current thread
        driver.set(new RemoteWebDriver(
                new URL("https://" + username + ":" + accesskey + "@ondemand.saucelabs.com/wd/hub"),
                desiredCapabilities));

    }

    @BeforeMethod
    public void init(){

    }

    @AfterMethod
    public void tearDown(ITestResult result) throws Exception {
        ((JavascriptExecutor) driver.get()).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        driver.get().quit();
    }

    protected void annotate(String text) {
        ((JavascriptExecutor) driver.get()).executeScript("sauce:context=" + text);
    }

}
