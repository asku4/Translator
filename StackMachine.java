import java.util.*;

public class StackMachine {
  private ArrayList<Object> RPN = new ArrayList<>();
  HashMap variable = new HashMap();
  Stack <Object> stack = new Stack<>();
  int iterator = 0;
  LinkList newLL;
  Set <Object> excludeList = new HashSet<>();
  boolean bConformed = false;

  public void StackMachine(ArrayList <Object> RPN, Set <Object> e){
    this.RPN = RPN;
    this.RPN.add("/123@");
    this.excludeList = e;


    stackMachineWork();
  }

  private void conformVariable (ArrayList <Object> RPN){
    if (bConformed)
      return;

    HashSet <Object> variables = new HashSet<>();
    Scanner in = new Scanner(System.in);

    for (Object o: RPN){
      if ( o.toString().matches("[a-z]+")){
        variables.add(o);
      }
    }
    variables.removeAll(excludeList);
    for (Object o: variables){
      System.out.println("Введите значение " + o.toString() + ": ");
      variable.put (o.toString(), in.nextInt());
    }
    bConformed = true;
    System.out.println("Исходные данные: " + variable);
  }

  private boolean stackMachineWork (){

    while ( ! RPN.get(iterator).equals("/123@") ) {
      while (RPN.get(iterator).toString().matches("[a-z]+|[0-9]+")) {
        if (RPN.get(iterator).equals("linkedlist")){
          newLL = new LinkList();
          iterator += 2;
        } else {
          stack.push(RPN.get(iterator));
          iterator++;
        }
      }

      if (RPN.get(iterator).equals(".")){
        if (!stack.empty()) {
          String o = stack.pop().toString();
          stack.pop();

          switch (o){
            case "add": {
              iterator++;
              newLL.add(RPN.get(iterator));
              break;
            }
            case "remove":{
              iterator++;
              newLL.remove(RPN.get(iterator));
              break;
            }
            case "set": {
              iterator++;
              Integer i = Integer.parseInt(RPN.get(iterator).toString());
              iterator++;
              newLL.set(i, RPN.get(iterator));
              break;
            }
            case "get":{
              iterator++;
              Integer i = Integer.parseInt(RPN.get(iterator).toString());
              newLL.get(i);
              break;
            }
          }
          iterator++;
          continue;
        }
      }

      conformVariable(RPN);

      if (RPN.get(iterator).equals("=")) {
        if (!stack.empty()) {
          int i = Integer.parseInt(stack.pop().toString());
          if (!variable.containsKey(stack.peek()))
            variable.put(stack.pop(), i);
          else {
            variable.remove(stack.peek());
            variable.put(stack.pop(), i);
          }
        }
        iterator++;
      }

      if (sign())
        iterator++;

      if (RPN.get(iterator).equals("!F")){
        iterator++;
        if (stack.peek().equals("T")){
          stack.pop();
          iterator++;
        }
        else if (stack.pop().equals("F"))
          iterator = Integer.parseInt(RPN.get(iterator).toString());

      } else if (RPN.get(iterator).equals("!")){
        iterator++;
        iterator = Integer.parseInt(RPN.get(iterator).toString());
      }

    }
    System.out.println("Результат: " + variable);
    newLL.print();
    return true;
  }

  private boolean sign(){
    Boolean f = false;
    Integer o1, o2;


    if ( ! stack.empty()) {
      Object o = stack.pop();
      if (!o.toString().matches("[0-9]+"))
        o1 = (Integer) variable.get(o);
      else o1 = Integer.parseInt(o.toString());

      o = stack.pop();
      if (!o.toString().matches("[0-9]+"))
        o2 = (Integer) variable.get(o);
      else o2 = Integer.parseInt(o.toString());

      switch (RPN.get(iterator).toString()) {
        case "<": {
          if (o2 < o1)
            stack.push("T");
          else stack.push("F");
          f = true;
          break;
        }
        case ">": {
          if (o2 > o1)
            stack.push("T");
          else stack.push("F");
          f = true;
          break;
        }
        case "==": {
          if (o2 == o1)
            stack.push("T");
          else stack.push("F");
          f = true;
          break;
        }
        case "<=": {
          if (o2 <= o1)
            stack.push("T");
          else stack.push("F");
          f = true;
          break;
        }
        case ">=": {
          if (o2 >= o1)
            stack.push("T");
          else stack.push("F");
          f = true;
          break;
        }
        case "+": {
          stack.push(o2 + o1);
          f = true;
          break;
        }
        case "-": {
          stack.push(o2 - o1);
          f = true;
          break;
        }
        case "*": {
          stack.push(o2 * o1);
          f = true;
          break;
        }
        case "/": {
          stack.push(o2 / o1);
          f = true;
          break;
        }
      }
    }
    return f;
  }
}
