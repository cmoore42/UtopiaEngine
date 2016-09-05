/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.actions;

/**
 *
 * @author cmoore
 */
public class ThrowAwayAction extends Action {
    private int which;
    
    public ThrowAwayAction(int die) {
        which = die;
    }
    
    public int getWhich() {
        return which;
    }
    
}
