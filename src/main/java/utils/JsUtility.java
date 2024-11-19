package utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

@Slf4j
@UtilityClass
public class JsUtility {

    @SuppressWarnings("all")
    public static void waitForDomToLoad() {
        var webDriver = getWebDriver();

        if (null != webDriver) {
            AwaitUtility.waitForCondition(Duration.ofSeconds(30), Duration.ofSeconds(2),
                    () -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"),
                    Matchers.is(true));
        } else
            log.error("Failed to wait until DOM is loaded!");
    }
}
