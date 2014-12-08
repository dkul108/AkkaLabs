package com.luxoft.akkalabs.day1.firstactor;

import akka.actor.UntypedActor;

/**
 * Created by User on 08.12.2014.
 */
public class MyFirstActor extends UntypedActor {

    public MyFirstActor()  {
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if("ping".equals(message)){
            System.out.println("Oh, hi there " + message +"!" + System.currentTimeMillis());
        }

    }
}
