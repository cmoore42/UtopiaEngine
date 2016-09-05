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
import static javafx.geometry.Pos.CENTER;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import utopiaengine.Construct;
import utopiaengine.Event;
import utopiaengine.Game;
import utopiaengine.Location;
import static utopiaengine.Tool.CHARM;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.EndOfWorldAction;

/**
 *
 * @author moorechr
 */
public class ActivateDialog extends Dialog<Integer> implements ActionListener {
    private Construct construct;
    private GridPane pane;
    private DiceUi dice;
    private ButtonType doneButton;
    private ActivationField firstAttempt;
    private ActivationField secondAttempt;
    private VBox energyBox;
    private Label topTotal;
    private Label bottomTotal;
    private Label charmTotal;
    private Label visionTotal;
    private Label overallTotal;
    private Button charmButton;
    private boolean charmUsed = false;
    private boolean fleetingVision = false;
    
    private int dieNumber;

    @Override
    public void handleAction(Action a) {
        if (a instanceof EndOfWorldAction) {
            firstAttempt.disable();
            secondAttempt.disable();
            charmButton.setDisable(true);
        }
    }
    
    private class ActivationField extends HBox {
        private VBox[] column;
        private Button[] rolls;
        private Label[] results;
        private boolean first;
        private boolean done;
        private int energy;
        private ActivateDialog parent;
        
        public ActivationField(boolean first, ActivateDialog parent) {
            super();
            this.parent = parent;
            
            column = new VBox[4];
            rolls = new Button[8];
            results = new Label[4];
            
            this.done = false;
            
            int nextButton = 0;
            int nextLabel = 0;
            for (int c=0; c<4; c++) {
                column[c] = new VBox();
                
                rolls[nextButton] = new Button();
                column[c].getChildren().add(rolls[nextButton]);
                ++nextButton;
                
                rolls[nextButton] = new Button();
                column[c].getChildren().add(rolls[nextButton]);
                ++nextButton;
                
                results[nextLabel] = new Label();
                column[c].getChildren().add(results[nextLabel]);
                nextLabel++;
            }

            
            for (int i=0; i<8; i++) {
                rolls[i].setPrefSize(25.0, 25.0);
                rolls[i].setDisable(!first);
                rolls[i].setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        Button source = (Button)event.getSource();
                        int dieValue;
   
                        dieValue = Game.getDice().getDie(dieNumber);
                        
                        source.setText(Integer.toString(dieValue));
                        source.setDisable(true);
                        
                        ++dieNumber;
                        if (dieNumber > 2) {
                            Game.getDice().roll();
                            dieNumber = 1;
                        }
                        dice.highlight(dieNumber);
                        
                        if (update()) {
                            done = true;
                            int totalEnergy = updateNumbers();
                            /*
                             * Ugly bit of code to determine if Fleeting Vision is active
                             * at the region this construct came from.
                           
                            
                            boolean fleetingVision = false;
                            for (Location l : Location.values()) {
                                if ((l.getConstruct() == construct) && (l.hasEvent(Event.VISION))) {
                                    fleetingVision = true;
                                    Game.info("You get an extra energy from FleetingVision");
                                }
                            }
                            */
                            
                            if (first) {
                                /* Do we have enough energy? */
                                /*int totalEnergy = 0;

                                totalEnergy = energy;
                                
                                if (fleetingVision) {
                                    ++totalEnergy;
                                }
                                
                                if (charmUsed) {
                                    totalEnergy += 2;
                                }
                                */
                                
                                if (totalEnergy < 4) {
                                    /* Spend a day */
                                    Game.getTimeTrack().tick();
                                    
                                    /* Start the second attempt */
                                    Game.info(totalEnergy + " energy generated on day one, starting day two.");
                                    
                                    secondAttempt.enable();
                                } else {
                                    /* We're done */
                                    construct.activate();
                                    Game.info("You generated " + totalEnergy + " energy, " + construct.getName() + " activated.");
                                    if (totalEnergy > 4) {
                                        int excess = totalEnergy-4;
                                        Game.info(excess + " extra energy send to the God's Hand");
                                        for (int i=0; i<excess; i++) {
                                            Game.getTimeTrack().powerGodsHand();
                                        }
                                    }
                                }
                            } else {
                                Game.info("Second attempt done");
                                
                                /* Do we have enough energy? */
                                
                                /*int totalEnergy = firstAttempt.getEnergy();
                                
                                totalEnergy += energy;
                                
                                if (fleetingVision) {
                                    ++totalEnergy;
                                }
                                
                                if (charmUsed) {
                                    totalEnergy += 2;
                                }
                                */
                                                                
                                if (totalEnergy < 4) {
                                    /* Spend a day */
                                    Game.getTimeTrack().tick();
                                    construct.activate();
                                    /* Start the second attempt */
                                    Game.info("Taking one more day to activate construct");
                                } else {
                                    /* We're done */
                                    construct.activate();
                                    Game.info("You generated " + totalEnergy + " energy, " + construct.getName() + " activated.");
                                    if (totalEnergy > 4) {
                                        int excess = totalEnergy - 4;
                                        Game.info(excess + " extra energy send to the God's Hand");
                                        for (int i=0; i<excess; i++) {
                                            Game.getTimeTrack().powerGodsHand();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                });
            }
            
            for (int i=0; i<4; i++) {
                results[i].setPrefSize(25.0, 25.0);
                results[i].setAlignment(CENTER);
            }
            
            getChildren().addAll(column);
        }
        
        public int getEnergy() {
            return energy;
        }
        
        public boolean isDone() {
            return done;
        }
        
        public void enable() {
            for (int i=0; i<8; i++) {
                rolls[i].setDisable(false);
            }
        }
        
        public void disable() {
            for (int i=0; i<8; i++) {
                rolls[i].setDisable(true);
            }
        }
        
        /**
         * 
         * @return true if all columns are filled
         */
        private boolean update() {
            int columnsFilled = 0;
            energy = 0;
           
            for (int col=0; col<4; col++) {
                try {
                    int top = Integer.parseInt(rolls[col*2].getText());
                    int bottom = Integer.parseInt(rolls[col*2 + 1].getText());
                    int delta = top-bottom;
                    
                    if (delta == 0) {
                        rolls[col*2].setText("");
                        rolls[col*2].setDisable(false);
                        rolls[col*2+1].setText("");
                        rolls[col*2+1].setDisable(false);
                    } else if (delta == 4) {
                        results[col].setText("1");
                        energy += 1;
                        columnsFilled++;
                    } else if (delta == 5) {
                        results[col].setText("2");
                        energy += 2;
                        columnsFilled++;
                    } else {
                        if ((delta < 0) && (!(results[col].getText().equals("X")))) {
                            /* Deal damage to player */
                            Game.info("The construct backfires, you take one damage.");
                            Game.getPlayer().dealDamage();
                        }
                        results[col].setText("X");
                        columnsFilled++;
                    }
                                       
                } catch (NumberFormatException e) {
                    /* NumberFormatException just means one of the two buttons
                     * in the column doesn't have a value yet, so we just do
                     * nothing here.
                     * Good programming practice suggests that I test for
                     * the condition rather than relying on the exception,
                     * but I'm lazy.
                     */
                }
            }
            parent.updateNumbers();
            return (columnsFilled == 4);
        }
    }
    
    public ActivateDialog(Construct construct) {
        setTitle("Activate");
        setHeaderText("Activating " + construct.getName());
        
        this.construct = construct;
        
        pane = new GridPane();
        firstAttempt = new ActivationField(true, this);
        secondAttempt = new ActivationField(false, this);
        dice = new DiceUi();
        energyBox = new VBox();
        topTotal = new Label();
        bottomTotal = new Label();
        charmTotal = new Label();
        visionTotal = new Label();
        overallTotal = new Label();
        
        double width = 100.0;
        energyBox.setPrefWidth(width);

        updateNumbers();

        charmButton = new Button("Use Focus Charm");
        if (CHARM.isCharged()) {
            charmButton.setDisable(false);
        } else {
            charmButton.setDisable(true);
        }
        charmButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                charmUsed = true;
                charmButton.setDisable(true);
                CHARM.setCharged(false);
                Game.info("You activate the Focus Charm");
                updateNumbers();
            }
            
        });
        
