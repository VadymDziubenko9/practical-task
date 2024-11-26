package page.object;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import utils.WebDriverUtility;

import java.util.*;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static utils.JsUtility.waitForDomToLoad;

@Slf4j
public class HomePage {

    private static final String UPCOMING_EVENTS_URL = "https://s.gsb.co.zm/sportsbook/upcoming";

    private final SelenideElement showMoreButton = $x("//div[@class='show-more']");
    private final SelenideElement upcomingButton = $x("//li[@id='menusportbookprematch']");
    private final ElementsCollection upcomingEventsKeys = $$x("//tbody//tr[@class='event-row']//td[not(@class='odds-box')]");
    private final ElementsCollection upcomingEventsValues = $$x("//tbody//tr[@class='event-row']//td[contains(@class,'odds-box')]//span[contains(@class,'odds')]");

    @Step("Open 'Upcoming' page")
    public void openUpcomingPage() {
        open("https://s.gsb.co.zm/");
        waitForDomToLoad();
        if (!WebDriverUtility.getCurrentUrl().equals(UPCOMING_EVENTS_URL)) {
            upcomingButton.shouldBe(visible, enabled).click();
            waitForDomToLoad();
        }
    }

    @Step("Get upcoming events")
    public Map<String, String> getUpcomingEvents() {
        clickShowMore();

        if (!upcomingEventsKeys.isEmpty() && !upcomingEventsValues.isEmpty()) {
            int valuesPerKey = 8;

            Map<String, String> keyValueMap = new HashMap<>();

            for (int i = 0; i < upcomingEventsKeys.size(); i++) {
                String key = upcomingEventsKeys.get(i).getText().replace("\n", " - ").trim();
                List<String> valueList = new ArrayList<>();

                for (int j = 0; j < valuesPerKey; j++) {
                    valueList.add(upcomingEventsValues.get(i + j).getText()
                            .replace(" ", ",")
                            .replace("\n", ",")
                            .trim());
                }
                var odds = String.join(",", valueList);
                keyValueMap.put(key, odds);
            }

            keyValueMap.forEach((key, value) -> log.info(key + " : " + value));

            return keyValueMap;
        } else {
            return new HashMap<>();
        }
    }

    @Step("Sort events from {oddMin} to {oddMax} odds")
    public Map<String, String> sortEvents(Map<String, String> eventsMap, double oddMin, double oddMax) {
        HashMap<String, String> resultEvents = new HashMap<>();
        for (Map.Entry<String, String> entry : eventsMap.entrySet()) {
            var anyMatched = Arrays.stream(entry.getValue().split(","))
                    .anyMatch(odd -> Double.parseDouble(odd) > oddMin && Double.parseDouble(odd) < oddMax);
            if (anyMatched) {
                resultEvents.put(entry.getKey(), entry.getValue());
            }
        }
        return resultEvents;
    }

    @Step("Click 'Show more' button")
    public void clickShowMore() {
        IntStream.rangeClosed(0, 10).forEach(each -> {
            waitForDomToLoad();
            if (showMoreButton.isDisplayed()) {
                showMoreButton.scrollIntoView(true).shouldBe(visible, clickable).click();
                waitForDomToLoad();
            }
        });
    }
}
