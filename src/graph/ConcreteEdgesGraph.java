/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Arrays;
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
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a graph with a set of nodes (vertices) and 
    //      a list of weighted directed edges connecting the nodes (edges).
    // Representation invariant:
    //   Vertices are of the same type and are distinct Strings.
    //   There is at most one edge pointing from U to V (i.e., the graph is simple)
    // Safety from rep exposure:
    //   All fields are private.
    //   Vertices are Strings and are thus immutable.
    
    public ConcreteEdgesGraph() {};
    
    private void checkRep() {
        Map<String, Set<String>> edgeMap = new HashMap<String, Set<String>>();
        for (Edge e: edges) {
            String src = e.getSource();
            String tgt = e.getTarget();
            
            Set<String> mapTos = edgeMap.get(src);
            if (mapTos != null) {
                assert !mapTos.contains(tgt); // cannot have duplicate edges
            }
            
            Set<String> ends = new HashSet<String>();
            if (edgeMap.containsKey(src)) {
                ends = edgeMap.get(src);
            }
            ends.add(tgt);
            edgeMap.put(src, ends);
        }
    }
    
    @Override public boolean add(String vertex) {
        return vertices.add(vertex);
    }
    
    @Override public int set(String source, String target, int weight) {
        int oldWeight = 0;
        
        // protect from self-loops
        if (source.equals(target)) {
            return oldWeight;
        }
        
        // remove edge if it already exists (changing weights requires this as edges are immutable)
        Edge toRemove = null;
        for (Edge e: edges) {
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
            
            edges.add(new Edge(source, target, weight));
        }
        
        checkRep();
        return oldWeight;
    }
    
    @Override public boolean remove(String vertex) {
        boolean res = vertices.remove(vertex);
        
        // remove edges with source or target == vertex
        for (Iterator<Edge> iter = edges.iterator(); iter.hasNext();) {
            Edge e = iter.next();
            if (e.getSource().equals(vertex) || e.getTarget().equals(vertex)) {
                iter.remove();
            }
        }
        
        return res;
    }
    
    @Override public Set<String> vertices() {
        Set<String> vs = new HashSet<String>();
        for (String v : vertices) {
            vs.add(v);
        }
        return vs;
    }
    
    @Override public Map<String, Integer> sources(String target) {
        Map<String, Integer> srcs = new HashMap<String, Integer>();
        
        for (Edge e: edges) {
            if (e.getTarget().equals(target)) {
                srcs.put(e.getSource(), e.getWeight());
            }
        }
        
        return srcs;
    }
    
    @Override public Map<String, Integer> targets(String source) {
        Map<String, Integer> tgts = new HashMap<String, Integer>();
                
        for (Edge e: edges) {
            if (e.getSource().equals(source)) {
                tgts.put(e.getTarget(), e.getWeight());
            }
        }
        
        return tgts;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("vertices:\n");
        for (String v : vertices) {
            s.append(v + "\n");
        }
        s.append("\nedges:\n");
        for (Edge e: edges) {
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
class Edge {

    private final String source;
    private final String target;
    private final int weight;
    
    // Abstraction function:
    //   Represents a directed weighted connection between unique nodes.
    // Representation invariant:
    //   Source and target must be unique Strings.
    //   No self-loops.
    //   Weights must be nonzero.
    // Safety from rep exposure:
    //   All private final fields.
    
    public Edge(String src, String tgt, Integer wgt) {
        source = src;
        target = tgt;
        weight = wgt;
        checkRep();
    }
    
    private void checkRep() {
        assert weight != 0;
        assert !source.equals(target);
    }
    
    String getSource() {
        return source;
    }
    
    String getTarget() {
        return target;
    }
    
    Integer getWeight() {
        return weight;
    }
    
    @Override
    public String toString() {
        return String.format("%s -> %s (%d)", source, target, weight);
    }
    
}
