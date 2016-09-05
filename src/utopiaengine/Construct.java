/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import javafx.scene.control.Tooltip;
import static utopiaengine.Construct.State.ACTIVATED;
import static utopiaengine.Construct.State.CONNECTED;
import static utopiaengine.Construct.State.FOUND;
import static utopiaengine.Construct.State.MISSING;
import utopiaengine.actions.ConstructChangedAction;

/**
 *
 * @author moorechr
 */
public enum Construct {
    SEAL("Seal of Balance", "Once per game, cancel all of the events at your location"),
    MIRROR("Hermetic Mirror", "Subtract 10 from any search result in Halebeard Peaks or The Fiery Maw."),
    GATE("Void Gate", "Recovery takes 4 days instead of 6."),
    CHASSIS("Golden Chassis", "Add 1 to the result of each die when in combat with spirits."),
    LENS("Scrying Lens", "Subtract 10 from any search result in Glassrock Canyon or Root Strangled Marshes"),
    BATTERY("Crystal Battery", "Once per game, spend three components to recharge a tool.");
    
    public enum State {
        MISSING("Missing"), 
        FOUND("Found"), 
        ACTIVATED("Activated"), 
        CONNECTED("Connected");
        
        private String name;
        
        
        private State(String name) {
            this.name = name;
            
        }
        
        public String getName() {
            return name;
        }
    }
        
    private String name;
    private State state;
    private Tooltip tooltip;
    private boolean used;
    
    private Construct(String name, String tooltip) {
        this.name = name;
        this.tooltip = new Tooltip(tooltip);
        state = MISSING;
        used = false;
    }

    public String getName() {
        return name;
    }
    
    public Tooltip getTooltip() {
        return tooltip;
    }

    public boolean isActivated() {
        return ((state == ACTIVATED) || (state == CONNECTED));
    }

    public void activate() {
        state = ACTIVATED;
        Game.postAction(new ConstructChangedAction(this));
    }

    public boolean isFound() {
        return (state != MISSING);
    }

    public void setFound() {
        state = FOUND;
        Game.postAction(new ConstructChangedAction(this));
    }
    
    public void setUsed(boolean used) {
        this.used = used;
        Game.postAction(new ConstructChangedAction(this));
    }
    
    public boolean isUsed() {
        return this.used;
    }
    
    public State getState() {
        return state;
    }
    
}
