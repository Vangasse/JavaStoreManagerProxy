/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storemanager.views;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import storemanager.models.Article;

/**
 *
 * @author Haris
 */
public class AdminView {
    public Group root;
    public TabPane tabPane;
    public BorderPane mainPane;
    public Tab lagerTab,cashierTab,recipeTab;
    public Scene scene;
    public TableView table;
    public Button addButton,deleteBtn;
    public TextField addArticle,addPrice,searchArticles,addQuantity;
    public HBox hb,wraper;
    public VBox vb, sideB;
    public TableColumn priceClmn ,nameClmn,quantityClmn ;
    
    public AdminView(){
        root = new Group();
        //new Scene 
        scene = new Scene(root,600,600);
        //creating the pane for the tabs
        tabPane = new TabPane();
        mainPane = new BorderPane();
        //Create Tabs 
        lagerTab = new Tab();
        lagerTab.setText("Lager");
        lagerTab.setClosable(false);
        //add Data to tab
        table = new TableView();
        table.setEditable(true);
        // creating the button for adding new elements
        addButton = new Button("Add");
        addButton.setDisable(true);
        addButton.setMinSize(600, 30);
        // creating the columns and data that they accept
        nameClmn = new TableColumn("Artical Name");
        nameClmn.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
        nameClmn.setCellValueFactory(
                    new PropertyValueFactory<Article, String>("Name"));
        nameClmn.setCellFactory(TextFieldTableCell.forTableColumn());
      
        priceClmn = new TableColumn("Artical Price");
        priceClmn.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        priceClmn.setCellValueFactory(
                    new PropertyValueFactory<Article, Float>("Price"));
        priceClmn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        //column for quantity in admin view
        quantityClmn = new TableColumn("Artical Quantity");
        quantityClmn.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        quantityClmn.setCellValueFactory(
                    new PropertyValueFactory<Article, Integer>("Quantity"));
        quantityClmn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        //creating text fields and their functionalities denying to enter empty values
        addArticle = new TextField();
        addArticle.setPromptText("Add Article");
        addArticle.setPrefWidth(195);
        
        addPrice = new TextField();
        addPrice.setPromptText("Add Price");
        addPrice.setPrefWidth(195);
        
        addQuantity = new TextField();
        addQuantity.setPromptText("Add Price");
        addQuantity.setPrefWidth(195);
        //functionalty to enable text field when they are filled out 
        addPrice.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    addPrice.setText(newValue.replaceAll("[^\\d]", ""));
                    System.out.println(addPrice.getText().toCharArray().length);   
                }
                addButton.setDisable(!((addPrice.getText().toCharArray().length > 0) && (addArticle.getText().toCharArray().length > 0)&& (addQuantity.getText().toCharArray().length > 0)) );
            }
        });
        addQuantity.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    addPrice.setText(newValue.replaceAll("[^\\d]", ""));
                    System.out.println(addPrice.getText().toCharArray().length);   
                }
                addButton.setDisable(!((addPrice.getText().toCharArray().length > 0) && (addArticle.getText().toCharArray().length > 0)&& (addQuantity.getText().toCharArray().length > 0)) );
            }
        });
        addArticle.textProperty().addListener((observable,oldValue,newValue)->{
            addButton.setDisable(!((addPrice.getText().toCharArray().length > 0) && (addArticle.getText().toCharArray().length > 0) && (addQuantity.getText().toCharArray().length > 0)) );
        });
        

        table.getColumns().addAll(nameClmn, priceClmn,quantityClmn);
        table.setMinWidth(600);
        hb = new HBox();
        hb.setSpacing(10);
        hb.setAlignment(Pos.BOTTOM_LEFT);
        hb.getChildren().addAll(addArticle, addPrice,addQuantity);

        //creating to Vertical Boxes to separate content and the 
        //left
        searchArticles = new TextField();
        searchArticles.setPromptText("Search Articles");
        searchArticles.setMinWidth(600);
        vb = new VBox();
        vb.setAlignment(Pos.TOP_LEFT);
        
        
        vb.setSpacing(10);
        deleteBtn = new Button("Delete");
        deleteBtn.setMinSize(600, 30);
        deleteBtn.setDisable(true);
        HBox searchBox = new HBox();
        searchBox.setSpacing(10);
        searchBox.getChildren().add(searchArticles);
        HBox addBox = new HBox();
        addBox.setSpacing(10);
        addBox.getChildren().add(addButton);
        HBox deleteBox = new HBox();
        deleteBox.setSpacing(10);
        deleteBox.getChildren().add(deleteBtn);
        vb.getChildren().addAll(table,searchBox,hb,addBox,deleteBox);
        vb.setVgrow(table,Priority.ALWAYS);
        HBox hBox = new HBox();
        hBox.setHgrow(tabPane,Priority.ALWAYS);
        hBox.getChildren().addAll(tabPane,vb);
        //adding them into TAB
        lagerTab.setContent(hBox);  
        cashierTab = new Tab();
        cashierTab.setText("Cashier");
        cashierTab.setClosable(false);
        
        recipeTab = new Tab();
        recipeTab.setText("Reports");
        recipeTab.setClosable(false);
        //adding tabs to tabpane
        tabPane.getTabs().addAll(lagerTab,cashierTab,recipeTab);
        
        //assigning tab to mainPane which is then assigned to group named ROOT
        mainPane.setCenter(tabPane);
        mainPane.prefHeightProperty().bind(scene.heightProperty());
        mainPane.prefWidthProperty().bind(scene.heightProperty());
        
        root.getChildren().add(mainPane);
  
    }
    public Scene getScene(){
        return this.scene;
    }

    public void fillTable(ObservableList<Article> articles){
        table.setItems(articles);
    }
}
