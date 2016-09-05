/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import javafx.scene.control.Tooltip;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.TOOL_CHANGED;

/**
 *
 * @author moorechr
 */
public enum Tool {
    CHARM("Focus Charm", "Add 2 energy points during activation"),
    WAND("Paralysis Wand", "+2 to each die in combat"),
    ROD("Dowsing Rod", "Up to -100 on any search");
    
    private final String name;
    private final Tooltip tooltip;
    private boolean charged;
    
    private Tool(String name, String tooltip) {
        this.name = name;
        this.tooltip = new Tooltip(tooltip);
        charged = true;
    }

    public String getName() {
        return name;
    }
    
    public Tooltip getTooltip() {
        return tooltip;
    }

    public boolean isCharged() {
        return charged;
    }

    public void setCharged(boolean charged) {
        this.charged = charged;
        Game.postAction(new Action(TOOL_CHANGED));
        
    }
    
    
    
}
