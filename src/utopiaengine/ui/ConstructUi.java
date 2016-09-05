/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import utopiaengine.Construct;
import static utopiaengine.Construct.BATTERY;
import static utopiaengine.Construct.SEAL;
import utopiaengine.Game;
import static utopiaengine.Location.WORKSHOP;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.ActivateAction;
import utopiaengine.actions.ConstructChangedAction;
import utopiaengine.actions.EndOfWorldAction;
import utopiaengine.actions.TravelAction;

/**
 *
 * @author moorechr
 */
public class ConstructUi extends HBox implements ActionListener {
    private Label constructState;
    private Button activateButton;
    private Construct construct;
    
    private static PseudoClass FOUND = PseudoClass.getPseudoClass("found");
    private static PseudoClass ACTIVATED = PseudoClass.getPseudoClass("activated");
    
    public ConstructUi(Construct construct) {
        this.construct = construct;
        constructState = new Label();
        activateButton = new Button("Activate");
        activateButton.setDisable(true);
        activateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Game.postAction(new ActivateAction(construct));
            }
                
        });
        getChildren().addAll(constructState, activateButton);

        getStyleClass().add("boxed");
        getStyleClass().add("missing");
        
        Tooltip tooltip = construct.getTooltip();
        Tooltip.install(this, tooltip);
        
        
        Game.getInstance().addListener(this);
        
        update();
    }
    
    
    private void update() {

        constructState.setText(construct.getName() + ": " + construct.getState().getName());
        switch(construct.getState()) {
            case MISSING:
                getStyleClass().clear();
                getStyleClass().add("boxed");
                getStyleClass().add("missing");
                break;
            case FOUND:
                getStyleClass().clear();
                getStyleClass().add("boxed");
                getStyleClass().add("found");
                break;
            case ACTIVATED:
                getStyleClass().clear();
                getStyleClass().add("boxed");
                getStyleClass().add("activated");
                break;
        }
        if (Game.getPlayer().getLocation() == WORKSHOP) {
            if (construct.isFound()) {
                activateButton.setDisable(false);
            }
        } else {
            activateButton.setDisable(true);
        }
        if (construct.isActivated()) {
            if ((construct == BATTERY) || (construct == SEAL)) {
                activateButton.setDisable(false);
                activateButton.setText("Use");
            } else {
                activateButton.setVisible(false);
            }
        }

    }

    @Override
    public void handleAction(Action a) {
        if (a instanceof ConstructChangedAction) {
            update();
        } else if (a instanceof TravelAction) {
            update();
        } else if (a instanceof EndOfWorldAction) {
            activateButton.setDisable(true);
        }
    }
    
}
