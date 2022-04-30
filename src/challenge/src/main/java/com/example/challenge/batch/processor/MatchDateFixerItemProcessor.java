package com.example.challenge.batch.processor;

import com.example.challenge.batch.model.Match;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MatchDateFixerItemProcessor implements ItemProcessor<Match, Match> {

    private static final Pattern datePattern = Pattern.compile("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12]\\d|3[01])$");

    @Override
    public Match process(Match match) {
        int h = match.getResult().equals("H") ? 3 : 0;
        int a = match.getResult().equals("A") ? 3 : 0;
        int d = match.getResult().equals("D") ? 1 : 0;
        match.setDate(fixDate(match.getDate()));
        match.setAwayScore((short) Math.max(a, d));
        match.setHomeScore((short) Math.max(h, d));
        return match;
    }

    private String fixDate(String date) {
        Matcher matcher = datePattern.matcher(date);
        if (!matcher.matches()){
            return attemptToFixDate(date);
        }else {
            return date;
        }
    }

    public String attemptToFixDate(String malformedDate){
        try {
            Date date = new SimpleDateFormat("dd/MM/yy").parse(malformedDate);
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
