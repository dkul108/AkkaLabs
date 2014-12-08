package com.luxoft.akkalabs.clients;

import java.util.Properties;

public class Config {

    private static final Properties properties = new Properties();

    public static String get(String key) {
        return properties.getProperty(key);
    }

    static {
        properties.put("twitter.customer.key", "8BYd5Zpfo7sjG0zVvWyWWGQ7u");
        properties.put("twitter.customer.secret", "AuGIF0BMHxup34IhprIGYg6UX3HpMCOjhzzUlcSZEzZBrHGUO7");
        properties.put("twitter.access.token", "2676659488-koxxJppLUyl6YWbQ2fqJXwIHSwIIq1jO1yUocBA");
        properties.put("twitter.access.secret", "rTeIyfNyZnYXmgBbTSSXilj8XvTAFwLDReXJ9WLWyw9c3");
    }
}
