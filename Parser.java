import java.util.*;


public class Parser{
  private ArrayList <Token> tokens;
  private int index = 0;
  private boolean forFlag = false;

  private ArrayList <Object> RPN = new ArrayList<>();
  private Stack <Token> stack = new Stack<>();

  private Set <Object> excludeList = new HashSet<>();

//  ArrayList<String> rightSide = new ArrayList<>();
//  private HashMap <String, ArrayList<String>> rules = new HashMap();

  private Token tokenStop = new Token("stop", "0", 0);

  public Parser (ArrayList<Token> tokens){
    this.tokens = tokens;
    this.tokens.add (tokenStop);
//    createRules();
  }

//  boolean lang () {
//    for (Token t : tokens) {
//      if (t.getType() != tokenStop.getType()) {
//        if (!expr(t.getType()))
//          return false;
//      }
//    }
//    return true;
//  }
//
//  public boolean expr(String ruleType) {
//    ArrayList <String> trueRules = new ArrayList<>();
//    for (Map.Entry<String, ArrayList<String>> entry : rules.entrySet()) {
//      if (entry.getValue().get(0) == ruleType) {
//        trueRules.add(entry.getKey());
//      }
//    }
//    if (trueRules.size() > 1){
//      for ()
//    }
//
//  }
//
//  private void createRules (){
//    ArrayList <String> langRule = new ArrayList<>();
//    langRule.add("expr");
//    rules.put("lang", langRule);
//
//    ArrayList <String> exprRule = new ArrayList<>();
//    exprRule.add("assignExpr");
//    rules.put("expr", exprRule);
//
//
//  }

  private String getTokenType(int index) {
    if (index < tokens.size())
      return tokens.get(index).getType();
    return "";
  }

  private String getTokenValue (int index) {
    if (index < tokens.size())
      return tokens.get(index).getValue();
    return "";
  }

  private void showRPN(){
    System.out.println(RPN.toString());
  }

  protected boolean lang () {

    if (tokens.size() < 1){
      return false;
    }
    while (! tokens.get(index).getType().equals("stop")){
      if (!expr()) {
        return false;
      }
    }
    showRPN();

    StackMachine stackMachine = new StackMachine();
    stackMachine.handle(RPN, excludeList);

    return true;
  }


  private boolean expr () {
    return (assignExpr() || ifCondition() || whileLoop()  || forLoop() || doWhileLoop() || linkList() );
  }




