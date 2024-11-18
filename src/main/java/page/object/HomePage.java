package page.object;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static utils.JsUtility.waitForDomToLoad;

public class HomePage {

    private final SelenideElement showMoreButton = $x("//div[@class='show-more']");
    private final SelenideElement upcomingButton = $x("//li[@id='menusportbookprematch']");
    private final SelenideElement activeUpcomingButton = $x("//li[contains(@class,'au-s-s') and @id='menusportbookprematch']");
    private final ElementsCollection sortedUpcomingEvents = $$x("//tbody//tr[@class='event-row']//td[@class='odds-box'][1]//span[contains(@class,'odds')]//span[text() > 1.5 and text() < 3.44]//ancestor::tr/td[1]//div[@class='general-info']");

    @Step
    public void openUpcomingTab() {
        open("https://s.gsb.co.zm/");
        waitForDomToLoad();
    }

    public boolean isUpcomingOpened() {
        upcomingButton.shouldBe(visible);
        return activeUpcomingButton.isDisplayed();
    }

    @Step
    public List<String> getUpcomingEvents() {
        showMore();
        if (!sortedUpcomingEvents.isEmpty()) {
            return sortedUpcomingEvents.stream().map(event -> event.getText().replace("\n", " - ")).toList();
        } else {
            return Collections.emptyList();
        }
    }

    public void showMore() {
        while (showMoreButton.isDisplayed()) {
            showMoreButton.scrollIntoView(true).shouldBe(visible, clickable).click();
            waitForDomToLoad();
        }
    }
}
