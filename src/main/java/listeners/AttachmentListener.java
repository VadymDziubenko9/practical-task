package listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import lombok.NonNull;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        WebDriver webDriver = null;
        try {
            webDriver = getWebDriver();
        } catch (IllegalStateException ex) {
            log.info("Browser wasn't opened!");
        }
        if (webDriver != null) {
            saveScreenshot(webDriver);
            Allure.addAttachment("Browser console logs", getConsoleLogs());
        }
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    private byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    private @NonNull String getConsoleLogs() {
        return String.valueOf(getBrowserLogs());
    }
}
