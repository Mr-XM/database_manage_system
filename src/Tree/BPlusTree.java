package Tree;

import Untils.DataTableUntils;
import Untils.DatabaseUntils;

import java.io.File;
import java.util.*;

public class BPlusTree implements B {

    /** 根节点 */
    protected Node root;

    /** 阶数，M值 */
    protected int order;

    /** 叶子节点的链表头*/
    protected Node head;

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public Object get(Comparable key) {
        return root.get(key);
    }

    @Override
    public void remove(Comparable key) {
        root.remove(key, this);

    }

    @Override
    public void insertOrUpdate(Comparable key, Object obj) {
        root.insertOrUpdate(key, obj, this);

    }

    public BPlusTree(int order){
        if (order < 3) {
            System.out.print("order must be greater than 2");
            System.exit(0);
        }
        this.order = order;
        root = new Node(true, true);
        head = root;
    }

    public static void printBPlusTree(BPlusTree tree){
        Queue<Node> queue = new LinkedList<>();
        queue.offer(tree.getRoot());
        int k=0;
        while (!queue.isEmpty()){
            System.out.print(queue.peek()+"  ");
            if(queue.peek().getChildren()!=null){
                List<Node> list=queue.peek().getChildren();
                if(list!=null){
                    for(int i=0;i<list.size();i++){
                        queue.offer(list.get(i));
                    }
                }
            }
            queue.remove();
            if(k<getNodeHigh(queue.peek())){
                k++;
                System.out.println();
            }
        }
        System.out.println();

    }
    public static int getNodeHigh(Node node){
        int i=0;
        if(node!=null){
            while(node.getParent()!=null){
                node=node.getParent();
                i++;
            }
        }
        return i;
    }
}
