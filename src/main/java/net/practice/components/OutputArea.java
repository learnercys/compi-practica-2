package net.practice.components;

import net.practice.ast.Program;

/**
 * @author learnercys on 18/10/15.
 */
public class OutputArea extends AbstractCodeArea {

  private final String EOL = "\n";

  public OutputArea() {
    super();
    setEditable(false);
  }

  /**
   * @param program the current ast program.
   *
   * receive a AST and replace the current text properly indented.
   */
  public void setText(Program program){

    // to empty the current text field.
    replaceText("");

    // TODO: show the packages in the right order.
    // set the packages
    program.getPackages().forEach(pkg-> appendText("#include " + pkg + EOL) );
  }

}
