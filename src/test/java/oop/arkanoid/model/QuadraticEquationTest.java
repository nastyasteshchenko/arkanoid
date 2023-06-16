package oop.arkanoid.model;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class QuadraticEquationTest {

    @Test
    public void quadraticEquationWithTwoRootsTest(){
        //3x^2 - 14x - 5 = 0
        QuadraticEquation quadraticEquation = new QuadraticEquation(3, -14, -5);
        List<Double> roots = quadraticEquation.roots;
        assertEquals(2, roots.size());
        assertEquals(-1.0/3, roots.get(0));
        assertEquals(5.0, roots.get(1));
    }

    @Test
    public void quadraticEquationWithOneRootTest(){
        //3x^2 - 18x + 27 = 0
        QuadraticEquation quadraticEquation = new QuadraticEquation(3, -18, 27);
        List<Double> roots = quadraticEquation.roots;
        assertEquals(1, roots.size());
        assertEquals(3, roots.get(0));
    }

    @Test
    public void quadraticEquationWithNoRootsTest(){
        //x^2 + 6x + 14 = 0
        QuadraticEquation quadraticEquation = new QuadraticEquation(1, 6, 14);
        List<Double> roots = quadraticEquation.roots;
        assertTrue(roots.isEmpty());
    }
}