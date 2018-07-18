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
public class ConcreteVerticesGraph implements Graph<String> {
    
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
        Set<String> s = new HashSet<String>();
        for (Vertex v : vertices) {
            String val = v.getValue();
            assert !v.contains(val); // no self-loops
            assert !s.contains(val); // no repeat vertices
            s.add(val);
        }
    }
    
    @Override public boolean add(String vertex) {
        boolean notFound = true;
        for (Vertex v : vertices) {
            if (v.getValue().equals(vertex)) {
                notFound = false;
                break;
            }
        }
        
        if (notFound) {
            vertices.add(new Vertex(vertex));
        }
        
        checkRep();
        return notFound;
    }
    
    @Override public int set(String source, String target, int weight) {

        if (source.equals(target)) { 
            return 0; // prevent self-loops
        }
        
        Vertex v = null;
        Vertex u = null;
        for (Vertex o : vertices) {
            String s = o.getValue();
            if (s.equals(source)) {
                v = o;
            }
            else if (s.equals(target)) {
                u = o;
            }
        }
        
        if (weight != 0) {
            if (v == null) {
                v = new Vertex(source);
                vertices.add(v);
            }
            if (u == null) {
                u = new Vertex(target);
                vertices.add(u);
            }  
        }
        
        checkRep();
        return v.update(target, weight);
    }
    
    @Override public boolean remove(String vertex) {
        for (Vertex v: vertices) {
            v.unlink(vertex);
        }
        
        boolean removed = false;
        for (Iterator<Vertex> iter = vertices.iterator(); iter.hasNext();) {
            Vertex v = iter.next();
            if (v.getValue().equals(vertex)) {
                iter.remove();
                removed = true;
            }
        }
        
        checkRep();
        return removed;
    }
    
    @Override public Set<String> vertices() {
        Set<String> vs = new HashSet<String>();
        for (Vertex v : vertices) {
            vs.add(v.getValue());
        }
        return vs;
    }
    
    @Override public Map<String, Integer> sources(String target) {
        Map<String, Integer> srcs = new HashMap<String, Integer>();
        
        for (Vertex v : vertices) {
            if (v.contains(target)) {
                srcs.put(v.getValue(), v.getWeight(target));
            }
        }
        
        return srcs;
    }
    
    @Override public Map<String, Integer> targets(String source) {
        Map<String, Integer> tgts = new HashMap<String, Integer>();
        
        for (Vertex v: vertices) {
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
        for (Vertex v : vertices) {
            s.append(v.getValue() + "\n");
        }
        s.append("\nedges:\n");
        for (Vertex v: vertices) {
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
class Vertex {

    private final String value;
    private Map<String, Integer> points;
    
    // Abstraction function:
    //   A vertex with a string value that maintains its own map to other string vertex values.
    // Representation invariant:
    //   Points cannot contain value (no self-loops)
    //   Weights cannot be zero.
    // Safety from rep exposure:
    //   Value is immutable and access-private.
    //   Points are returned as a new map to prevent mutation.
    
    public Vertex(String val) {
        value = val;
        points = new HashMap<String, Integer>();
    }
    
    private void checkRep() {
        assert !points.containsKey(value);
        for (Integer v : points.values()) {
            assert !v.equals(new Integer(0));
        }
    }
    
    public String getValue() {
        return value;
    }
    
    public Map<String, Integer> getPoints() {
        return new HashMap<String, Integer>(points);
    }
    
    public boolean contains(String target) {
        return points.containsKey(target);
    }
    
    public Integer getWeight(String target) {
        return points.get(target);
    }
    
    public void unlink(String target) {
        points.remove(target);
    }
    
    public int update(String target, int newWeight) {
        int oldWeight = 0;
        
        for (String p : points.keySet()) {
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
        for (Map.Entry<String, Integer> p : points.entrySet()) {
            s.append(String.format("%s -> %s (%d)\n", value, p.getKey(), p.getValue()));
        }
        return s.toString();
    }
    
    
}
