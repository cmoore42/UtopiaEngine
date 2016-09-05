/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import java.util.ArrayList;
import java.util.List;
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
import static utopiaengine.Event.WEATHER;
import static utopiaengine.Location.State.COSTLY;
import static utopiaengine.Location.State.FREE;
import static utopiaengine.Location.State.USED;
import static utopiaengine.Treasure.BRACELET;
import static utopiaengine.Treasure.MOONLACE;
import static utopiaengine.Treasure.PLATE;
import static utopiaengine.Treasure.RECORD;
import static utopiaengine.Treasure.SCALE;
import static utopiaengine.Treasure.SHARD;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.EVENT_CHANGED;
import static utopiaengine.actions.Action.EventType.LOCATION_CHANGED;

/**
 *
 * @author moorechr
 */

public enum Location {
    PEAK("Halebeard Peak", 1, SEAL, SILVER, PLATE, 0, 1, 3) {
        @Override
        public void initialize(ArrayList<Monster> monsterTable) {
            monsterTable.add(new Monster(1, "Ice Bear", 1, 5));
            monsterTable.add(new Monster(2, "Roving Bandits", 1, 6));
            monsterTable.add(new Monster(3, "Blood Wolves", 2, 6));
            monsterTable.add(new Monster(4, "Horse Eater Hawk", 3, 6));
            monsterTable.add(new Monster(5, "Giant of the Peaks", 4, 6));
        }
    },
    WILDS("The Great Wilds", 2, MIRROR, QUARTZ, BRACELET, 0, 3, 0){
        @Override
        public void initialize(ArrayList<Monster> monsterTable) {
            monsterTable.add(new Monster(1, "Rogue Thief", 2, 5));
            monsterTable.add(new Monster(2, "Blanket of Crows", 1, 6));
            monsterTable.add(new Monster(3, "Hornback Bison", 1, 6));
            monsterTable.add(new Monster(4, "Grassyback Troll", 3, 5));
            monsterTable.add(new Monster(5, "Thunder King", 4, 6, true));            
        }
    },
    MARSHES("Root Strangled Marshes", 3, GATE, GUM, MOONLACE, 0, 2, 4){
        @Override
        public void initialize(ArrayList<Monster> monsterTable) {
            monsterTable.add(new Monster(1, "Gemscale Boa", 1, 5));
            monsterTable.add(new Monster(2, "Ancient Alligator", 2, 6));
            monsterTable.add(new Monster(3, "Land Shark", 2, 6));
            monsterTable.add(new Monster(4, "Abyssal Leech", 3, 6));
            monsterTable.add(new Monster(5, "Dweller in the Tides", 4, 6));           
        }
    },
    CANYON("Glassrock Canyon", 4, CHASSIS, SILICA, SCALE, 0, 2, 4){
        @Override
        public void initialize(ArrayList<Monster> monsterTable) {
            monsterTable.add(new Monster(1, "Feisty Gremlin", 1, 5));
            monsterTable.add(new Monster(2, "Glasswing Drake", 1, 6));
            monsterTable.add(new Monster(3, "Reaching Claws", 2, 6, true));
            monsterTable.add(new Monster(4, "Terrible Wurm", 3, 6));
            monsterTable.add(new Monster(5, "Leviathan Serpent", 4, 6));            
        }
    },
    CITY("Ruined City of the Ancients", 5, LENS, WAX, RECORD, 0, 3, 0){
        @Override
        public void initialize(ArrayList<Monster> monsterTable) {
            monsterTable.add(new Monster(1, "Grave Robbers", 1, 5));
            monsterTable.add(new Monster(2, "Ghost Lights", 1, 6, true));
            monsterTable.add(new Monster(3, "Vengeful Shade", 2, 6, true));
            monsterTable.add(new Monster(4, "Nightmare Crab", 3, 6));
            monsterTable.add(new Monster(5, "The Unnamed", 4, 6, true));            
        }
    },
    MAW("The Fiery Maw", 6, BATTERY, LEAD, SHARD, 0, 1, 3){
        @Override
        public void initialize(ArrayList<Monster> monsterTable) {
            monsterTable.add(new Monster(1, "Minor Imp", 1, 5));
            monsterTable.add(new Monster(2, "Renegade Warlock", 1, 5));
            monsterTable.add(new Monster(3, "Giant Flame Lizard", 2, 5));
            monsterTable.add(new Monster(4, "Spark Elemental", 3, 6, true));
            monsterTable.add(new Monster(5, "Volcano Spirit", 4, 6, true));           
        }
    },
    WORKSHOP("Workshop", 0, null, null, null, 0, 0, 0){
        @Override
        public void initialize(ArrayList<Monster> monsterTable) {
            
        }
    };
    
    private String name;
    private int index;
    private Construct construct;
    private Component component;
    private Treasure treasure;
    private int searchCounter;
    private int[] timeTickDays;
    private ArrayList<Monster> monsterTable;
    private ArrayList<Event> events;
    
    public abstract void initialize(ArrayList<Monster> monsterTable);

    private Location(String name, int index, Construct construct, 
            Component component, Treasure treasure, int day1, int day2, int day3) {
        this.name = name;
        this.index = index;
        this.construct = construct;
        this.component = component;
        this.treasure = treasure;
        this.timeTickDays = new int[3];
        this.timeTickDays[0] = day1;
        this.timeTickDays[1] = day2;
        this.timeTickDays[2] = day3;
        this.monsterTable = new ArrayList<>();
        this.events = new ArrayList<>();
        initialize(monsterTable);
    }

