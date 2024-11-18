package DTO.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bets {
    private int id;
    private String n;
    private int o;
    private int s;
    private ArrayList<Odds> odds;
}
