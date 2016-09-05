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
public class SearchAction extends Action {
    private Location location;

    public SearchAction() {
        super("Search", "s");
    }

    public SearchAction(Location location) {
        super("Search", "s");
        this.location = location;
    }

    
    public Location getLocation() {
        return location;
    }
    
    
}
