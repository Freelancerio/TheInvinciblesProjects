package com.footballapi;

import java.util.List;

public class FixturesResponse {
    private List<Fixture> events;

    public List<Fixture> getEvents() { return events; }
    public void setEvents(List<Fixture> events) { this.events = events; }
}