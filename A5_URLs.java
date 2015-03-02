/*
 * This class a LinkedList for storing URL strings
 */
public class A5_URLs {

    Node head;
    int  count = 0;

    public void add(String data) {
        Node n = new Node();
        n.setData(data);
        n.setNext(head);
        head = n;
        count++;
    }

    public Object find(Object other) {
        for (Node curr = head; curr != null; curr = curr.getNext()) {
            //compare other to data at curr
            if (other.equals(curr.toString())) return curr.getData();
        }
        return null;
    }
    
    public void print() {
        Node curr = head;
        while (curr != null) {
            System.out.println("" + curr);
            curr = curr.getNext();
        }
    }
    
    public String[] getAll(){
        String[] stArr = new String[count];
        Node curr = head;

        for(int i = 0; i < count; i++) {
            stArr[i] = curr.getData();
            curr = curr.getNext();
        }
        return stArr;
    }

    public int size() {
        return count;
    }

    /**
     *	Node is a class for a linked list of URLs
     */
    private class Node {

        private String data;
        private Node next;

        public Node() {
            data = null;
            next = null;
        }

        public Node(String obj, Node other) {
            data = obj;
            next = other;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node other) {
            next = other;
        }

        public String getData() {
            return data;
        }

        public void setData(String obj) {
            data = obj;
        }
    } // End Node class
}
