package com.luxoft.akkalabs.day2.sessions;


import akka.actor.ActorContext;

import javax.websocket.Session;
import java.io.IOException;

public class EchoSessionProcessor implements  SessionProcessor {

    private Session session;
    @Override
    public void started(String sessionId, ActorContext context, Session session) throws IOException {
        this.session = session;
    }

    @Override
    public void stopped() throws IOException {

    }

    @Override
    public void incoming(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @Override
    public void outgoing(Object message) throws IOException {

    }
}
