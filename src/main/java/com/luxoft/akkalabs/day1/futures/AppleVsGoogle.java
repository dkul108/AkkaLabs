package com.luxoft.akkalabs.day1.futures;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.dispatch.OnSuccess;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.luxoft.akkalabs.clients.twitter.TwitterClient;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppleVsGoogle {

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("AppleVsGoogle");
        CollectTweets appleTask = new CollectTweets("Apple",system);
        CollectTweets googleTask = new CollectTweets("Google",system);

/*
        ExecutorService aes = Executors.newSingleThreadExecutor();
        FutureTask<Result> aTask = new FutureTask (apples);
        Future<Result> applesFuture =  aes.submit(aTask);

        ExecutorService ges = Executors.newSingleThreadExecutor();
        FutureTask<Result> gTask = new FutureTask (googles);
        Future<Result> googleFuture =  ges.submit(gTask);

        Result ar = applesFuture.get();
        Result gr = googleFuture.get();
*/

        Future<FinalResult> futureAppleLanguages =
                Futures.future(appleTask, system.dispatcher())
                .map(new FinalResultCalculator(), system.dispatcher());
        Future<FinalResult> futureGoogleLanguages =
                Futures.future(googleTask, system.dispatcher())
                .map(new FinalResultCalculator(), system.dispatcher());

        List<Future<FinalResult>> allResults =
                Arrays.asList(futureAppleLanguages, futureGoogleLanguages);
        Future<Iterable<FinalResult>> result =
                Futures.sequence(allResults, system.dispatcher());
/*
        Result appleResult = Await.result(futureAppleLanguages,
                Duration.create(60, TimeUnit.SECONDS));
        Result googleResult = Await.result(futureGoogleLanguages,
                Duration.create(60, TimeUnit.SECONDS));


        System.out.println(appleResult.getLanguges().toString());
        System.out.println(appleResult.getMessages().toString());
        System.out.println("A Messages count:" + appleResult.getMessagesCount());
        System.out.println("A Languages count:" + appleResult.getLanguagesCount());
        System.out.println(googleResult.getLanguges().toString());
        System.out.println(googleResult.getMessages().toString());
        System.out.println("G Languages count:" + googleResult.getLanguagesCount());
        System.out.println("G Messages count:" + googleResult.getMessagesCount());
*/
        //async
/*    result.onSuccess(
                new OnSuccess<Iterable<FinalResult>>() {
                    @Override
                    public void onSuccess(Iterable<FinalResult> success) {
                        Iterator<FinalResult> i = success.iterator();
                        …
                    }
                }, system.dispatcher());     }
*/}
   }