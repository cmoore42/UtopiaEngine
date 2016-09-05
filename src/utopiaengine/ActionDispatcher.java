/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javafx.application.Platform;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.SHUTDOWN;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author moorechr
 */
public class ActionDispatcher extends Thread {
    private static ActionDispatcher instance = null;
    private final Queue<Action> actionQueue;
    private static ArrayList<ActionListener> listeners;
    
    private ActionDispatcher() {
        actionQueue = new LinkedList<>();
        listeners = new ArrayList<>();
    }
    
    public static ActionDispatcher getInstance() {
        if (instance == null) {
            instance = new ActionDispatcher();
            instance.start();
        }
        return instance;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                synchronized(actionQueue) {
                    while (actionQueue.isEmpty()) {
                        actionQueue.wait();
                    }
                    Action a = actionQueue.remove();
                    
                    if (a.getType() == SHUTDOWN) {
                        break;
                    }

                    synchronized(listeners) {
                        for (ActionListener l : listeners) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    l.handleAction(a);
                                }
                            });
                        }
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    
    public void postAction(Action a) {
        synchronized (actionQueue) {
            actionQueue.add(a);

            actionQueue.notify();
        }
    }
    
    public void addListener(ActionListener l) {
        synchronized(listeners) {
            System.out.println("Adding " + l.getClass().getName() + " as a listener.");
            listeners.add(l);
        }
    }
    
}
