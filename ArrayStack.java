import java.util.ArrayDeque;
public class ArrayStack<T>{
    //public int top = -1;
    public ArrayDeque<T> array;
    int top = -1;

    public ArrayStack(){
        this.array = new ArrayDeque();
    }
    
    //add element to the top of the stack
    public void push(T x) {
        array.addLast(x);
    }
    
    // Removes the element on the top of the stack and returns it.
    public T pop() {
        T ans = array.getLast();
        array.removeLast();
        return ans;
    }
    
    //Returns the element on the top of the stack.
    public T top() {
        return array.getLast();
    }

    //Returns true if the stack is empty, false otherwise.
    public boolean empty() {
        return array.isEmpty();
    }

    //return the top element of the stack
    public T peek() {
        return array.getLast();
    }

    public static void main(String[] args) {
        ArrayStack a = new ArrayStack();
        a.push(1);
        a.push(2);
        a.push(3);
        a.pop();
        a.pop();
        System.out.println(a.top());

    }
}