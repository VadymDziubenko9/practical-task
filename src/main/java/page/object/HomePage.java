package page.object;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static utils.JsUtility.waitForDomToLoad;

@Slf4j
public class HomePage {

    private final SelenideElement showMoreButton = $x("//div[@class='show-more']");
    private final SelenideElement upcomingButton = $x("//li[@id='menusportbookprematch']");
    private final SelenideElement activeUpcomingButton = $x("//li[contains(@class,'au-s-s') and @id='menusportbookprematch']");
    private final ElementsCollection upcomingEventsKeys = $$x("//tbody//tr[@class='event-row']//td[not(@class='odds-box')]");
    private final ElementsCollection upcomingEventsValues = $$x("//tbody//tr[@class='event-row']//td[contains(@class,'odds-box')]//span[contains(@class,'odds')]");

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
    public Map<String, String> getUpcomingEvents() {
        showMore();

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

    @Step
    public void validateEvents(Map<String, String> resultEvents, Map<String, String> eventsMap, double odd1, double odd2) {
        for (Map.Entry<String, String> entry : eventsMap.entrySet()) {
            var anyMatched = Arrays.stream(entry.getValue().split(","))
                    .anyMatch(odd -> Double.parseDouble(odd) > odd1 && Double.parseDouble(odd) < odd2);
            if (anyMatched) {
                resultEvents.put(entry.getKey(), entry.getValue());
            }
        }
    }


    public void showMore() {
        IntStream.rangeClosed(0, 5).forEach(each -> {
            waitForDomToLoad();
            if (showMoreButton.isDisplayed()) {
                showMoreButton.scrollIntoView(true).shouldBe(visible, clickable).click();
                waitForDomToLoad();
            }
        });
    }
}
