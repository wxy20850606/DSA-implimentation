import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private int length;

    /**
     * create an empty array
     */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
        length = 8;
    }

    /**copy the old list to a new list after grow/shrink */
    private void copy(int capacity){
        T[] a = (T[]) new Object[capacity];
        int firstIndex = plusOne(nextFirst,length);
        int i = 0;
        while (i < size) {
            a[i] = items[firstIndex];
            firstIndex = plusOne(firstIndex, length);
            i++;}
        items = a;
    }
    /**grow the list if needed to add items*/
    private void grow() {
        copy(length*2);
        nextLast = length;
        nextFirst = length * 2 - 1;
        length = length * 2;
    }

    /**shrink the list if needed after removing items*/
    private void shrink(){
        copy(length/2);
        nextLast = size;
        nextFirst = length - 1;
        length = length / 2;
    }
    private int plusOne(int index,int length){
        index %= length;
        if (index == length - 1) {
            return 0;
        }
        return index + 1;
    }

    private int minusOne(int index,int length){
        if (index  == 0){
            return length-1;
        }
        return index-1;
    }
    /** Inserts X into the first of the list. */
    @Override
    public void addLast(T item){
        if(size == items.length){
            grow();
        }
        items[nextLast] = item;
        nextLast = plusOne(nextLast,length);
        size = size + 1;
    }

    @Override
    public void addFirst(T item){
        if(size == items.length){
            grow();
        }
        items[nextFirst] = item;
        nextFirst = minusOne(nextFirst,length);
        size = size + 1;
    }
    @Override
    public T get(int index){
        if(index >= size){
            return null;
        }
        int firstIndex = plusOne(nextFirst,length);
        for(int i = 0;i < index; i++){
            firstIndex = plusOne(firstIndex,length);
        }
        return items[firstIndex];
    }

    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public void printDeque(){
        int firstIndex = plusOne(nextFirst,length);
        while(firstIndex != nextLast){
            System.out.print(items[firstIndex]+" ");
            firstIndex = plusOne(firstIndex,length);
        }
        System.out.println();
    }
    @Override
    public T removeLast(){
        if(size ==0){
            return null;
        }
        int lastIndex = minusOne(nextLast,length);
        T item = items[lastIndex];
        items[lastIndex] = null;
        nextLast = minusOne(nextLast,length);
        size -= 1;
        if(size < length * 0.25 && length > 16){
            shrink();
        }
        return item;
    }
    @Override
    public T removeFirst(){
        if(size == 0){
            return null;
        }
        int firstIndex = plusOne(nextFirst,length);
        T item = items[firstIndex];
        items[firstIndex] = null;
        nextFirst = plusOne(nextFirst,length);
        size -= 1;
        if(size < (length * 0.25) && length > 16){
            shrink();
        }
        return item;
    }

    @Override
    public Iterator<T> iterator(){
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T>{
        private int wizPos;

        public ArrayDequeIterator(){
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

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o instanceof ArrayDeque otherDeque){
            if(this.size != otherDeque.size){
                return false;
            }
            for (int i = 0; i < size; i += 1){
                if(this.get(i) != otherDeque.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
        }
    @Override
    public String toString(){
        List<String> myString = new ArrayList<>();
        for(T x : this){
            myString.add(x.toString());
        }
        return "["+String.join(",",myString)+"]";
    }

    public void print(){
        System.out.println("test");
    }

    public static void main(String[] args) {

        Deque<String> lld1 = new ArrayDeque<>();
        Deque<String> lld2 = new ArrayDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        System.out.println(lld1.equals(lld2));
    }
}