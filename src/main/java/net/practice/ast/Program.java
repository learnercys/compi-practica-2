package net.practice.ast;

import java.util.LinkedList;

/**
 * @author learnercys on 10/19/15.
 */
public class Program {
  private LinkedList<String> packages;

  public Program(LinkedList<String> pl/*, todo: add statements to constructor */) {
    packages = pl;
  }

  /**
   * Return the current packages saved in the program.
   * @return The packages.
   */
  public LinkedList<String> getPackages() {
    return packages;
  }

}

