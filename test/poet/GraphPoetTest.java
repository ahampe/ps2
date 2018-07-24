/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy:
    //   Empty file
    //   One-word file
    //   File with same word in different cases
    //   File with repeated words
    //   File with higher weights than 1
    //   File with bridge words not directly in between w1 and w2
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // covers empty file input
    @Test
    public void testEmpty() throws IOException {
        GraphPoet empty = new GraphPoet(new File("test/poet/empty.txt"));
        String nothing = "Nothing should be changed.";
        assertEquals(empty.poem(nothing), nothing);
    }
    
    // covers one word poet
    @Test
    public void testOneWord() throws IOException {
        GraphPoet one = new GraphPoet(new File("test/poet/test.txt"));
        String foo = "This is a test for one.";
        assertEquals(one.poem(foo), foo);
    }
    
    // covers same word, different cases
    @Test
    public void testCases() throws IOException {
        GraphPoet cases = new GraphPoet(new File("test/poet/cases.txt"));
        assertEquals(cases.poem("Words are words"), "Words that are words");
    }
    
    // covers repeated words
    @Test
    public void testRepeat() throws IOException {
        GraphPoet repeat = new GraphPoet(new File("test/poet/repeat.txt"));
        assertEquals(repeat.poem("Repeat repeat"), "Repeat repeat repeat");
    }
    
    // covers higher weights than 1
    @Test
    public void testHigherWeight() throws IOException {
        GraphPoet high = new GraphPoet(new File("test/poet/weights.txt"));
        assertEquals(high.poem("One line"), "One good line");
    }
    
    // covers bridge words not directly in between w1 and w2
    @Test
    public void testIndirect() throws IOException {
        GraphPoet ind = new GraphPoet(new File("test/poet/indirects.txt"));
        System.out.println(ind);
        assertEquals(ind.poem("The first second"), "The first and second");
    }
}
