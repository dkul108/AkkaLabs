package com.luxoft.akkalabs.day1.futures;

import akka.actor.ActorSystem;
import com.luxoft.akkalabs.clients.twitter.QueueTwitterClient;
import com.luxoft.akkalabs.clients.twitter.TwitterClient;
import com.luxoft.akkalabs.clients.twitter.TwitterClients;

import java.util.concurrent.Callable;

/**
 * Created by User on 08.12.2014.
 */
public class CollectTweets implements Callable<Result> {

    private String keyword;
    private ActorSystem system;

    public CollectTweets(String keyword, ActorSystem system) {
        this.keyword = keyword;
        this.system = system;
    }
    @Override
    public Result call() throws Exception {
        Result r = new Result();
        try(QueueTwitterClient qtc = TwitterClients.start(system, keyword)) {
            for (int i = 0; i < 10; i++) {
                r.addTweet(qtc.next());
            }
        }
        return r;
    }
}
