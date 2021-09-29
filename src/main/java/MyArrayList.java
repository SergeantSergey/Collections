import java.util.*;
import java.util.function.Consumer;

public class MyArrayList<E> extends AbstractList<E> implements Cloneable {

    private static final Object[] EMPTY_ELEMENTDATA = {};
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    private static final int DEFAULT_CAPACITY = 10;
    transient Object[] elementData;
    private int size;

    // конструктор с параметром для установки емкости MyArrayList
    public MyArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initialCapacity);
        }
    }

    // пустой конструктор c пустым ArrayList
    public MyArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    public MyArrayList(Collection<? extends E> c) {
        Object[] a = c.toArray();
        if ((size = a.length) != 0) {
            if (c.getClass() == ArrayList.class) {
                elementData = a;
            } else {
                elementData = Arrays.copyOf(a, size, Object[].class);
            }
        } else {
            // replace with empty array.
            elementData = EMPTY_ELEMENTDATA;
        }
    }

    // возвращает объект из переданного списка по переданному индексу
    static <E> E elementAt(Object[] es, int index) {
        return (E) es[index];
    }

    // размер
    public int size() {
        return size;
    }

    // пустой ли массив
    public boolean isEmpty() {
        return size == 0;
    }

    // содержит ли объект
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // получает индекс объекта
    public int indexOf(Object o) {
        return indexOfRange(o, 0, size);
    }

    // возвращает индекс объекта в определенном промежутке
    int indexOfRange(Object o, int start, int end) {
        Object[] es = elementData;
        if (o == null) {
            for (int i = start; i < end; i++) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i < end; i++) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    // переопределенный метод cloneable для клонирования объекта
    public Object clone() {
        try {
            MyArrayList<?> v = (MyArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    // метод возвращает массив объектов
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    // возвращает объект по индексу
    public E get(int index) {
        Objects.checkIndex(index, size);
        return elementData(index);
    }

    // устанавливает объект по индексу и возвращает старое значение
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    // увеличивает размер на переданную емкость
    private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            int newCapacity = ArraysSupport.newLength(oldCapacity,
                    minCapacity - oldCapacity, /* minimum growth */
                    oldCapacity >> 1           /* preferred growth */);
            return elementData = Arrays.copyOf(elementData, newCapacity);
        } else {
            return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }

    // увеличивает размер на 1
    private Object[] grow() {
        return grow(size + 1);
    }

    // добавляет объект в List и увеличивает размер на 1
    private void add(E e, Object[] elementData, int s) {
        if (s == elementData.length)
            elementData = grow();
        elementData[s] = e;
        size = s + 1;
    }

    // возвращает элемент по index
    E elementData(int index) {
        return (E) elementData[index];
    }

    // добавляет объект в list
    public boolean add(E e) {
        modCount++;
        add(e, elementData, size);
        return true;
    }

    // добавляет объект в list по индексу
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        modCount++;
        final int s;
        Object[] elementData;
        if ((s = size) == (elementData = this.elementData).length)
            elementData = grow();
        System.arraycopy(elementData, index,
                elementData, index + 1,
                s - index);
        elementData[index] = element;
        size = s + 1;
    }

    // удаляеь объект из list по индексу
    public E remove(int index) {
        Objects.checkIndex(index, size);
        final Object[] es = elementData;

        @SuppressWarnings("unchecked") E oldValue = (E) es[index];
        fastRemove(es, index);

        return oldValue;
    }

    // быстрый метод удаления, без проверки границ и возврата значения
    private void fastRemove(Object[] es, int i) {
        modCount++;
        final int newSize;
        if ((newSize = size - 1) > i)
            System.arraycopy(es, i + 1, es, i, newSize - i);
        es[size = newSize] = null;
    }

    // проверка range для добавления бросает IndexOutOfBoundsException
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // создает сообщение для IndexOutOfBoundsException
    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    // добавить список объектов
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        modCount++;
        int numNew = a.length;
        if (numNew == 0)
            return false;
        Object[] elementData;
        final int s;
        if (numNew > (elementData = this.elementData).length - (s = size))
            elementData = grow(s + numNew);
        System.arraycopy(a, 0, elementData, s, numNew);
        size = s + numNew;
        return true;
    }

    // возращает iterator для прохождения по объектам листа
    public ListIterator<E> listIterator(int index) {
        rangeCheckForAdd(index);
        return new ListItr(index);
    }

    // статический класс для вычисления длины массива
    static class ArraysSupport {
        public static final int MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

        public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
            // assert oldLength >= 0
            // assert minGrowth > 0

            int newLength = Math.max(minGrowth, prefGrowth) + oldLength;
            if (newLength - MAX_ARRAY_LENGTH <= 0) {
                return newLength;
            }
            return hugeLength(oldLength, minGrowth);
        }

        private static int hugeLength(int oldLength, int minGrowth) {
            int minLength = oldLength + minGrowth;
            if (minLength < 0) { // overflow
                throw new OutOfMemoryError("Required array length too large");
            }
            if (minLength <= MAX_ARRAY_LENGTH) {
                return MAX_ARRAY_LENGTH;
            }
            return Integer.MAX_VALUE;
        }
    }

    // приватный кастомный класс Iterator
    private class Itr implements Iterator<E> {
        int cursor;       // индекс следующего элемента для возврата
        int lastRet = -1; // индекс последнего возвращенного элемента, если такого нет вернет -1
        int expectedModCount = modCount;

        // prevent creating a synthetic constructor ???
        Itr() {
        }

        // есть ли следующий элемент
        public boolean hasNext() {
            return cursor != size;
        }

        // возвращает следующий элемент
        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = MyArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        // удаляет элемент и ничего не возвращает
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                MyArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        // пробег по каждому элементу
        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int size = MyArrayList.this.size;
            int i = cursor;
            if (i < size) {
                final Object[] es = elementData;
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size && modCount == expectedModCount; i++)
                    action.accept(elementAt(es, i));
                // обновляется один раз, чтобы снизить запись траффика в кучу ???
                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
        }

        // проверяет, не произошло ли модификаций больше, чем ожидалсь
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    // приватный класс который создает кастомный ListIterator
    private class ListItr extends Itr implements ListIterator<E> {

        // конструктор
        ListItr(int index) {
            super();
            cursor = index;
        }

        // есть ли предыдущий
        public boolean hasPrevious() {
            return cursor != 0;
        }

        // возращает следующий индекс
        public int nextIndex() {
            return cursor;
        }

        // возращает предыдущий индекс
        public int previousIndex() {
            return cursor - 1;
        }

        // возращает предыдущий объект
        @SuppressWarnings("unchecked")
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = MyArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        // устанавливает новый объект в конец
        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                MyArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        // добавляет элемент в лист
        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                MyArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}