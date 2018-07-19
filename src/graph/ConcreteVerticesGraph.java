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
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a graph with nodes (Vertices) that maintain 
    //       their own list of outgoing edges.
    // Representation invariant:
    //   Vertices are of the same type and are distinct Strings.
    //   Edges cannot form self-loops.
    //   There is at most one edge pointing from U to V (i.e., the graph is simple)
    // Safety from rep exposure:
    //   All fields are private.
    //   Vertices are Strings and are thus immutable.
    
    public ConcreteVerticesGraph() {};
    
    private void checkRep() {
        Set<L> s = new HashSet<L>();
        for (Vertex<L> v : vertices) {
            L val = v.getValue();
            assert !v.contains(val); // no self-loops
            assert !s.contains(val); // no repeat vertices
            s.add(val);
        }
    }
    
    @Override public boolean add(L vertex) {
        boolean notFound = true;
        for (Vertex<L> v : vertices) {
            if (v.getValue().equals(vertex)) {
                notFound = false;
                break;
            }
        }
        
        if (notFound) {
            vertices.add(new Vertex<L>(vertex));
        }
        
        checkRep();
        return notFound;
    }
    
    @Override public int set(L source, L target, int weight) {

        if (source.equals(target)) { 
            return 0; // prevent self-loops
        }
        
        Vertex<L> v = null;
        Vertex<L> u = null;
        for (Vertex<L> o : vertices) {
            L s = o.getValue();
            if (s.equals(source)) {
                v = o;
            }
            else if (s.equals(target)) {
                u = o;
            }
        }
        
        if (weight != 0) {
            if (v == null) {
                v = new Vertex<L>(source);
                vertices.add(v);
            }
            if (u == null) {
                u = new Vertex<L>(target);
                vertices.add(u);
            }  
        }
        
        checkRep();
        return v.update(target, weight);
    }
    
    @Override public boolean remove(L vertex) {
        for (Vertex<L> v: vertices) {
            v.unlink(vertex);
        }
        
        boolean removed = false;
        for (Iterator<Vertex> iter = vertices.iterator(); iter.hasNext();) {
            Vertex<L> v = iter.next();
            if (v.getValue().equals(vertex)) {
                iter.remove();
                removed = true;
            }
        }
        
        checkRep();
        return removed;
    }
    
    @Override public Set<L> vertices() {
        Set<L> vs = new HashSet<L>();
        for (Vertex<L> v : vertices) {
            vs.add(v.getValue());
        }
        return vs;
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> srcs = new HashMap<L, Integer>();
        
        for (Vertex<L> v : vertices) {
            if (v.contains(target)) {
                srcs.put(v.getValue(), v.getWeight(target));
            }
        }
        
        return srcs;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> tgts = new HashMap<L, Integer>();
        
        for (Vertex<L> v: vertices) {
            if (v.getValue().equals(source)) {
                tgts = v.getPoints();
                break;
            }
        }
        
        return tgts;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("vertices:\n");
        for (Vertex<L> v : vertices) {
            s.append(v.getValue().toString() + "\n");
        }
        s.append("\nedges:\n");
        for (Vertex<L> v: vertices) {
            String e = v.toString();
            if (e.length() > 0) {
                s.append(e);
            }
        }
        return s.toString();
    }
    
}

/**
 * Vertex which holds a value and a map pointing to string targets with keys as int weights.
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {

    private final L value;
    private Map<L, Integer> points;
    
    // Abstraction function:
    //   A vertex with a string value that maintains its own map to other string vertex values.
    // Representation invariant:
    //   Points cannot contain value (no self-loops)
    //   Weights cannot be zero.
    // Safety from rep exposure:
    //   Value is immutable and access-private.
    //   Points are returned as a new map to prevent mutation.
    
    public Vertex(L val) {
        value = val;
        points = new HashMap<L, Integer>();
    }
    
    private void checkRep() {
        assert !points.containsKey(value);
        for (Integer v : points.values()) {
            assert !v.equals(new Integer(0));
        }
    }
    
    public L getValue() {
        return value;
    }
    
    public Map<L, Integer> getPoints() {
        return new HashMap<L, Integer>(points);
    }
    
    public boolean contains(L target) {
        return points.containsKey(target);
    }
    
    public Integer getWeight(L target) {
        return points.get(target);
    }
    
    public void unlink(L target) {
        points.remove(target);
    }
    
    public int update(L target, int newWeight) {
        int oldWeight = 0;
        
        for (L p : points.keySet()) {
            if (p.equals(target)) {
                oldWeight = points.get(p);
                break;
            }
        }
        
        if (newWeight != 0) {
            points.put(target, newWeight);
        } else { // remove the edge
            unlink(target);
        }
        
        checkRep();
        return oldWeight;
    }
    
    @Override public String toString() {
        StringBuilder s = new StringBuilder();
        for (Map.Entry<L, Integer> p : points.entrySet()) {
            s.append(String.format("%s -> %s (%d)\n", value, p.getKey().toString(), p.getValue()));
        }
        return s.toString();
    }
    
    
}
