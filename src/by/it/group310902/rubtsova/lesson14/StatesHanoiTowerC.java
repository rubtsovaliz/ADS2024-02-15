package by.it.group310902.rubtsova.lesson14;

import java.util.Scanner;

// Главный класс для решения задачи с башнями Ханой с использованием структуры объединенных множеств (DSU)
public class StatesHanoiTowerC {
    // Вложенный класс для реализации структуры данных DSU
    private static class DSU {
        int[] parent; // Массив для хранения родительских узлов
        int[] size;   // Массив для хранения размера каждого множества

        // Конструктор инициализации массивов
        DSU(int size) {
            parent = new int[size];
            this.size = new int[size];
        }

        // Метод для создания нового множества
        void makeSet(int v) {
            parent[v] = v; // Устанавливаем, что элемент сам себе родитель
            size[v] = 1;   // Инициализируем размер множества как 1
        }

        // Метод для получения размера множества, к которому принадлежит элемент
        int size(int v) {
            return size[findSet(v)]; // Возвращаем размер корня множества
        }

        // Метод для нахождения представителя множества (корня)
        int findSet(int v) {
            if (v == parent[v]) // Если элемент является корнем
                return v;
            return parent[v] = findSet(parent[v]); // Рекурсивно находим корень и применяем сжатие пути
        }

        // Метод для объединения двух множеств
        void unionSets(int a, int b) {
            a = findSet(a); // Находим корень для первого элемента
            b = findSet(b); // Находим корень для второго элемента
            if (a != b) { // Если корни разные
                // Объединяем по размеру
                if (size[a] < size[b]) {
                    int temp = a; // Меняем местами, чтобы a всегда был большим
                    a = b;
                    b = temp;
                }
                parent[b] = a; // Устанавливаем родителя
                size[a] += size[b]; // Увеличиваем размер объединенного множества
            }
        }
    }

    // Метод для определения перемещений по осям
    static int[] carryingOver(int N, int step, int k) {
        int axisX = 0, axisY, axisZ;
        axisY = (N % 2 == 0) ? 1 : 2; // Определяем ось Y в зависимости от четности N
        axisZ = (N % 2 == 0) ? 2 : 1; // Определяем ось Z в зависимости от четности N

        int[] result = new int[3]; // Массив для хранения перемещений
        int t = (step / (1 << (k - 1)) - 1) / 2; // Вычисление t для определения источника и назначения

        // Определяем порядок перемещения в зависимости от четности k
        int[] mapping = (k % 2 != 0) ? new int[]{axisX, axisY, axisZ} : new int[]{axisX, axisZ, axisY};
        int from = mapping[t % 3]; // Исходящая ось
        int to = mapping[(t + 1) % 3]; // Целевая ось

        result[from] = -1; // Уменьшаем высоту исходящей оси
        result[to] = 1;    // Увеличиваем высоту целевой оси
        return result;     // Возвращаем массив с изменениями
    }

    // Метод для подсчета количества нулей в двоичном представлении числа
    static int countBits(int num) {
        int count = 0;
        while (num % 2 == 0) { // Пока число четное
            count++;
            num /= 2; // Делим число на 2
        }
        return count; // Возвращаем количество нулей
    }

    // Основной метод программы
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int max_size = (1 << N) - 1; // Максимальное количество шагов
        int[] steps_heights = new int[N]; // Массив для хранения высот шагов
        for (int i = 0; i < N; i++)
            steps_heights[i] = -1; // Инициализируем высоты шагов как -1

        DSU dsu = new DSU(max_size);
        int[] heights = new int[3]; // Массив для хранения высот по осям
        heights[0] = N; // Начальная высота первой оси

        // Основной цикл по шагам
        for (int i = 0; i < max_size; i++) {
            int step = i + 1; // Текущий шаг
            // Получаем изменения высот в зависимости от четности шага
            int[] delta = (step % 2 != 0) ? carryingOver(N, step, 1) : carryingOver(N, step, countBits(step) + 1);

            // Обновляем высоты по осям
            for (int j = 0; j < 3; j++)
                heights[j] += delta[j];

            int max = Math.max(heights[0], Math.max(heights[1], heights[2])); // Находим максимальную высоту
            dsu.makeSet(i); // Создаем множество для текущего шага

            int maxHeightIndex = max - 1; // Индекс максимальной высоты
            // Если высота еще не была сохранена, сохраняем её
            if (steps_heights[maxHeightIndex] == -1)
                steps_heights[maxHeightIndex] = i;
            else
                dsu.unionSets(steps_heights[maxHeightIndex], i); // Объединяем множества
        }

        // Массив для хранения размеров кластеров
        int[] sizes = new int[N];
        for (int i = 0; i < N; i++)
            if (steps_heights[i] != -1)
                sizes[i] = dsu.size(steps_heights[i]); // Получаем размеры кластеров

        // Построение строки для вывода размеров кластеров
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            int max = i; // Индекс максимального размера
            // Находим индекс максимального размера в оставшихся
            for (int j = i + 1; j < N; j++)
                if (sizes[max] < sizes[j])
                    max = j;
            if (sizes[max] == 0) // Если максимальный размер равен 0, выходим
                break;
            // Меняем местами текущий и максимальный
            int temp = sizes[max];
            sizes[max] = sizes[i];
            sizes[i] = temp;
            sb.insert(0, sizes[i] + " "); // Добавляем размер в строку
        }
        sb.deleteCharAt(sb.length() - 1); // Удаляем последний пробел
        System.out.println(sb); // Выводим результаты
    }
}