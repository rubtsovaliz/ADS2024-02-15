package by.it.group310902.rubtsova.lesson13;
import java.util.*;
// Класс GraphC представляет граф с элементами в виде Map, где ключ - строка, а значение - список строк
public class GraphC {

    private Map<String, ArrayList<String>> elements = new HashMap<>();
    public GraphC(Scanner scanner) {
        // Обрабатываем входные данные в формате "узел1->узел2, узел3, ..."
        for (var connections : scanner.nextLine().split(", ")) {
            var nodes = connections.split("->");
            // Если узел1 еще не содержится в элементах, добавляем его
            if (!elements.containsKey(nodes[0]))
                elements.put(nodes[0], new ArrayList<>());
            // Добавляем узел2, узел3, ... в список для узла узел1
            elements.get(nodes[0]).add(nodes[1]);
        }
        scanner.close();
    }

    // Метод main для тестирования класса GraphC и вывода результата поиска сильно связанных компонент
    public static void main(String[] args) {
        System.out.print(String.join("\n",
                new GraphC(new Scanner(System.in)).findStronglyConnectedComponents()));
    }

    // Метод для поиска сильно связанных компонент в графе
    //стеки для отслеживания посещённых узлов и временных меток
     // Вызывает dfsVisit() для каждого непосещённого узла, чтобы установить временные метки
    //сортирует узлы по временным меткам и получает пути для сильно связанных компонент
    public ArrayList<String> findStronglyConnectedComponents() {
        var visited = new Stack<String>();
        var time = new HashMap<String, Integer>();
        var times = 0;
        for (var node : elements.keySet())
            if (!visited.contains(node))
                times = dfsVisit(node, visited, time, times);
        return getPaths(sortedVertices(time).toArray(new String[0]));
    }

    // Метод обхода в глубину для построения временных меток для узлов
    //Каждый узел получает временную метку, которая соответствует времени его завершения
    private int dfsVisit(String node, Stack<String> visited, HashMap<String, Integer> time, int times) {
        visited.add(node);
        if (elements.get(node) != null)
            for (var next_node : elements.get(node))
                if (!visited.contains(next_node))
                    times = dfsVisit(next_node, visited, time, ++times);
        time.put(node, times++);
        return times;
    }

    // Метод для сортировки узлов по временным меткам
    private ArrayList<String> sortedVertices(HashMap<String, Integer> time) {
        var entryList = new ArrayList<>(time.entrySet());
        entryList.sort((a, b) -> {
            int valueComparison = a.getValue().compareTo(b.getValue());
            return valueComparison == 0 ? a.getKey().compareTo(b.getKey()) : valueComparison;
        });
        var vertices = new ArrayList<String>();
        for (int i = entryList.size() - 1; i > -1; i--)
            vertices.add(entryList.get(i).getKey());
        return vertices;
    }

    // Метод для получения путей сильно связанных компонент
    // Находит сильно связанные компоненты, выполняя обход в глубину по обратному графу
    //  Каждая найденная компонента добавляется в результирующий список.
    private ArrayList<String> getPaths(String[] vertices) {
        var visited = new Stack<String>();
        var reversed = getReversedGraph();
        var result = new ArrayList<String>();
        for (var node : vertices)
            if (!visited.contains(node)) {
                var path = new ArrayList<String>();
                dfs(node, reversed, visited, path);
                path.sort(String::compareTo);
                result.add(String.join("", path));
            }
        return result;
    }

    // Метод для получения обратного графа
   // где направления рёбер противоположны
    public HashMap<String, ArrayList<String>> getReversedGraph() {
        var reversed = new HashMap<String, ArrayList<String>>();
        elements.forEach((node, neighbours) -> {
            neighbours.forEach(next -> {
                reversed.computeIfAbsent(next, k -> new ArrayList<>()).add(node);
            });
        });
        return reversed;
    }

    // Метод обхода в глубину для построения путей сильно связанных компонент
    //Выполняет обход в глубину по заданному графу и добавляет узлы в путь
    private void dfs(String node, HashMap<String, ArrayList<String>> graph, Stack<String> visited,
                     ArrayList<String> path) {
        visited.add(node);
        path.add(node);
        if (graph.get(node) != null)
            for (String next_node : graph.get(node))
                if (!visited.contains(next_node))
                    dfs(next_node, graph, visited, path);
    }
}