package net.practice.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.GridPane;
import net.practice.ast.Program;
import net.practice.components.InputArea;
import net.practice.components.OutputArea;
import net.practice.parser.Parser;
import net.practice.tokenizer.Tokenizer;

import java.io.StringReader;
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

  Parser parser;
  Program program;
  Tokenizer tokenizer;

  @FXML GridPane areasContainer;
  @FXML Menu fixFileMenu;
  @FXML Menu showGraphMenu;

  /**
   * TODO: fix the current file.
   */
  private void fixFile(){
    System.out.println("fixFile");

    if( inputArea.getText().trim().length() == 0) {
      // TODO: can't fix an empty file
    }

    StringReader stringReader = new StringReader(inputArea.getText());
    tokenizer = new Tokenizer(stringReader);
    parser = new Parser(tokenizer);

    try {

      program = ((Program) parser.parse().value);
      outputArea.setText(program);
      System.out.println("parse successful");
    } catch (Exception e) {
      // TODO: can't read the current file( if that can be possible)
    }
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
