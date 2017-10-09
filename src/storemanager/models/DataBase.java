/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storemanager.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author Haris
 */
public class DataBase {
    public ArrayList<Article> list;
    public Connection con;
    public Statement stmt;
    public ResultSet result;
    public SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    public DataBase(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/AnimaVet","root","");
            stmt = con.createStatement();
            
        }catch(Exception e){
            System.out.println("Failed to connect with database , check if XAMP is turned on");
        }
    }
    public ObservableList<Article> GetData(){
        try{
            String query = "SELECT * FROM articles";
            result = stmt.executeQuery(query);
            ArrayList articles = new ArrayList();
            while(result.next()){
                String name = result.getString("ArticleName");
                float price = result.getFloat("Price");
                int quantity = result.getInt("Quantity");
                articles.add(new Article(name,price,quantity));
            }
            ObservableList<Article> result = FXCollections.observableArrayList(articles);
            return result;
        }catch(Exception e){
            System.out.println("NOT able to query through table check");
            ObservableList<Article> result = FXCollections.observableArrayList();
            return result;
        }  
    }
    public void setArticle(Article article){
        String query = "INSERT INTO articles ( ArticleName, Price,Quantity) VALUES ('"+article.getName()+"','"+article.getPrice()+"','"+article.getQuantity()+"')";
        try{
            stmt.executeUpdate(query);
            System.out.println("Querry succesfully fullfiled no 3");
        }catch(Exception e){
            System.out.println("Failed to update database no 3 "+ e);
        }
        //INSERT INTO `articles` (`ID`, `ArticleName`, `Price`) VALUES (NULL, 'Nutrideal Govedje Povrce', '180');
    }
    public void removeFromDb(Article article){
        String query = "DELETE FROM articles WHERE articles.ArticleName ='"+ article.getName()+"'";
        try{
            stmt.executeUpdate(query);
            System.out.println("Querry succesfully fullfiled no 4");
        }catch(Exception e){
            System.out.println("Failed to update database no 4 "+ e);
        }
    }
    public void updateArticle(Article oldArticle,Article newArticle){
//        UPDATE `articles` SET `ArticleName` = 'Fun Dog Adult 1kg' WHERE `articles`.`ID` = 1;
        String query = "UPDATE articles SET ArticleName = '"+newArticle.getName()+"' WHERE articles.ArticleName ='"+ oldArticle.getName()+"'";
        String queryTwo = "UPDATE articles SET Price = '"+newArticle.getPrice()+"' WHERE articles.ArticleName ='"+ oldArticle.getName()+"'";
        String queryThree = "UPDATE articles SET Quantity = '"+newArticle.getQuantity()+"' WHERE articles.ArticleName ='"+ oldArticle.getName()+"'";
        try{
            stmt.executeUpdate(query);
            stmt.executeUpdate(queryTwo);
            stmt.executeUpdate(queryThree);
            System.out.println("Querry succesfully fullfiled");

        }catch(Exception e){
            System.out.println("Failed to update database no 5 "+ e);
        }
    }
    public void setRecipeToDb(Recipe recipe){
        try{
            DateFormat formater = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            String inputText = recipe.getDate();
            Date dateDM = (Date)formater.parse(inputText);
            String formatedDate = this.dateFormater(dateDM);
            System.out.println("formatedDate : " + formatedDate);  
            ObservableList<Item> listOfItems = recipe.getItems();
            String queryRecipe = "INSERT INTO recipe (Date) VALUES ('"+formatedDate+"')";
            stmt.executeUpdate(queryRecipe);
            
            for(Item it: listOfItems){
                String queryItem = "INSERT INTO items (ArticleName, ArticlePrice,Quantity,RecipeID) VALUES ('"+it.getArticle()+"', '"+it.getPrice()+"', '"+it.getQuantity()+"', '"+it.getRecipeID()+"');";
                stmt.executeUpdate(queryItem);
                System.out.println("Query succesfully executed no 6 ");
            }
        }catch(Exception ex){
            System.out.println("Failed to execute query");

        }
    }
    public int getLastRecipeId(){
//        SELECT ID FROM recipe ORDER BY id DESC LIMIT 1
        String query = "SELECT ID FROM recipe ORDER BY id DESC LIMIT 1";
        int lastID = 0;
        try{
            result = stmt.executeQuery(query);
            while(result.next()){
                lastID = Integer.parseInt(result.getString("ID"));
            }
            System.out.println(lastID);
            System.out.println("Querry succesfully fullfiled no 7");
        }catch(Exception e){
            System.out.println("Failed to update database no 7 "+ e);
        }
        return (lastID != 0)? lastID : 0;
    }
    public ObservableList<Recipe> getRecipes(){
        String getRecipes = "SELECT * FROM recipe";
        String getItems = "SELECT * FROM items";
        ObservableList<Recipe> recipes = FXCollections.observableArrayList();
        ObservableList<Item> items = FXCollections.observableArrayList();
        try{
            result = stmt.executeQuery(getRecipes);
            while(result.next()){
                int id = result.getInt("ID");
                Date dt = dateFormater.parse(result.getString("Date"));
                Recipe rec = new Recipe(id,dt);
                recipes.add(rec);
            }
            result = stmt.executeQuery(getItems);
            while(result.next()){
                int id = result.getInt("ID");
                String article = result.getString("ArticleName");
                float price = result.getFloat("ArticlePrice");
                int quantity = result.getInt("Quantity");
                int recipeID = result.getInt("RecipeID");
                items.add(new Item(id,article,price,quantity,recipeID));
            }
            for(Recipe r:recipes){
                r.items = FXCollections.observableArrayList();
                for(Item it:items){
                    if(r.getID() == it.getRecipeID()){
                        r.items.add(it);
                    }
                }
                r.setTotal();
            }
            for(Recipe r : recipes){
                r.setNumberOfItems(r.getItems().size());
            }
            
            return recipes;
        }catch(Exception e){
            System.out.println("Query failed to executed no 8");
            return recipes;
        }
    }
    public void updateArticlesQuantity(ObservableList<Article> articles){
        try{
            for(Article ar: articles){
                String query = "UPDATE articles SET Quantity = '"+ar.getQuantity()+"' WHERE articles.ArticleName ='"+ar.getName()+"'";
                stmt.executeUpdate(query);
            }
        }catch(Exception e){
            System.out.println("Query Update not executed no 9 ");
        }
        
    }
    
    public String dateFormater(Date dm){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dm);
        int year = cal.get(Calendar.YEAR);
        String month = (cal.get(Calendar.MONTH) > 8) ?  Integer.toString(cal.get(Calendar.MONTH)+1): "0"+Integer.toString(cal.get(Calendar.MONTH)+1);
        String day = (cal.get(Calendar.DATE) > 10 ) ? Integer.toString(cal.get(Calendar.DATE)) : "0" + Integer.toString(cal.get(Calendar.DATE)) ;

        String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String minutes = Integer.toString(cal.get(Calendar.MINUTE));

        String formatedDate =  year+ "-"+ month + "-" + day +" "+ hour+ ":"+minutes;
        
        return formatedDate;
    }

    public void removeRecipeFromDb(Recipe rec){
        String query = "DELETE FROM recipe WHERE recipe.ID="+rec.getID()+"";
        try{
            stmt.executeUpdate(query);
            System.out.println("Querry succesfully fullfiled no 4");
        }catch(Exception e){
            System.out.println("Failed to update database no 4 "+ e);
        }
    }

    
}
