/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storemanager.models;
import storemanager.models.ParagraphType;
/**
 *
 * @author Haris
 */
public class OutputStringLine {
    public ParagraphType type;
    public String text;
    
    public OutputStringLine(String text,ParagraphType type,float num){
        this.text = generateHorizontalRule(text,num);
        this.type = type;
    }
    public OutputStringLine(String text,ParagraphType type){
        this.text = text;
        this.type = type;
    }
    public String getText(){
        return this.text;
    }
    public ParagraphType getType(){
        return this.type;
    }
    public void setText(String text){
        this.text = text;
    }
    public void setType(ParagraphType type){
        this.type = type;
    }
    public String generateHorizontalRule(String s,float num){
        StringBuilder sb = new StringBuilder();
        int numb = (s.length() > 0 )? s.length() : -15;
        numb += (num > 0 ) ? String.valueOf(num).length() : 0 ;
        sb.append(s);
        for(int i = 0; i < 120 - numb; i++){
            sb.append("-");
        }
        sb.append((num > 0 ) ? String.valueOf(num): "");
        return sb.toString();
    }
}
