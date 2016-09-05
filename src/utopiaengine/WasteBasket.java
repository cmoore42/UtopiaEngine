/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import java.util.ArrayList;
import java.util.List;
import utopiaengine.actions.WasteBasketChangedAction;

/**
 *
 * @author cmoore
 */
public class WasteBasket {
    private ArrayList<Integer> contents;
    
    public WasteBasket() {
        contents = new ArrayList<>();
    }
    
    public void throwAway(int number) {
        if (contents.size() >= 10) {
            return;
        }
        contents.add(number);
        Game.postAction(new WasteBasketChangedAction());
    }
    
    public List<Integer> getContents() {
        return contents;
    }
    
    public boolean isFull() {
        return (contents.size() >= 10);
    }
}
