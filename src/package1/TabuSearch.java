package package1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TabuSearch<T> {

    private static final String filename = "src/package1/graph.txt";
    private static int MAX_TRIES = 5;
    private Graph<T> graph;
    private static int[] memory;
    private static int j = 4;

    public static void main(String[] args) throws IOException {
        Graph<String> g = new GraphInstance(filename);
        TabuSearch<String> ts = new TabuSearch<String>(g);
        long time = System.currentTimeMillis();
        ts.color();
        System.out.println(System.currentTimeMillis() - time);
        //GraphExporter.exportGraph("ts", g);
    }

    private class State {
        T info;
        int color;

        public State(T info, int color) {
            this.info = info;
            this.color = color;
        }
    }

    private class Solution {

        Set<State> states;
        int index;

        public Solution(Set<State> states, int index) {
            this.states = states;
            this.index = index;
        }

        public int evaluate() {
            Set<Integer> distinctColors = new HashSet<Integer>();
            for (State state : states)
                distinctColors.add(state.color);
            return distinctColors.size();
        }

        public Set<Solution> neighbors() {
            Set<Solution> set = new HashSet<Solution>();
            int i = 0;
            for (T info : graph.DFS()) {
                if (memory[i] == 0) {
                    // System.out.println("Analizando: " + info);
                    Set<State> aux = new HashSet<State>();
                    if (changeColor(info, aux)) {
                        aux.addAll(states);
                        set.add(new Solution(aux, i));
                    }
                }
                i++;
            }
            return set;
        }

        private boolean changeColor(T info, Set<State> ans) {
            int oldColor = graph.getColor(info);
            if (oldColor == 0)
                graph.setColor(info, oldColor + 1);
            else
                graph.setColor(info, oldColor - 1);

            ans.add(new State(info, graph.getColor(info)));

            for (T next : graph.neighborsColor(info)) {

                if (ans.contains(new State(next, 0))) {
                    graph.setColor(info, oldColor);
                    return false;
                }
                if (!changeColor(next, ans)){
                    graph.setColor(info, oldColor);
                    return false;
                }

            }

            graph.setColor(info, oldColor);
            return true;
        }
    }

    public TabuSearch(Graph<T> graph) {
        this.graph = graph;
    }

    public void color() {
        memory = new int[graph.vertexCount()];
        Solution localSolution = initialSolution();
        Solution bestSolution = localSolution;
        int localSolEval = localSolution.evaluate(), bestSolEval = localSolEval;
        for (int i = 0; i < MAX_TRIES; i++) {
            for (Solution neighbor : localSolution.neighbors()) {
                int neighborSolEval = neighbor.evaluate();
                if (localSolEval > neighborSolEval) {
                    System.out.println("asdasdasd");
                    localSolution = neighbor;
                    localSolEval = neighborSolEval;
                    memory[localSolution.index] = j;
                }
            }
            for (int k = 0; k < memory.length; k++)
                if (memory[k] != 0)
                    memory[k]--;
            if (localSolEval > bestSolEval) {
                System.out.println("Mejoro");
                bestSolution = localSolution;
                bestSolEval = localSolEval;
            }
        }
        for (State state : bestSolution.states) {
            graph.setColor(state.info, state.color);
        }
    }

    private Solution initialSolution() {
        List<Integer> available = new ArrayList<Integer>();
        Set<State> states = new HashSet<State>();
        available.add(0);
        for (T info : graph.DFS()) {
            nodeColor(info, available);
            states.add(new State(info, graph.getColor(info)));
        }
        return new Solution(states, -1);
    }

    private void nodeColor(T info, List<Integer> available) {
        Set<Integer> disabled = new HashSet<Integer>();
        for (T neighbor : graph.neighbors(info)) {
            if (graph.getColor(neighbor) != -1) {
                disabled.add(graph.getColor(neighbor));
            }
        }
        int newColor = 0;
        if (disabled.size() == available.size()) {
            newColor = available.get(available.size() - 1) + 1;
            available.add(newColor);
            graph.setColor(info, newColor);
        } else {
            int i = 0;
            while (graph.getColor(info) == -1) {
                newColor = available.get(i);
                if (!disabled.contains(newColor)) {
                    graph.setColor(info, newColor);
                }
                i++;
            }
        }
    }
}