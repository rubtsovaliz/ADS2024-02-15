package by.it.group310902.rubtsova.lesson12;

import java.util.*;

// Класс MySplayMap реализует интерфейс NavigableMap с использованием сплошного дерева
public class MySplayMap implements NavigableMap<Integer, String> {

    Node root; // Корень дерева

    // Вложенный класс Node представляет узел дерева, содержащий ключ, значение и ссылки на дочерние узлы
    class Node {
        Integer key; // Ключ узла
        String value; // Значение узла
        Node left, right, parent; // Ссылки на левого, правого и родительского узлов

        // Конструктор узла
        Node(Integer key, String value) {
            this.key = key; // Инициализация ключа
            this.value = value; // Инициализация значения
        }
    }

    // Метод для получения строкового представления карты
    @Override
    public String toString() {
        if (root == null) // Если дерево пустое, возвращаем пустую карту
            return "{}";
        StringBuilder sb = new StringBuilder().append("{");
        inOrderTraversal(root, sb); // Выполняем симметричный обход дерева
        sb.replace(sb.length() - 2, sb.length(), ""); // Убираем лишнюю запятую
        sb.append("}");
        return sb.toString(); // Возвращаем строковое представление
    }

    // Рекурсивный метод для симметричного обхода дерева
    void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb); // Обход левого поддерева
            sb.append(node.key + "=" + node.value + ", "); // Добавление текущего узла
            inOrderTraversal(node.right, sb); // Обход правого поддерева
        }
    }

    // Метод для получения размера дерева
    @Override
    public int size() {
        return size(root); // Возвращаем размер дерева
    }

    // Рекурсивный метод для вычисления размера дерева
    int size(Node node) {
        if (node == null) {
            return 0; // Если узел пустой, размер 0
        }
        return 1 + size(node.left) + size(node.right); // Размер = 1 (текущий узел) + размеры поддеревьев
    }

    // Проверка, пустое ли дерево
    @Override
    public boolean isEmpty() {
        return root == null; // Если корень null, то дерево пустое
    }

    // Проверка наличия ключа в дереве
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null; // Если ключ найден, возвращаем true
    }

    // Проверка наличия значения в дереве
    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value.toString()); // Запускаем рекурсивный поиск значения
    }

    // Рекурсивный метод для проверки наличия значения
    boolean containsValue(Node node, String value) {
        if (node == null) {
            return false; // Если узел пустой, значение не найдено
        }
        if (node.value.equals(value)) {
            return true; // Если значение найдено, возвращаем true
        }
        // Проверяем левое и правое поддеревья
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // Получение значения по ключу
    @Override
    public String get(Object key) {
        Node found = SearchKey((Integer) key, root); // Ищем узел по ключу
        if (found != null) {
            root = splay(root, found.key); // Смыкаем найденный узел
            return found.value; // Возвращаем значение
        }
        return null; // Если ключ не найден, возвращаем null
    }

    // Рекурсивный метод для поиска узла по ключу
    Node SearchKey(Integer key, Node node) {
        if (node == null)
            return null; // Базовый случай
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node; // Если ключ найден, возвращаем узел

        // Рекурсивный вызов для левого или правого поддерева
        return SearchKey(key, comparison < 0 ? node.left : node.right);
    }

    // Метод для добавления или обновления значения по ключу
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value); // Если дерево пустое, создаем новый узел
            return null;
        }

        root = splay(root, key); // Смыкаем узел с помощью ключа
        int cmp = key.compareTo(root.key);
        if (cmp == 0) { // Если ключ уже существует, обновляем значение
            String oldValue = root.value;
            root.value = value;
            return oldValue; // Возвращаем старое значение
        } else if (cmp < 0) { // Если ключ меньше корня, добавляем в левое поддерево
            Node newNode = new Node(key, value);
            newNode.left = root.left; // Устанавливаем левое поддерево
            newNode.right = root; // Устанавливаем правое поддерево
            newNode.right.parent = newNode; // Обновляем родителя
            root.left = null; // Обнуляем левое поддерево
            root = newNode; // Устанавливаем новый корень
        } else { // Если ключ больше корня, добавляем в правое поддерево
            Node newNode = new Node(key, value);
            newNode.right = root.right; // Устанавливаем правое поддерево
            newNode.left = root; // Устанавливаем левое поддерево
            newNode.left.parent = newNode; // Обновляем родителя
            root.right = null; // Обнуляем правое поддерево
            root = newNode; // Устанавливаем новый корень
        }
        return null; // Возвращаем null, так как новое значение не возвращается
    }

    // Метод для удаления узла по ключу
    @Override
    public String remove(Object key) {
        if (root == null) {
            return null; // Если дерево пустое, возвращаем null
        }

        root = splay(root, (Integer) key); // Смыкаем узел
        int cmp = ((Integer) key).compareTo(root.key);
        if (cmp != 0) {
            return null; // Если ключ не найден, возвращаем null
        }

        String removedValue = root.value; // Запоминаем удаляемое значение

        // Если у узла нет левого поддерева
        if (root.left == null) {
            root = root.right; // Ставим правое поддерево новым корнем
            if (root != null) {
                root.parent = null; // Обнуляем родителя
            }
        } else { // Если есть левое поддерево
            Node newRoot = root.right; // Запоминаем правое поддерево
            newRoot = splay(newRoot, (Integer) key); // Смыкаем правое поддерево
            newRoot.left = root.left; // Устанавливаем левое поддерево
            newRoot.left.parent = newRoot; // Обновляем родителя
            root = newRoot; // Устанавливаем новый корень
        }

        return removedValue; // Возвращаем удаленное значение
    }

    // Метод для смыкания узла
    Node splay(Node node, Integer key) {
        if (node == null) {
            return null; // Если узел пустой, возвращаем null
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) { // Если ключ меньше, идем в левое поддерево
            if (node.left == null) {
                return node; // Если левого поддерева нет, возвращаем текущий узел
            }
            int cmp2 = key.compareTo(node.left.key);
            if (cmp2 < 0) { // Случай "левый левый"
                node.left.left = splay(node.left.left, key); // Рекурсивно смачиваем
                node = rotateRight(node); // Поворачиваем вправо
            } else if (cmp2 > 0) { // Случай "левый правый"
                node.left.right = splay(node.left.right, key); // Рекурсивно смачиваем
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left); // Поворачиваем влево
                }
            }
            if (node.left == null) {
                return node; // Если левого узла нет, возвращаем текущий узел
            } else {
                return rotateRight(node); // Поворачиваем вправо
            }
        } else if (cmp > 0) { // Если ключ больше, идем в правое поддерево
            if (node.right == null) {
                return node; // Если правого поддерева нет, возвращаем текущий узел
            }
            int cmp2 = key.compareTo(node.right.key);
            if (cmp2 < 0) { // Случай "правый левый"
                node.right.left = splay(node.right.left, key); // Рекурсивно смачиваем
                if (node.right.left != null) {
                    node.right = rotateRight(node.right); // Поворачиваем вправо
                }
            } else if (cmp2 > 0) { // Случай "правый правый"
                node.right.right = splay(node.right.right, key); // Рекурсивно смачиваем
                node = rotateLeft(node); // Поворачиваем влево
            }
            if (node.right == null) {
                return node; // Если правого узла нет, возвращаем текущий узел
            } else {
                return rotateLeft(node); // Поворачиваем влево
            }
        } else {
            return node; // Если узел найден, возвращаем его
        }
    }

    // Метод для поворота узла вправо
    Node rotateRight(Node node) {
        Node leftChild = node.left; // Запоминаем левого ребенка
        node.left = leftChild.right; // Правое поддерево левого ребенка
        if (leftChild.right != null) {
            leftChild.right.parent = node; // Обновляем родителя правого поддерева
        }
        leftChild.right = node; // Поворачиваем узел вправо
        leftChild.parent = node.parent; // Обновляем родителя
        node.parent = leftChild; // Устанавливаем нового родителя
        return leftChild; // Возвращаем нового корня
    }

    // Метод для поворота узла влево
    Node rotateLeft(Node node) {
        Node rightChild = node.right; // Запоминаем правого ребенка
        node.right = rightChild.left; // Левое поддерево правого ребенка
        if (rightChild.left != null) {
            rightChild.left.parent = node; // Обновляем родителя левого поддерева
        }
        rightChild.left = node; // Поворачиваем узел влево
        rightChild.parent = node.parent; // Обновляем родителя
        node.parent = rightChild; // Устанавливаем нового родителя
        return rightChild; // Возвращаем нового корня
    }

    // Метод для очистки дерева
    @Override
    public void clear() {
        root = clear(root); // Очищаем дерево
    }

    // Рекурсивный метод для очистки узла
    Node clear(Node node) {
        if (node == null)
            return null; // Базовый случай
        node.left = clear(node.left); // Очищаем левое поддерево
        node.right = clear(node.right); // Очищаем правое поддерево
        return null; // Возвращаем null
    }

    // Получение ключа, меньшего заданного
    @Override
    public Integer lowerKey(Integer key) {
        if (root == null)
            return null; // Если дерево пустое, возвращаем null
        Node node = lowerKeyNode(key, root); // Ищем узел
        if (node != null) {
            return node.key; // Возвращаем ключ
        }
        return null; // Если узел не найден, возвращаем null
    }

    // Рекурсивный метод для поиска узла с меньшим ключом
    Node lowerKeyNode(Integer key, Node node) {
        if (node == null)
            return null; // Базовый случай
        int comparison = key.compareTo(node.key);
        if (comparison <= 0)
            return lowerKeyNode(key, node.left); // Ищем в левом поддереве
        Node rightResult = lowerKeyNode(key, node.right); // Ищем в правом поддереве
        if (rightResult != null)
            return rightResult; // Если узел найден, возвращаем его
        return node; // Возвращаем текущий узел
    }

    // Получение ключа, наибольшего, но не превышающего заданный
    @Override
    public Integer floorKey(Integer key) {
        if (root == null)
            return null; // Если дерево пустое, возвращаем null
        Node node = floorKeyNode(key, root); // Ищем узел
        if (node != null) {
            return node.key; // Возвращаем ключ
        }
        return null; // Если узел не найден, возвращаем null
    }

    // Рекурсивный метод для поиска узла с наибольшим ключом, не превышающим заданный
    Node floorKeyNode(Integer key, Node node) {
        if (node == null)
            return null; // Базовый случай
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node; // Если ключ равен, возвращаем узел
        if (comparison < 0)
            return floorKeyNode(key, node.left); // Ищем в левом поддереве
        Node rightResult = floorKeyNode(key, node.right); // Ищем в правом поддереве
        if (rightResult != null)
            return rightResult; // Если узел найден, возвращаем его
        return node; // Возвращаем текущий узел
    }

    // Получение ключа, наименьшего, но не менее заданного
    @Override
    public Integer ceilingKey(Integer key) {
        if (root == null)
            return null; // Если дерево пустое, возвращаем null
        Node node = ceilingKeyNode(key, root); // Ищем узел
        if (node != null) {
            return node.key; // Возвращаем ключ
        }
        return null; // Если узел не найден, возвращаем null
    }

    // Рекурсивный метод для поиска узла с наименьшим ключом, не менее заданного
    Node ceilingKeyNode(Integer key, Node node) {
        if (node == null)
            return null; // Базовый случай
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node; // Если ключ равен, возвращаем узел
        if (comparison > 0)
            return ceilingKeyNode(key, node.right); // Ищем в правом поддереве
        Node leftResult = ceilingKeyNode(key, node.left); // Ищем в левом поддереве
        if (leftResult != null)
            return leftResult; // Если узел найден, возвращаем его
        return node; // Возвращаем текущий узел
    }

    // Получение ключа, наибольшего, но не меньшего заданного
    @Override
    public Integer higherKey(Integer key) {
        if (root == null)
            return null; // Если дерево пустое, возвращаем null
        Node node = higherKeyNode(key, root); // Ищем узел
        if (node != null) {
            return node.key; // Возвращаем ключ
        }
        return null; // Если узел не найден, возвращаем null
    }

    // Рекурсивный метод для поиска узла с наибольшим ключом, большего заданного
    Node higherKeyNode(Integer key, Node node) {
        if (node == null)
            return null; // Базовый случай
        int comparison = key.compareTo(node.key);
        if (comparison >= 0)
            return higherKeyNode(key, node.right); // Ищем в правом поддереве
        Node leftResult = higherKeyNode(key, node.left); // Ищем в левом поддереве
        if (leftResult != null)
            return leftResult; // Если узел найден, возвращаем его
        return node; // Возвращаем текущий узел
    }

    // Получение подкарты, содержащей ключи меньше заданного
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap subMap = new MySplayMap(); // Создаем новую карту
        headMap(root, toKey, subMap); // Заполняем подкарты
        return subMap; // Возвращаем подкарты
    }

    // Рекурсивный метод для заполнения подкарты
    void headMap(Node node, Integer toKey, MySplayMap subMap) {
        if (node == null) {
            return; // Базовый случай
        }

        if (node.key.compareTo(toKey) < 0) { // Если ключ меньше toKey
            subMap.put(node.key, node.value); // Добавляем узел в подкарты
            headMap(node.right, toKey, subMap); // Проверяем правое поддерево
        }

        headMap(node.left, toKey, subMap); // Проверяем левое поддерево
    }

    // Получение подкарты, содержащей ключи больше заданного
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap subMap = new MySplayMap();
        tailMap(root, fromKey, subMap); // Заполняем подкарты
        return subMap; // Возвращаем подкарты
    }

    // Метод для заполнения подкарты с ключами, большими или равными fromKey
    void tailMap(Node node, Integer fromKey, MySplayMap subMap) {
        if (node == null) {
            return; // Базовый случай: если узел пуст, выходим
        }

        // Если ключ текущего узла больше или равен fromKey, добавляем его в подкарты
        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value); // Добавляем узел в подкарты
            tailMap(node.left, fromKey, subMap); // Рекурсивно обходим левое поддерево
        }

        // Рекурсивно обходим правое поддерево, независимо от условия
        tailMap(node.right, fromKey, subMap);
    }

    // Возвращает первый (наименьший) ключ в дереве
    @Override
    public Integer firstKey() {
        if (root == null)
            return null; // Если дерево пустое, возвращаем null
        Node node = root;
        // Ищем самый левый узел (наименьший ключ)
        while (node.left != null) {
            node = node.left;
        }
        return node.key; // Возвращаем ключ наименьшего узла
    }

    // Возвращает последний (наибольший) ключ в дереве
    @Override
    public Integer lastKey() {
        if (root == null)
            return null; // Если дерево пустое, возвращаем null
        Node node = root;
        // Ищем самый правый узел (наибольший ключ)
        while (node.right != null) {
            node = node.right;
        }
        return node.key; // Возвращаем ключ наибольшего узла
    }

