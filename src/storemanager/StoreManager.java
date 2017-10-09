/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storemanager;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import storemanager.models.*;
import storemanager.views.AdminView;
import storemanager.views.Cashier;
import storemanager.views.RecipeView;
import storemanager.views.WelcomeFrame;
import storemanager.models.Output;
import proxy.*;

/**
 *
 * @author Haris
 */
public class StoreManager extends Application {
    
    DataProxy db = new DataProxy("admin");
    public ObservableList<Article> articles = db.GetData();
    public ObservableList<Article> dataContainer = FXCollections.observableArrayList(articles);
    public ObservableList<Item> recipe = FXCollections.observableArrayList();
    public ObservableList<Recipe> recipesList = FXCollections.observableArrayList(db.getRecipes());
    public ObservableList<Recipe> printingList = FXCollections.observableArrayList();
    public ObservableList<Recipe> recipesListContainer = FXCollections.observableArrayList(recipesList);

    Article articleSwitch;
    Item toRecipe;
    int itemID = db.getLastRecipeId();
    @Override
    public void start(Stage primaryStage) {
        //Welcoming frame 
        //WelcomeFrame frame = new WelcomeFrame();
        //Admin Frame
         AdminView admin = new AdminView();
        //Cashier Tab
        Cashier cashier = new Cashier();
        //Recipe manager Tab
        RecipeView recipeView = new RecipeView();
       //Continue Button continue just to Cashier 

//        frame.btnCont.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Bello Cashier!");
//            }
//        });
//        // Log In button for Accesing the admin section
//        frame.btnLog.setOnAction(new EventHandler<ActionEvent>(){
//            @Override
//            public void handle(ActionEvent e){
//                if(frame.pass.getText().intern() == "admin"){
//                    frame.warnText.setText("");
//
//                }
//                else{
//                    frame.warnText.setText("The Password is Incorrect!!!");
//                }
//
//            }
//        });
//        primaryStage.setScene(admin.getScene());
        admin.cashierTab.setContent(cashier.sp);
        admin.recipeTab.setContent(recipeView.sp);
        
        cashier.fillTableItems(recipe);
        recipeView.fillTableRecipes(recipesList);
        primaryStage.setTitle("Admin View");
        primaryStage.setResizable(false);

//         Search bar for admin tab 
        FilteredList<Article> filteredArticles = new FilteredList<>(this.articles,p -> true);
        admin.searchArticles.textProperty().addListener((observable,oldValue,newValue)->{
            filteredArticles.setPredicate(article -> {
                if(newValue == null && oldValue == null){
                    return true;
                }
                String lowerCaseArticle = newValue.toLowerCase();
                Article ar = (Article) article;
                if(ar.getName().toLowerCase().contains(newValue)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Article> sortedArticles = new SortedList<>(filteredArticles);
        sortedArticles.comparatorProperty().bind(admin.table.comparatorProperty());
        
        
        cashier.searchArticle.textProperty().addListener((observable,oldValue,newValue)->{
            filteredArticles.setPredicate(article -> {
                if(newValue == null && oldValue == null){
                    return true;
                }
                String lowerCaseArticle = newValue.toLowerCase();
                Article ar = (Article) article;
                if(ar.getName().toLowerCase().contains(newValue)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Article> sortedArticlesCashier = new SortedList<>(filteredArticles);
        sortedArticlesCashier.comparatorProperty().bind(cashier.tableArticles.comparatorProperty());
        cashier.fillTable(sortedArticlesCashier);

        admin.fillTable(sortedArticles);
        System.out.println("Bello Admin !!");
        // btn adding the element that is entered into Input Boxes
        admin.addButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                Article article =new Article(admin.addArticle.getText(),Float.valueOf(admin.addPrice.getText()),Integer.parseInt(admin.addQuantity.getText()));
                articles.add(article);
                dataContainer.add(article);
                db.setArticle(article);
                admin.addArticle.clear();
                admin.addPrice.clear();
                admin.addQuantity.clear();
            }
        });
        // Delete btn from admin tab deleting the transition element
        admin.deleteBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                Article article = (Article)admin.table.getSelectionModel().getSelectedItem();
                articles.remove(article);
                dataContainer.remove(article);
                db.removeFromDb(article);
            }
        });
        // Assigning object to transition object Article Switch that is uset for Deleting assigned to DeleteBtn in Admin Tab
        admin.table.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            admin.deleteBtn.setDisable(!(newSelection != null)); 
            if(newSelection != null){
                Article ar = (Article) newSelection;
                articleSwitch = new Article(ar.getName(),ar.getPrice(),ar.getQuantity());
            }
        });
        // Commiting the edited Name Column in Article Table in Admin Tab
        admin.nameClmn.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Article, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Article, String> t) {
                    System.out.println(t.getNewValue().length());
                    if( t.getNewValue().length() > 0){
                        Article ar = (Article) t.getTableView().getItems().get(t.getTablePosition().getRow());
                        ((Article) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setName(t.getNewValue());
                        db.updateArticle(articleSwitch,ar);
                        dataContainer.clear();
                        dataContainer.addAll(articles);
                        System.out.println("String isn't empty" );
                    }
                    else{
                        Article ar = (Article) t.getTableView().getItems().get(t.getTablePosition().getRow());
                        ((Article) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setName(t.getOldValue());
                        articles.set(articles.indexOf(ar), new Article(t.getOldValue(),ar.getPrice(),ar.getQuantity()));
                        System.out.println("String is empty");
                    }
                    
                }
            }
        );
        // Commiting the edited Price Column in Article Table in Admin Tab
        admin.priceClmn.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Article, Float>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Article, Float> t) {
                    System.out.println(t.getNewValue());
                    if(t.getNewValue() != null){
                        Article ar = (Article) t.getTableView().getItems().get(t.getTablePosition().getRow());
                        ((Article) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPrice(t.getNewValue());
                        db.updateArticle(articleSwitch,ar);
                        dataContainer.clear();
                        dataContainer.addAll(articles);
                    }
                    else{
                        Article ar = (Article) t.getTableView().getItems().get(t.getTablePosition().getRow());
                        ((Article) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPrice(t.getOldValue());
                        articles.set(articles.indexOf(ar), new Article(ar.getName(),t.getOldValue(),ar.getQuantity()));
                        System.out.println("String is empty");
                    }
                }
            });
 
        admin.quantityClmn.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Article, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Article, Integer> t) {
                    System.out.println(t.getNewValue());
                    if(t.getNewValue() != null){
                        Article ar = (Article) t.getTableView().getItems().get(t.getTablePosition().getRow());
                        ((Article) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setQuantity(t.getNewValue());
                        db.updateArticle(articleSwitch,ar);

                        dataContainer.clear();
                        dataContainer.addAll(articles);

                    }
                    else{
                        Article ar = (Article) t.getTableView().getItems().get(t.getTablePosition().getRow());
                        ((Article) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setQuantity(t.getOldValue());
                        articles.set(articles.indexOf(ar), new Article(ar.getName(),ar.getPrice(),t.getOldValue()));
                        System.out.println("String is empty");

                    }
                }
            });


