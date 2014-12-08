package com.luxoft.akkalabs.day1.firstactor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class FirstActorApp {

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("Example");
        Props actorProps = Props.create(MyFirstActor.class);
        ActorRef actor = system.actorOf(actorProps);//  location transparent address
        actor.tell("ping", null);// null means doesn't knows from whom it come, so no reply possible
        Thread.sleep(1000);
        actor.tell("ping", null);
        Thread.sleep(1000);
        actor.tell("ping", null);
        Thread.sleep(1000);
        actor.tell("ping", null);
        Thread.sleep(1000);
        actor.tell("ping", null);

        Thread.sleep(1000);
        system.shutdown();
    }

}
