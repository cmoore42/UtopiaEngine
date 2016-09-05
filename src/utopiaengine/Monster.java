/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

/**
 *
 * @author cmoore
 */
public class Monster {
    private int level;
    private String name;
    private int attack;
    private int hit;
    private boolean spirit;

    public Monster(int level, String name, int attack, int hit) {
        this.level = level;
        this.name = name;
        this.attack = attack;
        this.hit = hit;
        this.spirit = false;
    }
    
    public Monster(int level, String name, int attack, int hit, boolean spirit) {
        this(level, name, attack, hit);
        this.spirit = spirit;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public boolean isSpirit() {
        return spirit;
    }

    public void setSpirit(boolean spirit) {
        this.spirit = spirit;
    }

    
    
    
}
