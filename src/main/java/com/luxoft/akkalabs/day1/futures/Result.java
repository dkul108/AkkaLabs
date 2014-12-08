package com.luxoft.akkalabs.day1.futures;

import com.luxoft.akkalabs.clients.twitter.TweetObject;

import java.util.*;

/**
 * Created by User on 08.12.2014.
 */
public class Result {

    private String keyword;
    private Collection<TweetObject> tweets = new HashSet<>();

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setTweets(Collection<TweetObject> tweets) {
        this.tweets = tweets;
    }

    public boolean addTweet(TweetObject t) {
        return tweets.add(t);
    }

    public Collection<TweetObject> getTweets() {
        return tweets;
    }

    public Set<String> getLanguges() {
        Set<String> ls = new TreeSet<>();
        for(TweetObject to : tweets) {
            ls.add(to.getLanguage());
        }
        return ls;
    }

    public Set<String> getMessages() {
        Set<String> ls = new TreeSet<>();
        for(TweetObject to : tweets) {
            ls.add(to.getText());
        }
        return ls;
    }

    public int getMessagesCountBy(String lang) {
        int i = 0;
        Set<String> ls = new TreeSet<>();
        for(TweetObject to : tweets) {
            if(to.getLanguage().equals(lang)) {
                i++;
            }
        }
        return i;
    }

    public int getMessagesCount() {
        return getMessages().size();
    }

    public int getLanguagesCount() {
        return getLanguges().size();
    }
}
