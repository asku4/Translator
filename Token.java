public class Token {
  private String type;
  private String value;
  private int priority;

  Token (String type, String value, int priority){
    this.type = type;
    this.value = value;
    this.priority = priority;
  }

  String getType() {
    return this.type;
  }
  String getValue() {
    return this.value;
  }
  int getPriority() { return this.priority; }


}