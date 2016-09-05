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
public class LocationChangedAction extends Action {
    private final Location location;
    
    public LocationChangedAction(Location location) {
        this.location = location;
    }
    
    public Location getLocation() {
        return location;
    }
    
}
