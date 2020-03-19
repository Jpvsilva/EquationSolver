import org.junit.Test;

import static org.junit.Assert.*;

public class TestEquation {

    @Test
    public void testisSignal(){
        assertFalse(EquationSolver.isSignal('/'));
        assertTrue(EquationSolver.isSignal('-'));
    }

    @Test
    public void testCheckOnlySignals() {
        assertFalse(EquationSolver.containsOnlySignals("b"));
        assertFalse(EquationSolver.containsOnlySignals("1"));
        assertFalse(EquationSolver.containsOnlySignals("+b+"));

        assertTrue(EquationSolver.containsOnlySignals("-"));
        assertTrue(EquationSolver.containsOnlySignals("+"));
        assertTrue(EquationSolver.containsOnlySignals("++"));
        assertTrue(EquationSolver.containsOnlySignals("--"));
    }

    /*
    @Test
    public void testgetExpression(){
        assertEquals("a+3",EquationSolver.getExpression("a+",3));
        assertEquals("b+9",EquationSolver.getExpression("+b",9));
    }*/
}
