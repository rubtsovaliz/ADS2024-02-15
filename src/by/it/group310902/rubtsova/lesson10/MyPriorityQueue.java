package by.it.group310902.rubtsova.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

// Класс MyPriorityQueue реализует интерфейс Queue и представляет собой очередь с приоритетом
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private static final int DEFAULT_INITIAL_CAPACITY = 10; // емкость кучи
    private int size; //размер очереди
    private E[] heap; // Массив для хранения элементов кучи

    // Конструктор по умолчанию
    public MyPriorityQueue() {
        heap = (E[]) new Comparable[DEFAULT_INITIAL_CAPACITY]; // массив
        size = 0;
    }

    // Метод для обмена местами двух элементов в куче
    void swap(int x, int y) {
        E temp = heap[x]; // Сохраняем элемент x во временной переменной
        heap[x] = heap[y];
        heap[y] = temp;
    }

    // Метод для перемещения элемента вниз по куче для поддержания ее свойств
    void siftDown(int i) {
        int child1, child2, min;

        while (2 * i + 1 < size) { // Пока есть хотя бы один дочерний элемент
            child1 = 2 * i + 1; // Индекс первого дочернего элемента
            child2 = 2 * i + 2; // Индекс второго дочернего элемента
            min = child1; // Предполагаем, что первый дочерний элемент минимален

            // Сравниваем с вторым дочерним элементом
            if ((child2 < size) && (heap[child2].compareTo(heap[min]) < 0)) {
                min = child2; // Обновляем min, если второй дочерний элемент меньше
            }

            // Если родительский элемент меньше дочернего, выходим
            if (heap[i].compareTo(heap[min]) < 0) {
                break;
            }

            swap(i, min); // Меняем местами родителя и минимального дочернего
            i = min; // Переходим к следующему уровню
        }
    }

    // Метод для перемещения элемента вверх по куче для поддержания ее свойств
    void siftUp(int i) {
        // Пока текущий элемент меньше родительского
        while (i > 0 && heap[i].compareTo(heap[(i - 1) / 2]) < 0) {
            swap(i, (i - 1) / 2); // Меняем местами с родителем
            i = (i - 1) / 2; // Переходим к родительскому элементу
        }
    }

    // Метод для приведения кучи в порядок (пересоздание кучи)
    void heapify() {
        // Запускаем siftDown с середины массива для восстановления кучи
        for (int i = (size / 2); i >= 0; i--) {
            siftDown(i);
        }
    }

    // Метод для получения строкового представления очереди
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) { // Проходим по всем элементам
            sb.append(heap[i]); // Добавляем элемент в строку
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        heap = (E[]) new Comparable[DEFAULT_INITIAL_CAPACITY]; // Сбрасываем массив
        size = 0;
    }

    // Метод для добавления элемента в очередь
    @Override
    public boolean add(E e) {
        if (size == heap.length) { // Если массив полон
            // Увеличиваем размер массива вдвое
            E[] newHeap = (E[]) new Comparable[size * 2];
            System.arraycopy(heap, 0, newHeap, 0, size); // Копируем элементы в новый массив
            heap = newHeap; // Меняем ссылку на массив
        }

        heap[size] = e; // Добавляем новый элемент в конец массива
        siftUp(size); // Восстанавливаем свойства кучи
        size++;
        return true;
    }

    // Метод для удаления и возвращения корня
    @Override
    public E remove() {
        if (size == 0) {
            return null;
        }

        E temp = heap[0]; // Сохраняем корень
        size--;
        heap[0] = heap[size]; // Перемещаем последний элемент на место корня
        siftDown(0); // Восстанавливаем свойства кучи

        return temp; // Возвращаем удаленный элемент
    }

    // Метод для проверки, содержится ли элемент в очереди
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) { // Если нашли элемент
                return true;
            }
        }
        return false;
    }

    // Метод для добавления элемента в очередь (аналогично add)
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    // Метод для удаления и возвращения элемента с наивысшим приоритетом (аналогично remove)
    @Override
    public E poll() {
        return remove();
    }

    // Метод для получения элемента с наивысшим приоритетом без удаления
    @Override
    public E element() {
        if (size == 0) {
            return null;
        }

        return heap[0]; // Возвращаем корень
    }

    // Метод для получения элемента с наивысшим приоритетом без удаления (аналогично element)
    @Override
    public E peek() {
        return element(); // Используем метод element
    }

    // Метод для проверки, содержатся ли все элементы из коллекции в очереди
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) { // Проходим по всем элементам коллекции
            if (!contains(item)) { // Если хоть один элемент не найден
                return false;
            }
        }
        return true; // Если все элементы найдены
    }

    // Метод для добавления всех элементов из коллекции в очередь
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean flag = false; // Флаг для отслеживания добавления элементов
        for (E item : c) { // Проходим по всем элементам коллекции
            if (add(item)) { // Если добавили
                flag = true;
            }
        }
        return flag; // Возвращаем true, если хотя бы один элемент был добавлен
    }

    // Метод для удаления всех элементов из коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        int cursor1 = 0, cursor2 = 0, counter = 0;

        // Проходим по всем элементам кучи
        for (cursor2 = 0; cursor2 < size; cursor2++) {
            if (!c.contains(heap[cursor2])) { // Если элемент не содержится в коллекции
                heap[cursor1++] = heap[cursor2]; // Перемещаем его вперед
            } else {
                counter++; // Увеличиваем счетчик удаленных элементов
            }
        }

        // Если были удалены элементы
        if (counter != 0) {
            size -= counter; // Уменьшаем размер
            heapify(); // Восстанавливаем кучу
            return true;
        }

        return false; // Если ничего не было удалено
    }

    // Метод для сохранения только тех элементов, которые содержатся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        int cursor1 = 0, cursor2 = 0, counter = 0;

        for (cursor2 = 0; cursor2 < size; cursor2++) {
            // Если элемент содержится в коллекции
            if (c.contains(heap[cursor2])) {
                heap[cursor1++] = heap[cursor2]; // Сохраняем его
            } else {
                counter++; // Увеличиваем счетчик удаленных элементов
            }
        }

        // Если были удалены элементы
        if (counter != 0) {
            size -= counter; // Уменьшаем размер
            heapify(); // Восстанавливаем кучу
            return true;
        }

        return false; // Если ничего не было удалено
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Метод для удаления указанного элемента
    @Override
    public boolean remove(Object o) {
        return false;
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
}