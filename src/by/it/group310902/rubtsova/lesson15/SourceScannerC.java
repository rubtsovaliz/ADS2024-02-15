package by.it.group310902.rubtsova.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC {
    static final int NORMAL_DISTANCE = 9; // Константа для предельного значения расстояния редактирования между строками.

    // Сравнивает два символа на равенство.
    private static int compareChars(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    // Находит минимальное значение из переменного количества целых чисел.
    private static int findMinimumValue(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    // Проверяет, находится ли расстояние редактирования между двумя строками (содержимым файлов) в допустимых пределах.
    private static boolean checkEditDistance(String file1, String file2) {
        // Если разница в длине превышает NORMAL_DISTANCE, файлы считаются различными.
        if (Math.abs(file1.length() - file2.length()) > NORMAL_DISTANCE)
            return false;

        int distance = 0; // Общее расстояние редактирования между двумя файлами.
        String[] array_s1 = file1.split(" "); // Разделяем содержимое на слова
        String[] array_s2 = file2.split(" ");

        // Сравниваем каждую пару слов из двух файлов.
        for (int index = 0; index < array_s1.length; index++) {
            String s1 = array_s1[index];
            String s2 = array_s2[index];
            int length = s2.length() + 1;
            int[] currRow = new int[length]; // Текущая строка для расчета расстояния редактирования.
            int[] prevRow; // Предыдущая строка для расчета расстояния редактирования.

            // Вычисляем расстояние редактирования с использованием динамического программирования.
            for (int i = 0; i <= s1.length(); i++) {
                prevRow = currRow; // Сохраняем текущую строку как предыдущую для следующей итерации.
                currRow = new int[length];

                for (int j = 0; j <= s2.length(); j++) {
                    currRow[j] = i == 0 ? j : (j == 0 ? i : findMinimumValue(
                            prevRow[j - 1] + compareChars(s1.charAt(i - 1), s2.charAt(j - 1)),
                            prevRow[j] + 1, // Удаление
                            currRow[j - 1] + 1 // Вставка
                    ));
                    // Вычисляем минимум из трех возможных операций (замена, добавление, удаление).
                }
            }
            distance += currRow[s2.length()]; // Добавляем вычисленное расстояние к общему расстоянию.
            if (distance > NORMAL_DISTANCE) // Если общее расстояние превышает норму, возвращаем false.
                return false;
        }
        return true; // Если все слова прошли проверку, возвращаем true.
    }

    // Класс для сравнения списков путей.
    protected static class PathListComparator implements Comparator<ArrayList<Path>> {
        @Override
        public int compare(ArrayList<Path> a1, ArrayList<Path> a2) {
            Collections.sort(a1); // Сортируем списки.
            Collections.sort(a2);
            return a1.get(0).compareTo(a2.get(0)); // Сравниваем первые элементы двух списков.
        }
    }

    // Метод для нахождения групп похожих файлов.
    private static ArrayList<ArrayList<Path>> findSimilarFilesGroups(HashMap<Path, String> filePaths) {
        ArrayList<ArrayList<Path>> equalFiles = new ArrayList<>(); // Для хранения групп похожих файлов.
        ArrayList<Path> array, used = new ArrayList<>(); // Для отслеживания обработанных файлов.

        for (Path filePath1 : filePaths.keySet()) { // Проходим по всем ключам (путям) в filePaths.
            if (!used.contains(filePath1)) { // Если файл еще не обработан.
                array = new ArrayList<>();
                array.add(filePath1); // Добавляем файл в текущую группу.

                for (Path filePath2 : filePaths.keySet()) // Сравниваем текущий файл с каждым другим файлом.
                    if (!filePath1.equals(filePath2) && checkEditDistance(filePaths.get(filePath1), filePaths.get(filePath2))) {
                        array.add(filePath2); // Добавляем похожий файл в группу.
                        used.add(filePath2); // Отмечаем файл как обработанный.
                    }

                if (array.size() > 1) // Если группа содержит более одного файла, добавляем ее в список групп.
                    equalFiles.add(array);
            }
        }
        return equalFiles; // Возвращаем список групп похожих файлов.
    }

    // Метод для нахождения копий файлов.
    private static void findCopies(HashMap<String, HashMap<Path, String>> classes) {
        ArrayList<ArrayList<Path>> equalFiles; // Для хранения групп похожих файлов.
        Set<String> classNames = classes.keySet(); // Получаем имена классов.

        int count; // Счетчик для групп похожих файлов.

        for (String className : classNames) {
            count = 0; // Инициализируем счетчик для каждой группы.
            equalFiles = findSimilarFilesGroups(classes.get(className)); // Находим группы похожих файлов.
            Collections.sort(equalFiles, new PathListComparator()); // Сортируем группы.

            if (!equalFiles.isEmpty()) { // Если группы найдены, выводим их на экран.
                System.out.println("\n---" + className + "---");
                for (ArrayList<Path> paths : equalFiles) {
                    System.out.println("\nКлоны №" + ++count);
                    for (Path path : paths)
                        System.out.println(path); // Выводим пути файлов в группе.
                }
            }
        }
    }

    // Метод для получения информации о Java-файлах в директории src.
    protected static void getInformation() throws IOException {
        HashMap<String, HashMap<Path, String>> javaClasses = new HashMap<>(); // Ключ - имя файла, значение - путь к файлу и его содержимое.

        Path src = Path.of(System.getProperty("user.dir") + File.separator + "src" + File.separator); // Получаем путь к директории src.

        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(directory -> {
                if (directory.toString().endsWith(".java")) { // Проверяем, является ли файл Java.
                    try {
                        String str = Files.readString(directory); // Читаем содержимое файла.
                        if (!str.contains("@Test") && !str.contains("org.junit.Test")) { // Игнорируем тестовые файлы.
                            // Удаляем пакеты и импорты.
                            str = str.replaceAll("package.*;", "").replaceAll("import.*;", "");

                            // Удаляем комментарии.
                            str = str.replaceAll("/\\*[\\w\\W\r\n\t]*?\\*/", "")
                                    .replaceAll("//.*?\r\n\\s*", "");

                            // Удаляем пустые строки.
                            while (str.contains("\r\n\r\n"))
                                str = str.replaceAll("\r\n\r\n", "\r\n");

                            // Проверяем, не пустая ли строка, и удаляем пробелы в начале и конце.
                            if (!str.isEmpty() && (str.charAt(0) < 33 || str.charAt(str.length() - 1) < 33)) {
                                char[] charArr = str.toCharArray();
                                int indexF = 0, indexL = charArr.length - 1;

                                while (indexF < charArr.length && charArr[indexF] < 33 && charArr[indexF] != 0)
                                    charArr[indexF++] = 0;

                                while (indexL >= 0 && charArr[indexL] < 33 && charArr[indexL] != 0)
                                    charArr[indexL--] = 0;

                                str = new String(move(charArr)); // Преобразуем массив символов обратно в строку.
                            }
                            str = str.replaceAll("[\u0000- ]++", " "); // Заменяем нулевые символы и пробелы на один пробел.

                            // Добавляем содержимое файла в хэш-таблицу.
                            if (!javaClasses.containsKey(directory.getFileName().toString()))
                                javaClasses.put(directory.getFileName().toString(), new HashMap<>());
                            javaClasses.get(directory.getFileName().toString()).put(src.relativize(directory), str);
                        }
                    } catch (IOException e) {
                        System.err.println(directory); // Выводим ошибку, если чтение файла не удалось.
                    }
                }
            });
            findCopies(javaClasses); // Находим копии файлов после обработки всех файлов.
        }
    }

    // Метод для обработки массива символов (пока не реализован).
    private static char[] move(char[] charArr) {
        return charArr; // Возвращаем массив символов без изменений.
    }

    public static void main(String[] args) throws IOException {
        getInformation(); // Получаем информацию о файлах.
    }
}
