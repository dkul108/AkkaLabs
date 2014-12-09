package com.luxoft.akkalabs.day2.trending.actors;

import akka.actor.UntypedActor;
import com.luxoft.akkalabs.clients.twitter.TweetObject;
import com.luxoft.akkalabs.day2.trending.messages.CurrentTrending;
import com.luxoft.akkalabs.day2.trending.messages.UpvoteTrending;
import org.apache.http.client.utils.URIBuilder;
import scala.concurrent.duration.FiniteDuration;

import java.net.InetAddress;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TrendingCalculatorActor extends UntypedActor {

    private final Object PING = new Object();
    private CurrentTrending currentTrending = new CurrentTrending(new ArrayList<String>());
    Map<String, Integer> counts = new TreeMap<>();
    
    @Override
    public void preStart() throws Exception {
            FiniteDuration oneSecond = FiniteDuration.
                    create(1, TimeUnit.SECONDS);
            context().system().scheduler().schedule(
                    oneSecond, oneSecond,
                    self(), PING,
                    context().dispatcher(), self());
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }
    
    

    @Override
    public void onReceive(Object message) throws Exception {
        if (message == PING) {
            int i = 0;
            for(String val : counts.keySet()) {
                currentTrending.getWords().add(val);
                i++;
                if(i == 30) {
                    break;
                }
            }
            getContext().actorSelection("/user/sessions").tell(currentTrending, self());
        } else if (message instanceof TweetObject) {
            ((TweetObject)message).getUrls().clear();
            String text = ((TweetObject)message).getText();
            splitByWordsWithNoUrls(text, 1);
        } else if (message instanceof UpvoteTrending) {
            splitByWordsWithNoUrls(((UpvoteTrending) message).getKeyword(), 5);
        }
    }

    private void splitByWordsWithNoUrls(String text, int points) {
        Scanner s = new Scanner(text);
        s.useDelimiter(" ");

        while(s.hasNext()) {
           String line = s. next();
           line = line.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
           line = line.replaceAll("\\w{1,3}", "");
            if(!counts.containsKey(line)) {
                counts.put(line, points);
            } else {
                Integer counter = counts.get(line) + points;
                counts.remove(line);
                counts.put(line, counter);
            }
       }
    }
}
