import java.util.ArrayList;
import java.util.regex.Pattern;

public class Lexer {
  enum lexeme {
    LINKEDLIST (Pattern.compile("^linkedlist$")),
    SET(Pattern.compile("^set$")),
    GET(Pattern.compile("^get$")),
    ADD (Pattern.compile("^add$")),
    REMOVE (Pattern.compile("^remove$")),

    WHILE_KW(Pattern.compile("^while$")),
    DO_KW(Pattern.compile("^do$")),
    IF_KW(Pattern.compile("^if$")),
    ELSE_KW(Pattern.compile("^else$")),
    FOR_KW(Pattern.compile("^for$")),
    BOOL (Pattern.compile("^true|false$")),
    VAR (Pattern.compile("^[a-z]*$")),
    DIGIT (Pattern.compile("^0|([1-9][0-9]*)$")),
    OP1 (Pattern.compile("^[\\+\\-]$"),1),
    OP2 (Pattern.compile("^[\\/\\*]$"),2),
    LOGIC_OP (Pattern.compile("^[<>]|==|>=|<=|!=$")),
    ASSOCIATION_OP (Pattern.compile("^\\&|\\|$")),
    ASSIGN_OP (Pattern.compile("^[=]$")),
    L_B (Pattern.compile("^[(]$")),
    R_B (Pattern.compile("^[)]$")),
    L_FB (Pattern.compile("^[{]$")),
    R_FB (Pattern.compile("^[}]$")),
    DELIMIT (Pattern.compile("^[;]$")),

    COMMA (Pattern.compile(",")),
    POINT (Pattern.compile("."));

    private Pattern p;
    private int priority;
    lexeme(Pattern p, int priority) {
      this.p = p;
      this.priority = priority;
    }
    lexeme(Pattern p) {
      this.p = p;
      this.priority = 0;
    }

    public Pattern getPattern (){
      return this.p;
    }
    public int getPriority () { return this.priority; }

  }

  int priority;

  private String comparison (String str){
    for (lexeme lex: lexeme.values()) {
      if (lex.getPattern().matcher(str).matches()){
        priority = lex.getPriority();
        return lex.name();
      }
    }
    return null;
  }

  boolean delimiter (String str){
    String type;
    String buf = new String();
    str = str.trim();
    str += "@123";

    while (!str.equals("@123")) {
      buf += str.charAt(buf.length());
      type = comparison (buf);

      if (type == null){
        if(buf.length() > 1) {
          buf = buf.substring (0, buf.length()-1);
          type = comparison (buf);
          addToken(new Token (type, buf, priority));
          str = str.substring(buf.length());
          str = str.trim();
          buf = "";
        } else {
          System.out.println ("Error: " + buf);
          return false;
        }
      }
    }
    return true;
  }


  private ArrayList<Token> tokens = new ArrayList<>();

  public ArrayList getTokens(){ return tokens; }

  void showTokens(){
    for (Token t: tokens){
      System.out.println (t.getType()+ " : "+ t.getValue());
    }
  }

  private void addToken(Token token) {
    tokens.add(token);
  }


}