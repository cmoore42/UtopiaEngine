/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import java.util.Random;
import utopiaengine.actions.DiceRolledAction;

/**
 *
 * @author moorechr
 */
public class Dice {
    private int die1;
    private int die2;
    private Random random;
    
    public Dice() {
        die1 = 1;
        die2 = 1;
        random = new Random();
    }
    
    public void roll() {
        die1 = random.nextInt(6) + 1;
        die2 = random.nextInt(6) + 1;
        Game.postAction(new DiceRolledAction());
    }
    
    public int getDie(int which) {
        if (which == 1) {
            return die1;
        } else {
            return die2;
        }
    }
    
}
