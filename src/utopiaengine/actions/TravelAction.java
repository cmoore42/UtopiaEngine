/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.actions;

import utopiaengine.Location;

/**
 *
 * @author moorechr
 */
public class TravelAction extends Action {
    private Location destination;

    public TravelAction() {
        super("Travel", "t");
    }
    
    public TravelAction(Location l) {
        super("Travel", "t");
        destination = l;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }
    
    
}
