/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.actions;

/**
 *
 * @author moorechr
 */
public class Action {
    protected String name;
    protected String shortcut;

    public Action() {
    }

    public Action(String name, String shortcut) {
        this.name = name;
        this.shortcut = shortcut;
    }

    public String getShortcut() {
        return shortcut;
    }
    
    
    public String getName() {
        return name;
    }
    
}
