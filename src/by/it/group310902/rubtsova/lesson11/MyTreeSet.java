package by.it.group310902.rubtsova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Класс MyTreeSet реализует интерфейс Set и представляет собой бинарное дерево поиска
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    // Вложенный класс Node для хранения элемента и ссылок на левого и правого потомков
    class Node {
        E data;
        Node left;
        Node right;

        Node(E data) {
            this.data = data;
        }
    }
    Node root;
    int count;

    // Метод для получения строкового представления дерева
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        inOrderTraversal(root, sb); // Проводим обход дерева в симметричном порядке
        return sb.append("]").toString();
    }

    // Метод для симметричного обхода дерева и добавления элементов
    void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return; // Если узел пуст, выходим
        inOrderTraversal(node.left, sb); // Рекурсивный вызов для левого поддерева
        if (sb.length() > 1)
            sb.append(", ");
        sb.append(node.data); // Добавляем данные узла в строку
        inOrderTraversal(node.right, sb); // Рекурсивный вызов для правого поддерева
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    // Рекурсивный метод для проверки наличия элемента в дереве
    boolean contains(Node node, E element) {
        if (node == null) return false; // Если узел пуст, элемент не найден
        int compare = element.compareTo(node.data); // Сравниваем элементы
        if (compare < 0)
            return contains(node.left, element); // Ищем в левом поддереве
        else if (compare > 0)
            return contains(node.right, element); // Ищем в правом поддереве
        else
            return true;
    }

    // Метод для проверки, содержится ли элемент в дереве
    @Override
    public boolean contains(Object o) {
        return contains(root, (E) o); //с корня дерева
    }

    // Рекурсивный метод для вставки нового элемента в дерево
    Node insert(Node node, E element) {
        if (node == null)
            return new Node(element); // Если узел пуст, создаем новый
        int compare = element.compareTo(node.data); // Сравниваем элементы
        if (compare < 0)
            node.left = insert(node.left, element); // Вставляем в левое поддерево
        else if (compare > 0)
            node.right = insert(node.right, element); // Вставляем в правое поддерево
        return node;
    }

    // Метод для добавления элемента в дерево
    @Override
    public boolean add(E e) {
        if (!contains(e)) { // Если элемент не существует
            root = insert(root, e); // Вставляем элемент
            count++;
            return true;
        }
        return false;
    }

    // Метод для нахождения минимального элемента в поддереве
    Node findMin(Node node) {
        while (node.left != null) { // Ищем самый левый узел
            node = node.left;
        }
        return node;
    }

    // Рекурсивный метод для удаления элемента из дерева
    Node delete(Node node, E element) {
        if (node == null) return null;
        int compare = element.compareTo(node.data); // Сравниваем элементы
        if (compare < 0) {
            node.left = delete(node.left, element);
        } else if (compare > 0) {
            node.right = delete(node.right, element);
        } else { // Если нашли элемент для удаления
            if (node.left == null) {
                return node.right; // Если нет левого поддерева, возвращаем правое
            } else if (node.right == null) {
                return node.left; // Если нет правого поддерева, возвращаем левое
            }
            // Если у узла есть оба поддерева, заменяем его данными минимального узла из правого поддерева
            node.data = findMin(node.right).data;
            node.right = delete(node.right, node.data); // Удаляем мин узел
        }
        return node;
    }

    // Метод для удаления элемента из дерева
    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            root = delete(root, (E) o);
            count--;
            return true;
        }
        return false;
    }

    // Проверяет, содержатся ли все элементы из коллекции в дереве
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj))
                return false;
        }
        return true;
    }

    // Добавляет все элементы из коллекции в дерево
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isModified = false;
        for (E element : c) {
            if (add(element)) // Если добавление было успешным
                isModified = true;
        }
        return isModified;
    }

    // Сохраняет только те элементы, которые содержатся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            this.clear();
            return true;
        }
        boolean isModified = false;
        MyTreeSet<E> retainSet = new MyTreeSet<>(); //дерево для хранения оставшихся элементов
        for (Object obj : c) {
            if (contains(obj)) { // Если элемент содержится в дереве
                retainSet.add((E) obj); // Добавляем его в новое дерево
                isModified = true;
            }
        }
        root = retainSet.root;
        count = retainSet.count;

        return isModified;
    }

    // Удаляет все элементы, которые содержатся в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isModified = false;
        for (Object obj : c) {

            if (remove(obj)) // Если удаление было успешным
                isModified = true;
        }
        return isModified;
    }

    // Очищает дерево
    @Override
    public void clear() {
        root = null;
        count = 0;
    }

    //////////////////////////////////////////////////////////////////////////////////////

    // Итератор
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    // Преобразует множество в массив
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    // Преобразует множество в массив указанного типа
    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}