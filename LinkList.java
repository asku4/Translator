public class LinkList {

  public ElemLinkList head;
  public ElemLinkList tail;

  private Integer size;

  public LinkList () {
    this.head = null;
    this.tail = null;
    this.size = 0;
  }

  public void print() {
    ElemLinkList e = head;

    System.out.println("LinkList size: " + size);
    while (e != null) {
      System.out.println(e.object);
      e = e.next;
    }
  }

  public void set(int index, Object o) {
    ElemLinkList e = get(index);

    if (e == null) {
      add(o);
    } else {
      ElemLinkList elem = new ElemLinkList(e.prev, o, e);

      if (e.prev != null) {
        e.prev.next = elem;
      }
      e.prev = elem;

      size++;
    }
  }

  public ElemLinkList get(int index) {
    int i = 0;
    ElemLinkList e = head;

    while (e != null) {
      if (i == index)
        return e;

      e = e.next;
      i++;
    }
    return null;
  }

  public void add(Object o) {
    if (size == 0) {
      ElemLinkList elem = new ElemLinkList(null, o, null);
      head = elem;
      tail = elem;
    } else {
      ElemLinkList elem = new ElemLinkList(tail, o, null);
      tail.next = elem;
      tail = elem;
    }
    size++;
  }

  public void remove(Object o) {
    if (size < 1)
      return;

    ElemLinkList iterator = this.head;

    while (iterator != null) {
      if (iterator.object.equals(o)) {
        break;
      }
      iterator = iterator.next;
    }

    ElemLinkList prev = iterator.prev;
    ElemLinkList next = iterator.next;

    if (prev != null) {
      prev.next = iterator.next;
    }
    if (next != null) {
      next.prev = iterator.prev;
    }

    iterator.prev = null;
    iterator.next = null;
    iterator.object = null;

    size--;
  }
}
