package net.practice.components;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/**
 * @author learnercys on 18/10/15.
 */
public  class AbstractCodeArea extends CodeArea{

  AbstractCodeArea() {
    this(true);
  }

  AbstractCodeArea(boolean codeLine) {
    if ( codeLine ) {
      super.setParagraphGraphicFactory(LineNumberFactory.get(this) );
    }
  }
}
