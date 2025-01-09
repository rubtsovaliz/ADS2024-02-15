package by.it.group310902.rubtsova.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Класс MyArrayDeque реализует интерфейс Deque и представляет собой двустороннюю очередь на основе массива
public class MyArrayDeque<E> implements Deque<E> {
    private static final int INITIAL_CAPACITY = 10; //емкость очереди
    private E[] elements;
    private int size; //размер очереди
    private int front; // Индекс первого элемента
    private int rear; // Индекс, следующий за последним элементом

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        this.elements = (E[]) new Object[INITIAL_CAPACITY]; // массив
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    // Переопределение метода toString для удобного отображения очереди
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(front + i) % elements.length]); // Получаем элемент с учетом круговой структуры
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Метод для получения итератора для элементов очереди
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    // Метод для преобразования очереди в массив объектов
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    // Метод для преобразования очереди в массив указанного типа
    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    // Метод для получения итератора в обратном порядке
    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    // Метод для добавления элемента в конец очереди
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // Метод для добавления элемента в конец очереди с учетом ограничений
    @Override
    public boolean offer(E e) {
        return false;
    }

    // Метод для удаления и возвращения элемента из начала очереди
    @Override
    public E remove() {
        return null;
    }

    // Метод для добавления элемента в начало очереди
    public void addFirst(E element) {
        ensureCapacity(); // Проверяем, достаточно ли места
        front = (front - 1 + elements.length) % elements.length; // Уменьшаем front с учетом круговой структуры
        elements[front] = element; // Добавляем элемент в начало
        size++;
    }

    // Метод для добавления элемента в конец очереди
    public void addLast(E element) {
        ensureCapacity(); // Проверяем, достаточно ли места
        elements[rear] = element; // Добавляем элемент в конец
        rear = (rear + 1) % elements.length; // Увеличиваем rear с учетом круговой структуры
        size++;
    }

    // Метод для добавления элемента в начало очереди с учетом ограничений
    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    // Метод для добавления элемента в конец очереди с учетом ограничений
    @Override
    public boolean offerLast(E e) {
        return false;
    }

    // Метод для удаления и возвращения первого элемента очереди
    @Override
    public E removeFirst() {
        return null;
    }

    // Метод для удаления и возвращения последнего элемента очереди
    @Override
    public E removeLast() {
        return null;
    }

    // Метод для получения первого элемента без его удаления
    public E element() {
        return getFirst();
    }

    // Метод для получения первого элемента без его удаления с учетом ограничений
    @Override
    public E peek() {
        return null;
    }

    // Метод для добавления всех элементов из коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    // Метод для удаления всех элементов из коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    // Метод для сохранения только тех элементов, которые содержатся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    // Метод для очистки очереди
    @Override
    public void clear() {
    }

    // Метод для добавления элемента в стек (в начало очереди)
    @Override
    public void push(E e) {
    }

    // Метод для удаления и возвращения элемента из стека (из начала очереди)
    @Override
    public E pop() {
        return null;
    }

    // Метод для удаления элемента по значению
    @Override
    public boolean remove(Object o) {
        return false;
    }

    // Метод для проверки, содержится ли все элементы из коллекции в очереди
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    // Метод для проверки, содержится ли элемент в очереди
    @Override
    public boolean contains(Object o) {
        return false;
    }

    // Метод для получения первого элемента без его удаления
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException(); // Исключение, если очередь пуста
        }
        return elements[front]; // Возвращаем первый элемент
    }

    // Метод для получения последнего элемента без его удаления
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException(); // Исключение, если очередь пуста
        }
        return elements[(rear - 1 + elements.length) % elements.length]; // Возвращаем последний элемент
    }

    // Метод для получения первого элемента без его удаления с учетом ограничений
    @Override
    public E peekFirst() {
        return null;
    }

    // Метод для получения последнего элемента без его удаления с учетом ограничений
    @Override
    public E peekLast() {
        return null;
    }

    // Метод для удаления первого вхождения элемента
    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    // Метод для удаления последнего вхождения элемента
    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    // Метод для удаления и возвращения элемента из начала очереди (с учетом удаления)
    public E poll() {
        return pollFirst();
    }

    // Метод для удаления и возвращения первого элемента очереди
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E result = elements[front]; // Сохраняем первый элемент
        elements[front] = null; // Очистка ссылки
        front = (front + 1) % elements.length; // Увеличиваем front с учетом круговой структуры
        size--;
        return result;
    }

    // Метод для удаления и возвращения последнего элемента очереди
    public E pollLast() {
        if (size == 0) {
            return null; // Возвращаем null, если очередь пуста
        }
        rear = (rear - 1 + elements.length) % elements.length; // Уменьшаем rear с учетом круговой структуры
        E result = elements[rear]; // Сохраняем последний элемент
        elements[rear] = null; // Очистка ссылки
        size--;
        return result;
    }

    // Метод для проверки, достаточно ли места в массиве
    private void ensureCapacity() {
        if (size == elements.length) {
            resize(2 * elements.length); // Если массив заполнен, увеличиваем его размер в 2 раза
        }
    }

    // Метод для изменения размера массива
    private void resize(int newCapacity) {
        E[] newElements = (E[]) new Object[newCapacity]; // новый массив
        for (int i = 0; i < size; i++) { // Копируем элементы
            newElements[i] = elements[(front + i) % elements.length];
        }
        elements = newElements; // Заменяем старый на новый
        front = 0;
        rear = size;
    }
}