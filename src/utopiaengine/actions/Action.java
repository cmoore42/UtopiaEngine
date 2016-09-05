/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.actions;

import utopiaengine.Connection;
import utopiaengine.Construct;
import utopiaengine.Location;

/**
 *
 * @author moorechr
 */
public class Action {
    
    public enum EventType {
        ACTIVATE,
        CAN_CONNECT,
        CONNECTION_COMPLETE,
        CONSTRUCT_CHANGED,
        DELAY_DOOMSDAY,
        DICE_ROLLED,
        END_OF_WORLD,
        ENGINE_ACTIVATED,
        EVENT_CHANGED,
        EXTENSIVE_SEARCH,
        INFO,
        LOCATION_CHANGED,
        PERFECT_ZERO_SEARCH,
        PLAYER_HEALTH_CHANGED,
        QUIT,
        REST,
        SEARCH,
        SHUTDOWN,
        STORES_CHANGED,
        THROW_AWAY,
        TIME_TRACKER_CHANGED,
        TOOL_CHANGED,
        TRAVEL,
        TREASURE_FOUND,
        WASTE_BASKET_CHANGED
        
    }
    
    private final EventType type;
    private Construct construct = null;
    private Connection connection = null;
    private Location location = null;
    private String text = null;
    private boolean bool;
    private int value;

    public Action(EventType type) {
        this.type = type;
    }
    
    public Action(EventType type, Construct construct) {
        this.type = type;
        this.construct = construct;
    }
    
    public Action(EventType type, Connection connection) {
        this.type = type;
        this.connection = connection;
    }
    
    public Action(EventType type, Connection connection, boolean bool) {
        this.type = type;
        this.connection = connection;
        this.bool = bool;
    }
    
    public Action(EventType type, String text) {
        this.type = type;
        this.text = text;
    }
    
    public Action(EventType type, Location location) {
        this.type = type;
        this.location = location;
    }
    
    public Action(EventType type, int value) {
        this.type = type;
        this.value = value;
    }
    
    public EventType getType() {
        return type;
    }

    public Construct getConstruct() {
        return construct;
    }

    public Connection getConnection() {
        return connection;
    }

    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public boolean isBool() {
        return bool;
    }

    public int getValue() {
        return value;
    }
    
    

}
