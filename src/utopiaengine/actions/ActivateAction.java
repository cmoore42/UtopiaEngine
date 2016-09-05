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
public class ActivateAction extends Action {
    private final Construct construct;
    
    public ActivateAction(Construct construct) {
        this.construct = construct;
    }
    
    public Construct getConstruct() {
        return construct;
    }
}
