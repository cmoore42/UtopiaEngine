/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import utopiaengine.Game;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.DelayDoomsdayAction;
import utopiaengine.actions.EndOfWorldAction;
import utopiaengine.actions.TimeTrackerChangedAction;

/**
 *
 * @author moorechr
 */
public class TimeTrackUi extends GridPane implements ActionListener {

    
    private Image skullWhite;
    private Image skullBlack;
    private Image dayEmpty;
    private Image dayEvent;
    private Image dayDone;
    private Image handPower;
    private Image handPowerCharged;
    
    private ImageView[] timeImages;
    private ImageView[] skullImages;
    private ImageView[] handImages;
    
    private Button extendButton;
    
    public TimeTrackUi() {
        super();
        
        skullWhite = new Image(getClass().getResourceAsStream("/resources/skull_white.jpg"));
        skullBlack = new Image(getClass().getResourceAsStream("/resources/skull_black.jpg"));
        dayEmpty = new Image(getClass().getResourceAsStream("/resources/day_empty.jpg"));
        dayEvent = new Image(getClass().getResourceAsStream("/resources/day_event.jpg"));
        dayDone = new Image(getClass().getResourceAsStream("/resources/day_done.jpg"));
        handPower = new Image(getClass().getResourceAsStream("/resources/hand_white.jpg"));
        handPowerCharged = new Image(getClass().getResourceAsStream("/resources/hand_black.jpg"));
        
        timeImages = new ImageView[22];
        for (int i=0; i<22; i++) {
            timeImages[i] = new ImageView();
            if (((i-1) % 3) == 0) {
                timeImages[i].setImage(dayEvent);
            } else {
                timeImages[i].setImage(dayEmpty);
            }
        }
        addRow(0, timeImages);
        
        skullImages = new ImageView[8];
        for (int i=0; i<8; i++) {
            skullImages[i] = new ImageView(skullWhite);
            add(skullImages[i], i+14, 1);
        }
        
        handImages = new ImageView[6];
        for (int i=0; i<6; i++) {
            handImages[i] = new ImageView(handPower);
        }
        addRow(2, handImages);
        
        extendButton = new Button("Delay Doomsday");
        extendButton.setDisable(true);
        add(extendButton, 0, 3, 5, 1);
        extendButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.postAction(new DelayDoomsdayAction());
            }
            
        });
        
        getStyleClass().add("boxed");
        
        Game.getInstance().addListener(this);
        
    }
    
    private void update() {
        int currentTime = Game.getTimeTrack().getCurrentTime();
            
        for (int i=0; i<currentTime; i++) {
            timeImages[i].setImage(dayDone);
        }
        
        for (int i=0; i<Game.getTimeTrack().getDaysExtended(); i++) {
            skullImages[i].setImage(skullBlack);
        }
        
        for (int i=0; i<6; i++) {
            if (i < Game.getTimeTrack().getGodsHand()) {
                handImages[i].setImage(handPowerCharged);
            } else {
                handImages[i].setImage(handPower);
            }
        }
        
        if (Game.getTimeTrack().getGodsHand() >= 3) {
            extendButton.setDisable(false);
        } else {
            extendButton.setDisable(true);
        }
    }

    @Override
    public void handleAction(Action a) {
        if (a instanceof TimeTrackerChangedAction) {
            update();
            
        } else if (a instanceof EndOfWorldAction) {
            extendButton.setDisable(true);
        }
    }
    
}
