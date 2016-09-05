/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import java.util.Optional;
import static utopiaengine.Event.MONSTERS;
import static utopiaengine.Location.WORKSHOP;
import static utopiaengine.Treasure.MOONLACE;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.ActivateAction;
import utopiaengine.actions.DelayDoomsdayAction;
import utopiaengine.actions.EndOfWorldAction;
import utopiaengine.actions.EngineActivatedAction;
import utopiaengine.actions.InfoAction;
import utopiaengine.actions.PerfectZeroSearchAction;
import utopiaengine.actions.PlayerHealthChanged;
import utopiaengine.actions.RestAction;
import utopiaengine.actions.SearchAction;
import utopiaengine.actions.ShutdownAction;
import utopiaengine.actions.ThrowAwayAction;
import utopiaengine.actions.TravelAction;
import utopiaengine.ui.ActivateDialog;
import utopiaengine.ui.CombatDialog;
import utopiaengine.ui.EndGameDialog;
import utopiaengine.ui.SearchDialog;

/**
 *
 * @author moorechr
 */
public class Game implements ActionListener {
    private static Game instance = null;
    private static Dice dice;
    private static TimeTrack timeTrack;
    private static Player player;
    private static ActionDispatcher dispatcher;
    private static WasteBasket wasteBasket;
    private static boolean engineActivated = false;
    private static int perfectSearches = 0;
    private static boolean gameOver = false;
    
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        
        return instance;
    }
    
    public void shutdown() {
        postAction(new ShutdownAction());
    }
    
    public static Player getPlayer() {
        return player;
    }
    
    public static TimeTrack getTimeTrack() {
        return timeTrack;
    }
    
    public static Dice getDice() {
        return dice;
    }
    
    public static WasteBasket getWasteBasket() {
        return wasteBasket;
    }
    
    public static boolean isGameOver() {
        return gameOver;
    }

    private Game() {
        dice = new Dice();
        timeTrack = new TimeTrack();
        player = new Player();
        dispatcher = ActionDispatcher.getInstance();
        wasteBasket = new WasteBasket();
        addListener(this);
    }
    
    public void addListener(ActionListener l) {
        dispatcher.addListener(l);
    }
    
    public static void postAction(Action a) {
        dispatcher.postAction(a);
    }
    
    @Override
    public  void handleAction(Action a) {
        
        if (a instanceof TravelAction) {
            TravelAction t = (TravelAction) a;
            
            if (t.getDestination() == player.getLocation()) {
                t.setDestination(WORKSHOP);
            }
            
            t.getDestination().travelTo();
            info("Traveling to " + t.getDestination().getName());
        }
        
        if (a instanceof SearchAction) {
            doSearch();
        }
        
        if (a instanceof ActivateAction) {
            ActivateAction b = (ActivateAction) a;
            ActivateDialog d = new ActivateDialog(b.getConstruct());
            d.showAndWait();
        }
        
        if (a instanceof RestAction) {
            info("Resting for a day.");
            player.heal();
            timeTrack.tick();
        }
        
        if (a instanceof DelayDoomsdayAction) {
            timeTrack.extendDay();
        }
        
        if (a instanceof ThrowAwayAction) {
            ThrowAwayAction t = (ThrowAwayAction) a;
            
            wasteBasket.throwAway(t.getWhich());
        }
        
        if (a instanceof EndOfWorldAction) {
            info("The world has ended");
            /* Final scoring stuff here */
            reportFinalScore();
        }
        
        if (a instanceof EngineActivatedAction) {
            info("You've saved the world!");
            engineActivated = true;
            gameOver = true;
            reportFinalScore();
        }
        
        if (a instanceof PerfectZeroSearchAction) {
            ++perfectSearches;
        }
        
        if (a instanceof PlayerHealthChanged) {
            if (player.isDead()) {
                info("You've died.");
                gameOver = true;
                reportFinalScore();
            }
        }
    }
    
    private static void doSearch() {
        player.getLocation().incrementSearchCounter();
        
        if (gameOver) {
            return;
        }
        
        SearchDialog d = new SearchDialog(player.getLocation());
        Optional<Integer> result = d.showAndWait();

        if (result.isPresent()) {
            int searchResult = result.get();

            info("Search result is " + searchResult);
 
            /* Figure out the outcome */
            int combatLevel;
            Construct construct = player.getLocation().getConstruct();
            Component component = player.getLocation().getComponent();

            if (searchResult == 0) {
                /* Perfect search */
                if (construct.isActivated()) {
                    info("Perfect search, but you've already activated the " + construct.getName());
                    info("You get two " + component.getName());
                    component.increment();
                    component.increment();
                } else {
                    info("Perfect search - you found and activated " + construct.getName());
                    construct.activate();
                    for (int i=0; i<5; i++) {
                        Game.getTimeTrack().powerGodsHand();
                    }
                    info("Powered up God's Hand.");
                }
            } else if (searchResult > 0) {
                /* Positive */
                if (searchResult < 11) {
                    /* Find a Construct */
                    if (construct.isFound()) {
                        info("Construct already found");
                        component.increment();
                        component.increment();
                        info("You found two " + component.getName());
                    } else {
                        info("You found " + construct.getName());
                        construct.setFound();
                    }
                } else if (searchResult < 100) {
                    /* Find a Component */
                    if (component.getQuantity() < 5) {
                        info("You found " + component.getName());
                        component.increment();
                    }
                } else {
                    /* Combat */
                    combatLevel = searchResult / 100;
                    doCombat(combatLevel);
                }
            } else {
                /* Negative */
                combatLevel = (99-searchResult) / 100;
                doCombat(combatLevel);
            }
        } else {
            info("No result");
        }
    }
    
    public static void doCombat(int combatLevel) {
        
        if (MOONLACE.isFound()) {
            info("Shimmering Moonlace hides you, no combat.");
            return;
        }
        
        if (player.getLocation().hasEvent(MONSTERS)) {
            combatLevel += 2;
            if (combatLevel > 5) {
                combatLevel = 5;
            }
            info("Combat incresed to level " + combatLevel + " due to Active Monsters.");
        }
        info("Level " + combatLevel + " combat.");
        CombatDialog d = new CombatDialog(combatLevel);
        
        d.showAndWait();
        
        if (!player.isDead() && !player.isUnconsious()) {
            dice.roll();
            if (dice.getDie(1) >= combatLevel) {
                /* Monster dropped something */
                if (combatLevel < 5) {
                    /* Got a component */
                    Component c = player.getLocation().getComponent();
                    c.increment();
                    info("Monster dropped " + c.getName());
                } else {
                    /* Got a treasure */
                    Treasure t = player.getLocation().getTreasure();
                    t.setFound();
                    info("Monster dropped " + t.getName() + "!");
                }
            }
        }
    }
    
    public static void info(String line) {
        postAction(new InfoAction(line));
    }
    
    private void reportFinalScore() {
        StringBuilder summary = new StringBuilder();
        int score = 0;
        int totalScore = 0;
        int constructsFound = 0;
        int treasuresFound = 0;
        int constructsActivated = 0;
        int connectionsCompleted = 0;
        int toolsCharged = 0;
        int healthRemaining = 0;
        int daysLeft = 0;
        
        for (Construct c : Construct.values()) {
            if (c.isFound()) {
                ++constructsFound;
            }
            if (c.isActivated()) {
                ++constructsActivated;
            }
        }
        
        for (Treasure t : Treasure.values()) {
            if (t.isFound()) {
                ++treasuresFound;
            }
        }
        
        for (Connection c : Connection.values()) {
            if (c.isConnected()) {
                ++connectionsCompleted;
            }
        }
        
        for (Tool t : Tool.values()) {
            if (t.isCharged()) {
                ++toolsCharged;
            }
        }
        
        healthRemaining = 6 - player.getDamage();
        if (healthRemaining < 0) {
            healthRemaining = 0;
        }
        
        daysLeft = 14 - timeTrack.getCurrentTime() + timeTrack.getDaysExtended();
        if (daysLeft < 0) {
            daysLeft = 0;
        }
        
        summary.append("Final score:\n");
        score = constructsFound * 10;
        summary.append(constructsFound + " constructs found = " + score + "\n");
        totalScore += score;
        score = perfectSearches * 20;
        summary.append(perfectSearches + " unmodified perfect zero searches = " + score + "\n");
        totalScore += score;
        score = treasuresFound * 10;
        summary.append(treasuresFound + " treasures found = " + score + "\n");
        totalScore += score;
        score = constructsActivated * 5;
        summary.append(constructsActivated + " constructs activated = " + score + "\n");
        totalScore += score;
        score = connectionsCompleted * 5;
        summary.append(connectionsCompleted + " connections completed = " + score + "\n");
        totalScore += score;
        score = toolsCharged * 10;
        summary.append(toolsCharged + " tools charged = " + score + "\n");
        totalScore += score;
        score = healthRemaining;
        summary.append(healthRemaining + " health remaining = " + score + "\n");
        totalScore += score;
        if (engineActivated) {
            score = 50;
            summary.append("Utopia Engine Activated = 50\n");
            totalScore += score;
        }
        score = daysLeft * 5;
        summary.append(daysLeft + " days remaining = " + score + "\n");
        totalScore += score;
        summary.append("Final Score: " + totalScore + "\n");
        
        EndGameDialog d = new EndGameDialog(summary.toString());
        
        d.showAndWait();
        
    }
}
