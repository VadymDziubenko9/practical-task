package DTO.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Odds {
    private String n;
    private int s;
    private int oc;
    private String p;
    private String l;
    private int or;
    private String id;
}
