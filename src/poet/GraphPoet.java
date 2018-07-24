/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // fields used only in checkRep:
    private final Set<String> initVertices;
    private final Map<String, Integer> initEdges = new HashMap<String, Integer>();
    
    // Abstraction function:
    //   A graph that takes a corpus (String) as input, generates a weighted digraph
    //   with weights being number of times the words are adjacent to each other,
    //   and can create new poems by exploring the graph and inserting bridges between
    //   words with a two-edge path between them.
    // Representation invariant:
    //   Graph is created in the constructor and is not modified elsewhere.
    //   Graph nodes are all lowercase words.
    // Safety from rep exposure:
    //   graph is private and final.
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(corpus));
        
        String line;
        while ((line = br.readLine()) != null) {
            String[] words = line.split("\\s");
            String lastWord = null;
            
            for (String word : words) {
                if (word.length() > 0) {
                    String lowWord = word.toLowerCase();
                    if (lastWord != null) {
                        Integer prevWght = graph.targets(lastWord).get(lowWord);
                        if (prevWght == null) prevWght = 0;
                        graph.set(lastWord, lowWord, prevWght + 1);
                    }
                    lastWord = lowWord;
                }
            }
        }
        
        initVertices = graph.vertices();
        for (String v : initVertices) {
            initEdges.putAll(graph.targets(v));
        }
        br.close();
    }
    
    private void checkRep() {
        Set<String> vertices = graph.vertices();
        Map<String, Integer> edges = new HashMap<String, Integer>();
        assert(graph.vertices().equals(initVertices));
        for (String vertex : vertices) {
            assert(vertex.equals(vertex.toLowerCase()));
            edges.putAll(graph.targets(vertex));
        }
        assert(edges.equals(initEdges));
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        List<String> output = new ArrayList<String>();
        
        String[] words = input.split("\\s");
        String lastWord = "";
        String bridge = "";
        
        for (String word : words) {
            if (word.length() > 0) {
                if (!lastWord.equals("")) {
                    bridge = findBridge(lastWord, word); 
                    if (bridge.length() > 0) output.add(bridge);
                }
                output.add(word);
                lastWord = word;
            }
        }
        
        checkRep();
        return String.join(" ", output);
    }
    
    private String findBridge(String source, String target) {
        String tgtLower = target.toLowerCase();
        String bridge = "";
        Integer maxWght = Integer.MIN_VALUE;
        // find all outgoing edges from source
        Map<String, Integer> intermeds = graph.targets(source.toLowerCase());
        
        for (Map.Entry<String, Integer> e1 : intermeds.entrySet()) {
            String intermed = e1.getKey();
            Integer weight1 = e1.getValue();
            // find length 2 paths from source
            Map<String, Integer> secondaries = graph.targets(intermed);
            for (Map.Entry<String, Integer> e2 : secondaries.entrySet()) {
                Integer weight2 = e2.getValue();
                if (e2.getKey().equals(tgtLower) && weight1 + weight2 > maxWght) {
                    maxWght = weight1 + weight2;
                    bridge = intermed;
                }
            }
        }
        
        return bridge;
    }
    
    @Override
    public String toString() {
        return graph.toString();
    }
    
}
