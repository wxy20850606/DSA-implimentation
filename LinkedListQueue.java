import java.util.LinkedList;

public class LinkedListQueue<T> {
    public LinkedList<T> list;
    public LinkedListQueue(){
        list = new LinkedList<>();
    }

    //Adds an element to the rear of the queue. If the queue is full, it throws an exception.
    public void add(T item){
        list.addLast(item);
    }

    //Retrieves and removes the head of this queue.
    //This method differ from poll() only in that it throws an exception if this queue is empty.


    //remove(): Removes and returns the element at the front of the queue. If the queue is empty, it throws an exception.
    public T remove() throws Exception {
        if(list.isEmpty()){
            throw new Exception("queue is empty");
        }
        T item = list.getFirst();
        list.removeFirst();
        return item;
    }

    //poll(): Removes and returns the element at the front of the queue. If the queue is empty, it returns null.
    public T poll(){
        if(list.isEmpty()){
            return null;
        }
        T item = list.getFirst();
        list.removeFirst();
        return item;
    }

    //element(): Returns the element at the front of the queue without removing it. If the queue is empty, it throws an exception.
    public T element() throws Exception {
        if(list.isEmpty()){
            throw new Exception("queue is empty");
        }
        return list.getFirst();
    }

    //peek(): Returns the element at the front of the queue without removing it. If the queue is empty, it returns null.
    public T peek(){
        if(list.isEmpty()){
            return null;}
        return list.getFirst();
    }
}
