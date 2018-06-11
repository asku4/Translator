import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Translator {
  String str;
  public Translator (String str){
    this.str = str;
  }
  private boolean translate(){
    Lexer lexer = new Lexer();
    if (!lexer.delimiter (str)) {
      System.out.println("Impossible type");
      return false;
    }
    //lexer.showTokens();

    Parser parser = new Parser(lexer.getTokens());
    return parser.lang();
    }

  public static void main (String [] args){
    ArrayList <String> listExample = new ArrayList<>();
    String enter = new String();
    Scanner in = new Scanner(System.in);

    System.out.print("Введите исходный код:");

    while (!enter.equals("@")) {
      enter = in.nextLine();
      listExample.add(enter);
    }
    listExample.remove(listExample.size()-1);

    StringBuilder sb = new StringBuilder();
    for (String s : listExample)
    {
      sb.append(s);
    }

    System.out.println(sb);

//    String example = "a=(5+6)+(75+5)";
    Translator translator = new Translator(sb.toString());
    System.out.println(translator.translate());



  }
}