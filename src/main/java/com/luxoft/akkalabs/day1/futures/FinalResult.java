package com.luxoft.akkalabs.day1.futures;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 08.12.2014.
 */
public class FinalResult {

    private String keyword;

    private Map<String, Integer> languages;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Map<String, Integer> getLanguages() {
        if(languages == null) {
            languages = new HashMap<>();
        }
        return languages;
    }
}
