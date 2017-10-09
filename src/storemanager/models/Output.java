/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storemanager.models;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.poi.xwpf.usermodel.*;
/**
 *
 * @author Haris
 */
public class Output {
    public XWPFDocument document;
    public FileOutputStream out;
    
    public Output(){
           
    }
    public void printRecipes(ArrayList<OutputStringLine> list){
        try{
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd HHmmss");//dd/MM/yyyy
            Date now = new Date();
            String strDate = sdfDate.format(now);
            document = new XWPFDocument();
            out = new FileOutputStream(new File(strDate+" Report.docx"));
            for(OutputStringLine item:list){
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                if(item.getType() == ParagraphType.Title){
                    run.setFontSize(18);
                    run.setBold(true);
                }
                else if(item.getType() == ParagraphType.ItalicHeading){
                    run.setFontSize(12);
                    run.setItalic(true);
                }
                
                run.setText(item.getText());
            }
            document.write(out);
            out.close();
            System.out.println("NewReport.docx written successfully");
        }catch(Exception ex){
            System.out.println("File not printed \n" + ex.getMessage());
        }
        
    }
}
