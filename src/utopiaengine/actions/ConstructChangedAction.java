/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.actions;

import utopiaengine.Construct;

/**
 *
 * @author moorechr
 */
public class ConstructChangedAction extends Action {
    private Construct construct;
    
    public ConstructChangedAction(Construct c) {
        construct = c;
    }
    
    public Construct getConstruct() {
        return construct;
    }
    
}
