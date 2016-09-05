/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import static utopiaengine.Location.WORKSHOP;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.PLAYER_HEALTH_CHANGED;

/**
 *
 * @author moorechr
 */

public class Player {
    private int damage;
    private Location location;
    
    Player() {
        location = WORKSHOP;
        damage = 0;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
        /* Handle travel results */
    }
    
    public boolean isUnconsious() {
        return (damage == 6);
    }
    
    public boolean isDead() {
        return (damage > 6);
    }
    
    public void dealDamage() {
        ++damage;
        Game.postAction(new Action(PLAYER_HEALTH_CHANGED));
    }
    
    public void heal() {
        if (damage > 0) {
            --damage;
            Game.postAction(new Action(PLAYER_HEALTH_CHANGED));
        }
    }

    public int getDamage() {
        return damage;
        
    }
    
}
