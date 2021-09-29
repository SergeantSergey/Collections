import java.util.*;

public class MyLinkedList<T> implements List<T> {

    // описывает связь с левым и правыи элементом массивом
    // если левый элемент null то это первый элемент массива
    // если правый элемент null, то это последний элемент массива
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    // modeCount считает колличество изменений листе (был удален)

    // размерность массива
    transient int size = 0;

    transient Node<T> first;

    transient Node<T> last;

    // возвращает размерность массива
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        // не нужно реализовывать
        return false;
    }

    // возвращает boolean содержит ли массив данный элемент
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // возращает iterator для прохождения по объектам листа
    @Override
    public Iterator<T> iterator() {
        // не нужно реализовывать
        return null;
    }

    // возвращает массив объектов
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<T> x = first; x != null; x = x.next)
            result[i++] = x.item;
        return result;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        // не нужно реализовывать
        return null;
    }

    // добавляет элемент в массив
    @Override
    public boolean add(T t) {
        linkLast(t);
        return true;
    }

    // добавляет элементы в конец массива
    void linkLast(T e) {
        final Node<T> l = last;
        final Node<T> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    T unlink(Node<T> x) {
        // assert x != null;
        final T element = x.item;
        final Node<T> next = x.next;
        final Node<T> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<T> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<T> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // todo не нашел реализации
        return false;
    }

    // добавляет все эелементы в масив
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    // я так понимаю это расширение для класса Node для поиска его по индексу
    Node<T> node(int index) {

        if (index < (size >> 1)) {
            Node<T> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<T> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    // генерирует сообщение о позиции элемента и размерности листа
    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    // проверяет находиться жлемент в списке или позиции для итератора
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    // проверяет находиться ли элемент в листе по позиции
    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        checkPositionIndex(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;

        Node<T> pred, succ;
        if (index == size) {
            succ = null;
            pred = last;
        } else {
            succ = node(index);
            pred = succ.prev;
        }

        for (Object o : a) {
            @SuppressWarnings("unchecked") T e = (T) o;
            Node<T> newNode = new Node<>(pred, e, null);
            if (pred == null)
                first = newNode;
            else
                pred.next = newNode;
            pred = newNode;
        }

        if (succ == null) {
            last = pred;
        } else {
            pred.next = succ;
            succ.prev = pred;
        }

        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // не нужно реализовывать
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // не нужно реализоывать
        return false;
    }

    // очищает лист
    // проходиться по каждой ноде и удаляет их ссылки на объекты
    @Override
    public void clear() {
        for (Node<T> x = first; x != null; ) {
            Node<T> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }

    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // возврщает элемент по индексу
    @Override
    public T get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    @Override
    public T set(int index, T element) {
        checkElementIndex(index);
        Node<T> x = node(index);
        T oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    void linkBefore(T e, Node<T> succ) {
        // assert succ != null;
        final Node<T> pred = succ.prev;
        final Node<T> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        if (pred == null)
            first = newNode;
        else
            pred.next = newNode;
        size++;
    }

    // изменяет элементесли он находиться в списке и добавляет, если его нет
    @Override
    public void add(int index, T element) {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));
    }

    // удаляет элемент по индексу
    @Override
    public T remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    // возвращает индекс первого подобного элемента
    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<T> x = first; x != null; x = x.next) {
                if (x.item == null)
                    return index;
                index++;
            }
        } else {
            for (Node<T> x = first; x != null; x = x.next) {
                if (o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }

    // возвращает индекс последнего вхождения в этот элемент
    @Override
    public int lastIndexOf(Object o) {
        int index = size;
        if (o == null) {
            for (Node<T> x = last; x != null; x = x.prev) {
                index--;
                if (x.item == null)
                    return index;
            }
        } else {
            for (Node<T> x = last; x != null; x = x.prev) {
                index--;
                if (o.equals(x.item))
                    return index;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        // не нужно реализовывать
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        // не нужно реализовывать
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        // не нужно реализовывать
        return null;
    }
}
