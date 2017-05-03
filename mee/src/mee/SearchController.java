package mee;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;

public class SearchController  {

    @FXML
   
    private JFXButton search;

     @FXML
    private JFXButton show;
    @FXML
    private JFXTextField txt1;

    @FXML
    private JFXButton browse;
    
    @FXML
    private JFXTextArea resultTextArea;
    
    DirectoryChooser dirchose = new DirectoryChooser();
    File file;
    
    public String fileName;
   
    String Contents ;
    
    File [] list_of_file;
    ArrayList<String> tokenn = new ArrayList<String>();
    ArrayList<String> Docs = new ArrayList<String>();
    HashMap <String ,document> index  = new HashMap<String ,document>();
    
    // Exit Button Action
    public void exitButtonAction (ActionEvent event){
        System.exit(0);
    }
    
    @FXML
    public void browesfile(ActionEvent e) throws  Exception{
         int count = 0;
        try{
            list_of_file = dirchose.showDialog(null).listFiles();
                   }
        catch(Exception ex){
                    resultTextArea.clear();
                    resultTextArea.appendText("No Folder Selected  ");
                    }
    	
                        for (File fi : list_of_file){
                            count++;
                            Scanner s = new Scanner(fi);
                             
                            while (s.hasNext()){
                            
                            Contents = s.next();
                            StringTokenizer token = new StringTokenizer(Contents , "   ,%$&");
                            
                            while(token.hasMoreTokens()){
                                tokenn.add(token.nextToken());
                                Docs.add(fi.getName());
                           }
                        
                            }
                        }
                            resultTextArea.clear();
                            resultTextArea.appendText("The Number of readable file is  " + count );
                        
                    //Deleting stop words from the array list
                           for (int i =0; i< tokenn.size();i++){
                            String word = tokenn.get(i);
                            boolean flag = true;
                        for (int j=0;j< StopWords.stopwords.length;j++){
                        if (word.equalsIgnoreCase(StopWords.stopwords[j]))
                        {
                            Docs.remove(i);
                            tokenn.remove(i);
                        }

                        }
                        
             }
                        
                    Doc_stammer(tokenn);
                    getindex();
    }
    
   public void getindex(){
     int idf = 1 ;   // inverted term frequence
     for(int i= 0 ; i <tokenn.size(); i ++ ){ // < 6 
        document d = new document();
        int tf = 1; // term frequence 
        int tf2 = 1;
        String dd=Docs.get(i);
        // System.out.println(dd);
         System.out.println(tokenn.get(i));
        for (int j = 0 ; j <tokenn.size(); j++){ 
        boolean    flag = true ;
        if (tokenn.get(i).equals(tokenn.get(j))  && Docs.get(j).equals(dd)  ) {   //                                            Ashraf.txt
        
        d.doc.put(Docs.get(j), tf);
         
        tf++ ;
        }
        else{
               d.doc.put(Docs.get(j), tf2);
               tf2++;
        }
      
        
       }
        
        
       // System.out.println(d.doc.toString());
        index.put(tokenn.get(i), d);
         
    }
   
    
    } 
    public void Doc_stammer(ArrayList a) {
        char[] w = new char[501];
        PorterStemmer s = new PorterStemmer();

        for (int i = 0; i < a.size(); i++) {
            try {
                int var = 0;
                int ch = a.get(i).toString().charAt(var);
                if (Character.isLetter((char) ch)) {
                    int j = 0;
                    while (true) {
                        var++;
                        ch = Character.toLowerCase((char) ch);
                        w[j] = (char) ch;
                        if (j < 500) {
                            j++;
                        }
                        if (var == a.get(i).toString().length()) {
                            for (int c = 0; c < j; c++) {
                                s.add(w[c]);
                            }
                            s.stem();
                            a.set(i , s.toString());
                          //  portred.add(s.toString());
                            break;
                        } else {
                            ch = a.get(i).toString().charAt(var);
                        }
                    }
                }
                if (ch < 0) {
                    break;
                }
            } catch (Exception ex) {
                resultTextArea.appendText("Stemmer error");
            }
        }
    }                        

    public String Quiry_stammer(ArrayList a) {
        char[] w = new char[501];
        PorterStemmer s = new PorterStemmer();

        for (int i = 0; i < a.size(); i++) {
            try { 
                int var = 0;
                int ch = a.get(i).toString().charAt(var);
                if (Character.isLetter((char) ch)) {
                    int j = 0;
                    while (true) {
                        var++;
                        ch = Character.toLowerCase((char) ch);
                        w[j] = (char) ch;
                        if (j < 500) {
                            j++;
                        }
                        if (var == a.get(i).toString().length()) {
                            for (int c = 0; c < j; c++) {
                                s.add(w[c]);
                            } 
                            s.stem();

                            break;
                        } else {
                            ch = a.get(i).toString().charAt(var);
                        }
                    }
                }
                if (ch < 0) {
                    break;
                }
            } catch (Exception ex) {
               resultTextArea.appendText("stemmer error in  Query");
            }
        }
        return s.toString();
    }	   
    	   
       public void searchButtonAction (ActionEvent event) throws  IOException{
    	 resultTextArea.clear();
    	   String Text = txt1.getText();
                          ArrayList s_query =new ArrayList() ;
                          s_query.add(Text);
   	     String query = Quiry_stammer(s_query);
                        searchWord(query);
    	      	   
    	   }
       
        public  void searchWord(String query) {
        
          try {
        
          document result =(document) index.get(query); // ??
  
           
                Collection doc_id  = result.doc.keySet(); //return keys 
                Collection doc_freq = result.doc.values(); //return values
        
            Iterator t1 =doc_id.iterator() ;
            Iterator t2 = doc_freq.iterator() ;
             resultTextArea.appendText( "Query is  \t"+query + "\n");
             resultTextArea.appendText("Doc id \t \t  \t  \t \t tf \n"); 
            
            while (t1.hasNext()){
                
          resultTextArea.appendText(t1.next().toString() +"\t  \t "+ t2.next() + " \n");
            }
          }
          catch(Exception ee){
                resultTextArea.clear();
           resultTextArea.appendText( " Error , No Result  ");
          }

                
  }
  
  public void showInverted_index(){
      try{
          
        resultTextArea.clear();
    Collection term  = index.keySet(); //return keys 
    Collection freq = index.values(); //return values
        
            Iterator t11 =term.iterator() ;
            Iterator t22= freq.iterator() ;
             resultTextArea.appendText("Term \t \t  \t  \t \t tf \n"); 
            
            while (t11.hasNext()){
                
          resultTextArea.appendText(t11.next().toString() +"\t  \t "+t22.next().toString()   + " \n");
            }
      }
              
        
        catch(Exception e){
           resultTextArea.clear();
           resultTextArea.appendText( " Error , In Inverted Index ");
           
            }
  }
    

  
     
  }
 
    
    	
  

    
  