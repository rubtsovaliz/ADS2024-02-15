package by.it.group310902.rubtsova.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// Класс ListB реализует интерфейс List и представляет собой динамический массив
public class ListB<E> implements List<E> {
    private E[] array;
    private int size;
    public ListB() {
        this.array = (E[]) new Object[10];
        this.size = 0;
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
            sb.append(array[i]); // Добавляем элемент в строку
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString(); // Завершаем строку и возвращаем
    }

    // Метод для увеличения размера массива
    private void growArray() {
        E[] newArray = (E[]) new Object[array.length * 2]; // Создаем новый массив в 2 раза больше
        System.arraycopy(array, 0, newArray, 0, size); // Копируем элементы
        array = newArray; // Заменяем старый на новый
    }

    // Метод для добавления элемента в конец списка
    @Override
    public boolean add(E e) {
        if (size == array.length) { // Если массив заполнен увеличиваем размер массива
            growArray();
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
        E removed = array[index]; // Сохраняем удаляемый элемент
        // Сдвигаем элементы влево, чтобы заполнить пустое место
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        size--;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    // Метод для добавления элемента по индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(); // Исключение для некорректного индекса
        }
        if (size == array.length) { // Если массив заполнен увеличиваем размер массива
            growArray();
        }
        // Сдвигаем элементы вправо, чтобы вставить новый элемент
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        size++; //
    }

    // Метод для удаления элемента по значению
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o); // Находим индекс элемента
        if (index == -1) { // Если элемент не найден
            return false;
        }
        remove(index); // Удаляем элемент
        return true;
    }

    // Метод для замены элемента по индексу
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E old = array[index]; // Сохраняем старый элемент
        array[index] = element; // Заменяем на новый элемент
        return old;
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
        if (o == null) { // Если объект null
            for (int i = 0; i < size; i++) {
                if (array[i] == null) { // Ищем null в массиве
                    return i; //если найден
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(array[i])) { // Сравниваем с элементами массива
                    return i; // Возвращаем индекс, если найден
                }
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
        return array[index];
    }

    // Метод для проверки, содержится ли элемент в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1; // Если индекс не равен -1, элемент найден
    }

    // Метод для поиска последнего индекса элемента
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) { // Если объект null
            for (int i = size - 1; i >= 0; i--) {
                if (array[i] == null) { // Ищем null в массиве
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(array[i])) { // Сравниваем с элементами массива
                    return i; // Возвращаем индекс, если найден
                }
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
                modified = true;
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
                remove(i--); // Удаляем элемент и уменьшаем индекс
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

        ListB<E> subList = new ListB<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(array[i]); // Добавляем элементы в подсписок
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
                return cursor; // индекс следующего элемента
            }

            @Override
            public int previousIndex() {
                return cursor - 1; //индекс предыдущего элемента
            }

            @Override
            public void remove() {
                ListB.this.remove(--cursor); // Удаляем текущий элемент
            }

            @Override
            public void set(E e) {
                ListB.this.set(cursor, e); // Заменяем текущий элемент
            }

            @Override
            public void add(E e) {
                ListB.this.add(cursor++, e); // Добавляем элемент на текущую позицию
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
                return array[cursor++]; // Возвращаем текущий элемент и переходим к следующему
            }

            @Override
            public void remove() {
                ListB.this.remove(--cursor); // Удаляем текущий элемент
            }
        };
    }
}