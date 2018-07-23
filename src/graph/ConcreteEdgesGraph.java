/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a graph with a set of nodes (vertices) and 
    //      a list of weighted directed edges connecting the nodes (edges).
    // Representation invariant:
    //   Vertices are of the same immutable type.
    //   There is at most one edge pointing from U to V (i.e., the graph is simple)
    // Safety from rep exposure:
    //   All fields are private.
    
    public ConcreteEdgesGraph() {};
    
    private void checkRep() {
        Map<L, Set<L>> edgeMap = new HashMap<L, Set<L>>();
        for (Edge<L> e: edges) {
            L src = e.getSource();
            L tgt = e.getTarget();
            
            Set<L> mapTos = edgeMap.get(src);
            if (mapTos != null) {
                assert !mapTos.contains(tgt); // cannot have duplicate edges
            }
            
            Set<L> ends = new HashSet<L>();
            if (edgeMap.containsKey(src)) {
                ends = edgeMap.get(src);
            }
            ends.add(tgt);
            edgeMap.put(src, ends);
        }
    }
    
    @Override public boolean add(L vertex) {
        return vertices.add(vertex);
    }
    
    @Override public int set(L source, L target, int weight) {
        int oldWeight = 0;
        
        // remove edge if it already exists (changing weights requires this as edges are immutable)
        Edge<L> toRemove = null;
        for (Edge<L> e: edges) {
            if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                oldWeight = e.getWeight();
                toRemove = e;
                break;
            }
        }
        if (toRemove != null) {
            edges.remove(toRemove);
        }
        
        if (weight != 0) {
            // add nodes to vertices if they don't already exist
            if (!vertices.contains(source)) {
                vertices.add(source);
            }
            if (!vertices.contains(target)) {
                vertices.add(target);
            }
            
            edges.add(new Edge<L>(source, target, weight));
        }
        
        checkRep();
        return oldWeight;
    }
    
    @Override public boolean remove(L vertex) {
        boolean res = vertices.remove(vertex);
        
        // remove edges with source or target == vertex
        for (Iterator<Edge<L>> iter = edges.iterator(); iter.hasNext();) {
            Edge<L> e = iter.next();
            if (e.getSource().equals(vertex) || e.getTarget().equals(vertex)) {
                iter.remove();
            }
        }
        
        return res;
    }
    
    @Override public Set<L> vertices() {
        Set<L> vs = new HashSet<L>();
        for (L v : vertices) {
            vs.add(v);
        }
        return vs;
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> srcs = new HashMap<L, Integer>();
        
        for (Edge<L> e: edges) {
            if (e.getTarget().equals(target)) {
                srcs.put(e.getSource(), e.getWeight());
            }
        }
        
        return srcs;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> tgts = new HashMap<L, Integer>();
                
        for (Edge<L> e: edges) {
            if (e.getSource().equals(source)) {
                tgts.put(e.getTarget(), e.getWeight());
            }
        }
        
        return tgts;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("vertices:\n");
        for (L v : vertices) {
            s.append(v.toString() + "\n");
        }
        s.append("\nedges:\n");
        for (Edge<L> e: edges) {
            s.append(e.toString() + "\n");
        }
        return s.toString();
    }
}

/**
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {

    private final L source;
    private final L target;
    private final int weight;
    
    // Abstraction function:
    //   Represents a directed weighted connection between nodes.
    // Representation invariant:
    //   Source and target must be immutable.
    //   Weights must be nonzero.
    // Safety from rep exposure:
    //   All private final fields.
    
    public Edge(L src, L tgt, Integer wgt) {
        source = src;
        target = tgt;
        weight = wgt;
        checkRep();
    }
    
    private void checkRep() {
        assert weight != 0;
    }
    
    L getSource() {
        return source;
    }
    
    L getTarget() {
        return target;
    }
    
    Integer getWeight() {
        return weight;
    }
    
    @Override
    public String toString() {
        return String.format("%s -> %s (%d)", source.toString(), target.toString(), weight);
    }
    
}
