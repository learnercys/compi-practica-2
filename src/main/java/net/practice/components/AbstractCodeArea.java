package net.practice.components;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.concurrent.Task;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.reactfx.EventStream;

/**
 * @author learnercys on 18/10/15.
 */
public  class AbstractCodeArea extends CodeArea{

  private ExecutorService executor;

  private static final String[] KEYWORD = new String[] {
    "int", "float", "char", "void"
  };

  private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORD) + ")\\b";
  //private static final String INCLUDE_PATTERN = "";
  private static final String PAREN_PATTERN = "\\(|\\)";
  private static final String BRACE_PATTERN = "\\{|\\}";
  private static final String BRACKET_PATTERN = "\\[|\\]";
  private static final String SEMICOLON_PATTERN = "\\;";
  private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
  private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

  private static final Pattern PATTERN = Pattern.compile(
    "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
    + "|(?<PAREN>" + PAREN_PATTERN + ")"
    + "|(?<BRACE>" + BRACE_PATTERN + ")"
    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
    + "|(?<STRING>" + STRING_PATTERN + ")"
    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
  );

  /**
   * Initiate the Code Area showing the line numbers.
   */
  public AbstractCodeArea() {
    this(true);
  }

  /**
   * Initiate the Code Area
   * @param codeLine To know if the user wants to show the line numbers
   */
  public AbstractCodeArea(boolean codeLine) {
    executor = Executors.newSingleThreadExecutor();

    if ( codeLine ) {
      super.setParagraphGraphicFactory(LineNumberFactory.get(this) );
    }

    EventStream<?> codeAreaChanges = this.richChanges();

    codeAreaChanges
      .successionEnds(Duration.ofMillis(500))
      .supplyTask(this::computeHighlightingAsync)
      .awaitLatest(codeAreaChanges)
      .filterMap(styleSpansTry -> {
        if(styleSpansTry.isSuccess()) {
          return Optional.of(styleSpansTry.get());
        } else {
          styleSpansTry.getFailure().printStackTrace();
          return Optional.empty();
        }
      })
      .subscribe(this::applyHighlighting);
  }

  /**
   * Apply the given highlighting
   * @param highlighting The new highlighting.
   */
  private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
    setStyleSpans(0, highlighting);
  }

  private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
    String text = getText();
    Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
      @Override
      protected StyleSpans<Collection<String>> call() throws Exception {
        return computeHighlighting(text);
      }
    };
    executor.execute(task);
    return task;
  }

  private static StyleSpans<Collection<String>> computeHighlighting(String text) {
    Matcher matcher = PATTERN.matcher(text);
    int lastKwEnd = 0;
    StyleSpansBuilder<Collection<String>> spansBuilder
      = new StyleSpansBuilder<>();
    while(matcher.find()) {
      String styleClass =
        matcher.group("KEYWORD") != null ? "keyword" :
          matcher.group("PAREN") != null ? "paren" :
            matcher.group("BRACE") != null ? "brace" :
              matcher.group("BRACKET") != null ? "bracket" :
                matcher.group("SEMICOLON") != null ? "semicolon" :
                  matcher.group("STRING") != null ? "string" :
                    matcher.group("COMMENT") != null ? "comment" :
                      null; /* never happens */ assert styleClass != null;

      spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
      spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
      lastKwEnd = matcher.end();
    }
    spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
    return spansBuilder.create();
  }

}
