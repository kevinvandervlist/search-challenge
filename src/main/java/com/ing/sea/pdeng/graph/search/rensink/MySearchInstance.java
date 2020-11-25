package com.ing.sea.pdeng.graph.search.rensink;

import com.ing.sea.pdeng.graph.Type;
import com.ing.sea.pdeng.graph.Vertex;
import com.ing.sea.pdeng.graph.search.javacompat.JHyperEdgeInfo;

import java.util.*;

public class MySearchInstance implements Iterator<Solution> {
    private final GraphFacade gf;
    private final Vertex product;
    /** List of nodes already made. */
    private final Deque<Vertex> made;
    /** List of nodes yet to be made. */
    private final Deque<Vertex> frontier;
    /** Mapping from found nodes to their (currently known) downstream nodes. */
    private final Map<Vertex, Set<Vertex>> downstreamMap;
    /** Mapping from made nodes to the index of their maker. */
    private final Map<Vertex, Integer> makerIxMap;
    /**
     * Mapping from producing edges to the downstream delta of their early
     * producers.
     */
    private final Map<JHyperEdgeInfo, Map<Vertex, Set<Vertex>>> deltasMap;
    /** Flag indicating that the search space is exhausted. */
    private boolean exhausted;
    /**
     * Flag indicating that the next solution has been found but not yet delivered.
     */
    private boolean nextValid;
    /** Counts the number of steps taken during search. */
    private int stepCount;

    public MySearchInstance(GraphFacade gf, Vertex product) {
        this.gf = gf;
        this.product = product;
        this.made = new LinkedList<>();
        this.frontier = new LinkedList<>();
        this.makerIxMap = new HashMap<>();
        this.downstreamMap = new HashMap<>();
        this.deltasMap = new HashMap<>();
        this.frontier.add(product);
        this.downstreamMap.put(product, Collections.emptySet());
        this.exhausted = ! gf.getNodes().contains(product);
        this.nextValid = false;
    }

    public Graph getGraph() {
        return this.gf.getGraph();
    }

    public Vertex getProduct() {
        return this.product;
    }

    public int getStepCount() {
        return this.stepCount;
    }

    @Override
    public boolean hasNext() {
        if (!this.exhausted && !this.nextValid) {
            findNext();
        }
        return !this.exhausted;
    }

