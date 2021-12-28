package agh.ics.darwin;

import agh.ics.darwin.Math.MoveDirection;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class MoveDirectionTest {

    @Test
    public void nextDirectionTest(){

        assertEquals(MoveDirection.NORTH.next(), MoveDirection.NORTHEAST);
        assertEquals(MoveDirection.NORTHEAST.next(), MoveDirection.EAST);
        assertEquals(MoveDirection.EAST.next(), MoveDirection.SOUTHEAST);
        assertEquals(MoveDirection.SOUTHEAST.next(), MoveDirection.SOUTH);
        assertEquals(MoveDirection.SOUTH.next(), MoveDirection.SOUTHWEST);
        assertEquals(MoveDirection.SOUTHWEST.next(), MoveDirection.WEST);
        assertEquals(MoveDirection.WEST.next(), MoveDirection.NORTHWEST);
        assertEquals(MoveDirection.NORTHWEST.next(), MoveDirection.NORTH);

    }

    @Test
    public void previousDirectionTest(){
        
        assertEquals(MoveDirection.NORTH.previous(), MoveDirection.NORTHWEST);
        assertEquals(MoveDirection.NORTHWEST.previous(), MoveDirection.WEST);
        assertEquals(MoveDirection.WEST.previous(), MoveDirection.SOUTHWEST);
        assertEquals(MoveDirection.SOUTHWEST.previous(), MoveDirection.SOUTH);
        assertEquals(MoveDirection.SOUTH.previous(), MoveDirection.SOUTHEAST);
        assertEquals(MoveDirection.SOUTHEAST.previous(), MoveDirection.EAST);
        assertEquals(MoveDirection.EAST.previous(), MoveDirection.NORTHEAST);
        assertEquals(MoveDirection.NORTHEAST.previous(), MoveDirection.NORTH);

    }
}