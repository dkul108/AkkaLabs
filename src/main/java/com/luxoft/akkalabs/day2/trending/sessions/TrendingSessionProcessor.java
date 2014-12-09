package com.luxoft.akkalabs.day2.trending.sessions;

import akka.actor.ActorContext;
import akka.actor.ActorSelection;
import com.luxoft.akkalabs.clients.twitter.TweetObject;
import com.luxoft.akkalabs.day2.sessions.SessionProcessor;
import com.luxoft.akkalabs.day2.topics.messages.SubscribeToTopic;
import com.luxoft.akkalabs.day2.topics.messages.UnsubscribeFromTopic;
import com.luxoft.akkalabs.day2.trending.messages.CurrentTrending;
import com.luxoft.akkalabs.day2.trending.messages.UpvoteTrending;

import java.io.IOException;
import javax.websocket.Session;

public class TrendingSessionProcessor implements SessionProcessor {

    private String sessionId;
    private ActorContext context;
    private Session session;

    @Override
    public void started(String sessionId, ActorContext context, Session session) {
        this.sessionId = sessionId;
        this.context = context;
        this.session = session;
    }

    @Override
    public void stopped() {
    }

    @Override
    public void incoming(String message) {
        String[] tokens = message.split(" ", 2);
        if (tokens.length == 2) {
            ActorSelection selection = null;
            switch (tokens[0]) {
                case "subscribe":
                    selection = context.actorSelection("/user/topics");//trending
                    selection.tell(new SubscribeToTopic(tokens[1]), context.self());
                    //context.self().tell(new SubscribeToTopic(tokens[1]), context.self());
                    break;
                case "unsubscribe":
                    //context.self().tell(new UnsubscribeFromTopic(tokens[1]), context.self());
                    selection = context.actorSelection("/user/topics");//trending
                    selection.tell(new UnsubscribeFromTopic(tokens[1]), context.self());
                    break;
                case "upvote" :
                    selection = context.actorSelection("/user/topics");//trending
                    selection.tell(new UpvoteTrending(tokens[1]), context.self());
                    break;
            }
        }
        //session.getBasicRemote().sendText(message);

    }

    @Override
    public void outgoing(Object message) throws IOException {
        if(message instanceof CurrentTrending) {
            session.getBasicRemote().sendText("trend JSON representation of CurrentTrending: " + ((CurrentTrending) message).toJSON());
        } else {
            session.getBasicRemote().sendText("tweet " + ((TweetObject) message).getText());
        }

    }
}
