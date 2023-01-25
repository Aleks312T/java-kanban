package managers;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager extends Managers implements HistoryManager
{
    ArrayList<Task> history;
    final static int maxSize = 10;
    public InMemoryHistoryManager()
    {
        history = new ArrayList<>();
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
            if(size == 1)
            {
                ArrayList<Task> result = new ArrayList<>(size);
                result.add(head.data);
                return result;
            }

            ArrayList<Task> result = new ArrayList<>(size);
            Node<Task> currentNode = tail;

            do {
                result.add(currentNode.data);
                currentNode = currentNode.prev;
            } while(currentNode.prev != null);
            return result;
        }
    }

    CustomLinkedList customLinkedList = new CustomLinkedList();

    public void customAdd(Task task)
    {
        customLinkedList.linkLast(task);
    }

    public ArrayList customGetTasks()
    {
        return customLinkedList.getTasks();
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
