package by.it.group310902.rubtsova.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Класс MyLinkedList реализует интерфейс Deque и представляет собой двусвязный список
public class MyLinkedList<E> implements Deque<E> {
    private Node<E> head; // Указатель на первый узел списка
    private Node<E> tail; // Указатель на последний узел списка
    private int size;
    // Внутренний класс для представления узла списка
    private static class Node<E> {
        E data; // Данные узла
        Node<E> prev; // Указатель на предыдущий узел
        Node<E> next; // Указатель на следующий узел

        // Конструктор узла
        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    // Метод для получения строкового представления списка
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head; // Начинаем с первого узла
        while (current != null) {
            sb.append(current.data); // Добавляем данные узла
            if (current.next != null) {
                sb.append(", "); // Добавляем запятую, если это не последний элемент
            }
            current = current.next; // Переходим к следующему узлу
        }
        sb.append("]");
        return sb.toString();
    }

    // Метод для добавления элемента в конец списка
    @Override
    public boolean add(E element) {
        addLast(element);
        return true; //
    }

    // Метод для удаления элемента по индексу
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) { // Если удаляем первый элемент
            return removeFirst();
        } else if (index == size - 1) { // Если удаляем последний элемент
            return removeLast();
        } else { // Если удаляем элемент из середины
            Node<E> current = getNode(index); // Получаем узел по индексу
            current.prev.next = current.next; // Связываем предыдущий узел с следующим
            current.next.prev = current.prev; // Связываем следующий узел с предыдущим
            size--;
            return current.data; // Возвращаем данные удаленного узла
        }
    }

    // Метод для удаления указанного элемента
    @Override
    public boolean remove(Object o) {
        Node<E> current = head; // Начинаем с первого узла
        while (current != null) {
            if (current.data.equals(o)) { // Проверяем, совпадают ли данные
                if (current == head) { // Если это первый узел
                    removeFirst();
                } else if (current == tail) { // Если это последний узел
                    removeLast();
                } else { // Если это узел в середине
                    current.prev.next = current.next; // Связываем соседние узлы
                    current.next.prev = current.prev;
                    size--;
                }
                return true;
            }
            current = current.next; // Переходим к следующему узлу
        }
        return false; // Если элемент не найден
    }

    @Override
    public int size() {
        return size;
    }

    // Метод для добавления элемента в начало списка
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element, null, head); // Создаем новый узел
        if (head == null) { // Если список пуст
            tail = newNode; // Новый узел становится и головой, и хвостом
        } else {
            head.prev = newNode; // Устанавливаем связь с новым узлом
        }
        head = newNode; // Новый узел становится головой списка
        size++;
    }

    // Метод для добавления элемента в конец списка
    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, tail, null); // Создаем новый узел
        if (tail == null) { // Если список пуст
            head = newNode; // Новый узел становится и головой, и хвостом
        } else {
            tail.next = newNode; // Устанавливаем связь с новым узлом
        }
        tail = newNode; // Новый узел становится хвостом списка
        size++;
    }

    // Метод для получения первого элемента списка без его удаления
    @Override
    public E element() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.data; // Возвращаем данные первого узла
    }

    // Метод для получения первого элемента без удаления
    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.data; // Возвращаем данные первого узла
    }

    // Метод для получения последнего элемента без удаления
    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.data; // Возвращаем данные последнего узла
    }

    // Метод для удаления и возвращения первого элемента
    @Override
    public E poll() {
        if (head == null) {
            return null; //null, если список пуст
        }
        return removeFirst(); // Удаляем и возвращаем первый элемент
    }

    // Метод для удаления и возвращения первого элемента
    @Override
    public E pollFirst() {
        if (head == null) {
            return null; // null, если список пуст
        }
        return removeFirst(); // Удаляем и возвращаем первый элемент
    }

    // Метод для удаления и возвращения последнего элемента
    @Override
    public E pollLast() {
        if (tail == null) {
            return null; // null, если список пуст
        }
        return removeLast(); // Удаляем и возвращаем последний элемент
    }

    // Метод для удаления первого элемента
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        E data = head.data; // Сохраняем данные первого узла
        head = head.next; // Переходим к следующему узлу
        if (head == null) { // Если список стал пустым
            tail = null; // Устанавливаем tail в null
        } else {
            head.prev = null; // Убираем связь с предыдущим узлом
        }
        size--;
        return data; // Возвращаем данные удаленного узла
    }

    // Метод для удаления последнего элемента
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        E data = tail.data; // Сохраняем данные последнего узла
        tail = tail.prev; // Переходим к предыдущему узлу
        if (tail == null) { // Если список стал пустым
            head = null; // Устанавливаем head в null
        } else {
            tail.next = null; // Убираем связь с следующим узлом
        }
        size--;
        return data; // Возвращаем данные удаленного узла
    }

    // Метод для получения узла по индексу
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current;
        // Поиск узла с использованием двух подходов для оптимизации
        if (index < size / 2) { // Если индекс в первой половине
            current = head; // Начинаем с головы
            for (int i = 0; i < index; i++) {
                current = current.next; // Идем по следующему узлу
            }
        } else { // Если индекс во второй половине
            current = tail; // Начинаем с хвоста
            for (int i = size - 1; i > index; i--) {
                current = current.prev; // Идем по предыдущему узлу
            }
        }
        return current; // Возвращаем найденный узел
    }


    // Остальные методы интерфейса Deque<E> оставлены пустыми для краткости
    // Их можно реализовать аналогичным образом
    @Override
    public void push(E e) {
    }

    @Override
    public E pop() {
        // Метод для удаления и возвращения элемента из стека (из начала списка)
        return null;
    }

    @Override
    public boolean offer(E e) {
        // Метод для добавления элемента в очередь
        return false;
    }

    @Override
    public E remove() {
        // Метод для удаления и возвращения элемента
        return null;
    }

    @Override
    public boolean offerFirst(E e) {
        // Метод для добавления элемента в начало очереди
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        // Метод для добавления элемента в конец очереди
        return false;
    }

    @Override
    public E peek() {
        // Метод для получения элемента без его удаления
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Метод для добавления всех элементов из коллекции
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Метод для удаления всех элементов из коллекции
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Метод для сохранения только тех элементов, которые содержатся в коллекции
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public E peekFirst() {
        // Метод для получения первого элемента без его удаления
        return null;
    }

    @Override
    public E peekLast() {
        // Метод для получения последнего элемента без его удаления
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        // Метод для удаления первого вхождения элемента
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        // Метод для удаления последнего вхождения элемента
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        // Метод для получения итератора для элементов списка
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        // Метод для получения итератора в обратном порядке
        return null;
    }

    @Override
    public boolean contains(Object o) {
        // Метод для проверки, содержится ли элемент в списке
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // Метод для преобразования списка в массив указанного типа
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Метод для проверки, содержатся ли все элементы из коллекции в списке
        return false;
    }
}