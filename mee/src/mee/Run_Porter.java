package mee;

import javafx.application.Application;
import javax.swing.JOptionPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Run_Porter extends Application{

     @Override
    public void start(Stage primaryStage) throws Exception {
    	 try {
       Parent root = FXMLLoader.load(getClass().getResource("searchh.fxml"));
       Scene scene = new Scene(root);
       primaryStage.setScene(scene);
       primaryStage.initStyle(StageStyle.UNDECORATED);
       primaryStage.sizeToScene();
       primaryStage.show();
       
       
    }catch(Exception e){
    	JOptionPane.showMessageDialog(null, e);
    }

  }
     
     public static void main(String[] args) {
         launch(args);
     }
}
    