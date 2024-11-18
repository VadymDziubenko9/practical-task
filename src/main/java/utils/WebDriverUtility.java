package utils;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.selenide.LogType.*;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static org.openqa.selenium.UnexpectedAlertBehaviour.ACCEPT_AND_NOTIFY;
import static org.openqa.selenium.remote.CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR;

@Slf4j
@UtilityClass
public class WebDriverUtility {

    @Step("Verify URL current URL")
    public static String getCurrentUrl() {
        return getWebDriver().getCurrentUrl();
    }

    public static @NonNull List<String> getBrowserLogs() {
        return Selenide.getWebDriverLogs("browser", SEVERE);
    }

    @Step("Driver Initialization")
    public static void initDriver() {
        log.info("Initialization of WebDriver");
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .includeSelenideSteps(true)
                .enableLogs(SERVER, INFO)
                .enableLogs(CLIENT, INFO)
                .enableLogs(DRIVER, INFO)
                .enableLogs(BROWSER, INFO)
                .screenshots(true));

        Configuration.browserCapabilities.setCapability(UNHANDLED_PROMPT_BEHAVIOUR, ACCEPT_AND_NOTIFY);
        Configuration.baseUrl = "https://s.gsb.co.zm/";
        Configuration.browserSize = "1920x1080";
        Configuration.fastSetValue = false;
        Configuration.timeout = 10_000;
        Configuration.headless = false;
        Configuration.downloadsFolder = "selenideFolder";
        Configuration.fileDownload = FOLDER;
        Configuration.proxyEnabled = false;
        Configuration.browser = "chrome";
    }
}