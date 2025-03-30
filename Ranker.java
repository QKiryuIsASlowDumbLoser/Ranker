
import java.util.*;



//Note if you want to use this with customClasses make sure the equals method will always return true for 2 objects where compareTo is 0
public class Ranker<E extends Comparable<E>>{
    private static class Node<E>{
        private Deque<E> items;
        private E value;
        private Node<E> left;
        private Node<E> right;
        private Node<E> parent;
        private int leftSubTreeSize;
        private int rightSubTreeSize;
        private int rank;
    
        
        public void addItem(E value){
            this.items.add(value); 
        }
    
        public void setRank(int rank){
            this.rank= rank; 
        }
    
        public Node<E> getRight(){
            return this.right;
        }
        
        public Node<E> getLeft(){
            return this.left;
        }
    
        public Node<E> getParent(){
            return this.parent;
        }
    
        public void setParent(Node<E> newParent){
            this.parent= newParent;
        }
    
        public E getValue(){
            return this.value;
        }
    
        public int getCount(){
            return this.items.size();
        }
    
        public Iterator<E> getItemsIterator(){
            return this.items.iterator();
        }
    
        public Iterator<E> getDescendingItemsIterator(){
            return this.items.descendingIterator();
        }

    
        public Node(E value){
            this.parent = this.left = this.right = null;
            this.value = value;
            this.rightSubTreeSize = this.leftSubTreeSize=  this.rank= 0;
            this.items = new ArrayDeque<>();
            this.items.add(value);
          
        }
    
    
        public int getLeftSubTreeSize(){
            return this.leftSubTreeSize;
        }
    
        public int getRightSubTreeSize(){
            return this.rightSubTreeSize;
        }
    
        public int getRank(){
            return this.rank;
        }
    
        public int getSize(){
            return this.leftSubTreeSize + this.rightSubTreeSize + 1;
        }            
        public void setLeftSubTreeSize(int newSize){
            this.leftSubTreeSize= newSize;
        }
    
        public void setRightSubTreeSize(int newSize){
            this.rightSubTreeSize= newSize;
        }
    
    
        public void setRight(Node<E> newNode){
            if(this.right != null && this.right.getParent() == this)
                this.right.setParent(null);
            this.right= newNode;
            if(newNode != null)
                newNode.setParent(this);
        }
    
        public void setLeft(Node<E> newNode){
            if(this.left != null && this.left.getParent() == this)
                this.left.setParent(null);
            this.left= newNode;
            if(newNode != null)
                newNode.setParent(this);
        }
    
    }
    private static class RotationFlag{
        static final int LEFT  = 1;
        static final int RIGHT = 0; 
        static final int LEFT_RIGHT = 1;
        static final int RIGHT_LEFT = 2;
        static final int RIGHT_RIGHT= 0;
        static final int LEFT_LEFT  = 3;
    }
    
    private HashMap<E, Node<E>>  nodeMap;
    private E max;
    private E min;
    private int size;

    private Node<E> tree;

    public Ranker(){
        this.tree=  null;
        this.min= null;
        this.max = null;
        this.nodeMap = new HashMap<>();
        this.size = 0;

    }

        
    public Ranker(E value){
        this.tree=  null;
        this.min= null;
        this.max = null;
        this.nodeMap = new HashMap<>();
        this.add(value);
        this.size = 0;

    }

    public Ranker(E[] values){
        this.tree=  null;
        this.min= null;
        this.max = null;
        this.nodeMap = new HashMap<>();
        this.size = values.length;

        for(E value: values)
            this.add(value);
        
    }
    
    
    public Ranker(Collection<E> values){
        this.tree=  null;
        this.min= null;
        this.max = null;
        this.nodeMap = new HashMap<>();
        this.size = values.size();
        for(E value: values)
            this.add(value);
    }


    public int getMaxRank(E value){
        return this.getMinRank(value, this.tree) + this.getCount(value) - 1;
    }

    public int getMinRank(E value){
        if(this.nodeMap.containsKey(value))
        return getMinRank(value, this.tree);
        return -1;
    }
    
    private int getMinRank(E value, Node<E> node){
        if(value.compareTo(node.getValue()) > 0){
          return getMinRank(value, node.getRight()) + node.getCount() + node.getRank();
        }
        else if(value.compareTo(node.getValue()) == 0){
          return node.getRank();
        }
        else{
          return getMinRank(value, node.getLeft());
        }
    }

    public int getSize(){
        return this.size;
    }

    public E getMax(){
        return this.max;
    }

    public E getMin(){
        return this.min;
    }
                
    public int getCount(E value){
        return this.nodeMap.containsKey(value)? this.nodeMap.get(value).getCount(): 0; 
    }

    public List<E> nSmallest(int n){
        List<E> items = new ArrayList<>();

        if(n == 1){
            items.add(this.getMin());
            return items;
        }
        insertNSmallestItems(this.tree, 0, items, n);
        return items;
    }

    private void insertNSmallestItems(
        Node<E> node, 
        int minRank, 
        List<E> items,
        int n
    ){  
        if(node != null){
            if(minRank < n)
                insertNSmallestItems(node.getLeft(), minRank, items, n);
            int numElementsInserted = Math.min(n - node.getRank() - minRank, node.getCount());
            Iterator<E> iter= node.getItemsIterator();
            for(int i =0; i < numElementsInserted; i++)
                items.add(iter.next());

            if(minRank + node.getCount() + node.getRank() < n)
                insertNSmallestItems(node.getRight(), node.getRank() +  node.getCount() + minRank, items, n);
        }
    }

