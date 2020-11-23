package com.ing.sea.pdeng.graph.search.rensink;

import com.ing.sea.pdeng.graph.Type;
import com.ing.sea.pdeng.graph.Vertex;
import com.ing.sea.pdeng.graph.search.javacompat.JHyperEdgeInfo;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution extends ArrayList<JHyperEdgeInfo> {
    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String DOT_DIR = System.getProperty("user.dir") + FILE_SEP + "dots";
    private static int id_count;
    private int stepCount;
    private final Graph graph;
    private final Vertex target;
    private final int id;

    public Solution(Graph graph, Vertex target) {
        this.graph = graph;
        this.target = target;
        this.id = id_count;
        id_count++;
    }

    public int getId() {
        return this.id;
    }

    public int getStepCount() {
        return this.stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    /**
     * Checks whether this is a valid solution leading to a given target
     * 
     * @param target
     * @return
     */
	public boolean validate(Vertex target) {
        Set<Type> nodes = this.stream().map(x -> x.out).collect(Collectors.toSet());
		if (nodes.size() != this.size()) {
			return false;
		}
        HashSet<Vertex> produced = new HashSet<>();
        HashSet<Type> unusedNodes = new HashSet<>(nodes);
        HashSet<JHyperEdgeInfo> unusedEdges = new HashSet<>(this);
		Map<Vertex, Set<JHyperEdgeInfo>> outEdges = new HashMap<>();
		nodes.forEach(n -> outEdges.put(n, new HashSet<>()));
		try {
			this.forEach(e -> e.in.forEach(n -> outEdges.get(n).add(e)));
		} catch (NullPointerException exc) {
			return false;
		}
        LinkedList<Vertex> fresh = new LinkedList<>();
        for (JHyperEdgeInfo e : this) {
            if (e.in.isEmpty()) {
                unusedEdges.remove(e);
                fresh.add(e.out);
            }
        }
        while (!fresh.isEmpty()) {
            Vertex next = fresh.poll();
            produced.add(next);
            for (JHyperEdgeInfo e : outEdges.get(next)) {
                if (produced.containsAll(e.in)) {
                    boolean oldEdge = unusedEdges.remove(e);
                    assert oldEdge;
                    unusedNodes.removeAll(e.in);
                    fresh.add(e.out);
                }
            }
		}
		if (!produced.equals(nodes)) {
			return false;
		}
        if (!unusedEdges.isEmpty()) {
            return false;
        }
        if (unusedNodes.size() != 1) {
			return false;
		}
        if (!unusedNodes.iterator().next().equals(target)) {
			return false;
		}
		return true;
	}

    public String getName() {
        return this.graph.getName() + "-" + this.target.name() + "-" + this.id;
    }
}