///////////////////////////////////////////////////////////////

// Методы ниже не реализованы и возвращают null, так как функциональность не реализована

    // Возвращает запись с наименьшим ключом, большим или равным заданному
    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    // Возвращает запись с наименьшим ключом, большим заданному
    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    // Возвращает запись с первым (наименьшим) ключом в дереве
    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    // Возвращает запись с последним (наибольшим) ключом в дереве
    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    // Удаляет и возвращает запись с первым (наименьшим) ключом
    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    // Удаляет и возвращает запись с последним (наибольшим) ключом
    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    // Возвращает подкарты с ключами в диапазоне от fromKey до toKey
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    // Возвращает карту с ключами в порядке убывания
    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    // Возвращает множество ключей в навигационном порядке
    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    // Возвращает множество ключей в порядке убывания
    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    // Возвращает подкарты с учетом включения/исключения границ
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    // Возвращает подкарты до указанного ключа с учетом включения/исключения
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    // Возвращает подкарты от указанного ключа с учетом включения/исключения
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    // Возвращает компаратор для ключей
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    // Добавляет все записи из указанной карты
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }

    // Возвращает множество ключей карты
    @Override
    public Set<Integer> keySet() {
        return null;
    }

    // Возвращает коллекцию значений карты
    @Override
    public Collection<String> values() {
        return null;
    }

    // Возвращает множество записей карты
    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }

    // Возвращает запись с наименьшим ключом, меньшим заданному
    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    // Возвращает запись с наибольшим ключом, меньшим или равным заданному
    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }
}