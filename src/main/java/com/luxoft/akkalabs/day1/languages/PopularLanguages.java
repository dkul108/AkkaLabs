package com.luxoft.akkalabs.day1.languages;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.luxoft.akkalabs.day1.futures.CollectTweets;
import com.luxoft.akkalabs.day1.futures.FinalResult;
import com.luxoft.akkalabs.day1.futures.FinalResultCalculator;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PopularLanguages {

    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create("PopularLanguages");
        ActorRef actor = system.actorOf(
                Props.create(LanguagesCounterActor.class));

        List<String> keywords =
                Arrays.asList("Google", "Apple", "Android", "iPhone", "Lady Gaga");
        for (String keyword : keywords) {
            //1
            /*
            Futures.future(new CollectTweets(keyword, system), system.dispatcher()).
                    map( new FinalResultCalculator(), system.dispatcher()).
                    onSuccess( new SendToActor(actor), system.dispatcher());
*/
            //Another and more preferable way to achieve the same functionality is to use the pipe pattern

            Future<FinalResult> f = Futures.future(new CollectTweets(keyword, system), system.dispatcher()).
                    map( new FinalResultCalculator(), system.dispatcher());
            akka.pattern.Patterns.pipe(f, system.dispatcher()).to(actor);
        }

        Timeout timeout = Timeout.apply(1, TimeUnit.SECONDS);
        for (int i = 0; i < 30; i++) {
            Thread.sleep(1000);
            Future<Object> future = Patterns.ask(actor, "get", timeout);
            //Get the data and display it. Use Await or future.onComplete.
            future.onComplete(new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable throwable, Object o) throws Throwable {
                    Map<String, Integer> res = (Map<String, Integer>)o;
                    System.out.println("LANGS:"+res.toString());
                }
            }, system.dispatcher());
            //);
            //or
        }
        system.shutdown();


    }

    private static class SendToActor extends OnSuccess<FinalResult> {
        private final ActorRef actor;
        public SendToActor(ActorRef actor) {
            this.actor = actor;
        }
        @Override
        public void onSuccess(FinalResult success) throws Throwable {
            actor.tell(success, null);
        }
    }
}
