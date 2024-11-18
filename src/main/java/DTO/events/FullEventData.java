package DTO.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FullEventData {
    private ArrayList<EventData> data;
    private int ec;
    private int tec;
    private long lts;
    private ArrayList<Integer> acid;
    private Object result;
    @JsonProperty("isSuccessfull")
    private boolean isSuccessfull;
    private Object userInfo;
}
