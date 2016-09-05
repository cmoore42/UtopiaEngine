/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import javafx.scene.control.Tooltip;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.TREASURE_FOUND;

/**
 *
 * @author moorechr
 */
public enum Treasure {
    PLATE("Ice Plate", "Subtract 1 from the ATK range of all monsters."),
    BRACELET("Bracelet of Ios", "Add one energy point to the Go'd Hand each time you cross out a day on the time track."),
    MOONLACE("Shimmering Moonlace", "You may ignore encounters"),
    SCALE("Scale of the Infinity Wurm", "Recover 1 hit point each tim eyou cross out a day o nthe time track"),
    RECORD("The Ancient Record", "All connection attempts automatically succeed"),
    SHARD("The Molten Shard", "Add 1 to the HIT range of all monsters.");
    
    private final String name;
    private final Tooltip tooltip;
    private boolean found;

    private Treasure(String name, String tooltip) {
        this.name = name;
        this.found = false;
        this.tooltip = new Tooltip(tooltip);
    }

    public String getName() {
        return name;
    }
    
    public Tooltip getTooltip() {
        return tooltip;
    }
    
    public void setFound() {
        this.found = true;
        Game.postAction(new Action(TREASURE_FOUND));
    }
    
    public boolean isFound() {
        return this.found;
        
    }
    
    
    
}
