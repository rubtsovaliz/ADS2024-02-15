package by.it.group310902.rubtsova.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// Класс ListC реализует интерфейс List и представляет собой динамический массив
public class ListC<E> implements List<E> {
    private E[] elements;

    private int size;

    public ListC() {
        elements = (E[]) new Object[10];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Переопределение метода toString для удобного отображения списка
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]); // Добавляем элемент в строку
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
        if (size == elements.length) { // Проверяем, заполнен ли массив
            grow(); // Если да, увеличиваем размер массива
        }
        elements[size++] = e; // Добавляем элемент и увеличиваем размер
        return true;
    }

    // Метод для увеличения размера массива
    private void grow() {
        E[] newElements = (E[]) new Object[elements.length * 2]; // Создаем новый массив в 2 раза больше
        System.arraycopy(elements, 0, newElements, 0, elements.length); // Копируем существующие элементы
        elements = newElements; // Заменяем старый массив на новый
    }

    // Метод для удаления элемента по индексу
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E removedElement = elements[index]; // Сохраняем удаляемый элемент
        // Сдвигаем элементы влево, чтобы заполнить пустое место
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
        return removedElement;
    }

    // Метод для получения текущего размера списка
    @Override
    public int size() {
        return size;
    }

    // Метод для добавления элемента по индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == elements.length) { // Если массив заполнен
            grow(); // Увеличиваем размер массива
        }
        // Сдвигаем элементы вправо, чтобы вставить новый элемент
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element; // Вставляем новый элемент
        size++;
    }

    // Метод для удаления элемента по значению
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) { // Если элемент найден
                remove(i); // Удаляем элемент по индексу
                return true;
            }
        }
        return false;
    }

    // Метод для замены элемента по индексу
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E oldElement = elements[index]; // Сохраняем старый элемент
        elements[index] = element; // Заменяем на новый элемент
        return oldElement; // Возвращаем старый элемент
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
            if (o.equals(elements[i])) { // Если элемент найден
                return i; // Возвращаем индекс
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
        return elements[index];
    }

    // Метод для проверки, содержится ли элемент в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1; // Если индекс не равен -1, элемент найден
    }

    // Метод для поиска последнего индекса элемента
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) { // Проходим по элементам в обратном порядке
            if (o.equals(elements[i])) { // Если элемент найден
                return i; // Возвращаем индекс
            }
        }
        return -1;
    }

    // Метод для проверки, содержит ли список все элементы из коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) { // Проходим по всем элементам коллекции
            if (!contains(o)) { // Если хотя бы один элемент не найден
                return false;
            }
        }
        return true; // Если все найдены
    }

    // Метод для добавления всех элементов из коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false; // Флаг изменения
        for (E element : c) { // Проходим по всем элементам коллекции
            add(element); // Добавляем элемент
            modified = true;
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
        for (E element : c) { // Проходим по всем элементам коллекции
            add(index++, element); // Добавляем элемент по индексу
            modified = true;
        }
        return modified;
    }

    // Метод для удаления всех элементов из коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения
        int writeIndex = 0; // Индекс для записи
        for (int i = 0; i < size; i++) { // Проходим по всем элементам
            if (!c.contains(elements[i])) { // Если элемент не содержится в коллекции
                elements[writeIndex++] = elements[i]; // Записываем его на новое место
            } else {
                modified = true;
            }
        }
        size = writeIndex; // Обновляем размер
        return modified;
    }

    // Метод для сохранения только тех элементов, которые содержатся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) { // Если элемент не содержится в коллекции
                remove(i); // Удаляем элемент
                i--; // Уменьшаем индекс, так как элементы сдвинулись
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Метод для получения подсписка от fromIndex до toIndex
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        ListC<E> subList = new ListC<>();
        for (int i = fromIndex; i < toIndex; i++) { // Проходим по указанным индексам
            subList.add(elements[i]); // Добавляем элементы в подсписок
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
                return elements[cursor++]; // Возвращаем текущий элемент и переходим к следующему
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
                return elements[--cursor]; // Возвращаем предыдущий элемент и переходим назад
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
                ListC.this.remove(--cursor); // Удаляем текущий элемент
            }

            @Override
            public void set(E e) {
                ListC.this.set(cursor, e); // Заменяем текущий элемент
            }

            @Override
            public void add(E e) {
                ListC.this.add(cursor++, e); // Добавляем элемент на текущую позицию
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
            T[] newArray = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size); // Создаем новый массив
            a = newArray; // Меняем ссылку на массив
        }
        System.arraycopy(elements, 0, a, 0, size); // Копируем элементы в переданный массив
        if (a.length > size) {
            a[size] = null; // Устанавливаем null после последнего элемента
        }
        return a;
    }

    // Метод для преобразования списка в массив объектов
    @Override
    public Object[] toArray() {
        return toArray(new Object[size]); // Используем toArray с пустым массивом
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
                return elements[cursor++]; // Возвращаем текущий элемент и переходим к следующему
            }

            @Override
            public void remove() {
                ListC.this.remove(--cursor); // Удаляем текущий элемент
            }
        };
    }
}