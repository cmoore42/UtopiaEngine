/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import static utopiaengine.Construct.LENS;
import static utopiaengine.Construct.MIRROR;
import static utopiaengine.Event.FORTUNE;
import utopiaengine.Game;
import utopiaengine.Location;
import static utopiaengine.Location.CANYON;
import static utopiaengine.Location.MARSHES;
import static utopiaengine.Location.MAW;
import static utopiaengine.Location.PEAK;
import static utopiaengine.Tool.ROD;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.PERFECT_ZERO_SEARCH;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author moorechr
 */
public class SearchDialog extends Dialog<Integer> implements ActionListener {
    private int rollNumber;
    private int dieNumber;
    private ButtonType doneButton;
    private Button[] gridSquares;
    private Label resultLabel;
    private Button dowsingRodButton;
    private boolean rodUsed;
    private Location where;
    private boolean constructHelps;
    
    public SearchDialog(Location where) {
        setTitle("Search");
        
        if (where.hasEvent(FORTUNE)) {
            setHeaderText("Searching at " + where.getName() + " with Good Fortune");
        } else {
            setHeaderText("Searching at " + where.getName());
        }

        constructHelps = false;
        if (LENS.isActivated()) {
            if ((where == CANYON) && (where == MARSHES)) {
                Game.info("The Scrying Lens makes your searches more effective here.");
                constructHelps = true;
            }
        }
        if (MIRROR.isActivated()) {
            if ((where == PEAK) && (where == MAW)) {
                Game.info("The Hermetic Mirror makes your searches more effective here.");
                constructHelps = true;
            }
        }
        
        rollNumber = 1;
        dieNumber = 1;
        rodUsed = false;
        this.where = where;
        
        VBox outer = new VBox();
        
        DiceUi dice = new DiceUi();
        
        GridPane middle = new GridPane();
        gridSquares = new Button[6];
        for (int row=0; row<2; row++) {
            for (int col=0; col<3; col++) {
                int buttonIndex = row*3+col;
                gridSquares[buttonIndex] = new Button();
                gridSquares[buttonIndex].setText(" ");
                gridSquares[buttonIndex].setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        Button source = (Button)event.getSource();
                        source.setText(Integer.toString(Game.getDice().getDie(dieNumber)));
                        if (dieNumber == 1) {
                            ++dieNumber;
                        } else {
                            ++rollNumber;
                            dieNumber = 1;
                            if (rollNumber < 4) {
                                Game.getDice().roll();
                            }
                        }
                        source.setDisable(true);
                        dice.highlight(dieNumber);

                        
                        if (rollNumber > 3) {
                            getDialogPane().lookupButton(doneButton).setDisable(false);
                            resultLabel.setText("Result: " + calculateResult());
                            if (ROD.isCharged()) {
                                dowsingRodButton.setDisable(false);
                            }
                            dowsingRodButton.setOnAction(new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    rodUsed = true;
                                    ROD.setCharged(false);
                                    dowsingRodButton.setDisable(true);
                                    resultLabel.setText("Result: " + calculateResult());
                                    Game.info("You use the Dowsing Rod to reduce the search result to " + calculateResult());
                                }
                                
                            });
                        }

                    }
                    
                });
                middle.add(gridSquares[buttonIndex], col, row);
            }
        }
        
        resultLabel = new Label();
        
        dowsingRodButton = new Button("Use Dowsing Rod");
        dowsingRodButton.setDisable(true);
        dowsingRodButton.setTooltip(ROD.getTooltip());

        doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        
        outer.getChildren().addAll(dice, middle, resultLabel, dowsingRodButton);
        
        getDialogPane().getButtonTypes().add(doneButton);
        getDialogPane().setContent(outer);
        getDialogPane().lookupButton(doneButton).setDisable(true);

        
        setResultConverter(new Callback<ButtonType, Integer>() {

            @Override
            public Integer call(ButtonType param) {
                checkForPerfectZero();
                return calculateResult();
            }
            
        });
        
        Game.getInstance().addListener(this);
        
        if (Game.isGameOver()) {
            getDialogPane().lookupButton(doneButton).setDisable(false);
        }
                
        Game.getDice().roll();
        dice.highlight(1);
    }
    
    private int calculateResult() {
        int result;
        int first;
        int second;

        try {
            first = Integer.parseInt(gridSquares[0].getText()) * 100;
            first += Integer.parseInt(gridSquares[1].getText()) * 10;
            first += Integer.parseInt(gridSquares[2].getText());

            second = Integer.parseInt(gridSquares[3].getText()) * 100;
            second += Integer.parseInt(gridSquares[4].getText()) * 10;
            second += Integer.parseInt(gridSquares[5].getText());

            result = first - second;

            /* If the result is negative there's no point in applying effects
             * to make it more negative.
             */
            if (result >= 0) {
                if (rodUsed) {
                    result -= 100;
                    if (result < 1) {
                        result = 1;
                    }
                }

                if (where.hasEvent(FORTUNE)) {
                    result -= 10;
                    if (result < 0) {
                        result = 0;
                    }
                }

                if (constructHelps) {
                    result -= 10;
                    if (result < 0) {
                        result = 0;
                    }
                }
            }
        } catch (NumberFormatException e) {
            /* 
             * This is a tricky one.  calculateResult() should only be called
             * after all three rolls are done, which means all the boxes are
             * filled in.  But if the user closes the search box rather than
             * going through the search then we can get here with blank boxes.
             * 
             * The game mechanics don't allow ending a search early, so one
             * solution would be to prevent the closing of the dialog early - 
             * but that seems like a poor interface design choice.  There 
             * may be times the user just wants to shut down and quit.
             *
             * The solution here is that if the box is closed early, causing
             * us to catch this exception, we'll just return a really big 
             * number for the search value.  It's not ideal, but it's the best
             * I have.
             */
            result = 999;
        }
        
        return result;    
    }
    
    private void checkForPerfectZero() {
        int result;
        int first;
        int second;

        /* We can't use calculateResult() here, because we want to know
         * if it was a perfect zero without modifications.
         */
        try {
            first = Integer.parseInt(gridSquares[0].getText()) * 100;
            first += Integer.parseInt(gridSquares[1].getText()) * 10;
            first += Integer.parseInt(gridSquares[2].getText());

            second = Integer.parseInt(gridSquares[3].getText()) * 100;
            second += Integer.parseInt(gridSquares[4].getText()) * 10;
            second += Integer.parseInt(gridSquares[5].getText());

            result = first - second;

            if (result == 0) {
                Game.postAction(new Action(PERFECT_ZERO_SEARCH));
            }
        } catch (NumberFormatException e) {
            
        }
    }

    @Override
    public void handleAction(Action a) {
        switch(a.getType()) {
            case END_OF_WORLD:
                close();
                break;
            case PLAYER_HEALTH_CHANGED:
                if (Game.getPlayer().isDead() || Game.getPlayer().isUnconsious()) {
                    /*
                    dowsingRodButton.setDisable(true);
                            */
                    close();
                }
                break;
        }
    }    
}
