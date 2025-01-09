package by.it.group310902.rubtsova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Класс MyHashSet реализует интерфейс Set и представляет собой хеш-таблицу для хранения уникальных элементов
public class MyHashSet<E> implements Set<E> {

    //класс Node для хранения элемента и ссылки на следующий узел
    class Node<E> {
        E data; // Данные узла
        Node<E> next; // Ссылка на следующий узел

        Node(E data) {
            this.data = data; // Инициализация данных
        }
    }
    static final int defaultSize = 32; // Начальный размер хеш-таблицы
    Node<E>[] items; // Массив узлов для хранения элементов
    int count; // Счетчик количества элементов в множестве

    // Конструктор по умолчанию, инициализирует хеш-таблицу с начальным размером
    public MyHashSet() {
        this(defaultSize);
    }

    // Конструктор с указанием начального размера хеш-таблицы
    public MyHashSet(int capacity) {
        items = new Node[capacity]; // Инициализация массива узлов
    }

    // Метод для получения индекса в хеш-таблице на основе хеш-кода
    int GetHash(Object value) {
        return (value.hashCode() & 0x7FFFFFFF) % items.length; // Возвращает индекс для элемента
    }

    // Метод для получения строкового представления множества
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true; // Флаг для управления добавлением запятой
        for (int i = 0; i < items.length; i++) { // Проходим по всем индексам хеш-таблицы
            Node<E> current = items[i]; // Начинаем с текущего узла
            while (current != null) { // Пока есть узлы
                if (!first) {
                    sb.append(", "); // Добавляем запятую перед следующим элементом
                }
                sb.append(current.data); // Добавляем данные узла в строку
                first = false; // Устанавливаем флаг в false после первого элемента
                current = current.next; // Переходим к следующему узлу
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Метод для получения текущего размера множества
    @Override
    public int size() {
        return count; // количество элементов
    }

    // Метод для проверки, пусто ли множество
    @Override
    public boolean isEmpty() {
        return count == 0; // Возвращаем true, если счетчик равен 0
    }

    // Метод для проверки, содержится ли элемент в множестве
    @Override
    public boolean contains(Object o) {
        Node<E> current = items[GetHash(o)]; // Получаем узел по хешу
        while (current != null) { // Ищем элемент
            if (current.data.equals(o)) { // Если нашли
                return true;
            }
            current = current.next; // Переходим к следующему узлу
        }
        return false;
    }

    // Метод для добавления элемента в множество
    @Override
    public boolean add(E e) {
        int index = GetHash(e); // Получаем индекс для нового элемента
        Node<E> current = items[index]; // Получаем первый узел в списке
        while (current != null) { // Проверяем на наличие дубликатов
            if (current.data.equals(e)) {
                return false; // Если элемент уже существует
            }
            current = current.next; // Переходим к следующему узлу
        }
        // Если элемент уникален, добавляем его
        Node<E> newNode = new Node<>(e); // Создаем новый узел
        newNode.next = items[index]; // Ставим новый узел в начало списка
        items[index] = newNode; // Обновляем массив
        count++; // Увеличиваем счетчик

        // Проверяем, нужно ли увеличивать размер хеш-таблицы
        if (count > items.length * 0.75) { // Если превышаем 75% заполненности
            resize(); // Увеличиваем хеш-таблицу
        }
        return true;
    }

    // Метод для изменения размера хеш-таблицы
    void resize() {
        Node<E>[] newItems = new Node[items.length * 2]; // Создаем новый массив большего размера
        for (int i = 0; i < items.length; i++) { // Перебираем старые элементы
            Node<E> current = items[i];
            while (current != null) { // Перемещаем узлы в новый массив
                Node<E> next = current.next; // Сохраняем ссылку на следующий узел
                int newIndex = current.data.hashCode() & 0x7FFFFFFF % newItems.length; // Получаем новый индекс
                current.next = newItems[newIndex]; // Устанавливаем следующий узел
                newItems[newIndex] = current; // Обновляем массив
                current = next; // Переходим к следующему узлу
            }
        }
        items = newItems; // Обновляем ссылку на массив
    }

    // Метод для удаления элемента из множества
    @Override
    public boolean remove(Object o) {
        int index = GetHash(o); // Получаем индекс для удаления
        Node<E> current = items[index]; // Получаем первый узел
        Node<E> previous = null; // Предыдущий узел для обновления ссылки
        while (current != null) { // Ищем элемент для удаления
            if (current.data.equals(o)) { // Если нашли элемент
                if (previous == null) {
                    items[index] = current.next; // Если это первый элемент, обновляем массив
                } else {
                    previous.next = current.next; // Удаляем узел из списка
                }
                count--;
                return true;
            }
            previous = current; // Обновляем предыдущий узел
            current = current.next; // Переходим к следующему узлу
        }
        return false;
    }
    @Override
    public void clear() {
        for (int i = 0; i < items.length; i++)
            items[i] = null;
        count = 0;
    }

    ////////////////////////////////////////////////////////////////////////
    // Методы, которые можно реализовать в будущем, оставлены не реализованными

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }
}