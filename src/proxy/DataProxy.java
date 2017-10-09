package proxy;

import java.util.Date;

import javafx.collections.ObservableList;
import storemanager.models.*;

public class DataProxy extends DataBase{
	protected String password;
	
	public void setPassword(String password) {
		this.password = password;
	}

	public DataProxy(String password) {
		this.password = password;
	}
	
	@Override
	public ObservableList<Article> GetData(){
		if(this.hasPermission()) {
			return super.GetData();
		}
		
		return null;
	}
	
	public void setArticle(Article article) {
		if(this.hasPermission()) {
			super.setArticle(article);
		}
	}
	
	public void removeFromDb(Article article) {
		if(this.hasPermission()) {
			super.removeFromDb(article);
		}
	}
	
	public void updateArticle(Article oldArticle,Article newArticle) {
		if(this.hasPermission()) {
			super.updateArticle(oldArticle, newArticle);
		}
	}
	
	public void setRecipeToDb(Recipe recipe) {
		if(this.hasPermission()) {
			super.setRecipeToDb(recipe);
		}
	}
	
	public int getLastRecipeId() {
		if(this.hasPermission()) {
			return super.getLastRecipeId();
		}
		
		return 0;
	}
	
	public ObservableList<Recipe> getRecipes(){
		if(this.hasPermission()) {
			return super.getRecipes();
		}
		
		return null;
	}
	
	public void updateArticlesQuantity(ObservableList<Article> articles) {
		if(this.hasPermission()) {
			super.updateArticlesQuantity(articles);
		}
	}
	
	public String dateFormater(Date dm) {
		if(this.hasPermission()) {
			return super.dateFormater(dm);
		}
		
		return null;
	}
	
	public void removeRecipeFromDb(Recipe rec) {
		if(this.hasPermission()) {
			super.removeRecipeFromDb(rec);
		}
	}
	
	public boolean hasPermission() {
		return this.password.equals("admin");
	}
}