    @Override
    public Solution next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Solution result = computeSolution();
        this.nextValid = false;
        return result;
    }

    private Solution computeSolution() {
        Solution result = new Solution(getGraph(), getProduct());
        this.made.stream().map(this::getMaker).forEach(result::add);
        result.setStepCount(getStepCount());
        return result;
    }

    private void findNext() {
        assert !this.nextValid;
        boolean forward = !this.frontier.isEmpty();
        while (!this.exhausted && !this.nextValid) {
            if (forward) {
                forward = nextNode();
            } else {
                forward = nextMaker();
            }
            log();
            if (forward) {
                this.nextValid = this.frontier.isEmpty();
            } else {
                this.exhausted = this.made.isEmpty();
            }
        }
    }

    /** Find a production for the next found, unproduced node. */
    private boolean nextNode() {
        Vertex next = this.frontier.poll();
        return nextStep(next, 0);
    }

    /** Find the next production for the most recently produced node. */
    private boolean nextMaker() {
        Vertex next = this.made.removeLast();
        Integer makerIx = this.makerIxMap.remove(next);
        removeMaker(getInEdge(next, makerIx));
        return nextStep(next, makerIx + 1);
    }

    private boolean nextStep(Vertex next, int from) {
        boolean success = false;
        List<JHyperEdgeInfo> inEdges = getInEdges(next);
        Set<Vertex> downstream = this.downstreamMap.get(next);
        int makerIx = from;
        while (!success && makerIx < inEdges.size()) {
            JHyperEdgeInfo e = inEdges.get(makerIx);
            if (this.gf.getPre(e).stream().anyMatch(downstream::contains)) {
                makerIx++;
            } else {
                success = true;
            }
        }
        if (success) {
            addMaker(next, makerIx);
        } else {
            this.frontier.push(next);
        }
        this.stepCount++;
        return success;
    }

    /**
     * Adds the source nodes of a given edge to the found nodes, and sets or updates
     * the upstream nodes.
     */
    /**
     * @param made
     * @param makerIx
     */
    private void addMaker(Vertex made, int makerIx) {
        this.made.add(made);
        this.makerIxMap.put(made, makerIx);
        JHyperEdgeInfo maker = getInEdge(made, makerIx);
        HashMap<Vertex, Set<Vertex>> makerDeltas = new HashMap<Vertex, Set<Vertex>>();
        this.deltasMap.put(maker, makerDeltas);
        // compute the resulting (additional) downstream
        ArrayList<Vertex> newDownstream = new ArrayList<>(this.downstreamMap.get(made));
        newDownstream.add(made);
        Set<Vertex> uniqueSource = new HashSet<>();
        for (Type pred : maker.in) {
            if(!uniqueSource.add(pred)) {
                // This is a duplicate source, do nothing
                continue;
            }
            if (this.downstreamMap.containsKey(pred)) {
                // this is a previously found node
                // add the new downstream to it and all its upstream
                LinkedList<Vertex> upstream = new LinkedList<>();
                upstream.add(pred);
                while (!upstream.isEmpty()) {
                    Vertex next = upstream.remove();
                    if (makerDeltas.containsKey(next)) {
                        continue;
                    }
                    Set<Vertex> nextDelta = new HashSet<Vertex>();
                    makerDeltas.put(next, nextDelta);
                    Set<Vertex> oldDownstream = this.downstreamMap.get(next);
                    newDownstream.stream().filter(oldDownstream::add).forEach(nextDelta::add);
                    if (isMade(next)) {
                        upstream.addAll(getMaker(next).in);
                    }
                }
            } else {
                // this is a newly found node
                this.frontier.push(pred);
                this.downstreamMap.put(pred, new HashSet<>(newDownstream));
            }
        }
    }

    private void removeMaker(JHyperEdgeInfo maker) {
        // Iterate over the maker's source nodes in reverse order
        ListIterator<Type> predIter = maker.in.listIterator(maker.in.size());
        Map<Vertex, Set<Vertex>> delta = this.deltasMap.remove(maker);
        HashSet<Vertex> uniqueSource = new HashSet<Vertex>();
        while (predIter.hasPrevious()) {
            Vertex pred = predIter.previous();
            if (!uniqueSource.add(pred)) {
                // this is a duplicate source; do nothing
                continue;
            }
            if (!delta.containsKey(pred)) {
                // this predecessor was found later; it must be at the head of the frontier
                assert pred == this.frontier.peek() : String.format("Source %s of %s is not at front of %s", pred, maker, this.frontier);
                this.frontier.remove();
                this.downstreamMap.remove(pred);
            }
        }
        // restore the downstream of the early predecessors
        delta.entrySet().forEach(e -> this.downstreamMap.get(e.getKey()).removeAll(e.getValue()));
    }

    private boolean isMade(Vertex n) {
        return this.makerIxMap.containsKey(n);
    }

    private int getMakerIx(Vertex n) {
        return this.makerIxMap.get(n);
    }

    private JHyperEdgeInfo getMaker(Vertex n) {
        return getInEdge(n, getMakerIx(n));
    }

    private JHyperEdgeInfo getInEdge(Vertex n, int i) {
        return getInEdges(n).get(i);
    }

    private List<JHyperEdgeInfo> getInEdges(Vertex node) {
        return this.gf.getInEdges(node);
    }

    static private final boolean LOG = false;

    private void log() {
        if (LOG) {
            StringBuilder b = new StringBuilder();
            int i = 0;
            for (Vertex n : this.made) {
                b.append("" + i + ":");
                b.append(this.makerIxMap.get(n));
                b.append('/');
                b.append(getInEdges(n).size());
                b.append(' ');
                i++;
            }
            for (Vertex n : this.frontier) {
                b.append("" + i + ":. ");
                i++;
            }
            System.out.println(b);
        }
    }
}
