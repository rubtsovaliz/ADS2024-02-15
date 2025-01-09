package by.it.group310902.rubtsova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Класс MyLinkedHashSet реализует интерфейс Set и представляет собой связанное множество,
// которое хранит уникальные элементы в порядке их добавления
public class MyLinkedHashSet<E> implements Set<E> {

    // Вложенный класс Node для хранения элемента и ссылок на следующий и предыдущий узлы
    class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev, follow;

        Node(E e) {
            data = e;
        }
    }
    static final int START_SIZE = 20; // Начальный размер хеш-таблицы
    int size = 0;
    Node<E>[] items; // Массив узлов для хранения элементов

    Node<E> head, tail; // Головной и хвостовой узлы для связанного списка

    // Конструктор по умолчанию, инициализирует хеш-таблицу с начальным размером
    MyLinkedHashSet() {
        this(START_SIZE);
    }

    // Конструктор с указанием начального размера хеш-таблицы
    MyLinkedHashSet(int size) {
        items = new Node[size];
    }

    // Метод для получения строкового представления множества
    @Override
    public String toString() {
        StringBuilder line = new StringBuilder("[");
        Node<E> curr = head; // Начинаем с головного узла
        while (curr != null) { // Проходим по связанному списку
            line.append(curr.data); // Добавляем данные узла в строку
            if (curr.follow != null)
                line.append(", ");
            curr = curr.follow; // Переходим к следующему узлу
        }
        line.append("]");
        return line.toString();
    }

    // Метод для получения текущего размера множества
    @Override
    public int size() {
        return size;
    }

    // Метод для проверки, пусто ли множество
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Метод для проверки, содержится ли элемент в множестве
    @Override
    public boolean contains(Object o) {
        for (Node<E> item : items) { // Перебираем все элементы хеш-таблицы
            Node<E> curr = item;
            while (curr != null) {
                if (o.equals(curr.data)) { // Если нашли элемент
                    return true;
                }
                curr = curr.next; // Переходим к следующему узлу
            }
        }
        return false;
    }

    // Метод для получения итератора
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    // Метод для получения индекса в хеш-таблице на основе хеш-кода
    int getHash(Object o) {
        return (o.hashCode() & 0x7FFFFFFF) % items.length;
    }

    // Метод для добавления узла в связанный список
    void addNode(Node<E> newNode) {
        if (head == null) // Если список пуст
            head = newNode; // Устанавливаем новый узел как голову
        else {
            tail.follow = newNode; // Устанавливаем следующий узел для текущего хвоста
            newNode.prev = tail; // Устанавливаем предыдущий узел для нового узла
        }
        tail = newNode;
    }

    // Метод для удаления узла из связанного списка
    void removeNode(Node<E> newNode) {
        if (newNode.follow != null) { // Если есть следующий узел
            newNode.follow.prev = newNode.prev; // Обновляем ссылку на предыдущий узел
        } else {
            tail = newNode.prev; // Если это последний узел, обновляем хвост
        }
        if (newNode.prev != null) { // Если есть предыдущий узел
            newNode.prev.follow = newNode.follow; // Обновляем ссылку на следующий узел
        } else {
            head = newNode.follow; // Если это первый узел, обновляем голову
        }
    }

    // Метод для изменения размера хеш-таблицы
    void resize() {
        Node<E>[] newItems = new Node[items.length * 2]; //новый массив большего размера
        for (Node<E> curr : items) { // Перебираем старые элементы
            while (curr != null) { // Перемещаем узлы в новый массив
                Node<E> next = curr.next; // Сохраняем ссылку на следующий узел
                int newInd = (curr.data.hashCode() & 0x7FFFFFFF) % newItems.length; // Получаем новый индекс
                curr.next = newItems[newInd]; // Устанавливаем следующий узел
                newItems[newInd] = curr; // Обновляем массив
                curr = next; // Переходим к следующему узлу
            }
        }
        items = newItems;
    }

    // Метод для удаления элемента из множества
    @Override
    public boolean remove(Object o) {
        int ind = getHash(o); // Получаем индекс для удаления
        Node<E> curr = items[ind]; // Получаем первый узел
        Node<E> prev = null; // Предыдущий узел для обновления ссылки
        while (curr != null) { // Ищем элемент для удаления
            if (o.equals(curr.data)) {
                if (prev == null) {
                    items[ind] = curr.next; // Если это первый элемент, обновляем массив
                } else {
                    prev.next = curr.next; // Удаляем узел из списка
                }
                size--;
                removeNode(curr); // Удаляем узел из связанного списка
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    // Метод для очистки множества
    @Override
    public void clear() {
        for (int i = 0; i < items.length; i++) { // Устанавливаем все элементы в null
            items[i] = null;
        }
        size = 0;
        head = null;
        tail = null;
    }

    // Метод для добавления элемента в множество
    @Override
    public boolean add(Object o) {
        Node<E> newNode = new Node<E>((E) o); // Создаем новый узел
        int ind = getHash(o); // Получаем индекс для нового элемента
        Node<E> curr = items[ind]; // Получаем первый узел
        while (curr != null) { // Проверяем на наличие дубликатов
            if (curr.data.equals(o)) {
                return false; // Если элемент уже существует, возвращаем false
            }
            curr = curr.next;
        }
        newNode.next = items[ind]; // Ставим новый узел в начало списка
        items[ind] = newNode; // Обновляем массив
        addNode(newNode); // Добавляем узел в связанный список
        if (++size > items.length * 0.7) // Проверяем, нужно ли увеличивать размер хеш-таблицы
            resize(); // Увеличиваем хеш-таблицу
        return true;
    }

    // Метод для добавления всех элементов из коллекции
    @Override
    public boolean addAll(Collection c) {
        boolean isModified = false; // Флаг для отслеживания изменений
        for (Object item : c) { // Перебираем все элементы коллекции
            if (add(item)) { // Если добавление было успешным
                isModified = true; // Устанавливаем флаг
            }
        }
        return isModified;
    }

    // Метод для удаления всех элементов из коллекции
    @Override
    public boolean removeAll(Collection c) {
        boolean isModified = false; // Флаг для отслеживания изменений
        for (Object item : c) { // Перебираем все элементы коллекции
            if (remove(item)) { // Если удаление было успешным
                isModified = true; // Устанавливаем флаг
            }
        }
        return isModified;
    }

    // Метод для сохранения только тех элементов, которые содержатся в коллекции
    @Override
    public boolean retainAll(Collection c) {
        if (c.isEmpty()) { // Если коллекция пуста
            clear(); // Очищаем множество
            return true;
        }
        boolean isModified = false;
        Node<E> curr = head; // Начинаем с головного узла
        while (curr != null) { // Проходим по  списку
            Node<E> next = curr.follow; // Сохраняем ссылку на следующий узел
            if (!c.contains(curr.data)) { // Если элемент не содержится в коллекции
                remove(curr.data);
                isModified = true;
            }
            curr = next; // Переходим к следующему узлу
        }
        return isModified;
    }

    // Метод для проверки, содержатся ли все элементы из коллекции в множестве
    @Override
    public boolean containsAll(Collection c) {
        for (Object item : c) { // Перебираем все элементы коллекции
            if (!contains(item)) // Если хотя бы один элемент не найден
                return false;
        }
        return true;
    }

    // Метод для преобразования множества в массив
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    // Метод для преобразования множества в массив указанного типа
    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }
}