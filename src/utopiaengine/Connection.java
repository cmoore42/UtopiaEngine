/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import static utopiaengine.Component.GUM;
import static utopiaengine.Component.LEAD;
import static utopiaengine.Component.QUARTZ;
import static utopiaengine.Component.SILICA;
import static utopiaengine.Component.SILVER;
import static utopiaengine.Component.WAX;
import static utopiaengine.Construct.BATTERY;
import static utopiaengine.Construct.CHASSIS;
import static utopiaengine.Construct.GATE;
import static utopiaengine.Construct.LENS;
import static utopiaengine.Construct.MIRROR;
import static utopiaengine.Construct.SEAL;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.CanConnectAction;
import utopiaengine.actions.ConnectionCompleteAction;
import utopiaengine.actions.ConstructChangedAction;
import utopiaengine.actions.StoresChangedAction;

/**
 *
 * @author moorechr
 */
public enum Connection implements ActionListener {
    SILVER_CONNECTION(SILVER, SEAL, LENS),
    QUARTZ_CONNECTION(QUARTZ, SEAL, CHASSIS),
    SILICA_CONNECTION(SILICA, SEAL, MIRROR),
    GUM_CONNECTION(GUM, CHASSIS, GATE),
    LEAD_CONNECTION(LEAD, CHASSIS, BATTERY),
    WAX_CONNECTION(WAX, MIRROR, GATE);
    
    private Component component;
    private Construct leftConstruct;
    private Construct rightConstruct;
    private boolean connected;
    private int connectionCost;
    
    private Connection(Component component, Construct left, Construct right) {
        this.component = component;
        leftConstruct = left;
        rightConstruct = right;
        this.connected = false;
        
        Game.getInstance().addListener(this);
    }
    
    public Component getComponent() {
        return component;
    }
    
    public void connect(int cost) {
        this.connected = true;
        this.connectionCost = cost;
        Game.postAction(new ConnectionCompleteAction(this));
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public int getCost() {
        return this.connectionCost;
    }

    @Override
    public void handleAction(Action a) {
        
        /* I'm interested in constructs being activated and components being found */
        if ((a instanceof StoresChangedAction) || (a instanceof ConstructChangedAction)) {
            if ((component.getQuantity() > 0) &&
                (leftConstruct.isActivated()) &&
                (rightConstruct.isActivated()) &&
                (!connected)) {
                Game.postAction(new CanConnectAction(this, true));
            } else {
                Game.postAction(new CanConnectAction(this, false));
            }
        }

    }
    
}
