package com.footballapi;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.thesportsdb")
public class TheSportsDbConfig {
    private String baseUrl;
    private String pslLeagueId;
    private int timeout;
    private int connectionTimeout;
    private int readTimeout;

    // Constructors
    public TheSportsDbConfig() {}

    // Getters and Setters
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getPslLeagueId() { return pslLeagueId; }
    public void setPslLeagueId(String pslLeagueId) { this.pslLeagueId = pslLeagueId; }

    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }

    public int getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(int connectionTimeout) { this.connectionTimeout = connectionTimeout; }

    public int getReadTimeout() { return readTimeout; }
    public void setReadTimeout(int readTimeout) { this.readTimeout = readTimeout; }
}