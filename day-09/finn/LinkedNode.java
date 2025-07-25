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

    public void addBefore(T value) {
        final LinkedNode<T> newNode = new LinkedNode<>(this, this.pre, value);
        if (this.pre != null) {
            this.pre.setNext(newNode);
        }
        this.setPre(newNode);
    }

    public void addAfter(T value) {
        final LinkedNode<T> newNode = new LinkedNode<>(this.next, this, value);
        if (this.next != null) {
            this.next.setPre(newNode);
        }
        this.setNext(newNode);
    }

    public LinkedNode<T> getFirst() {
        LinkedNode<T> current = this;

        while (current.getPre() != null) {
            current = current.getPre();
        }

        return current;
    }

    public int getSize() {
        int size = 0;
        LinkedNode<T> current = getFirst();

        while (current != null) {
            size++;
            current = current.getNext();
        }

        return size;
    }

}
