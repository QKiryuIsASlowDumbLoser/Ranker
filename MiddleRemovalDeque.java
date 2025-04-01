import java.util.*;

public class MiddleRemovalDeque<E>{

    private static class Node<E>{
        private E value;
        private Node<E> next;
        private Node<E> prev;
        public Node(E value){
            this.value = value;
        }

        public void setNext(Node<E> node){
            this.next= node;
        }
        
        public void setPrev(Node<E> node){
            this.prev = node;
        }

        public E getValue(){
            return this.value;
        }

        public Node<E> getNext(){
            return this.next;
        }

        public Node<E> getPrev(){
            return this.prev;
        }
    }

    Node<E> front;
    Node<E> back;
    Map<Object, LinkedList<Node<E>>> nodePointers;
    private int size;

    public MiddleRemovalDeque(){
        front= null;
        back= null;
        nodePointers= new HashMap<>();
        size= 0;
    }

    public void addFront(E value){
        Node<E> newHead= new Node<>(value);
        if(front == null)
            this.front= this.back= newHead;
        else{
            newHead.setNext(this.front);
            this.front.setPrev(newHead);
            this.front = newHead;
        }
        if(!nodePointers.containsKey(value)){
            nodePointers.put(value, new LinkedList<>());
        }
        nodePointers.get(value).add(newHead);
        this.size++;
    }

    public void add(E value){
        addBack(value);
    }

    public void addBack(E value){
        Node<E> newBack= new Node<>(value);
        if(this.back == null)
            this.front= this.back= newBack;
        else{
            newBack.setPrev(this.back);
            this.back.setNext(newBack);
            this.back = newBack;
        }
        if(!nodePointers.containsKey(value)){
            nodePointers.put(value, new LinkedList<>());
        }
        nodePointers.get(value).add(newBack);
        this.size++;
    }

    public E removeFront(){
        if(this.front == null)
            throw new IllegalStateException("Tried to remove Element when List is Empty");
        E value = this.front.getValue();
        if(this.front == this.back)
            this.front= this.back = null;
        this.front= this.front.getNext();
        this.size--;
        removeKeyifEmpty(value);
        return value;
    }

    public void removeKeyifEmpty(E value){
        if(nodePointers.get(value).size() == 0)
            nodePointers.remove(value);
    }
    public E removeBack(){
        if(this.back == null)
            throw new IllegalStateException("Tried to remove Element when List is Empty");
        E value = this.back.getValue();
        if(this.front == this.back)
            this.front= this.back = null;
        this.back= this.back.getPrev();
        this.size--;
        removeKeyifEmpty(value);
        return value;
    }

    public E removeFirst(E value){
        if(!nodePointers.containsKey(value))
            throw new IllegalStateException("Value does not exist");
        Node<E> node= nodePointers.get(value).removeFirst();
        if(this.front == this.back)
            this.front= this.back = null;
        if(node.getNext() != null)
            node.getNext().setPrev(node.getPrev());
        if(node.getPrev() != null)
            node.getPrev().setNext(node.getNext());
        this.size--;
        removeKeyifEmpty(value);
        return node.getValue();
    }


    public E removeLast(E value){
        if(!nodePointers.containsKey(value))
            throw new IllegalStateException("Value does not exist");
        Node<E> node= nodePointers.get(value).removeLast();
        if(this.front == this.back)
            this.front= this.back = null;
        if(node.getNext() != null)
            node.getNext().setPrev(node.getPrev());
        if(node.getPrev() != null)
            node.getPrev().setNext(node.getNext());
        this.size--;
        removeKeyifEmpty(value);
        return node.getValue();
    }

    public int size(){
        return this.size;
    }

    public boolean contains(E value){
        return this.nodePointers.containsKey(value);
    }

    public Iterator<E> iterator(){
        return new ascendingIterator();
    }

    public Iterator<E> descendingIterator(){
        return new descendingIterator();
    } 


    private class ascendingIterator implements Iterator<E> {
        private Node<E> current= front;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next(){
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<E> temp = current;
            current = current.getNext();
            return temp.getValue(); // Return current node and move to the next one
        }

    }

    private class descendingIterator implements Iterator<E> {
        private Node<E> current= back;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next(){
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<E> temp = current;
            current = current.getPrev();
            return temp.getValue(); // Return current node and move to the next one
        }
    }

}
