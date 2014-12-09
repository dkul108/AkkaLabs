package com.luxoft.akkalabs.day1.wikipedia2.actors;

import akka.actor.UntypedActor;
import com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics.Deliver;
import com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics.Register;
import com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics.Unregister;
import com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics.WikipediaListener;

import java.util.HashMap;
import java.util.Map;

public class ConnectionsActor extends UntypedActor {

    private Map<String, WikipediaListener> listeners = new HashMap<>();

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Deliver) {
            for(Map.Entry<String, WikipediaListener> e :listeners.entrySet()) {
                e.getValue().deliver(((Deliver) message).getPage());
            }
        } else if (message instanceof Register) {
            WikipediaListener l = ((Register)message).getListener();//
            listeners.put(l.getStreamId(), l);
        }
        if (message instanceof Unregister) {
            listeners.remove(((Unregister)message).getId());
        }
    }
}
