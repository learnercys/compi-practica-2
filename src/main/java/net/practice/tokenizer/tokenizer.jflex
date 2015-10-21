package net.practice.tokenizer;

import java_cup.runtime.Symbol;
import java.util.HashMap;
import java.util.ArrayList;
import net.practice.parser.sym;

%%
%public
%class Tokenizer
%unicode
%line
%column
%cupsym sym
%cup

%{
    public ArrayList<HashMap<String, String>> errors = new ArrayList<>();

    private Symbol symbol(int type) {
      System.out.println(yytext());
      return new Symbol(type, yyline, yycolumn, yytext());
    }

    private Symbol symbol(int type, String value) {
      return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol intSymbol(int type) {
      return new Symbol(type, yyline, yycolumn, Integer.parseInt(yytext()));
    }

%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

// ER
ID = [a-zA-Z_][a-zA-Z0-9_]+
INT_VALUE = [:digit:]+
FLOAT_VALUE = [:digit:]+\.[:digit:]

// words
PACKAGE = {ID}".h"

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?

%state STRING, CHAR

%%

<YYINITIAL> {
  // keywords
  "#include"                      { return symbol(sym.INCLUDE); }
  '{ID}\.h'                       { return symbol(sym.PKG_NAME); }

  // comments
  {Comment}                       { /* ignore comments */ }

  /* whitespace */
  {WhiteSpace}                    { /* ignore */ }
}


/* error fallback */
[^] {
  System.out.println("Line: " + (yyline + 1) + ", column: " + (yycolumn + 1) + ", Lexical error in: " + yytext());
  HashMap<String, String> error = new HashMap<>();
  error.put("line", Integer.toString(yyline + 1));
  error.put("column", Integer.toString(yycolumn +1));
  error.put("text", yytext());
  error.put("number", Integer.toString(errors.size() + 1));
  errors.add( error );
}

