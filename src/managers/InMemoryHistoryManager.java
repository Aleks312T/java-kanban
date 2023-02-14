package managers;

import tasks.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager extends Managers implements HistoryManager
{
    CustomLinkedList customHistory;
    HashMap<Integer, Node<Task>> historyHashMap;

    final static int maxSize = 10;
    public InMemoryHistoryManager()
    {
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
                Node<Task> oldNode = tail;
                tail = newNode;
                tail.prev = oldNode;
                oldNode.next = tail;
                size++;
            }
            historyHashMap.put(task.getId(), newNode);
        }

        protected ArrayList<Task> getTasks()
        {
            if(size == 0)
                return new ArrayList<>();

            ArrayList<Task> result = new ArrayList<>(size);
            Node<Task> currentNode = tail;
            result.add(currentNode.data);

            while(currentNode.prev != null) {
                currentNode = currentNode.prev;
                result.add(currentNode.data);
            }
            return result;
        }

        protected void removeNode(Node<Task> node)
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
                head.prev = null;
            } else
            if(!flagHead && flagTail)                               //Случай с хвостом
            {
                tail = node.prev;
                tail.next = null;
            } else
            if(!flagHead && !flagTail)                              //Случай с серединой
            {
                node.prev.next = node.next;                         //Привязываем предыдущую вершину к следующей
                node.next.prev = node.prev;                         //Привязываем следующую вершину к предыдущей
            }
            size--;
            historyHashMap.remove(node.data.getId());
        }

        protected void clearHistory()
        {
            head = null;
            tail = null;
            size = 0;
            historyHashMap.clear();
        }
    }

    public void clearHistory()
    {
        customHistory.clearHistory();
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public void add(Task task)
    {
        if(customHistory.size == maxSize)
            customHistory.removeNode(customHistory.head);

        if(!historyHashMap.containsKey(task.getId()))
        {
            customHistory.linkLast(task);                           //Добавляем вершину в конец если ее не было
        } else
        {
            //Если вершина уже была ее надо сначала стереть
            customHistory.removeNode(historyHashMap.get(task.getId()));
            //А потом добавить в конец
            customHistory.linkLast(task);
        }
    }

    @Override
    public void remove(int id)
    {
        if(historyHashMap.containsKey(id))
        {
            customHistory.removeNode(historyHashMap.get(id));
            historyHashMap.remove(id);
        }
    }

    @Override
    public ArrayList<Task> getHistory()
    {
        return customHistory.getTasks();
    }
}
