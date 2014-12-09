package com.luxoft.akkalabs.day2.sessions.actors;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.luxoft.akkalabs.day2.sessions.SessionProcessor;
import com.luxoft.akkalabs.day2.sessions.messages.OutgoingBroadcast;
import com.luxoft.akkalabs.day2.sessions.messages.OutgoingToSession;
import com.luxoft.akkalabs.day2.sessions.messages.RegisterSession;
import com.luxoft.akkalabs.day2.sessions.messages.UnregisterSession;

public class SessionsHubActor extends UntypedActor {

    private final Class<? extends SessionProcessor> processorClass;

    public SessionsHubActor(Class<? extends SessionProcessor> processor) {
        this.processorClass = processor;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RegisterSession) {
            SessionProcessor sessionProcessor = processorClass.newInstance();
            getContext().actorOf(Props.create(SessionActor.class, ((RegisterSession)message).getSessionId(),((RegisterSession)message).getSession(), sessionProcessor ));
        } else if (message instanceof UnregisterSession) {
            String sessionId = ((UnregisterSession)message).getSessionId();
            ActorRef child = getContext().getChild(sessionId);
            child.tell(PoisonPill.getInstance(),self());
        } else if (message instanceof OutgoingToSession) {
            String sessionId = ((OutgoingToSession)message).getSessionId();
            ActorRef child = getContext().getChild(sessionId);
            child.forward(((OutgoingToSession)message).getMessage(), getContext());
        } else if (message instanceof OutgoingBroadcast) {
            for(ActorRef child : getContext().getChildren()) {
                child.forward(((OutgoingBroadcast) message).getMessage(), getContext());
            }
        }
    }
}
