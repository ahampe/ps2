/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //   Create a graph and ensure that all vertices and edges are accounted for.
    //   Vertices and edges are returned in non-deterministic order, so cannot
    //      check string equality.
    
    @Test
    public void testToString() {
        Graph<String> g = emptyInstance();
        g.add("Foo");
        g.set("Foo", "Bar", 1);
        g.set("Food", "Barn", 2);
        g.add("Bucket");
        System.out.println(g.toString());
    }
    
    
    
}
