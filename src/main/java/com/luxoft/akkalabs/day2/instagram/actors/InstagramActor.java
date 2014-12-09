package com.luxoft.akkalabs.day2.instagram.actors;

import akka.actor.UntypedActor;
import com.luxoft.akkalabs.clients.instagram.InstagramClient;

public class InstagramActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            String urlToIcon = InstagramClient.pageToImageUrl((String)message);
            getSender().tell(urlToIcon, self());
        }
    }

}
