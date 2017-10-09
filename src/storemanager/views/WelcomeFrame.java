/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storemanager.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author Haris
 */
public class WelcomeFrame extends GridPane{
    public GridPane grid = new GridPane();
    public Button btnCont,btnLog;
    public HBox hbBtn,hbBtn1;
    public Text instruction,wText,warnText;
    public PasswordField pass;
    public WelcomeFrame(){
        //creating buttons and giving them functions
        btnCont = new Button();
        btnCont.setText("Continue");
        
        btnLog = new Button();
        btnLog.setText("Log In");
        btnLog.setDisable(true);
        
        //positioning buttons in GUI
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbBtn.getChildren().add(btnCont);
        
        hbBtn1 = new HBox(10);
        hbBtn1.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn1.getChildren().add(btnLog);
        
        //creating text fields for instructions
        
        wText = new Text("Welcome to Store Manager");
        instruction = new Text("Enter password to enter Admin View \n or Continue as Cashier"); 
        //creating the grid to align elements 
        
        
        
        //creating label and text field
        Label lblPass = new Label("Password:");
        pass = new PasswordField();
        warnText = new Text("");
        
        //adding event listener on text field change
        pass.textProperty().addListener((observable,oldValue,newValue)->{
            btnLog.setDisable(!(newValue.length() > 0));
        });
        //adding event listener to buttons 
        
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        //adding elements to grid 
        
        grid.add(wText, 0, 0, 2,1);
        grid.add(instruction, 0, 1, 2,1);
        grid.add(lblPass,0,2,1,1);
        grid.add(pass,1,2,1,1);
        grid.add(warnText,0,3,2,1);
        grid.add(hbBtn,0,4,1,1);
        grid.add(hbBtn1,1,4,1,1);
    }
    public Scene CreateScene(){
        StackPane root = new StackPane();
        root.getChildren().add(this.grid);
        return new Scene(root,300,250);
    }
            
}
