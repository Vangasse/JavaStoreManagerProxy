/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storemanager.models;

import java.lang.reflect.InvocationTargetException;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Haris
 */
public class Item {
    private int ID;
    public DataBase db = new DataBase();
    public SimpleStringProperty article;
    public SimpleFloatProperty price;
    public SimpleIntegerProperty quantity;
    public int RecipeID;

    public Item(String article,float price){
        this.article = new SimpleStringProperty(article);
        this.price = new SimpleFloatProperty(price);
        this.quantity = new SimpleIntegerProperty(1);
        this.RecipeID = db.getLastRecipeId() + 1;   
    }
    public Item(int id,String article,float price,int quantity,int recipeID){
        this.ID = id;
        this.article = new SimpleStringProperty(article);
        this.price = new SimpleFloatProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.RecipeID = recipeID;
        
    }
    public Item(){}
    public void setArticle(String articleName){
        this.article.set(articleName);
    }
    public String getArticle(){
        return this.article.get();
    }
    public void setPrice(float articlePrice){
        this.price.set(articlePrice);
    }
    public float getPrice(){
        return this.price.get();
    }
    public void setQuantity(int quan){
        this.quantity.set(quan);
    }
    public int getQuantity(){
        return this.quantity.get();
    }
    public void setRecipeID(int id){
        this.RecipeID = id;
    }
    public int getRecipeID(){
        return this.RecipeID;
    }
    public int getID(){
        return this.ID;
    }
    public void setID(int id){
        this.ID = id;
    }
}
