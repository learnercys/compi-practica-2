package net.practice.ast;

import java.util.Stack;

/**
 * @author learnercys on 10/19/15.
 */
public class Program {
  private Stack<String> packages;

  Program(Stack<String> pl/*, todo: add statements to constructor */) {
    packages = pl;
  }

  /**
   * Return the current packages saved in the program.
   * @return The packages.
   */
  public Stack<String> getPackages() {
    return packages;
  }

}

