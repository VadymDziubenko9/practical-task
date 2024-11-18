package DTO.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventData {
    private int id;
    private int st;
    private Date gt;
    private String gn;
    private boolean bk;
    private int et;
    private int o;
    private int cid;
    private int sci;
    private boolean ng;
    private int sid;
    private String sn;
    private int co;
    private String cn;
    private int lid;
    private int lo;
    private String ln;
    private String h;
    private String a;
    private int btc;
    private ArrayList<Bets> bts;
    private boolean hco;
}
