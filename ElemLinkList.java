public class ElemLinkList {

  public Object object;
  public ElemLinkList prev;
  public ElemLinkList next;

  ElemLinkList (ElemLinkList prev, Object o, ElemLinkList next) {
    this.object = o;
    this.prev = prev;
    this.next = next;
  }
}
