package tasks;

public class Node <T> {

    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    @Override
    public String toString() {
        String result = "Node{";
        if(data != null)
            result += "data=" + data;
        else
            result += "data=null";

        if(next != null)
            result += ", next=" + next.data.hashCode();
        else
            result += ", next=null";

        if(prev != null)
            result += ", prev=" + prev.data.hashCode() + '}';
        else
            result += ", prev=null}";

        return result;
    }
}