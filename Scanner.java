//
//  SNARL/SCANNER. Token scanner.
//
//    James Moen
//    16 Sep 11
//

import java.util.Hashtable;

//  SCANNER. Snarl source language token scanner.

class Scanner extends Common
{ private Hashtable<String, Integer> reservedNames;  //  Map names to tokens.
  private int                        token;          //  Current token.
  private int                        tokenInteger;   //  TOKEN as an INT.
  private Source                     source;         //  Source file.
  private String                     tokenString;    //  TOKEN as a STRING.

//  Constructor. Make a new token scanner that reads tokens from SOURCE.

  public Scanner(Source source)
  { reservedNames = new Hashtable<String, Integer>();
    reservedNames.put("and",    new Integer(boldAndToken));
    reservedNames.put("begin",  new Integer(boldBeginToken));
    reservedNames.put("code",   new Integer(boldCodeToken));
    reservedNames.put("do",     new Integer(boldDoToken));
    reservedNames.put("else",   new Integer(boldElseToken));
    reservedNames.put("end",    new Integer(boldEndToken));
    reservedNames.put("if",     new Integer(boldIfToken));
    reservedNames.put("int",    new Integer(boldIntToken));
    reservedNames.put("or",     new Integer(boldOrToken));
    reservedNames.put("not",    new Integer(boldNotToken));
    reservedNames.put("proc",   new Integer(boldProcToken));
    reservedNames.put("string", new Integer(boldStringToken));
    reservedNames.put("then",   new Integer(boldThenToken));
    reservedNames.put("value",  new Integer(boldValueToken));
    reservedNames.put("while",  new Integer(boldWhileToken));
    this.source = source;
    tokenInteger = 0;
    tokenString = "";
    nextToken(); }

//  GET INTEGER. Return the INT value of TOKEN, if it exists.

  public int getInteger()
  { return tokenInteger; }

//  GET STRING. Return the STRING value of TOKEN, if it exists.

  public String getString()
  { return tokenString; }

//  GET TOKEN. Return the current token.

  public int getToken()
  { return token; }

//  IS DIGIT. Test if CH is a decimal digit.

  private boolean isDigit(char ch)
  { return '0' <= ch && ch <= '9'; }

//  IS LETTER. Test if CH is a Roman letter.

