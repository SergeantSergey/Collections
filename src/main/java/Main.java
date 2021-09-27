import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        LinkedList<Integer> linkedList = new LinkedList<>();

        Integer a = 1;
        Integer b = 2;
        Integer c = 3;

        linkedList.add(a);
        linkedList.add(b);
        linkedList.add(c);

        ArrayList<Integer> arrayList = new ArrayList<>();

        arrayList.add(a);
        arrayList.add(b);
        arrayList.add(c);

        for (int i = 0; i < linkedList.size(); i++) {
            System.out.println(linkedList.get(i));
        }

        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i));
        }
    }
}
