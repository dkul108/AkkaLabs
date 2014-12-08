package com.luxoft.akkalabs.day1.futures;

import akka.dispatch.Mapper;

/**
 * Created by User on 08.12.2014.
 */
public class FinalResultCalculator extends Mapper<Result, FinalResult> {
    @Override
    public FinalResult apply(Result r) {
        FinalResult fr = new FinalResult();
        fr.setKeyword(r.getKeyword());
        for(String language : r.getLanguges()) {
            fr.getLanguages().put(language, r.getMessagesCountBy(language));
        }
        return fr;
    }
}
