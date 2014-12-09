package com.luxoft.akkalabs.day2.topics.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.luxoft.akkalabs.clients.twitter.TweetObject;
import com.luxoft.akkalabs.clients.twitter.TwitterClient;
import com.luxoft.akkalabs.clients.twitter.TwitterClients;
import com.luxoft.akkalabs.day2.topics.messages.StopTopic;
import com.luxoft.akkalabs.day2.topics.messages.SubscribeToTopic;
import com.luxoft.akkalabs.day2.topics.messages.TopicIsEmpty;
import com.luxoft.akkalabs.day2.topics.messages.UnsubscribeFromTopic;

import java.util.HashSet;
import java.util.Set;

public class TwitterTopicActor extends UntypedActor {

    private final String keyword;
    private TwitterClient client;
    private Set<ActorRef> subscribers =  new HashSet<>();

    public TwitterTopicActor(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void preStart() throws Exception {
        client= TwitterClients.start(getContext().system(), self(), keyword);
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        client.stop();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TweetObject) {
            for(ActorRef actorRef : subscribers) {
                System.out.print("onReceive-$$$$$$$$$$$$$"+((TweetObject)message).getText());
                actorRef.forward(message, getContext());
            }
        } else if (message instanceof SubscribeToTopic) {
            subscribers.add(getSender());
        } else if (message instanceof UnsubscribeFromTopic) {
            subscribers.remove(getSender());
            if(subscribers.isEmpty()) {
                context().parent().tell(new TopicIsEmpty(keyword), self());
            }
        } else if (message instanceof StopTopic) {
            if (!subscribers.isEmpty()) {
                for (ActorRef subscriber : subscribers) {
                    context().parent().tell(
                            new SubscribeToTopic(keyword),
                            subscriber);
                }
            }
            context().stop(self());
        }
    }
}
