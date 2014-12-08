package com.luxoft.akkalabs.day1.languages;

import akka.actor.UntypedActor;
import com.luxoft.akkalabs.day1.futures.FinalResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 08.12.2014.
 */
public class LanguagesCounterActor extends UntypedActor {
    private final Map<String, Integer> languages = new HashMap<>();
    @Override
    public void onReceive(Object message) throws Exception {
       if(message instanceof  String && ((String)message).equals("get")) {
           getSender().tell(languages, getSelf());
       } else if (message instanceof FinalResult){
           languages.putAll(((FinalResult)message).getLanguages());
       }
    }
}
