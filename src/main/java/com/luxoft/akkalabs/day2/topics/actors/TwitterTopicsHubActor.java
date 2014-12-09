package com.luxoft.akkalabs.day2.topics.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.luxoft.akkalabs.day2.topics.messages.StopTopic;
import com.luxoft.akkalabs.day2.topics.messages.SubscribeToTopic;
import com.luxoft.akkalabs.day2.topics.messages.TopicIsEmpty;
import com.luxoft.akkalabs.day2.topics.messages.UnsubscribeFromTopic;

public class TwitterTopicsHubActor extends UntypedActor {

    private final Class<? extends UntypedActor> topicClass;

    public TwitterTopicsHubActor(Class<? extends UntypedActor> topicClass) {
        this.topicClass = topicClass;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SubscribeToTopic) {
            String keyword = ((SubscribeToTopic)message).getKeyword();
            ActorRef actorref = getContext().getChild(keyword);
            if(actorref == null) {
                actorref = getContext().actorOf(Props.create(topicClass,keyword ));
            }
            actorref.forward(message, getContext());


        } else if (message instanceof UnsubscribeFromTopic) {
            String keyword = ((UnsubscribeFromTopic)message).getKeyword();
            ActorRef actorref = getContext().getChild(keyword);
            if(actorref != null) {
                actorref.forward(message, getContext());
            }

        } else if (message instanceof TopicIsEmpty) {
            String keyword = ((TopicIsEmpty)message).getKeyword();
            ActorRef actorref = getContext().getChild(keyword);
            if(actorref != null) {
                actorref.tell(StopTopic.class.newInstance(),self());
            }
        }
    }
}
