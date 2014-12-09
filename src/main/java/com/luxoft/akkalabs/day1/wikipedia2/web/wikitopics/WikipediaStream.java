package com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/day1/wikitopics"})
public class WikipediaStream extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");

        AsyncContext context = req.startAsync();
        context.setTimeout(240000);

        ActorSystem actorSystem = (ActorSystem)getServletContext().getAttribute("actorSystem");
        ActorSelection actorSelection = actorSystem.actorSelection("/user/connections");
        String streamId = UUID.randomUUID().toString();
        WikipediaListenerImpl wl = new WikipediaListenerImpl(streamId, context);
        actorSelection.tell(new Register(wl), null);
        context.addListener(new WikiAsyncListener(streamId, actorSelection));
    }

    class WikiAsyncListener implements AsyncListener {
        private String streamId;
        private ActorSelection actorSelection;

        public WikiAsyncListener (String streamId, ActorSelection actorSelection) {
            this.streamId = streamId;
            this.actorSelection = actorSelection;
        }
        @Override
        public void onComplete(AsyncEvent event) throws IOException {
            unregister();
        }

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {
            unregister();
            event.getAsyncContext().complete();
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
            unregister();
        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {

        }

        private void unregister() {
            Unregister u = new Unregister(streamId);
            actorSelection.tell(u,null);
        }
    }

}
