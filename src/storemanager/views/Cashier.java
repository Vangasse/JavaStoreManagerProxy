/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storemanager.views;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import storemanager.models.Article;
import storemanager.models.Item;

/**
 *
 * @author Haris
 */
public class Cashier {
    public TableView tableArticles,tableItems;
    public TableColumn nameArticles,priceArticles,quantityArticles,nameItems,priceItems,itemsQuantity;
    public SplitPane sp;
    public StackPane stackP1,stackP2;
    public VBox vbArticles,vbItems;
    public HBox hb,hb1,hb2;
    public TextField searchArticle;
    public Label total;
    public Button deleteItem,restartRecipe,newRecipe;
    
    
    public Cashier(){
        // creating NON editable Tables
        tableArticles = new TableView();
        tableArticles.setEditable(false);
        tableArticles.setMinHeight(480);
        
        tableItems = new TableView();
        tableItems.setEditable(false);
        tableItems.setMinHeight(480);
        


        
        tableItems = new TableView();
        tableItems.setEditable(false);
        tableItems.setMinHeight(480);

        // Creating columns for Article Table
        nameArticles = new TableColumn("Article Name");
        nameArticles.prefWidthProperty().bind(tableArticles.widthProperty().multiply(0.4));
        nameArticles.setCellValueFactory(
                    new PropertyValueFactory<Article, String>("Name"));
        nameArticles.setCellFactory(TextFieldTableCell.forTableColumn());
        
        priceArticles = new TableColumn("Article Price");
        priceArticles.prefWidthProperty().bind(tableArticles.widthProperty().multiply(0.3));
        priceArticles.setCellValueFactory(
                    new PropertyValueFactory<Article, String>("Price"));
        priceArticles.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        
        quantityArticles = new TableColumn("Article Quantity");
        quantityArticles.prefWidthProperty().bind(tableArticles.widthProperty().multiply(0.3));
        quantityArticles.setCellValueFactory(
                    new PropertyValueFactory<Article, String>("Quantity"));
        quantityArticles.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        
        nameItems = new TableColumn("Article");
        nameItems.prefWidthProperty().bind(tableItems.widthProperty().multiply(0.3));
        nameItems.setCellValueFactory(
                    new PropertyValueFactory<Item, String>("Article"));
        nameItems.setCellFactory(TextFieldTableCell.forTableColumn());
        
        priceItems = new TableColumn("Price");
        priceItems.prefWidthProperty().bind(tableItems.widthProperty().multiply(0.3));
        priceItems.setCellValueFactory(
                    new PropertyValueFactory<Item, Float>("Price"));
        priceItems.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        
        itemsQuantity = new TableColumn("Quantity");
        itemsQuantity.prefWidthProperty().bind(tableItems.widthProperty().multiply(0.4));
        itemsQuantity.setCellValueFactory(
                    new PropertyValueFactory<Item, Integer>("Quantity"));
        itemsQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        
        searchArticle = new TextField();
        searchArticle.setPromptText("Search Articles");
        searchArticle.setPrefWidth(400);
        
        //storing columns into Table
        tableArticles.getColumns().addAll(nameArticles,priceArticles,quantityArticles);
        tableItems.getColumns().addAll(nameItems,itemsQuantity,priceItems);
        
        //elements for item side
        
        total = new Label("");
        hb = new HBox();
        hb.setSpacing(10);
        hb.setAlignment(Pos.BOTTOM_RIGHT);
        hb.getChildren().add(total );
        deleteItem =  new Button("Delete");
        deleteItem.setDisable(true);
        deleteItem.setMinWidth(280);
        restartRecipe = new Button("Restart");
        restartRecipe.setMinWidth(140);
        
        newRecipe = new Button("Sold");
        newRecipe.setMinWidth(140);
        
        hb1 = new HBox();
        hb1.setSpacing(10);
        hb1.setAlignment(Pos.BOTTOM_CENTER);
        hb1.getChildren().add(deleteItem );
        
        hb2 = new HBox();
        hb2.setSpacing(10);
        hb2.setAlignment(Pos.BOTTOM_CENTER);
        hb2.getChildren().addAll(restartRecipe, newRecipe );
        
        vbItems = new VBox();
        vbItems.setAlignment(Pos.TOP_LEFT);
        vbItems.setPrefSize(300, 100);
        vbItems.setMinSize(300, 100);
        vbItems.setMaxSize(300, 300);
        vbItems.setSpacing(10);
        vbItems.getChildren().addAll(tableItems,hb,hb1,hb2);
        
        vbArticles = new VBox();
        vbArticles.setAlignment(Pos.TOP_LEFT);
        vbArticles.setPrefSize(300, 100);
        vbArticles.setMinSize(300, 100);
        vbArticles.setMaxWidth(300);
        vbArticles.setSpacing(10);
        vbArticles.getChildren().addAll(tableArticles,searchArticle);
        
        stackP1 = new StackPane();
        stackP1.setAlignment(Pos.TOP_LEFT);
        stackP1.getChildren().add(vbArticles);
        
        stackP2 = new StackPane();
        stackP2.setAlignment(Pos.TOP_LEFT);
        stackP2.getChildren().add(vbItems);
        
        sp = new SplitPane();
        sp.getItems().addAll(stackP1,stackP2);
        sp.setDividerPositions(0.1f, 0.6f, 0.9f);
    }
    public SplitPane getSplitP(){
        return this.sp;
    }
    public void fillTable(ObservableList<Article> articles){
        tableArticles.setItems(articles);
    }
    public void fillTableItems(ObservableList<Item> items){
        tableItems.setItems(items);
    }
    public Item getItem(Article article){
        return new Item(article.getName(),article.getPrice());
    }
}
