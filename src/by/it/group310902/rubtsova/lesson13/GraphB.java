package by.it.group310902.rubtsova.lesson13;
import java.util.*;
// Класс GraphB представляет граф с элементами в виде Map, где ключ - строка, а значение - список строк
public class GraphB {

    private Map<String, ArrayList<String>> elements = new HashMap<>();
    public GraphB(Scanner input) {
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

    // Метод для определения наличия циклов в графе
    public boolean isCyclic() {
        // Проверяем каждый узел графа на наличие циклов с помощью обхода в глубину (DFS)
        for (var i : elements.keySet())
            if (dfs(i, new Stack<String>()))
                return true;
        return false;
    }

    // Метод обхода в глубину для поиска циклов в графе
    public boolean dfs(String node, Stack<String> visited) {
        visited.add(node);//Узел добавляется в стек visited, который отслеживает текущие узлы, находящиеся в процессе обхода
        if (elements.get(node) != null)
            for (var next : elements.get(node))
                if (visited.contains(next) || dfs(next, visited))
                    return true;//Если узел имеет соседей,рекурсивный вызов dfs
       // Если уже находится в стеке, наличие цикла
        visited.remove(node);
        return false;
    }

    // Метод main для тестирования класса GraphB и вывода результата наличия циклов в графе
    public static void main(String[] args) {
        System.out.print(new GraphB(new Scanner(System.in)).isCyclic() ? "yes" : "no");
    }
}
