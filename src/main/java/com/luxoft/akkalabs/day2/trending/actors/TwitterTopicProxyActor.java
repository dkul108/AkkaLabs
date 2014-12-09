package com.luxoft.akkalabs.day2.trending.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import com.luxoft.akkalabs.clients.twitter.TweetObject;
import com.luxoft.akkalabs.day2.topics.actors.TwitterTopicActor;
import com.luxoft.akkalabs.day2.topics.messages.SubscribeToTopic;
import com.luxoft.akkalabs.day2.topics.messages.UnsubscribeFromTopic;

import java.util.HashSet;
import java.util.Set;

public class TwitterTopicProxyActor extends UntypedActor {

    private final String keyword;
    private ActorRef tta;
    private Set<ActorRef> subscribers = new HashSet<ActorRef>();

    public TwitterTopicProxyActor(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void preStart() throws Exception {
        tta = getContext().actorOf(Props.create(TwitterTopicActor.class, keyword));
        context().watch(tta);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof Terminated) {
            removeAndUnsubscribeLastSender();
            getContext().stop(self());
            return;
        }
        if(!tta.equals(getSender())) {
            if(message instanceof SubscribeToTopic)  {
                if(subscribers.isEmpty()) {
                    subscribers.add(self());
                }
                subscribers.add(getSender());
            } else if(message instanceof UnsubscribeFromTopic) {
                removeAndUnsubscribeLastSender();
            } else {
                notifyAllSubscribers(message);
            }
        } else {
            if(message instanceof TweetObject)  {
                notifyAllSubscribers(message);
                getContext().actorSelection("/user/trending").tell(message, self());
            } else {
                getContext().parent().tell(message, self());
            }
        }
    }

    private void removeAndUnsubscribeLastSender() {
        subscribers.remove(getSender());
        if(subscribers.size() == 1) {
            subscribers.remove(self());
        }
    }

    private void notifyAllSubscribers(Object message) {
        for(ActorRef subscriber : subscribers) {
            subscriber.tell(message, self());
        }

    }
}
