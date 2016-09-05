/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import static utopiaengine.Treasure.BRACELET;
import static utopiaengine.Treasure.SCALE;
import utopiaengine.actions.EndOfWorldAction;
import utopiaengine.actions.TimeTrackerChangedAction;

/**
 *
 * @author moorechr
 */
public class TimeTrack {
    private int currentTime;
    private int godsHand;
    private int daysExtended;
    private boolean outOfTime;

    public TimeTrack() {
        currentTime = 0;
        godsHand = 0;
        daysExtended = 0;
        outOfTime = false;
    }

    public int getCurrentTime() {
        return currentTime;
    }
    
    public void powerGodsHand() {
        ++godsHand;
        if (godsHand > 6) {
            godsHand = 6;
        }
        Game.postAction(new TimeTrackerChangedAction());
    }
    
    public int getGodsHand() {
        return godsHand;
    }
    
    public void extendDay() {
        if ((godsHand > 2) && (daysExtended < 8)) {
            godsHand -= 3;
            ++daysExtended;
            
            Game.info("You put off doomsday for another day.");
            Game.info("Total extension is now " + daysExtended);
            Game.postAction(new TimeTrackerChangedAction());
        }
    }
    
    public int getDaysExtended() {
        return daysExtended;
    }
    
    public boolean outOfTime() {
        return outOfTime;
    }
    
    public void tick() {
        if (currentTime < 22) {
            ++currentTime;
        }
        
        /* Has the world ended? */
        if ((currentTime - daysExtended) >= 15) {
            Game.postAction(new EndOfWorldAction());
            outOfTime = true;
        }
        
        /* Do we need to fire an event? */
        switch(currentTime) {
            case 2:
            case 5:
            case 8:
            case 11:
            case 14:
            case 17:
            case 20:
                fireEvent();
            default:
        }
        
        if (BRACELET.isFound()) {
            powerGodsHand();
            Game.info("The Bracelet of Ios adds power to the God's Hand.");
        }
        
        if (SCALE.isFound()) {
            Game.getPlayer().heal();
            Game.info("The Scale of the Infinity Wurm heals you.");
        }
        
        Game.postAction(new TimeTrackerChangedAction());
    }
    
    private void fireEvent() {
        for (Location l : Location.values()) {
            l.clearEvents();
        }
        
        for (Event e : Event.values()) {
            Game.getDice().roll();
            for (Location l : Location.values()) {
                if (l.getIndex() == Game.getDice().getDie(1)) {
                    l.addEvent(e);
                    Game.info(l.getName() + " gets event " + e.getName());
                }
            }
        }        
    }
    
    
    
}
