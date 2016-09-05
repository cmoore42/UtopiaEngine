/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.STORES_CHANGED;

/**
 *
 * @author moorechr
 */
public enum Component {
    LEAD ("Lead"),
    QUARTZ ("Quartz"),
    SILICA("Silica"),
    GUM("Gum"),
    WAX("Wax"),
    SILVER("Silver");
    
    private final String name;
    private int quantity;
    
    private Component(String name) {
        this.name = name;
        this.quantity = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void increment() {
        if (quantity < 4) {
            ++quantity;
            Game.postAction(new Action(STORES_CHANGED));
        }
    }
    
    public void decrement() {
        if (quantity > 0) {
            --quantity;
            Game.postAction(new Action(STORES_CHANGED));
        }
    }
    
}