        //When tabs are changed to re set the value of the Article Table because it is used in both Tabs
        admin.tabPane.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {

                 
                    if(t.getText().equalsIgnoreCase("Cashier")){
                        clearItemTable();
                        cashier.total.setText("");
                        cashier.searchArticle.textProperty().set("");
                        admin.table.refresh();
                    }
                    else if(t.getText().equalsIgnoreCase("Lager")){
                        clearItemTable();
                        admin.searchArticles.textProperty().set("");
                        cashier.tableArticles.refresh();
                        
                    }                    
                }
            }
        );
        //selection of Transition object "toRecipe" on One click 
        cashier.tableArticles.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            admin.deleteBtn.setDisable(!(newSelection != null)); 
            if(newSelection != null){
                Article ar = (Article) newSelection;
                toRecipe = new Item(ar.getName(),ar.getPrice());
            }
        });
        // adding article into Item Table with double click
        cashier.tableArticles.setRowFactory(tv ->{
            TableRow<Article> row = new TableRow();
                row.setOnMouseClicked(event ->{
                    if(event.getClickCount() == 2 &&(!row.isEmpty())){
                        boolean contains = true;
                        int indexArticle = -1;
                        for(int i = 0; i < articles.size(); i++){
                            if(articles.get(i).getName().equals(toRecipe.getArticle())){
                                indexArticle = i;
                            }
                        }
                        if(articles.get(indexArticle).getQuantity() > 0){
                            for(int i =0; i < recipe.size(); i++){
                                if(recipe.get(i).getArticle().equals(toRecipe.getArticle())){
                                    recipe.get(i).setQuantity(recipe.get(i).getQuantity()+1);
                                    articles.get(indexArticle).setQuantity(articles.get(indexArticle).getQuantity() - 1);
                                    cashier.tableArticles.refresh();
                                    cashier.tableItems.refresh();
                                    contains = false;
                                }
                            }
                        
                            if(contains){
                                recipe.add(toRecipe);
                                articles.get(indexArticle).setQuantity(articles.get(indexArticle).getQuantity() - 1);
                                cashier.tableArticles.refresh();
                                cashier.tableItems.refresh();
                            }
                        }
                        else{
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Article out of stock");
                            alert.setHeaderText("It seems that article is out of stock");
                            String articleName = articles.get(indexArticle).getName().toString();
                            alert.setContentText("The article "+articleName+" is out of stock, please go to LAGER and edit quantity in order to use it !");
                            alert.showAndWait();
                        }
                        float totall = refreshTotalInCashierTab();
                        cashier.total.setText((totall > 0) ? "Totall:"+ String.valueOf(totall) : "");
                    }
                });
            return row;
        });
        // search in cashier table of articles and passing it into recipe table with ENTER button
        cashier.tableItems.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            cashier.deleteItem.setDisable(!(newSelection != null));
        });
        cashier.deleteItem.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                Item it = (Item) cashier.tableItems.getSelectionModel().getSelectedItem();
                for(int i =0; i < articles.size(); i++){
                    if(articles.get(i).getName().contains(it.getArticle())){
                        articles.get(i).setQuantity(articles.get(i).getQuantity() + it.getQuantity());
                        cashier.tableArticles.refresh();
                    }
                }

                recipe.remove(it);
                float totall = refreshTotalInCashierTab();
                cashier.total.setText((totall > 0) ? "Totall:"+ String.valueOf(totall) : "");
            }
        });
        cashier.restartRecipe.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent e){
               clearItemTable();
           }
        });
        cashier.newRecipe.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                Recipe newRecipe = new Recipe(recipe);
                db.setRecipeToDb(newRecipe);
                db.updateArticlesQuantity(articles);
                recipesList.add(newRecipe);
                recipesListContainer.add(newRecipe);
                recipe.clear();
            }
        });

        recipeView.tableRecipes.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            Recipe r = (Recipe) newSelection;
            recipeView.total.setText("Total: "+r.getTotallPrice());
            recipeView.fillTableItems(r.items);
            
        });
        recipeView.monthReport.setOnAction(new EventHandler<ActionEvent>(){
        @Override
            public void handle(ActionEvent e){
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                int yearNum = cal.get(Calendar.YEAR);
                String monthSelected = getNumberOfMonth(recipeView.monthsPick.getSelectionModel().getSelectedItem().toString());
                String monthPicked = String.valueOf(yearNum)+"-"+monthSelected;
                recipesList.forEach((r) -> {
                    String date = db.dateFormater(r.getDateOfCreating()).substring(0,10);

                    if (date.indexOf(monthPicked) != -1) {
                        printingList.add(r);
                    }
                });

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Report ready to print");
                alert.setHeaderText("Your Reports for specific month " +monthPicked+ " are ready");
                alert.setContentText("There are "+ printingList.size() +" recipes in report sheet. Are you sure you want to print them ?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    printReport(printingList);
                }
                printingList.clear();
            }           
        });
        recipeView.dayReport.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                recipesList.forEach((r) -> {
                    String date = db.dateFormater(r.getDateOfCreating()).substring(0,10);
                    if (recipeView.dayReportDatePicker.getValue().toString().equals(date)) {
                        printingList.add(r);
                    }
                });
                printReport(printingList);

                String pickedDate = recipeView.dayReportDatePicker.getValue().toString();
                recipesList.forEach((r) -> {
                    String date = db.dateFormater(r.getDateOfCreating()).substring(0,10);
                    if (pickedDate.equals(date)) {
                        printingList.add(r);
                    }
                });
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Report ready to print");
                alert.setHeaderText("Your Reports for specific day " +pickedDate+ " are ready");
                alert.setContentText("There are "+ printingList.size() +" recipes in report sheet. Are you sure you want to print them ?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    printReport(printingList);
                }
                printingList.clear();
            }
        });
        recipeView.yearReport.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                String year = recipeView.yearPick.getSelectionModel().getSelectedItem().toString();
                recipesList.forEach((r) -> {
                    String date = db.dateFormater(r.getDateOfCreating()).substring(0,10);

                    if (date.contains(year)) {
                        printingList.add(r);
                    }
                });      
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Report ready to print");
                alert.setHeaderText("Your Reports for full " +year+ " ready");
                alert.setContentText("There are "+ printingList.size() +" recipes in report sheet. Are you sure you want to print them ?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    printReport(printingList);
                }
                printingList.clear();
            }
        });
        recipeView.filter.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int monthNum = recipeView.comboMonth.getSelectionModel().getSelectedIndex()+ 1; 
                String month = (monthNum < 10 )? "0"+String.valueOf(monthNum) :  String.valueOf(monthNum);
                String year = recipeView.comboYear.getSelectionModel().getSelectedItem().toString();
                int dayNum = Integer.parseInt(recipeView.dayPick.getSelectionModel().getSelectedItem().toString());
                String day = (dayNum < 10)? "0"+String.valueOf(dayNum) : String.valueOf(dayNum);
                String fullDate = year+"-"+month+"-"+day;
                recipesList.clear();
                if(recipeView.filter.getText().equals("Filter")){
                    for(Recipe re:recipesListContainer){
                        String dateInRecipe = re.getDate().substring(0,11);
                        System.out.println(dateInRecipe+ " "+ fullDate);
                        if(dateInRecipe.contains(fullDate)){
                            recipesList.add(re);
                        }
                    }
                    recipeView.filter.setText("Remove Filter");
                }
                else{
                    recipesList.addAll(recipesListContainer);
                    recipeView.filter.setText("Filter");
                }
                recipeView.tableRecipes.refresh();
            }
        });
        recipeView.deleteRecipe.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Recipe rec = (Recipe) recipeView.tableRecipes.getSelectionModel().getSelectedItem();
                for(Article ar:articles){
                    for(Item it: rec.getItems()){
                        if (ar.getName().equals(it.getArticle())){
                            ar.setQuantity(ar.getQuantity() + it.getQuantity());
                        }
                    }
                }
                db.updateArticlesQuantity(articles);
                admin.table.refresh();
                cashier.tableArticles.refresh();
                recipeView.tableRecipes.getItems().remove(rec);
                recipeView.tableRecipes.refresh();
                db.removeRecipeFromDb(rec);
                
            }
        });
        

        primaryStage.setTitle("Store Manager");
        primaryStage.setScene(admin.getScene());
        primaryStage.show();
    }

    public void clearItemTable(){
        for(int i =0; i < articles.size(); i++){
            for(Item it : recipe){
                if(articles.get(i).getName().equals(it.getArticle())){
                 articles.get(i).setQuantity(articles.get(i).getQuantity() + it.getQuantity());
                 dataContainer = FXCollections.observableArrayList(articles);
                 articles.clear();
                 articles.addAll(dataContainer);
             }
            }  
         }
        recipe.clear();
    }
    public void printReport(ObservableList<Recipe> printingList){
        Output out = new Output();
        ArrayList<OutputStringLine> mylist = new ArrayList<OutputStringLine>();
        if(!printingList.isEmpty()){
            printingList.forEach((r) -> {
            mylist.add(new OutputStringLine("This is Recipe with ID " + r.getID(),ParagraphType.Title));
            mylist.add(new OutputStringLine("",ParagraphType.Normal,0));
            mylist.add(new OutputStringLine("Created on " + r.getDateOfCreating(),ParagraphType.ItalicHeading));
            mylist.add(new OutputStringLine("",ParagraphType.Normal,0));
            mylist.add(new OutputStringLine("With " + r.getNumberOfItems() +" items bought",ParagraphType.Normal));
            mylist.add(new OutputStringLine("",ParagraphType.Normal,0));
            mylist.add(new OutputStringLine("",ParagraphType.Normal,0));
            mylist.add(new OutputStringLine("Items are",ParagraphType.Normal));

            for(Item it: r.getItems()){
                String articleName = it.getArticle()+" "+it.getQuantity()+"x ";
                mylist.add(new OutputStringLine(articleName,ParagraphType.Normal,it.getPrice()));

            }
            mylist.add(new OutputStringLine("",ParagraphType.Normal,0));
            mylist.add(new OutputStringLine("Total: "+r.getTotallPrice(),ParagraphType.Normal));
            mylist.add(new OutputStringLine("",ParagraphType.Normal,0));
            mylist.add(new OutputStringLine("",ParagraphType.Normal,0));


            });
        out.printRecipes(mylist);
        }
        else{
            System.out.println("Nothing to PRINT");
        }
    }
    public String getNumberOfMonth(String month){
        switch(month){
            case "January":
                return "01";
             case "February":
                return "02"; 
            case "March":
                return "03";
            case "April":
                return "04";
            case "May":
                return "05";
            case "June":
                return "06";
            case "July":
                return "07";
            case "Avgust":
                return "08";
            case "September":
                return "09";
            case "Octomber":
                return "10";
            case "November":
                return "11";
            case "December":
                return "12";
            default:
                break;
        }
        return "";
    }

    public float refreshTotalInCashierTab(){
        float totall = 0;
        totall = recipe.stream().map((it) -> (it.getPrice() * it.getQuantity())).reduce(totall, (accumulator, _item) -> accumulator + _item);
        return totall;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
