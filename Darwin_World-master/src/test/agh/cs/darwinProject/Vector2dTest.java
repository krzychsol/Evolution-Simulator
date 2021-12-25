package agh.cs.darwinProject;
import agh.cs.darwinProject.Math.Vector2d;
import org.junit.Test;
import static org.junit.Assert.*;
//using junit 4.13 as 4.5 is available in alpha only

public class Vector2dTest {


    @Test
    public void equalsTest() {

        assertEquals(new Vector2d(1, 0), new Vector2d(1, 0));
        assertNotEquals(new Vector2d(1, 0), new Vector2d(0, 0));
        assertNotEquals(new Vector2d(1, 0), new Vector2d(0,1));
        assertNotEquals(new Vector2d(1, 0), new Vector2d(1,1));
        assertNotEquals(new Vector2d(10, 0), new Vector2d(-10,0));


    }

    @Test
    public void toStringTest() {

        assertEquals(new Vector2d(1, 0).toString(), "(1,0)");
        assertEquals(new Vector2d(0, 0).toString(), "(0,0)");
        assertEquals(new Vector2d(17, 8).toString(), "(17,8)");
        assertEquals(new Vector2d(0,-1).toString(), "(0,-1)");

    }

    @Test
    public void precedesTest() {

        assertTrue(new Vector2d(1, 0).precedes(new Vector2d(17, 8)));
        assertTrue(new Vector2d(1, 0).precedes(new Vector2d(1, 0)));
        assertTrue(new Vector2d(0, 0).precedes(new Vector2d(0,1)));
        assertTrue(new Vector2d(0,1).precedes(new Vector2d(1,1)));
        assertTrue(new Vector2d(1, 0).precedes(new Vector2d(1,1)));

        assertFalse(new Vector2d(17, 8).precedes(new Vector2d(1,1)));
        assertFalse(new Vector2d(0,1).precedes(new Vector2d(1, 0)));
        assertFalse(new Vector2d(1, 0).precedes(new Vector2d(0,1)));
        assertFalse(new Vector2d(1, 0).precedes(new Vector2d(0, 0)));
    }

    @Test
    public void followsTest() {

        assertTrue(new Vector2d(1, 0).follows(new Vector2d(0, 0)));
        assertTrue(new Vector2d(1, 0).follows(new Vector2d(1, 0)));
        assertTrue(new Vector2d(17, 8).follows(new Vector2d(1,1)));
        assertTrue(new Vector2d(1,1).follows(new Vector2d(1, 0)));
        assertTrue(new Vector2d(1,1).follows(new Vector2d(0, 0)));

        assertFalse(new Vector2d(1, 0).follows(new Vector2d(17, 8)));
        assertFalse(new Vector2d(0, 0).follows(new Vector2d(1, 0)));
        assertFalse(new Vector2d(1, 0).follows(new Vector2d(0,1)));

    }

    @Test
    public void upperRightTest() {

        assertEquals(new Vector2d(1, 0).upperRight(new Vector2d(1, 0)), new Vector2d(1, 0));
        assertEquals(new Vector2d(4, 4).upperRight(new Vector2d(17, 8)), new Vector2d(17, 8));
        assertEquals(new Vector2d(1, 0).upperRight(new Vector2d(0,1)), new Vector2d(1,1));
        assertEquals(new Vector2d(1, 0).upperRight(new Vector2d(1,1)), new Vector2d(1,1));

    }

    @Test
    public void lowerLeftTest() {

        assertEquals(new Vector2d(1, 0).lowerLeft(new Vector2d(1, 0)), new Vector2d(1, 0));
        assertEquals(new Vector2d(1, 0).lowerLeft(new Vector2d(17, 8)), new Vector2d(1, 0));
        assertEquals(new Vector2d(17, 8).lowerLeft(new Vector2d(1, 0)), new Vector2d(1, 0));
        assertEquals(new Vector2d(1, 0).lowerLeft(new Vector2d(0,1)), new Vector2d(0, 0));
        assertEquals(new Vector2d(1,1).lowerLeft(new Vector2d(1, 0)), new Vector2d(1, 0));

    }

    @Test
    public void addTest() {

        assertEquals(new Vector2d(1, 0).add(new Vector2d(0, 0)), new Vector2d(1, 0));
        assertEquals(new Vector2d(1, 0).add(new Vector2d(1, 0)), new Vector2d(2, 0));
        assertEquals(new Vector2d(1,1).add(new Vector2d(17, 8)), new Vector2d(18, 9));
    }

    @Test
    public void subtractTest() {

        assertEquals(new Vector2d(1, 0).subtract(new Vector2d(1, 0)), new Vector2d(0, 0));
        assertEquals(new Vector2d(1, 0).subtract(new Vector2d(0, 0)), new Vector2d(1, 0));
        assertEquals(new Vector2d(17, 8).subtract(new Vector2d(1,1)), new Vector2d(16, 7));
        assertEquals(new Vector2d(0,1).subtract(new Vector2d(1, 0)), new Vector2d(-1, 1));
    }

    @Test
    public void oppositeTest() {

        assertEquals(new Vector2d(0, 0).opposite(), new Vector2d(0, 0));
        assertEquals(new Vector2d(1, 0).opposite(), new Vector2d(-1, 0));
        assertEquals(new Vector2d(17, 8).opposite(), new Vector2d(-17, -8));


    }
}