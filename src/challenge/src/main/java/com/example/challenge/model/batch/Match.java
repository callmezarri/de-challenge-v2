package com.example.challenge.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match implements ResourceAware {

    private String season;

    @JsonProperty("HomeTeam")
    private String homeTeam;

    @JsonProperty("AwayTeam")
    private String awayTeam;

    private short homeScore;

    private short awayScore;

    @JsonProperty("FTR")
    private String result;

    @JsonProperty("Date")
    private String date;

    @JsonProperty("FTHG")
    private short fullTimeHomeGoals;

    @JsonProperty("FTAG")
    private short fullTimeAwayGoals;

    @JsonProperty("HST")
    private short homeShotTarget;

    @JsonProperty("AST")
    private short awayShotTarget;

    @Override
    public void setResource(Resource resource) {
        String seasonName = resource.getFilename();
        assert seasonName != null;
        int start = seasonName.indexOf('-') + 1;
        this.season = seasonName.substring(start, start + 2)+"-"+seasonName.substring(start + 2, start + 4);
    }

}
