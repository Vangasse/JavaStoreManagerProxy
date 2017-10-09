/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storemanager.views;


import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
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
import javafx.util.Callback;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import storemanager.models.Article;
import storemanager.models.Item;
import storemanager.models.Recipe;

/**
 *
 * @author Haris
 */
public class RecipeView {
    public TableView tableRecipes,tableRecipeItems;
    public TableColumn dateRecipe,RecipeID,numOfItems,nameItems,priceItems,itemsQuantity;
    public SplitPane sp;
    public StackPane stackP1,stackP2;
    public VBox vbRecipes,vbItems;
    public HBox hb,hb1,hb2,hb3,hb4,hb5;
    public TextField searchArticle;
    public Label total;
    public Button dayReport,monthReport,yearReport,filter,deleteRecipe;
    public DatePicker dayReportDatePicker;
    public ChoiceBox monthsPick,yearPick,comboYear,comboMonth,dayPick;
    public List<String> months = new ArrayList<String>();
    
    public RecipeView(){
        this.fillMonthsArray();
        // creating NON editable Tables
        tableRecipes = new TableView();
        tableRecipes.setEditable(false);
        tableRecipes.setMinHeight(500);
        
        tableRecipeItems = new TableView();
        tableRecipeItems.setEditable(false);
        tableRecipeItems.setMinHeight(350);
//         Creating columns for Recipe Table
        dateRecipe = new TableColumn("Recipe Made On");
        dateRecipe.prefWidthProperty().bind(tableRecipes.widthProperty().multiply(0.4));
        dateRecipe.setCellValueFactory(
                    new PropertyValueFactory<Recipe, String>("date"));
        dateRecipe.setCellFactory(TextFieldTableCell.forTableColumn());
        
        RecipeID = new TableColumn("Recipe ID");
        RecipeID.prefWidthProperty().bind(tableRecipes.widthProperty().multiply(0.3));
        RecipeID.setCellValueFactory(
                    new PropertyValueFactory<Recipe, String>("ID"));
        RecipeID.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        
        numOfItems = new TableColumn("Number of Items");
        numOfItems.prefWidthProperty().bind(tableRecipes.widthProperty().multiply(0.3));
        numOfItems.setCellValueFactory(new PropertyValueFactory<Recipe, String>("numberOfItems"));
        numOfItems.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        
        nameItems = new TableColumn("Name");
        nameItems.prefWidthProperty().bind(tableRecipeItems.widthProperty().multiply(0.3));
        nameItems.setCellValueFactory(
                    new PropertyValueFactory<Item, String>("Article"));
        nameItems.setCellFactory(TextFieldTableCell.forTableColumn());
        
        priceItems = new TableColumn("Price");
        priceItems.prefWidthProperty().bind(tableRecipeItems.widthProperty().multiply(0.3));
        priceItems.setCellValueFactory(
                    new PropertyValueFactory<Item, Float>("Price"));
        priceItems.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        
        itemsQuantity = new TableColumn("Quantity");
        itemsQuantity.prefWidthProperty().bind(tableRecipeItems.widthProperty().multiply(0.4));
        itemsQuantity.setCellValueFactory(
                    new PropertyValueFactory<Item, Integer>("Quantity"));
        itemsQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        

 


        comboYear = new ChoiceBox<String>();
        comboYear.getItems().add("2017");
        comboYear.getItems().add("2018");
        comboYear.getItems().add("2019");
        comboYear.getSelectionModel().selectFirst();
        comboMonth = new ChoiceBox(FXCollections.observableArrayList(months));
        comboMonth.getSelectionModel().selectFirst();
        
        comboMonth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                dayPick.getItems().clear();
                int numOfMonth = comboMonth.getSelectionModel().getSelectedIndex();
                int yearSelected = Integer.parseInt(comboYear.getSelectionModel().getSelectedItem().toString());
              
                System.out.println(numOfMonth +" "+yearSelected);
                Calendar mycal = new GregorianCalendar(yearSelected, numOfMonth, 1);
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for(int i = 1; i < daysInMonth + 1 ; i++){
                    dayPick.getItems().add(String.valueOf(i));
                    dayPick.getSelectionModel().selectFirst();
                }
            }
        });
        comboYear.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                dayPick.getItems().clear();
                int numOfMonth = comboMonth.getSelectionModel().getSelectedIndex();
                int yearSelected = Integer.parseInt(comboYear.getSelectionModel().getSelectedItem().toString());
              
                System.out.println(numOfMonth +" "+yearSelected);
                Calendar mycal = new GregorianCalendar(yearSelected, numOfMonth, 1);
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for(int i = 1; i < daysInMonth + 1 ; i++){
                    dayPick.getItems().add(String.valueOf(i));
                    dayPick.getSelectionModel().selectFirst();
                }
            }
        });
        dayPick = new ChoiceBox<String>(); 
        dayPick.getItems().add("1");
        dayPick.getSelectionModel().selectFirst();
        filter = new Button("Filter");
        
        

        //storing columns into Table
        tableRecipes.getColumns().addAll(dateRecipe,RecipeID,numOfItems);
        tableRecipeItems.getColumns().addAll(nameItems,itemsQuantity,priceItems);
        hb = new HBox();
        hb.setSpacing(10);
        hb.setAlignment(Pos.BOTTOM_CENTER);
        hb.getChildren().addAll(comboYear,comboMonth,dayPick,filter);
        
        
        deleteRecipe = new Button("Delete");
        deleteRecipe.setDisable(true);
        deleteRecipe.setMinWidth(290);
        tableRecipes.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            deleteRecipe.setDisable(!(newSelection != null));
        });
        hb5 = new HBox();
        hb5.setSpacing(10);
        hb5.setAlignment(Pos.BOTTOM_CENTER);
        hb5.getChildren().add(deleteRecipe);
        
        vbRecipes = new VBox();
        vbRecipes.setAlignment(Pos.TOP_LEFT);
        vbRecipes.setPrefSize(300, 100);
        vbRecipes.setMinSize(300, 100);
        vbRecipes.setMaxWidth(300);
        vbRecipes.setSpacing(10);
        vbRecipes.getChildren().addAll(tableRecipes,hb,hb5);
        
        stackP1 = new StackPane();
        stackP1.setAlignment(Pos.TOP_LEFT);
        stackP1.getChildren().add(vbRecipes);
        
        // stack1 finsihed
        dayReportDatePicker = new DatePicker();
        Date input = new Date();
        Instant instant = input.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate date = zdt.toLocalDate();
        dayReportDatePicker.setValue(date);
        dayReport = new Button("Print Day Report");
        
        hb1 = new HBox();
        hb1.setSpacing(10);
        hb1.setAlignment(Pos.BOTTOM_CENTER);
        hb1.getChildren().addAll(dayReportDatePicker, dayReport );
        
        monthsPick = new ChoiceBox(FXCollections.observableArrayList(months));
        monthsPick.getSelectionModel().selectFirst();
        monthReport = new Button("Print Month Report");
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int yearNum = cal.get(Calendar.YEAR);
        Label year = new Label(String.valueOf(yearNum));
        
        hb2 = new HBox();
        hb2.setSpacing(10);
        hb2.setAlignment(Pos.BOTTOM_CENTER);
        hb2.getChildren().addAll(monthsPick,year, monthReport );
        
        yearPick = new ChoiceBox(FXCollections.observableArrayList("2017","2018"));
        yearPick.getSelectionModel().selectFirst();
        yearReport = new Button("Print Year Report");
        
        hb3 = new HBox();
        hb3.setSpacing(10);
        hb3.setAlignment(Pos.BOTTOM_CENTER);
        hb3.getChildren().addAll(yearPick, yearReport);
        
        total = new Label("");
        hb4 = new HBox();
        hb4.setSpacing(10);
        hb4.setStyle("-fx-padding: 0 10 0 0;");
        hb4.setAlignment(Pos.BOTTOM_RIGHT);
        hb4.getChildren().add(total);
        
        
        vbItems = new VBox();
        vbItems.setAlignment(Pos.TOP_LEFT);
        vbItems.setPrefSize(300, 100);
        vbItems.setMinSize(300, 100);
        vbItems.setMaxSize(300, 300);
        vbItems.setSpacing(10);
        vbItems.getChildren().addAll(tableRecipeItems,hb4,hb1,hb2,hb3);
        
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
    public void fillTableRecipes(ObservableList<Recipe> recipes){
        this.tableRecipes.setItems(recipes);
        
    }
    public void fillTableItems(ObservableList<Item> items){
        this.tableRecipeItems.setItems(items);
    }
    public void fillMonthsArray(){
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("Avgust");
        months.add("September");
        months.add("Octomber");
        months.add("November");
        months.add("December");
    }
        public int getNumberOfMonth(String month){
        switch(month){
            case "January":
                return 0;
             case "February":
                return 1; 
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "Avgust":
                return 7;
            case "September":
                return 8;
            case "Octomber":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
            default:
                break;
        }
        return 0;
    }
}
