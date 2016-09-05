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
public class InfoAction extends Action {
    private String line;
    
    public InfoAction(String line) {
        this.line = line;
    }
    
    public String getLine() {
        return this.line;
    }
    
}
