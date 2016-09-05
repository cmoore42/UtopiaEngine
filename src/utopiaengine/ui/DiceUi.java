/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import utopiaengine.Dice;
import utopiaengine.Game;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.DICE_ROLLED;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author moorechr
 */
public class DiceUi extends HBox implements ActionListener {

    private Image[] dieImages;
    private BorderPane die1Pane;
    private BorderPane die2Pane;
    private ImageView die1;
    private ImageView die2;
    
    public DiceUi() {
        super();
        
        dieImages = new Image[6];
        
        dieImages[0] = new Image(getClass().getResourceAsStream("/resources/die1.jpg"));
        dieImages[1] = new Image(getClass().getResourceAsStream("/resources/die2.jpg"));
        dieImages[2] = new Image(getClass().getResourceAsStream("/resources/die3.jpg"));
        dieImages[3] = new Image(getClass().getResourceAsStream("/resources/die4.jpg"));
        dieImages[4] = new Image(getClass().getResourceAsStream("/resources/die5.jpg"));
        dieImages[5] = new Image(getClass().getResourceAsStream("/resources/die6.jpg"));
        
        die1 = new ImageView();
        die2 = new ImageView();
        
        die1Pane = new BorderPane();
        die1Pane.setCenter(die1);
        
        die2Pane = new BorderPane();
        die2Pane.setCenter(die2);
        
        die1.setImage(dieImages[Game.getDice().getDie(1) - 1]);
        die2.setImage(dieImages[Game.getDice().getDie(2) - 1]);
        
        getChildren().addAll(die1Pane, die2Pane);
        
        Game.getInstance().addListener(this);
    }
    
    private void update() {
        Dice dice = Game.getDice();
        
        die1.setImage(dieImages[dice.getDie(1) - 1]);
        die2.setImage(dieImages[dice.getDie(2) - 1]);
    }
    
    public void highlight(int which) {
        if (which == 1) {
            die1Pane.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else {
            die1Pane.setStyle("-fx-border-color: black");
        }
        if (which == 2) {
            die2Pane.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else {
            die2Pane.setStyle("-fx-border-color: black");
        }
    }

    @Override
    public void handleAction(Action a) {
        if (a.getType() == DICE_ROLLED) {
            update();
        }
        
    }

    
    
}
