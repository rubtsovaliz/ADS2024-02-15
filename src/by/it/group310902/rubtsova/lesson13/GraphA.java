package by.it.group310902.rubtsova.lesson13;

import java.util.*;
// Класс GraphA представляет граф с элементами в виде Map, где ключ - строка, а значение - список строк
public class GraphA {
    private Map<String, ArrayList<String>> elements = new HashMap<>();

    public GraphA(Scanner input) {
        // Обрабатываем входные данные в формате "узел1 -> узел2, узел3, ..."
        for (var connections : input.nextLine().split(", ")) {
            var nodes = connections.split(" -> ");
            // Если узел1 еще не содержится в элементах, добавляем его
            if (!elements.containsKey(nodes[0]))
                elements.put(nodes[0], new ArrayList<>());
            // Добавляем узел2, узел3, ... в список для узла узел1
            elements.get(nodes[0]).add(nodes[1]);
        }
        input.close();
    }

    // Метод сортировки элементов графа в порядке убывания
    public GraphA sort() {
        // Сортируем все списки значений в элементах графа
        for (var i : elements.values())
            i.sort((a, b) -> b.compareTo(a));
        return this;
    }

    // Метод toString для вывода элементов графа в определенном порядке
    public String toString() {
        var sb = new StringBuilder();
        var stack = new Stack<String>();
        var visited = new HashSet<String>();

        // Обходим все узлы графа с помощью обхода в глубину
        for (var node : elements.keySet())
            if (!visited.contains(node))
                dfs(node, visited, stack);

        // Формируем строку вывода из элементов стека
        sb.append(stack.pop());
        while (!stack.empty())
            sb.append(' ').append(stack.pop());

        return sb.toString();
    }

    // Метод обхода в глубину для поиска всех связанных узлов
    private void dfs(String node, Set<String> visited, Stack<String> stack) {
        visited.add(node);
        if (elements.get(node) != null)
            for (var next : elements.get(node))
                if (!visited.contains(next))
                    dfs(next, visited, stack);
        stack.push(node);
    }

    // Метод main для тестирования класса GraphA
    public static void main(String[] args) {
        System.out.print(new GraphA(new Scanner(System.in)).sort());
    }
}