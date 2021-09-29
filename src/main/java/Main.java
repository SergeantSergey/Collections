import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        MyArrayList<String> myArrayList = new MyArrayList<>();
        myArrayList.add("a");
        myArrayList.add(0, "a");
        myArrayList.add(null);
        myArrayList.add("d");
        myArrayList.add(1, "b");
        myArrayList.remove("d");
        myArrayList.remove(3);
        myArrayList.set(2, "c");
        for (int i = 0; i < myArrayList.size(); i++) {
            System.out.println("array list at " + i + ": " + myArrayList.get(i));
        }

        System.out.println();

        MyLinkedList<String> myLinkedList = new MyLinkedList<>();
        myLinkedList.add("a");
        myLinkedList.add(0, "a");
        myLinkedList.add(null);
        myLinkedList.add("d");
        myLinkedList.add(1, "b");
        myLinkedList.remove("d");
        myLinkedList.remove(3);
        myLinkedList.set(2, "c");
        for (int i = 0; i < myLinkedList.size(); i++) {
            System.out.println("array list at " + i + ": " + myLinkedList.get(i));
        }
    }
}
