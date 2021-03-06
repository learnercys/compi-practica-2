package net.practice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.practice.controllers.MainCtrl;

/**
 * @author learnercys on 18/10/15.
 */
public class Main extends Application{
  @Override
  public void start(Stage primaryStage) throws Exception {
    BorderPane root = FXMLLoader.load(getClass().getResource("main_ctrl.fxml"));
    Scene scene = new Scene(root);
    scene.getStylesheets().add(Main.class.getResource("components/c-styles.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setMaximized(false);
    primaryStage.show();
  }
}
