import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class LinkedListDeque<T> {
    /* subclass */
    private class StuffNode{
        private T item;
        private StuffNode next;
        private StuffNode prev;
    /* constructor for node */  
        public StuffNode(T i,StuffNode n,StuffNode p){
            item = i;
            next = n;
            prev = p;
        }
     
        public StuffNode(StuffNode n,StuffNode p) {
            next = n;
            prev = p;
        }
    }

    /*instance variable */
    private StuffNode sentinel;
    private int size;

    /*create an empty list */
    public LinkedListDeque(){
        sentinel = new StuffNode(null,null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item){
        sentinel.next  = new StuffNode(item,sentinel.next,sentinel);
        sentinel.next.next.prev = sentinel.next;
        size = size + 1;
    }


    public void addLast(T item){
        sentinel.prev.next= new StuffNode(item,sentinel,sentinel.prev);
        sentinel.prev = sentinel.prev.next;
        size += 1;
    }


    public boolean isEmpty(){
        return size == 0;
    }


    public int size(){
        return size;
    }


    public void printDeque(){
        StuffNode p = sentinel.next;
        for(int i = 0; i < size; i += 1){
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }


    public T removeFirst(){
        if(size == 0){
            return null;
        }
        T item = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return item;
    }


    public T removeLast(){
        if(size == 0){
            return null;
        }
        T item = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return item;
    }


    public T get(int index){
        StuffNode p = sentinel.next;
        for(int i = 0; i < index; i += 1){
            p = p.next;
        }
        return p.item;
    }

    private T getRecursiveHelper(StuffNode p,int index) {
        if (index == 0) {
            return p.item;
        }
        return getRecursiveHelper(p.next, index - 1);
    }

    public T getRecursive(int index){
        StuffNode p = sentinel.next;
        return getRecursiveHelper(p,index);
    }


    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T>{
        private int wizPos;

        public LinkedListDequeIterator(){
            wizPos = 0;
        }

        public boolean hasNext(){
            return wizPos < size;
        }

        public T next(){
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o instanceof LinkedListDeque other){
            if(this.size != other.size){
                return false;
            }
            for(int i = 0; i < size(); i += 1){
                if(this.get(i) != other.get(i)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }




    public static void main(String[] args) {

        ArrayDeque<String> lld1 = new ArrayDeque<>();
        ArrayDeque<String> lld2 = new ArrayDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        System.out.println(lld1);
    }
}