    public String getName() {
        return name;
    }

    public Construct getConstruct() {
        return construct;
    }

    public Component getComponent() {
        return component;
    }

    public Treasure getTreasure() {
        return treasure;
    }
    
    public void travelTo() {
        Player player = Game.getPlayer();
        
        player.setLocation(this);
        searchCounter = 0;
        Game.postAction(new Action(LOCATION_CHANGED, this));
    }

    public int getIndex() {
        return this.index;
    }
    
    public enum State {COSTLY, FREE, USED};
    
    /**
     * Used to determine how to display the search attempt.
     * 
     * @param searchNumber - Which search attempt, 0-5
     * @return Whether the search is free, costly, or used
     */
    public State getState(int searchNumber) {
        if (searchNumber < searchCounter) {
            return USED;
        }
        if (timeTickDays[0] == searchNumber) {
            return COSTLY;
        }
        if (timeTickDays[1] == searchNumber) {
            return COSTLY;
        }
        if (timeTickDays[2] == searchNumber) {
            return COSTLY;
        }
        return FREE;
    }
    
    public void incrementSearchCounter() {
        
        if (searchCounter >= 6) {
            /* Special case - extensive search */
            Game.getTimeTrack().tick();
            if (construct.isFound()) {
                component.increment();
                Game.info("Extensive search found " + component.getName());
            } else {
                construct.setFound();
                Game.info("Extensive search found " + construct.getName());
            }
            return;
        }
        
        if ((timeTickDays[0] == searchCounter) || (timeTickDays[1] == searchCounter) ||
                (timeTickDays[2] == searchCounter)) {
            Game.getTimeTrack().tick();
            if (hasEvent(WEATHER)) {
                Game.info("Foul Weather costs an extra day.");
                Game.getTimeTrack().tick();
            }
        }
        
        ++searchCounter;
        Game.postAction(new Action(LOCATION_CHANGED, this));


    }

    private void initializeMonsterTable() {
        monsterTable = new ArrayList<>();
        
        switch(this) {
            case PEAK:
                monsterTable.add(new Monster(1, "Ice Bear", 1, 5));
                monsterTable.add(new Monster(2, "Roving Bandits", 1, 6));
                monsterTable.add(new Monster(3, "Blood Wolves", 2, 6));
                monsterTable.add(new Monster(4, "Horse Eater Hawk", 3, 6));
                monsterTable.add(new Monster(5, "Giant of the Peaks", 4, 6));
                break;
            case WILDS:
                monsterTable.add(new Monster(1, "Rogue Thief", 2, 5));
                monsterTable.add(new Monster(2, "Blanket of Crows", 1, 6));
                monsterTable.add(new Monster(3, "Hornback Bison", 1, 6));
                monsterTable.add(new Monster(4, "Grassyback Troll", 3, 5));
                monsterTable.add(new Monster(5, "Thunder King", 4, 6, true));
                break;
            case CANYON:
                monsterTable.add(new Monster(1, "Feisty Gremlin", 1, 5));
                monsterTable.add(new Monster(2, "Glasswing Drake", 1, 6));
                monsterTable.add(new Monster(3, "Reaching Claws", 2, 6, true));
                monsterTable.add(new Monster(4, "Terrible Wurm", 3, 6));
                monsterTable.add(new Monster(5, "Leviathan Serpent", 4, 6));
                break;
            case MARSHES:
                monsterTable.add(new Monster(1, "Gemscale Boa", 1, 5));
                monsterTable.add(new Monster(2, "Ancient Alligator", 2, 6));
                monsterTable.add(new Monster(3, "Land Shark", 2, 6));
                monsterTable.add(new Monster(4, "Abyssal Leech", 3, 6));
                monsterTable.add(new Monster(5, "Dweller in the Tides", 4, 6));
                break;
            case CITY:
                monsterTable.add(new Monster(1, "Grave Robbers", 1, 5));
                monsterTable.add(new Monster(2, "Ghost Lights", 1, 6, true));
                monsterTable.add(new Monster(3, "Vengeful Shade", 2, 6, true));
                monsterTable.add(new Monster(4, "Nightmare Crab", 3, 6));
                monsterTable.add(new Monster(5, "The Unnamed", 4, 6, true));
                break;
            case MAW:
                monsterTable.add(new Monster(1, "Minor Imp", 1, 5));
                monsterTable.add(new Monster(2, "Renegade Warlock", 1, 5));
                monsterTable.add(new Monster(3, "Giant Flame Lizard", 2, 5));
                monsterTable.add(new Monster(4, "Spark Elemental", 3, 6, true));
                monsterTable.add(new Monster(5, "Volcano Spirit", 4, 6, true));
                break;
            default:
                break;
        }
    }
    
    public Monster getMonster(int combatLevel) {
        for (Monster m : monsterTable) {
            if (m.getLevel() == combatLevel) {
                return m;
            }
        }
        
        return null;
    }
    
    public void clearEvents() {
        events.clear();
        Game.postAction(new Action(EVENT_CHANGED));
    }
    
    public void addEvent(Event e) {
        events.add(e);
        Game.postAction(new Action(EVENT_CHANGED));
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    public boolean hasEvent(Event e) {
        return (events.contains(e));
        
    }
}
