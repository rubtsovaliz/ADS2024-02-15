package by.it.group310902.rubtsova.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// Класс ListA реализует интерфейс List и представляет собой динамический массив
public class ListA<E> implements List<E> {
    private E[] array;
    private int size;

    public ListA() {
        this.array = (E[]) new Object[10];
        this.size = 0;
    }

    // отображение эл в строку
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Метод для добавления элемента в конец списка
    @Override
    public boolean add(E e) {
        if (size == array.length) { // Если массив заполнен увеличиваем размер массива
            growArray(); //
        }
        array[size++] = e; // Добавляем элемент и увеличиваем размер
        return true;
    }

    // Метод для удаления элемента по индексу
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E removedElement = array[index]; // Сохраняем удаляемый элемент

        for (int i = index; i < size - 1; i++) { // Сдвигаем элементы влево, чтобы заполнить пустое место
            array[i] = array[i + 1];
        }
        size--;
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    //добавление элемента по индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == array.length) { // Если массив заполнен увеличиваем размер массива
            growArray();
        }
        // Сдвигаем элементы вправо, чтобы вставить новый элемент
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        size++;
    }

    // Метод для увеличения размера массива
    private void growArray() {
        E[] newArray = (E[]) new Object[array.length * 2]; // Создаем новый массив в 2 раза больше
        // Копируем старые элементы в новый массив
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray; // Заменяем старый на новый
    }

    // Метод для удаления элемента по значению
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(o)) { //Если элемент найден
                remove(i); //Удаляем
                return true;
            }
        }
        return false; // Если элемент не найден
    }

    // Метод для замены элемента по индексу
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E oldElement = array[index]; // Сохраняем старый
        array[index] = element; // Заменяем на новый
        return oldElement; // Возвращаем старый
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
    }

    // Метод для поиска индекса элемента
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? array[i] == null : o.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    // Метод для получения элемента по индексу
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return array[index]; // Возвращаем элемент
    }

    // Метод для проверки, содержится ли элемент в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0; // Если индекс >= 0, значит элемент найден
    }

    // Метод для поиска последнего индекса элемента
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? array[i] == null : o.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    // Метод для проверки, содержит ли список все элементы из коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) { // Если хотя бы один элемент не найден
                return false;
            }
        }
        return true; // Если все элементы найдены
    }

    // Метод для добавления всех элементов из коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false; // Флаг изменения
        for (E element : c) {
            if (add(element)) { // Если элемент добавлен
                modified = true; // Устанавливаем флаг
            }
        }
        return modified;
    }

    // Метод для добавления всех элементов из коллекции по индексу
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        boolean modified = false; // Флаг изменения
        for (E element : c) {
            add(index++, element); // Добавляем элемент по индексу
            modified = true;
        }
        return modified;
    }

    // Метод для удаления всех элементов из коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения
        for (Object o : c) {
            if (remove(o)) { // Если элемент удален
                modified = true;
            }
        }
        return modified;
    }

    // Метод для сохранения только тех элементов, которые содержатся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения
        for (int i = 0; i < size; i++) {
            if (!c.contains(array[i])) { // Если элемент не содержится в коллекции
                remove(i--); // Удаляем эл и уменьшаем индекс
                modified = true;
            }
        }
        return modified;
    }

    // Метод для получения подсписка от fromIndex до toIndex
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        ListA<E> subList = new ListA<>(); //новый список
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(array[i]); // Добавляем элементы
        }
        return subList;
    }

    // Метод для получения итератора, который позволяет проходить по элементам списка
    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        return new ListIterator<E>() {
            private int cursor = index; // Индекс текущего положения итератора

            @Override
            public boolean hasNext() {
                return cursor < size; // Проверка, есть ли следующий элемент
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new IndexOutOfBoundsException(); // Исключение, если нет следующего элемента
                }
                return array[cursor++]; // Возвращаем текущий элемент и переходим к следующему
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0; // Проверка, есть ли предыдущий элемент
            }

            @Override
            public E previous() {
                if (!hasPrevious()) {
                    throw new IndexOutOfBoundsException(); // Исключение, если нет предыдущего элемента
                }
                return array[--cursor]; // Возвращаем предыдущий элемент и переходим назад
            }

            @Override
            public int nextIndex() {
                return cursor; // Возвращаем индекс следующего элемента
            }

            @Override
            public int previousIndex() {
                return cursor - 1; // Возвращаем индекс предыдущего элемента
            }

            @Override
            public void remove() {
                ListA.this.remove(cursor--); // Удаляем текущий элемент
            }

            @Override
            public void set(E e) {
                ListA.this.set(cursor, e); // Заменяем текущий элемент
            }

            @Override
            public void add(E e) {
                ListA.this.add(cursor++, e); // Добавляем элемент на текущую позицию
            }
        };
    }

    // Перегруженный метод для получения итератора без указания индекса
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0); // Возвращаем итератор, начиная с нуля
    }

    // Метод для преобразования списка в массив
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) { // Если длина массива меньше размера списка

            T[] result = (T[]) new Object[size]; // Создаем новый массив
            System.arraycopy(array, 0, result, 0, size); // Копируем элементы
            return result;
        }
        System.arraycopy(array, 0, a, 0, size); // Копируем элементы в переданный массив
        if (a.length > size) {
            a[size] = null; // Устанавливаем null после последнего элемента
        }
        return a; // Возвращаем массив
    }

    // Метод для преобразования списка в массив объектов
    @Override
    public Object[] toArray() {
        return toArray(new Object[size]); // Используем toArray с пустым массивом
    }

    // Метод для получения итератора по элементам списка
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0; // Индекс текущего положения итератора

            @Override
            public boolean hasNext() {
                return cursor < size; // Проверка, есть ли следующий элемент
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new IndexOutOfBoundsException(); // Исключение, если нет следующего элемента
                }
                return array[cursor++]; // Возвращаем текущий элемент и переходим к следующему
            }

            @Override
            public void remove() {
                ListA.this.remove(--cursor); // Удаляем текущий элемент
            }
        };
    }
}