  private boolean isLetter(char ch)
  { return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z'; }

//  IS LETTER OR DIGIT. Test if CH is a decimal digit or a Roman letter.

  private boolean isLetterOrDigit(char ch)
  { return isLetter(ch) || isDigit(ch); }

//  MAIN. For testing.

  public static void main(String[] files)
  { Scanner scanner = new Scanner(new Source(files[0]));
    while (scanner.getToken() != endFileToken)
    { System.out.print(tokenToString(scanner.getToken()));
      switch (scanner.getToken())
      { case intConstantToken:
        { System.out.print(" " + scanner.getInteger());
          break; }
        case nameToken:
        case stringConstantToken:
        { System.out.print(" \"" + scanner.getString() + "\"");
          break; }}
      System.out.println();
      scanner.nextToken(); }}

//  NEXT TOKEN. Get the next token from READER.

  public void nextToken()
  { token = ignoredToken;
    while (token == ignoredToken)
    { if (isLetter(source.getChar()))
      { nextName(); }
      else if (isDigit(source.getChar()))
           { nextIntConstant(); }
           else
           { switch (source.getChar())
             { case eofChar: { nextEndFile();        break; }
               case ' ':     { source.nextChar();    break; }
               case '"':     { nextStringConstant(); break; }
               case '#':     { nextComment();        break; }
               case '(':     { nextOpenParen();      break; }
               case ')':     { nextCloseParen();     break; }
               case '*':     { nextStar();           break; }
               case '+':     { nextPlus();           break; }
               case ',':     { nextComma();          break; }
               case '-':     { nextDash();           break; }
               case '/':     { nextSlash();          break; }
               case ':':     { nextColon();          break; }
               case ';':     { nextSemicolon();      break; }
               case '<':     { nextLess();           break; }
               case '=':     { nextEqual();          break; }
               case '>':     { nextGreater();        break; }
               case '[':     { nextOpenBracket();    break; }
               case ']':     { nextCloseBracket();   break; }
               default:      { nextIllegal();        break; }}}}}

//  NEXT TOKEN. If the current token is EXPECTED TOKEN, then get the next token
//  as above. If it isn't, then write an error and halt the program.

  public void nextToken(int expectedToken)
  { if (token == expectedToken)
    { nextToken(); }
    else
    { source.error(
       "\"" + tokenToString(expectedToken) +
       "\" expected instead of \"" +
       tokenToString(token) + "\"."); }}

//  NEXT CLOSE BRACKET. Scan a CLOSE BRACKET TOKEN.

  private void nextCloseBracket()
  { token = closeBracketToken;
    source.nextChar(); }

//  NEXT CLOSE PAREN. Scan a CLOSE PAREN TOKEN.

  private void nextCloseParen()
  { token = closeParenToken;
    source.nextChar(); }

//  NEXT COLON. Scan a COLON EQUAL TOKEN.

  private void nextColon()
  { source.nextChar();
    if (source.getChar() == '=')
    { token = colonEqualToken;
      source.nextChar(); }
    else
    { token = colonToken; }}

//  NEXT COMMA. Scan a COMMA TOKEN.

  private void nextComma()
  { token = commaToken;
    source.nextChar(); }

//  NEXT COMMENT. Scan a comment. Skip chars until we hit the end of the line.

  private void nextComment()
  { while (! source.atLineEnd())
    { source.nextChar(); }
    source.nextChar(); }

//  NEXT DASH. Scan a DASH TOKEN.

  private void nextDash()
  { token = dashToken;
    source.nextChar(); }

//  NEXT END FILE. Scan an END FILE TOKEN.

  private void nextEndFile()
  { token = endFileToken; }

//  NEXT EQUAL. Scan an EQUAL TOKEN.

  private void nextEqual()
  { token = equalToken;
    source.nextChar(); }

//  NEXT GREATER. Scan a GREATER TOKEN or a GREATER EQUAL TOKEN.

  private void nextGreater()
  { source.nextChar();
    if (source.getChar() == '=')
    { token = greaterEqualToken;
      source.nextChar(); }
    else
    { token = greaterToken; }}

//  NEXT ILLEGAL. Scan an illegal token.

  private void nextIllegal()
  {
    source.error("Illegal token.");
  }

//  NEXT INT CONSTANT. Scan an INT CONSTANT TOKEN.

  private void nextIntConstant()
  { long temp = 0;
    while (isDigit(source.getChar()))
    { temp = 10 * temp + source.getChar() - '0';
      if (temp > Integer.MAX_VALUE)
      { source.error("Integer constant is too large."); }
      source.nextChar(); }
    token = intConstantToken;
    tokenInteger = (int) temp; }

//  NEXT LESS. Scan a LESS TOKEN, a LESS EQUAL TOKEN, or a LESS GREATER TOKEN.

  private void nextLess()
  { source.nextChar();
    if (source.getChar() == '=')
    { token = lessEqualToken;
      source.nextChar(); }
    else if (source.getChar() == '>')
         { token = lessGreaterToken;
           source.nextChar(); }
         else
         { token = lessToken; }}

//  NEXT NAME. Scan a NAME TOKEN or one of the reserved tokens.

  private void nextName()
  { StringBuffer buffer = new StringBuffer();
    while (isLetterOrDigit(source.getChar()))
    { buffer.append(source.getChar());
      source.nextChar(); }
    tokenString = buffer.toString();
    if (reservedNames.containsKey(tokenString))
    { token = ((Integer) reservedNames.get(tokenString)).intValue(); }
    else
    { token = nameToken; }}

//  NEXT OPEN BRACKET. Scan an OPEN BRACKET TOKEN.

  private void nextOpenBracket()
  { token = openBracketToken;
    source.nextChar(); }

//  NEXT OPEN PAREN. Scan an OPEN PAREN TOKEN.

  private void nextOpenParen()
  { token = openParenToken;
    source.nextChar(); }

//  NEXT PLUS. Scan a PLUS TOKEN.

  private void nextPlus()
  { token = plusToken;
    source.nextChar(); }

//  NEXT SEMICOLON. Scan a SEMICOLON TOKEN.

  private void nextSemicolon()
  { token = semicolonToken;
    source.nextChar(); }

//  NEXT SLASH. Scan a SLASH TOKEN.

  private void nextSlash()
  { token = slashToken;
    source.nextChar(); }

//  NEXT STAR. Scan a STAR TOKEN.

  private void nextStar()
  { token = starToken;
    source.nextChar(); }

//  NEXT STRING CONSTANT. Scan a STRING CONSTANT TOKEN.

  private void nextStringConstant()
  { token = stringConstantToken;
    StringBuffer buffer = new StringBuffer();
    source.nextChar();
    while (source.getChar() != '"' && ! source.atLineEnd())
    { buffer.append(source.getChar());
      source.nextChar(); }
    if (source.getChar() == '"')
    { source.nextChar(); }
    else
    { source.error("String has no closing quote."); }
    tokenString = buffer.toString(); }
}