  private boolean assignExpr() {
    if (tokens.size() - index < 2){
      return false;
    }
    if (getTokenType(index).equals("VAR")){
      RPN.add(tokens.get(index).getValue());
      index++;
      if (getTokenType(index).equals("ASSIGN_OP")){
        stack.push(tokens.get(index));
        index++;
        while (valueExpr ()){
          if (getTokenType(index).equals("DELIMIT") || forFlag){
            while (! stack.empty()) {
              RPN.add(stack.pop().getValue());
            }
            index++;
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean valueExpr () {
    if (value(index)){
      RPN.add(tokens.get(index).getValue());
      index++;
      return true;
    }

    if (getTokenType(index).equals("L_B")){
      index++;
      if (getTokenType(index).equals("OP1") || getTokenType(index).equals("OP2"))
        return false;
      while(!getTokenType(index).equals("R_B")){
        if (index >= tokens.size()){
          return false;
        }
        if (! valueExpr()){
          return false;
        }
      }
      index++;
      if (index + 1 < tokens.size()) {
        if (getTokenType(index).equals("OP1") || getTokenType(index).equals("OP2")) {
          stack.push(tokens.get(index));
          index++;
          if (getTokenType(index).equals("OP1") || getTokenType(index).equals("OP2"))
            return valueExpr();
          return false;
        }
        if (getTokenType(index).equals("DELIMIT")){
          return true;
        }
        return lang();
      }
      return true;
    }

    if (index > 0) {
      if (getTokenType(index - 1).equals("R_B") || value(index - 1)) {
        if (getTokenType(index).equals("OP1") || getTokenType(index).equals("OP2")) {
          stack.push(tokens.get(index));
          index++;
          while (valueExpr()) {
            if (tokens.get(index).getPriority() > stack.peek().getPriority()){
              stack.push(tokens.get(index));
              index++;
            }
            else RPN.add(stack.pop().getValue());
            return true;
          }
        }
      }
    }
    return false;
  }

  private ArrayList <Integer> ifLabels = new ArrayList<>();
  private ArrayList <Integer> ifLabelsValue = new ArrayList<>();

  private boolean ifCondition (){
    if (getTokenType(index).equals("IF_KW")){
      index++;
      if (getTokenType(index).equals("L_B")){
        index++;
        if (comp()){
          RPN.add("!F");
          RPN.add("");
          ifLabels.add(RPN.size() - 1);
          if (getTokenType(index).equals("R_B")){
           index++;
           if (!body()) {
             return false;
           }
           if (getTokenType(index).equals("ELSE_KW")) {
             RPN.set(ifLabels.get(ifLabels.size() - 1), RPN.size());
             ifLabels.remove(ifLabels.size() - 1);
             index++;
             return body();
           }

           ifLabelsValue.add(RPN.size());
           RPN.add(ifLabels.get(0), ifLabelsValue.get(0));
           ifLabels.remove(0);
           ifLabelsValue.remove(0);
           RPN.remove("");

           return true;
          }
        }
      }
    }
    return false;
  }

  private ArrayList <Integer> whileLabels = new ArrayList<>();
  private ArrayList <Integer> whileLabelsValue = new ArrayList<>();

  private boolean whileLoop (){
    if (getTokenType(index).equals("WHILE_KW")){
      index++;
      if (getTokenType(index).equals("L_B")){
        index++;
        whileLabelsValue.add(RPN.size());

        if (comp()){
          RPN.add("!F");
          RPN.add("");
          whileLabels.add(RPN.size() - 1);
          if (getTokenType(index).equals("R_B")){
            index++;
            if ( ! body()) {
              return false;
            }

            RPN.add("!");
            RPN.add(whileLabelsValue.get(0));
            whileLabelsValue.remove(0);
            whileLabelsValue.add(RPN.size());
            RPN.set(whileLabels.get(0), whileLabelsValue.get(whileLabelsValue.size() - 1));
            whileLabels.remove(0);
            whileLabelsValue.remove(0);
            System.out.println(whileLabelsValue);
          }
        }
      }
    }
    return false;
  }


  private boolean doWhileLoop () {
    if (getTokenType(index).equals("DO_KW")){
      index++;
      if (body()) {
        if (getTokenType(index).equals("WHILE_KW")){
          index++;
          if (getTokenType(index).equals("L_B")) {
            index++;
            if (comp()) {
              if (getTokenType(index).equals("R_B")) {
                index++;
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }


  private boolean forLoop(){
    if (getTokenType(index).equals("FOR_KW")){
      index++;
      forFlag = true;
      if (getTokenType(index).equals("L_B")){
        index++;
        if (assignExpr()){
          if (comp()) {
            if (getTokenType(index).equals("DELIMIT")) {
              index++;
              if (assignExpr()) {
                index++;
                forFlag = false;
                if (getTokenType(index).equals("R_B")) {
                  index++;
                  return body();
                }
              }
            }
          }
        }
      }
    }
  return false;
  }

  Set<Object> names = new HashSet<>();

  private boolean linkList(){
    if (getTokenType(index).equals("LINKEDLIST")){
      excludeList.add(getTokenValue(index));
      RPN.add(getTokenValue(index));
      index++;
      if (getTokenType(index).equals("VAR")){
        excludeList.add(getTokenValue(index));
        RPN.add(getTokenValue(index));
        names.add(getTokenValue(index));
        index++;
        if (getTokenType(index).equals("DELIMIT")){
          index++;
          return true;
        }
      }
      return false;
    }
    if ( ! names.isEmpty()){
      index--;
      if (names.contains(getTokenValue(index))){
        index++;
        if (getTokenType(index).equals("POINT")){
          index++;
          if (getTokenType(index).equals("ADD")){
            excludeList.add(getTokenValue(index));
            RPN.add(getTokenValue(index));
            RPN.add(".");
            index++;
            if (getTokenType(index).equals("L_B")){
              index++;
              excludeList.add(getTokenValue(index));
              RPN.add(getTokenValue(index));
              index++;
              if (getTokenType(index).equals("R_B")){
                index++;
                if (getTokenType(index).equals("DELIMIT")){
                  index++;
                  return true;
                }
              }
            }
          }
          if (getTokenType(index).equals("REMOVE")){
            excludeList.add(getTokenValue(index));
            RPN.add(getTokenValue(index));
            RPN.add(".");
            index++;
            if (getTokenType(index).equals("L_B")) {
              index++;
              excludeList.add(getTokenValue(index));
              RPN.add(getTokenValue(index));
              index++;
              if (getTokenType(index).equals("R_B")) {
                index++;
                if (getTokenType(index).equals("DELIMIT")) {
                  index++;
                  return true;
                }
              }
            }
          }
          if (getTokenType(index).equals("SET")){
            excludeList.add(getTokenValue(index));
            RPN.add(getTokenValue(index));
            RPN.add(".");
            index++;
            if (getTokenType(index).equals("L_B")) {
              index++;
              RPN.add(getTokenValue(index));      //индекс
              index++;
              if (getTokenType(index).equals("COMMA")) {
                index++;
                excludeList.add(getTokenValue(index));
                RPN.add(getTokenValue(index));      //значение
                index++;
                if (getTokenType(index).equals("R_B")) {
                  index++;
                  if (getTokenType(index).equals("DELIMIT")) {
                    index++;
                    return true;
                  }
                }
              }
            }
          }
        }
      }
    }
    return false;
  }



  private boolean comp(){
    if (valueExpr()) {
      if (getTokenType(index).equals("LOGIC_OP") || getTokenType(index).equals("ASSOCIATION_OP")){
        stack.push(tokens.get(index));
        index++;
        if (valueExpr()){
          RPN.add(stack.pop().getValue());
          return true;
        }
      }
    }
    if (getTokenType(index).equals("BOOL")){
      RPN.add(tokens.get(index).getValue());
      index++;
      return true;
    }
    return false;
  }

  private boolean body(){
    if (getTokenType(index).equals("L_FB")) {
      index++;
      while (!getTokenType(index).equals("R_FB")) {
        if (index + 1 > tokens.size()) {
          return false;
        }
        if (!expr()) {
          return false;
        }
      }
      index++;
      return true;
    }
    return false;
  }

  private boolean value (int localIndex){
    if (!getTokenType(localIndex).equals("VAR"))
      if (!getTokenType(localIndex).equals("DIGIT"))
        return false;
    return true;
  }


}