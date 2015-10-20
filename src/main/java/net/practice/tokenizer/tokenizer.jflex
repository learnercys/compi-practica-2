package net.practice.tokenizer;

import java_cup.runtime.Symbol;
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

%state STRING, CHAR, INCLUDE


%%

<YYINITIAL> {
  // keywords
  "#include"                      { yybegin(INCLUDE); return symbol(sym.INCLUDE); }
  "int"                           { return symbol(sym.INT); }
  "char"                          { return symbol(sym.CHAR); }
  "float"                         { return symbol(sym.FLOAT); }
  "do"                            { return symbol(sym.DO); }
  "while"                         { return symbol(sym.WHILE); }
  "for"                           { return symbol(sym.FOR); }
  "switch"                        { return symbol(sym.SWITCH); }
  "case"                          { return symbol(sym.CASE); }
  "default"                       { return symbol(sym.DEFAULT); }
  "break"                         { return symbol(sym.BREAK); }
  "if"                            { return symbol(sym.IF); }
  "else"                          { return symbol(sym.ELSE); }

  // single chars with meaning - arithmetic operators
  \*                              { return symbol(sym.MULTIPLICATION); }
  \+                              { return symbol(sym.PLUS); }
  \-                              { return symbol(sym.MINUS); }
  \/                              { return symbol(sym.SLASH); }
  "%"                             { return symbol(sym.MODULE); }

  // single chars with meaning - relational operators
  "<"                             { return symbol(sym.LT); }
  ">"                             { return symbol(sym.MT); }
  "<="                            { return symbol(sym.LTEQ); }
  ">="                            { return symbol(sym.MTEQ); }
  "=="                            { return symbol(sym.EQEQ); }
  "!="                            { return symbol(sym.NEQ); }

  // single charts with meaning - increase, decrease
  "++"                            { return symbol(sym.PLUS_PLUS); }
  "--"                            { return symbol(sym.MINUS_MINUS); }

  // single chars with meaning - assignation operations
  "+="                            { return symbol(sym.PLUS_EQ); }
  "-="                            { return symbol(sym.MINUS_EQ); }
  "*="                            { return symbol(sym.MULTIPLICATION_EQ); }

  // values
  {INT_VALUE}                     { return symbol(sym.INT_VALUE); }
  {FLOAT_VALUE}                   { return symbol(sym.FLOAT_VALUE); }
  \"                              { string.setLength(0); yybegin(STRING); }
  \'                              { string.setLength(0); yybegin(CHAR); }
  ":"                             { return symbol(sym.COLON); }
  ";"                             { return symbol(sym.SEMICOLON); }
  ","                             { return symbol(sym.COMMA); }
  "="                             { return symbol(sym.EQ); }
  "{"                             { return symbol(sym.O_BRACE); }
  "}"                             { return symbol(sym.C_BRACE); }
  "("                             { return symbol(sym.O_PAREN); }
  ")"                             { return symbol(sym.C_PAREN); }
  {ID}                            { return symbol(symID); }


  // comments
  {Comment}                       { /* ignore comments */ }

  /* whitespace */
  {WhiteSpace}                    { /* ignore */ }

}

<STRING> {
  \"                              { yybegin(YYINITIAL); return symbol(sym.STRING_VALUE, string.toString()); }
  [^\n\r\"\\]+                    { string.append( yytext() ); }
  \\t                             { string.append('\t'); }
  \\n                             { string.append('\n'); }

  \\r                             { string.append('\r'); }
  \\\"                            { string.append('\"'); }
  \\                              { string.append('\\'); }
}

<CHAR> {
  [^\n\r\"\\]\'                   { yybegin(YYINITIAL); string.append( yytext() ); return symbol(sym.CHAR_VALUE, string.toString());}
  \\t\'                           { yybegin(YYINITIAL); string.append('\t'); return symbol(sym.CHAR_VALUE, string.toString());}
  \\n\'                           { yybegin(YYINITIAL); string.append('\n'); return symbol(sym.CHAR_VALUE, string.toString());}

  \\r\'                           { yybegin(YYINITIAL); string.append('\r'); return symbol(sym.CHAR_VALUE, string.toString());}
  \\\'                            { yybegin(YYINITIAL); string.append('\''); return symbol(sym.CHAR_VALUE, string.toString());}
}

<INCLUDE> {
  \'{ID}\.h\'                     { yybegin(YYINITIAL); return symbol(sym.PACKAGE_NAME); }
}