        pane.add(firstAttempt, 0, 1);
        pane.add(secondAttempt, 0, 2);
        pane.add(dice, 0, 3);
        pane.add(energyBox, 1, 0, 1, 3);
        pane.add(charmButton, 0, 4);
        
        doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        
        getDialogPane().getButtonTypes().add(doneButton);
        getDialogPane().setContent(pane);
        
        setResultConverter(new Callback<ButtonType, Integer>() {

            @Override
            public Integer call(ButtonType param) {
                return 0;
            }
            
        });
        
        dieNumber = 1;
        Game.getDice().roll();
        dice.highlight(dieNumber);
        
    }
    
    private int updateNumbers() {
        int total = 0;
        int top = firstAttempt.getEnergy();
        int bottom = secondAttempt.getEnergy();
        
        energyBox.getChildren().clear();
        
        
        topTotal.setText("1st Attempt: " + Integer.toString(top));
        energyBox.getChildren().add(topTotal);
        
        if (firstAttempt.isDone()) {
            bottomTotal.setText("2nd Attempt: " + Integer.toString(bottom));
            energyBox.getChildren().add(bottomTotal);
        }
       
        total = top + bottom;
        
        if (charmUsed) {
            charmTotal.setText("Focus Charm: 2");
            energyBox.getChildren().add(charmTotal);
            total += 2;
        }
        
        fleetingVision = false;
        for (Location l : Location.values()) {
            if ((l.getConstruct() == construct) && (l.hasEvent(Event.VISION))) {
                fleetingVision = true;
            }
        }
        if (fleetingVision) {
            visionTotal.setText("Fleeting Vision: 1");
            energyBox.getChildren().add(visionTotal);
            ++total;
        }
        
        overallTotal.setText("Total: " + Integer.toString(total));
        energyBox.getChildren().add(overallTotal);
        
        return total;
    }
    
}
