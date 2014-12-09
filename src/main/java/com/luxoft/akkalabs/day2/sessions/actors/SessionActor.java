package com.luxoft.akkalabs.day2.sessions.actors;

import akka.actor.UntypedActor;
import com.luxoft.akkalabs.day2.sessions.SessionProcessor;
import com.luxoft.akkalabs.day2.sessions.messages.Incoming;
import com.luxoft.akkalabs.day2.sessions.messages.Outgoing;

import javax.websocket.Session;

public class SessionActor extends UntypedActor {

    private String sessionId;
    private SessionProcessor sessionProcessor;
    private Session session;

    public SessionActor(String sessionId, Session session, SessionProcessor sessionProcessor) {
        this.sessionId = sessionId;
        this.sessionProcessor =sessionProcessor;
        this.session = session;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        session.addMessageHandler(new WebSocketSessionListener());//???
        sessionProcessor.started(sessionId, getContext(), session);
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        sessionProcessor.stopped();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Incoming) {

            sessionProcessor.incoming(((Incoming)message).getMessage());
        } else if (message instanceof Outgoing) {
            //getContext().getChild()
            sessionProcessor.outgoing(((Outgoing)message).getMessage());
        } else {
            //??
            //sessionProcessor.started(sessionId, getContext(), session);
        }
    }

    private class WebSocketSessionListener implements  javax.websocket.MessageHandler.Whole<String> {

        @Override
        public void onMessage(String message) {
            self().tell(new Incoming(message), self());
        }
    }
}