    public List<E> nLargest(int n){
        List<E> items = new ArrayList<>();
        if(n == 1){
            items.add(this.getMax());
            return items;
        }
        insertNLargestItems(this.tree, this.size, 0, items, n);
        return items;
    }

    private void insertNLargestItems(
        Node<E> node, 
        int maxRank,
        int minRank, 
        List<E> items,
        int n
    ){  
        if(node != null){

            if(maxRank >= this.size - n)
                insertNLargestItems(node.getRight(), maxRank, node.getRank() +  node.getCount() + minRank, items, n);
            int numElementsInserted = Math.min(n + minRank + node.getCount() + node.getRank()  - this.size, node.getCount());
            Iterator<E> iter= node.getDescendingItemsIterator();
            for(int i =0; i < numElementsInserted; i++)
                items.add(iter.next());
            
            if(node.getRank()  + minRank >=  this.size - n)
                insertNLargestItems(node.getLeft(), node.getRank()  + minRank, minRank, items, n);
        }
    }
    
    public void add(E value){
        this.size++;
        if(this.tree == null){
            tree= new Node<>(value);
            this.max = this.min= value;
            this.nodeMap.put(value, this.tree);
        }
        else{
            boolean inTree = this.nodeMap.containsKey(value);
            this.min = this.min.compareTo(value) > 0? value: this.min;
            this.max = this.max.compareTo(value) < 0? value: this.max;
            add(tree, value, inTree);
        }

    }
    

    private void add(Node<E> node, E value, boolean inTree){
        if(value.compareTo(node.getValue()) < 0){
            if(!inTree)
                node.setLeftSubTreeSize(node.getLeftSubTreeSize() + 1);
            node.setRank(node.getRank() + 1);
            if(node.getLeft() == null){
                node.setLeft(new Node<>(value));
                nodeMap.put(value, node.getLeft());
            }
            else
                add(node.getLeft(), value, inTree);
        } 
        else if(value.compareTo(node.getValue()) > 0){
            if(!inTree)
                node.setRightSubTreeSize(node.getRightSubTreeSize() + 1);
            if(node.getRight() == null){
                node.setRight(new Node<>(value));
                nodeMap.put(value, node.getRight());
            }
            else
                add(node.getRight(), value, inTree);
        }
        else{
            node.addItem(value);
        }
        Node<E> rotatedNode= node.getParent();
        if(rotatedNode != null && !inTree){
            int heaverSide = Math.max(rotatedNode.getLeftSubTreeSize(),  rotatedNode.getRightSubTreeSize());
            int lighterSide= heaverSide ^ rotatedNode.getLeftSubTreeSize() ^ rotatedNode.getRightSubTreeSize();
            if(heaverSide - lighterSide > 1 && lighterSide << 1 < heaverSide){
                int parentFlag= heaverSide == rotatedNode.getLeftSubTreeSize()   ? RotationFlag.LEFT      : RotationFlag.RIGHT;
                Node<E> check = parentFlag == RotationFlag.LEFT? rotatedNode.getLeft() : rotatedNode.getRight();
                int nodeFlag  = check.getRightSubTreeSize() < check.getLeftSubTreeSize()? RotationFlag.LEFT << 1 : RotationFlag.RIGHT;
                int flag = parentFlag ^ nodeFlag;
                switch (flag){
                    case RotationFlag.RIGHT_RIGHT:
                        LRotate(rotatedNode);
                        break;
                    case RotationFlag.LEFT_LEFT:
                        RRotate(rotatedNode);
                        break;
                    case RotationFlag.LEFT_RIGHT:
                        LRotate(rotatedNode.getLeft());
                        RRotate(rotatedNode);
                        break;
                    case RotationFlag.RIGHT_LEFT:
                        RRotate(rotatedNode.getRight()); 
                        LRotate(rotatedNode);
                        break;
                }
            }
        }
    }

    private void LRotate(Node<E>  node){
        Node<E> parent = node.getParent();
        Node<E> right    = node.getRight();
        Node<E> rightleft= right.getLeft();
        right.setLeft(node);
        node.setRight(rightleft);
        if(node == this.tree)
            this.tree= right;
        else{
            if(parent.getRight() == node)
                parent.setRight(right);
            else
                parent.setLeft(right);
        }
        node.setRightSubTreeSize(right.getLeftSubTreeSize());
        right.setLeftSubTreeSize(node.getSize());
        right.setRank(right.getRank() + node.getCount() + node.getRank());
    }

    private void RRotate(Node<E>  node){
        Node<E> parent = node.getParent();
        Node<E> left     = node.getLeft();
        Node<E> leftRight= left.getRight();
        left.setRight(node);
        node.setLeft(leftRight);
        if(node == this.tree)
            this.tree= left;
        else{
            if(parent.getRight() == node)
                parent.setRight(left);
            else{
                parent.setLeft(left);
            }
        }
        node.setLeftSubTreeSize(left.getRightSubTreeSize());
        left.setRightSubTreeSize(node.getSize());
        node.setRank(node.getRank() - left.getCount() - left.getRank());
    }
  
}
    
        
        

    
