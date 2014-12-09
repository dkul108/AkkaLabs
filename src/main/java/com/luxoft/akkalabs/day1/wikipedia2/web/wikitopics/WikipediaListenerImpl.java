package com.luxoft.akkalabs.day1.wikipedia2.web.wikitopics;

import com.luxoft.akkalabs.clients.wikipedia.WikipediaPage;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.io.PrintWriter;

public class WikipediaListenerImpl implements WikipediaListener {

    private String streamId;
    private AsyncContext asyncContext;

    public WikipediaListenerImpl(String streamId, AsyncContext asyncContext) {
        this.streamId = streamId;
        this.asyncContext = asyncContext;
    }

    @Override
    public void deliver(WikipediaPage page) throws NotDeliveredException {
        PrintWriter out;
        try {
            out = asyncContext.getResponse().getWriter();
            String data = page.toJSONString();
            String eventId = Long.toString(System.currentTimeMillis());
            String[] lines = data.split("\n");
            out.append("id: ").append(eventId).append('\n');
            for (String line : lines) {
                out.append("data: ").append(line).append('\n');
            }
            out.append("\n\n");
            asyncContext.getResponse().flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStreamId() {
        return streamId;
    }

    @Override
    public void close() {
    }
}
