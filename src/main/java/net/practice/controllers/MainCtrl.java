package net.practice.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.GridPane;
import net.practice.components.InputArea;
import net.practice.components.OutputArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author carlos on 18/10/15.
 */
public class MainCtrl implements Initializable{

  Label fixFileLabel;
  Label showGraphLabel;

  InputArea inputArea = new InputArea();
  OutputArea outputArea = new OutputArea();

  @FXML GridPane areasContainer;
  @FXML Menu fixFileMenu;
  @FXML Menu showGraphMenu;

  /**
   * TODO: fix the current file.
   */
  private void fixFile(){
    System.out.println("fixFile");
  }

  /**
   * TODO: show the current graph.
   */
  private void showGraph() {
    System.out.println("showGraph");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    fixFileLabel = new Label("Arreglar Archivo");
    fixFileLabel.setOnMouseClicked(event -> fixFile());
    fixFileMenu.setGraphic(fixFileLabel);

    showGraphLabel = new Label("Ver gráfica");
    showGraphLabel.setOnMouseClicked(event -> showGraph());
    showGraphMenu.setGraphic(showGraphLabel);

    areasContainer.add(inputArea, 0, 0);
    areasContainer.add(outputArea,1,0);
  }
}
