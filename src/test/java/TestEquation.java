import org.junit.Test;

import static org.junit.Assert.*;

public class TestEquation {

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

}
