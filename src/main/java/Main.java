public class Main {

    public static void main(String[] args) {

        MyArrayList<String> linkedList = new MyArrayList<>();
        linkedList.add("a");
        linkedList.add(0, "a");
        linkedList.add(null);
        linkedList.add("d");
        linkedList.add(1, "b");
        linkedList.remove("d");
        linkedList.remove(3);
        linkedList.set(2, "c");
        for (int i = 0; i < linkedList.size(); i++) {
            System.out.println("array list at " + i + ": " + linkedList.get(i));
        }
    }
}
