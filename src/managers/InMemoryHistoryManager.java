package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager extends Managers implements HistoryManager
{
    ArrayList<Task> history;
    CustomLinkedList customHistory;
    HashMap<Integer, Node> historyHashMap;

    final static int maxSize = 10;
    public InMemoryHistoryManager()
    {
        history = new ArrayList<>();
        customHistory = new CustomLinkedList();
        historyHashMap = new HashMap<>(10);
    }

    private class CustomLinkedList
    {
        public Node<Task> head;
        public Node<Task> tail;
        private int size = 0;
        protected void linkLast(Task task)
        {
            Node<Task> newNode = new Node<>(task);
            if(size == 0)
            {
                head = newNode;
                tail = newNode;
                size = 1;
            } else
            if(size == 1)
            {
                tail = newNode;
                tail.prev = head;
                head.next = tail;
                size = 2;
            } else
            {
                Node oldNode = tail;
                tail = newNode;
                tail.prev = oldNode;
                oldNode.next = tail;
                size++;
            }
        }

        protected ArrayList<Task> getTasks()
        {
            if(size == 0)
                return new ArrayList<>();
            System.out.println("HEAD: " + head.toString());
            System.out.println("TAIL: " + tail.toString());
            if(size == 1)
            {
                ArrayList<Task> result = new ArrayList<>(size);
                result.add(head.data);
                return result;
            }

            ArrayList<Task> result = new ArrayList<>(size);
            Node<Task> currentNode = tail;
            result.add(currentNode.data);

            while(currentNode.prev != null) {
                currentNode = currentNode.prev;
                result.add(currentNode.data);
            }
            return result;
        }

        protected void removeNode(Node node)
        {
            boolean flagHead = false;
            boolean flagTail = false;

            if(node.prev == null)
                flagHead = true;
            if(node.next == null)
                flagTail = true;

            if(flagHead && flagTail)                                //Случай с одной вершиной
            {
                head = null;
                tail = null;
            } else
            if(flagHead && !flagTail)                               //Случай с головой
            {
                head = node.next;
            } else
            if(!flagHead && flagTail)                               //Случай с хвостом
            {
                tail = node.prev;
            } else
            if(!flagHead && !flagTail)                              //Случай с серединой
            {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            size--;

        }
    }

    public void customAdd(Task task)
    {
        customHistory.linkLast(task);
    }

    public ArrayList customGetTasks()
    {
        return customHistory.getTasks();
    }

    //public void customRemoveNode(Node node)
    public void customRemoveNode(int code)
    {
        if(code == 1)
            customHistory.removeNode(customHistory.head);
        else
        if(code == 2)
            customHistory.removeNode(customHistory.tail);
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public void add(Task task)
    {
        if(history.size() == maxSize)
        {
            history.remove(0);
            history.add(task);
        } else
        {
            history.add(task);
        }
    }

    @Override
    public void remove(int id)
    {

    }

    @Override
    public ArrayList<Task> getHistory()
    {
        return history;
    }
}
