package by.it.group310902.rubtsova.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

// Класс MyAvlMap реализует интерфейс Map для хранения пар "ключ-значение" с использованием AVL-дерева
public class MyAvlMap implements Map<Integer, String> {

    // Вложенный класс Node представляет узел дерева
    private class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;
        Node(Integer k, String v) {
            key = k;
            value = v;
            left = null;
            right = null;
            height = 0;         }
    }

    // Метод для получения высоты узла
    private int getHeight(Node node) {
        if (node == null) {
            return 0; // Если узел пуст, высота 0
        }
        return 1 + Math.max(getHeight(node.left), getHeight(node.right)); // Высота узла = 1 + высота максимального поддерева
    }

    // Метод для вычисления разности высот левого и правого поддеревьев
    private int solveDif(Node node) {
        return getHeight(node.left) - getHeight(node.right);
    }

    // Метод для получения высоты узла
    private int takeHeight(Node node) {
        return (node == null) ? 0 : node.height;
    }

    // Правый поворот вокруг узла
    private Node rotateRight(Node node) {
        Node left = node.left; // Левый узел
        Node swapNode = node.left.right; // Правый узел левого поддерева
        left.right = node; // Поворачиваем узел вправо
        node.left = swapNode; // Обновляем левое поддерево
        // Обновляем высоту узлов
        node.height = Math.max(takeHeight(node.left), takeHeight(node.right)) + 1;
        left.height = Math.max(takeHeight(left.left), takeHeight(left.right)) + 1;
        return left;
    }

    // Левый поворот вокруг узла
    private Node rotateLeft(Node node) {
        Node right = node.right; // Правый узел
        Node NodeSwap = node.right.left; // Левый узел правого поддерева
        right.left = node; // Поворачиваем узел влево
        node.right = NodeSwap; // Обновляем правое поддерево
        // Обновляем высоту узлов
        node.height = Math.max(takeHeight(node.left), takeHeight(node.right)) + 1;
        right.height = Math.max(takeHeight(right.left), takeHeight(right.right)) + 1;
        return right;
    }

    // Двойной поворот: правый, затем левый
    private Node rightNLeft(Node node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    // Двойной поворот: левый, затем правый
    private Node leftRight(Node node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    // Метод для добавления узла в дерево
    private Node add(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value); // Если узел пуст, создаем новый
        }
        if (key > node.key) {
            node.right = add(node.right, key, value); // Рекурсивно добавляем в правое поддерево
        } else if (key < node.key) {
            node.left = add(node.left, key, value); // Рекурсивно добавляем в левое поддерево
        } else {
            node.value = value; // Если ключ существует, обновляем значение
            return node; // Возвращаем текущий узел
        }

        // Проверка на балансировку дерева
        int diff = solveDif(node);

        // Левый случай
        if (diff > 1) {
            if (key < node.left.key) {
                return rotateRight(node); // Правый поворот
            } else {
                return leftRight(node); // Двойной поворот
            }
        }
        // Правый случай
        if (diff < -1) {
            if (key < node.right.key) {
                return rightNLeft(node); // Двойной поворот
            } else {
                return rotateLeft(node); // Левый поворот
            }
        }
        return node;
    }

    // Метод для удаления узла из дерева
    private Node rmNode(Node node, Integer key) {
        if (node == null) {
            return null; // Если узел пуст, ничего не удаляем
        }
        // Рекурсивный поиск узла для удаления
        if (key < node.key) {
            node.left = rmNode(node.left, key);
        } else if (key > node.key) {
            node.right = rmNode(node.right, key);
        } else { // Если нашли узел
            // Случай, когда узел - лист
            if (node.right == null && node.left == null) {
                return null; // Удаляем узел
            }
            // Случай, когда есть только правый потомок
            if (node.left == null) {
                return node.right;
            }
            // Случай, когда есть только левый потомок
            if (node.right == null) {
                return node.left;
            }
            // Если у узла есть оба поддерева, находим максимальный узел в левом поддереве
            Node temp = node.left;
            while (temp.right != null) {
                temp = temp.right; // Находим максимальный узел
            }
            Integer k = temp.key; // Сохраняем ключ и значение
            String s = temp.value;
            remove(temp.key); // Удаляем узел
            node.key = k; // Обновляем ключ узла
            node.value = s; // Обновляем значение узла
        }

        // Проверка и балансировка дерева после удаления
        int diff = solveDif(node);

        if (diff > 1) {
            if (key < node.left.key) {
                return rotateRight(node); // Правый поворот
            } else {
                return leftRight(node); // Двойной поворот
            }
        }
        if (diff < -1) {
            if (key < node.right.key) {
                return rotateLeft(node); // Левый поворот
            } else {
                return rightNLeft(node); // Двойной поворот
            }
        }
        return node; // Возвращаем текущий узел
    }

    private Node root = null; // Корень дерева
    private int size = 0; // Размер карты

    // Метод для получения строкового представления карты
    private String elemToString(Node node) {
        if (node == null) {
            return ""; // Если узел пуст, возвращаем пустую строку
        }
        // Рекурсивно собираем строку в формате "ключ=значение"
        return elemToString(node.left) + node.key + "=" + node.value + ", " + elemToString(node.right);
    }

    // Переопределение метода toString для отображения карты
    @Override
    public String toString() {
        String elems = elemToString(root);
        int l = elems.length();
        String result = "";
        if (elems.length() != 0) {
            result = elems.substring(0, l - 2); // Убираем лишнюю запятую
        }
        return "{" + result + "}"; // Возвращаем строковое представление
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public boolean containsKey(Object key) {
        Node temp = root;
        // Перебираем узлы для поиска ключа
        while (temp != null) {
            if (temp.key.equals(key)) {
                return true; // Если нашли ключ, возвращаем true
            }
            if ((Integer) key > temp.key) {
                temp = temp.right;
            } else {
                temp = temp.left;
            }
        }
        return false; // Если не нашли ключ, возвращаем false
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public String get(Object key) {
        Node temp = root;
        // Перебираем узлы для поиска значения по ключу
        while (temp != null) {
            if (temp.key.equals(key)) {
                return temp.value; // Если нашли, возвращаем значение
            }
            if ((Integer) key > temp.key) {
                temp = temp.right;
            } else {
                temp = temp.left;
            }
        }
        return null; // Если не нашли, возвращаем null
    }

    @Override
    public String put(Integer key, String value) {
        String Old = get(key); // Получаем старое значение
        root = add(root, key, value); // Добавляем новый ключ-значение
        size += (Old == null) ? 1 : 0; // Увеличиваем размер, если ключ новый
        return Old; // Возвращаем старое значение
    }

    @Override
    public String remove(Object key) {
        String removed = get(key); // Получаем значение для удаления
        if (removed != null) {
            root = rmNode(root, (Integer) key); // Удаляем узел
            size--; // Уменьшаем размер
        }
        return removed; // Возвращаем удаленное значение
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }

    @Override
    public void clear() {
        root = null; // Очищаем дерево
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}