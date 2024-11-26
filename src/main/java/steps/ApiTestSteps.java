package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.events.FullEventData;
import dto.events.Odds;
import exception.AutoTestsException;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Slf4j
public class ApiTestSteps {

    @Step("Validate Json schema")
    public String validateJsonSchema(String leagueId) {
        var path = generateRequest(leagueId);

        return given()
                .when()
                .get("/evapi/event/GetEvents?" + path)
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema.json"))
                .extract().asString();
    }

    @Step("Get events")
    public List<String> getEventsWithAllOdds(FullEventData fullEventData) {
        return fullEventData.getData().stream()
                .map(eventData -> {
                    String event = eventData.getA();
                    String odds = eventData.getBts().stream()
                            .flatMap(bets -> bets.getOdds().stream())
                            .map(Odds::getP)
                            .collect(Collectors.joining(", "));
                    return String.format("%s : %s", event, odds);
                })
                .toList();
    }


    @Step("Deserialize events")
    public FullEventData deserializeEvents(String eventsResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(eventsResponse, FullEventData.class);
        } catch (JsonProcessingException e) {
            throw new AutoTestsException(e.getMessage());
        }
    }

    private static String generateRequest(String leagueId) {
        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("betTypeIds", "-1");
        queryParams.put("take", "100");
        queryParams.put("statusId", "0");
        queryParams.put("eventTypeId", "0");
        queryParams.put("leagueIds", String.format("%s", leagueId));
        queryParams.put("skip", "0");
        queryParams.put("sportTypeIds", "31");

        return queryParams
                .entrySet()
                .stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("&"));
    }
}
