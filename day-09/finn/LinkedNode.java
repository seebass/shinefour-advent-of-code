public class LinkedNode<T> {

    private LinkedNode<T> next;
    private LinkedNode<T> pre;
    private T value;

    public LinkedNode(LinkedNode<T> next, LinkedNode<T> pre, T value) {
        this.next = next;
        this.pre = pre;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public LinkedNode<T> getNext() {
        return next;
    }

    public void setNext(LinkedNode<T> next) {
        this.next = next;
    }

    public boolean hasNext() {
        return next != null;
    }

    public LinkedNode<T> getPre() {
        return pre;
    }

    public void setPre(LinkedNode<T> pre) {
        this.pre = pre;
    }

    public boolean hasPre() {
        return pre != null;
    }

    public void addAfter(T value) {
        final LinkedNode<T> newNode = new LinkedNode<>(this.next, this, value);
        if (this.next != null) {
            this.next.setPre(newNode);
        }
        this.setNext(newNode);
    }

}
