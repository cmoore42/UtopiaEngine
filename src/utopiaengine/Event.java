/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import javafx.scene.control.Tooltip;

/**
 *
 * @author moorechr
 */
public enum Event {
    MONSTERS("Active Monsters", "Increase the level of all encounters by two"),
    VISION("Fleeting Vision", "Begin with one free energy point when activating construct"),
    FORTUNE("Good Fortune", "Subtract up to 10 from your search result"),
    WEATHER("Foul Weather", "-1 circles cause you to lose 2 days instead of 1");
    
    private final String name;
    private Location currentRegion;
    private final Tooltip tooltip;
    
    private Event(String name, String tooltip) {
        this.name = name;
        this.tooltip = new Tooltip(tooltip);
    }
    
    public void setRegion(Location region) {
        this.currentRegion = region;
    }
    
    public String getName() {
        return name;
    }
    
    public Tooltip getTooltip() {
        return tooltip;
    }
}
