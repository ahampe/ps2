/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    // 
    // empty: ensure graph is empty
    //
    // add: 1 vertex, multiple distinct vertices, duplicate vertices
    //      
    // set: nonzero for existing edge, zero for existing edge
    //      nonzero for non-existing edge, zero for non-existing edge
    //      add edge with one or both vertices missing
    //      reset edge weight
    //      attempt to make self-loop
    //
    // remove: unconnected vertex, vertex with connections-, non-existent vertex
    //
    // sources: target with no sources, target with 1+ sources
    // 
    // targets: source with no targets, source with 1+ targets
    // 
    // vertices: empty graph, non-empty graph
    // 
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
        
    // covers adding 1 vertex, adding duplicate vertex,
    // removing 1 vertex, 
    // targets: source with 0 targets
    // sources: source with 0 sources
    @Test
    public void testOneVertex() {
        Graph<String> one = emptyInstance();
        assertEquals("expected vertex to be added", one.add("Hello"), true);
        assertEquals("expected vertex to already exist", one.add("Hello"), false);
        assertEquals("expected graph to have one vertex",
                new HashSet<String>(Arrays.asList("Hello")), one.vertices());
        assertEquals("expected vertex to be removed", one.remove("Hello"), true);
        assertEquals("expected graph to have no vertices",
                Collections.emptySet(), one.vertices());
        assertEquals("expected graph to have no edges",
                Collections.emptyMap(), one.sources("Hello"));
        assertEquals("expected graph to have no edges",
                Collections.emptyMap(), one.targets("Hello"));
    }
    
    // covers adding 2 distinct vertices,
    // setting non-zero weight between existing
    // re-setting edge weight
    // setting non-zero weight between non-existing
    @Test
    public void testTwoVertices() {
        Graph<String> two = emptyInstance();
        assertEquals(two.add("First"), true);
        assertEquals(two.add("Second"), true);
        assertEquals("expected edge to not exist", two.set("First", "Second", 1), 0);
        assertEquals("expected edge to exist", two.set("First", "Second", 2), 1);
        assertEquals("expected edge to not exist", two.set("First", "Third", 2), 0);
        assertEquals("expected graph to have three vertices",
                new HashSet<String>(Arrays.asList("First", "Second", "Third")), two.vertices());
        
        Map<String, Integer> sources1 = new HashMap<String, Integer>();
        sources1.put("First", 2);
        assertEquals("expected edge with weight",
                sources1, two.sources("Second"));
        
        Map<String, Integer> targets1 = new HashMap<String, Integer>();
        targets1.put("Second", 2);
        targets1.put("Third", 2);
        assertEquals("expected two edges with weight",
                targets1, two.targets("First"));
        }
    
    @Test
    public void testSelfLoop() {
        Graph<String> sloop = emptyInstance();
        assertEquals(sloop.set("First", "First", 1), 0);
        assertEquals("expected graph to have 1 vertex",
                new HashSet<String>(Arrays.asList("First")), sloop.vertices());
        
        Map<String, Integer> g2 = new HashMap<String, Integer>();
        g2.put("First", 1);
        assertEquals("expected sources to have edge", 
                g2, sloop.sources("First"));
        assertEquals("expected targets to have edge", 
                g2, sloop.targets("First"));
    }
    
    // set: 0 for existing edge, 0 for non-existing edge
    // removes: vertex with connections, vertex without connections
    @Test
    public void testRemoves() {
        Graph<String> three = emptyInstance();
        assertEquals(three.add("First"), true);
        assertEquals(three.add("Second"), true);
        assertEquals("expected edge to not exist", three.set("First", "Second", 1), 0);
        assertEquals("expected edge to exist", three.set("First", "Second", 0), 1);
        assertEquals("expected edge to not exist", three.set("First", "Second", 1), 0);
        
        assertEquals("expected node to exist", three.remove("First"), true);
        assertEquals("expected node to not exist", three.remove("First"), false);
        assertEquals(three.add("First"), true);
        
        assertEquals("expected edge to not exist", three.set("First", "Second", 0), 0);
        
        assertEquals("expected node to exist", three.remove("Second"), true);
        assertEquals("expected node to not exist", three.remove("Second"), false);
    }
    
}
