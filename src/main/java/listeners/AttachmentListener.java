package listeners;

import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static utils.WebDriverUtility.getBrowserLogs;

@Slf4j
public class AttachmentListener implements ITestListener, ISuiteListener {

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {}", result.getName(), result.getThrowable());

        WebDriver driver = getWebDriverSafely();
        if (driver != null) {
            attachScreenshot(driver);
            attachConsoleLogs();
        }
    }

    @Attachment(value = "Page Screenshot", type = "image/png")
    private byte[] attachScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
            return new byte[0];
        }
    }

    @Attachment(value = "Browser Console Logs", type = "text/plain")
    private String attachConsoleLogs() {
        try {
            return String.join("\n", getBrowserLogs());
        } catch (Exception e) {
            log.error("Failed to fetch browser logs: {}", e.getMessage());
            return "No logs available.";
        }
    }

    private WebDriver getWebDriverSafely() {
        try {
            return getWebDriver();
        } catch (IllegalStateException e) {
            log.warn("WebDriver is not available: {}", e.getMessage());
            return null;
        }
    }
}
