package ru.compscicenter.java2017.collections;

import java.util.*;

public class MyClass<E> extends AbstractCollection<E> implements MultiSet<E> {
    private Map<E, Integer> map = new HashMap<>();
    private int size;

    public MyClass() {
        map = new HashMap<>();
    }

    public MyClass(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    public class MyClassIterator implements Iterator<E> {
        private boolean flag = false;
        private final Iterator<HashMap.Entry<E, Integer>> iterator;
        private Map.Entry<E, Integer> currentElement = new HashMap.Entry<E, Integer>() {
            @Override
            public E getKey() {
                return null;
            }

            @Override
            public Integer getValue() {
                return null;
            }

            @Override
            public Integer setValue(Integer value) {
                return null;
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }
        };
        private int count;

        MyClassIterator() {
            iterator = map.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return count > 0 || iterator.hasNext();
        }

        @Override
        public E next() {
            flag = true;
            if (count == 0) {
                currentElement = iterator.next();
                count = currentElement.getValue();
            }
            --count;
            return currentElement.getKey();
        }

        @Override
        public void remove() {
            if (!flag) {
                throw new IllegalStateException("The next method has not yet been called,"
                        + " or the remove method has already been called "
                        + "after the last call to the next method");
            }
            flag = false;
            final E e = currentElement.getKey();
            Integer pastAmount = map.get(e);

            if (pastAmount > 1) {
                map.put(e, --pastAmount);
            } else {
                iterator.remove();
            }
            --size;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return count(o) > 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyClassIterator();
    }

    @Override
    public boolean add(Object o) {
        //return add(o, 1) < count(o);
        add(o, 1);
        return true;
    }

    @Override
    public int add(Object o, int occurrences) {
        if (occurrences < 0) {
            throw new IllegalArgumentException("Occurrences cannot be negative!");
        }

        Integer pastAmount = map.get(o);
        if (pastAmount == null) {
            pastAmount = 0;
        }
        map.put((E) o, pastAmount + occurrences);
        size += occurrences;

        return pastAmount;
    }

    @Override
    public boolean remove(Object e) {
        return remove(e, 1) > count(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean flag = false;

        for (Object item : c) {
            flag |= add(item);
        }

        return flag;
    }

    @Override
    public int remove(Object e, int occurrences) {
        if (occurrences < 0) {
            throw new IllegalArgumentException("Occurrences cannot be negative!");
        }
        Integer pastAmount = map.get(e);
        if (pastAmount == null || pastAmount == 0) {
            return 0;
        }
        if (pastAmount > occurrences) {
            map.put((E) e, pastAmount - occurrences);
            size -= occurrences;
        } else {
            map.remove((E) e);
            size -= pastAmount;
        }
        return pastAmount;
    }

    @Override
    public int count(Object e) {
        return map.getOrDefault(e, 0);
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (Object item : this) {
            array[i++] = item;
        }
        return array;
    }

    @Override
    public void clear() {
        map.clear();
        size = 0;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean flag = false;
        for (E key : map.keySet()) {
            if (!c.contains(key)) {
                size -= count(key);
                flag = true;
            }
        }
        if (flag) {
            map.keySet().retainAll(c);
        }
        return flag;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = false;
        for (Object item : c) {
            if (map.containsKey(item)) {
                size -= map.get(item);
                map.remove(item);
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public Object[] toArray(Object[] a) {
        try {
            if (a.length < size) {
                return (Object[]) Arrays.copyOf(this.toArray(), size, a.getClass());
            }
            System.arraycopy(this.toArray(), 0, a, 0, size);
            if (a.length > size) {
                a[size] = null;
            }
        } catch (ArrayStoreException | NullPointerException e) {
            System.out.println(e.toString());
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return map.keySet().containsAll(c);
    }

    @Override
    public final int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MultiSet)) {
            return false;
        }
        MultiSet temp = (MultiSet) o;
        if (!(containsAll(temp))) {
            return false;
        }
        return !retainAll(temp);
    }
}
