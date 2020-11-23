package com.ing.sea.pdeng.graph.search.rensink;

import com.ing.sea.pdeng.graph.Type;
import com.ing.sea.pdeng.graph.Vertex;
import com.ing.sea.pdeng.graph.search.javacompat.JHyperEdgeInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class GraphFacade {
    private final Graph graph;

    public GraphFacade(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public String getName() {
        return getGraph().getName();
    }

    public Set<Vertex> getNodes() {
        return getGraph().getNodes();
    }

    public Set<JHyperEdgeInfo> getEdges() {
        return getGraph().getEdges();
    }

    public Set<Vertex> getPre(Vertex node) {
        return getNodePreMap().get(node);
    }

    public Map<Vertex, Set<Vertex>> getNodePreMap() {
        if (this.nodePreMap == null) {
            computePreMaps();
        }
        return this.nodePreMap;
    }

    public Set<Vertex> getPre(JHyperEdgeInfo edge) {
        return getEdgePreMap().get(edge);
    }

    public Map<JHyperEdgeInfo, Set<Vertex>> getEdgePreMap() {
        if (this.edgePreMap == null) {
            computePreMaps();
        }
        return this.edgePreMap;
    }

    private void computePreMaps() {
        Map<Vertex, Set<Vertex>> nodePreMap = new LinkedHashMap<>();
        Map<JHyperEdgeInfo, Set<Vertex>> edgePreMap = new LinkedHashMap<>();
        getEdges().stream().filter(e -> e.in.isEmpty()).forEach(e -> edgePreMap.put(e, Collections.emptySet()));
        edgePreMap.keySet().stream().map(e -> e.out).forEach(n -> nodePreMap.put(n, Collections.emptySet()));
        LinkedList<Vertex> fresh = new LinkedList<>(nodePreMap.keySet());
        while (!fresh.isEmpty()) {
            Vertex next = fresh.poll();
            for (JHyperEdgeInfo e : getOutEdges(next)) {
                if (nodePreMap.keySet().containsAll(e.in)) {
                    Set<Vertex> pre = new LinkedHashSet<>(e.in);
                    e.in.stream().map(nodePreMap::get).forEach(pre::addAll);
                    Set<Vertex> ePre = edgePreMap.get(e);
                    if (ePre == null || ePre.size() > pre.size()) {
                        edgePreMap.put(e, pre);
                        if (!nodePreMap.containsKey(e.out)) {
                            assert !pre.contains(e.out);
                            nodePreMap.put(e.out, new LinkedHashSet<>(pre));
                            fresh.add(e.out);
                        } else if (nodePreMap.get(e.out).retainAll(pre)) {
                            fresh.add(e.out);
                        }
                    }
                }
            }
        }
        assert nodePreMap.keySet().equals(getNodes());
        assert edgePreMap.keySet().equals(getEdges());
        assert validPreMaps(nodePreMap, edgePreMap);
        this.nodePreMap = nodePreMap;
        this.edgePreMap = edgePreMap;
    }

    private boolean validPreMaps(Map<Vertex, Set<Vertex>> nodePreMap, Map<JHyperEdgeInfo, Set<Vertex>> edgePreMap) {
        Map<Vertex, Set<Vertex>> nPre = new LinkedHashMap<>();
        for (Map.Entry<JHyperEdgeInfo, Set<Vertex>> entry: edgePreMap.entrySet()) {
            JHyperEdgeInfo edge = entry.getKey();
            LinkedHashSet<Vertex> pre = new LinkedHashSet<>(edge.in);
            for (Type node: edge.in) {
                pre.addAll(nodePreMap.get(node));
            }
            if (!pre.equals(entry.getValue())) {
                return false;
            }
            if (!nPre.containsKey(edge.out)) {
                nPre.put(edge.out, new HashSet<>(entry.getValue()));
            } else {
                nPre.get(edge.out).retainAll(entry.getValue());
            }
        }
        return nPre.equals(nodePreMap);
    }

    private Map<Vertex, Set<Vertex>> nodePreMap;
    private Map<JHyperEdgeInfo, Set<Vertex>> edgePreMap;

    public int getDepth(Vertex node) {
        Integer result = getNodeDepthMap().get(node);
        return result == null ? Integer.MAX_VALUE : result;
    }

    public int getDepth(JHyperEdgeInfo edge) {
        Integer result = getEdgeDepthMap().get(edge);
        return result == null ? Integer.MAX_VALUE : result;
    }

    public Map<Vertex, Integer> getNodeDepthMap() {
        if (this.nodeDepthMap == null) {
            computeDepthMaps();
        }
        return this.nodeDepthMap;
    }

    public Map<JHyperEdgeInfo, Integer> getEdgeDepthMap() {
        if (this.edgeDepthMap == null) {
            computeDepthMaps();
        }
        return this.edgeDepthMap;
    }

    private void computeDepthMaps() {
        Map<Vertex, Integer> nodeMap = new LinkedHashMap<>();
        Map<JHyperEdgeInfo, Integer> edgeMap = new LinkedHashMap<>();
        getEdges().stream().filter(e -> e.in.isEmpty()).forEach(e -> edgeMap.put(e, 0));
        edgeMap.keySet().stream().map(e -> e.out).forEach(n -> nodeMap.put(n, 0));
        LinkedList<Vertex> fresh = new LinkedList<>(nodeMap.keySet());
        while (!fresh.isEmpty()) {
            Vertex next = fresh.pollFirst();
            for (JHyperEdgeInfo e: getOutEdges(next)) {
                if (nodeMap.keySet().containsAll(e.in)) {
                    int depth = e.in.stream().map(nodeMap::get).reduce(0, Integer::max);
                    edgeMap.put(e, depth + 1);
                    if (!nodeMap.containsKey(e.out) || nodeMap.get(e.out) > depth + 1) {
                        nodeMap.put(e.out, depth + 1);
                        fresh.add(e.out);
                    }
                }
            }
        }
        this.nodeDepthMap = nodeMap;
        this.edgeDepthMap = edgeMap;
    }

    private Map<Vertex, Integer> nodeDepthMap;
    private Map<JHyperEdgeInfo, Integer> edgeDepthMap;

    public List<JHyperEdgeInfo> getInEdges(Vertex node) {
        return getInEdgeMap().get(node);
    }

    public Map<Vertex, List<JHyperEdgeInfo>> getInEdgeMap() {
        if (this.inEdgeMap == null) {
            this.inEdgeMap = computeInEdgeMap();
        }
        return this.inEdgeMap;
    }

    private Map<Vertex, List<JHyperEdgeInfo>> computeInEdgeMap() {
        Map<Vertex, SortedSet<JHyperEdgeInfo>> map = new HashMap<>();
        this.graph.getNodes().forEach(n -> map.put(n, new TreeSet<>(GraphFacade::compareEdges)));
        this.graph.getEdges().forEach(e -> map.get(e.out).add(e));
        Map<Vertex, List<JHyperEdgeInfo>> result = new HashMap<>();
        map.forEach((key, value) -> result.put(key, new ArrayList<>(value)));
        return result;
    }

    /** Mapping from nodes to their incoming edges. */
    private Map<Vertex, List<JHyperEdgeInfo>> inEdgeMap;

    public List<JHyperEdgeInfo> getOutEdges(Vertex node) {
        return getOutEdgeMap().get(node);
    }

    public Map<Vertex, List<JHyperEdgeInfo>> getOutEdgeMap() {
        if (this.outEdgeMap == null) {
            this.outEdgeMap = computeOutEdgeMap();
        }
        return this.outEdgeMap;
    }

    private Map<Vertex, List<JHyperEdgeInfo>> computeOutEdgeMap() {
        Map<Vertex, List<JHyperEdgeInfo>> result = new HashMap<>();
        this.graph.getNodes().forEach(n -> result.put(n, new ArrayList<>()));
        this.graph.getEdges().forEach(e -> e.in.forEach(n -> result.get(n).add(e)));
        return result;
    }

    /** Mapping from nodes to their incoming edges. */
    private Map<Vertex, List<JHyperEdgeInfo>> outEdgeMap;

    static private int compareEdges(JHyperEdgeInfo e1, JHyperEdgeInfo e2) {
        int result = e1.in.size() - e2.in.size();
        if (result == 0) {
            result = e1.f.name().compareTo(e2.f.name());
        }
        return result;
    }
}
