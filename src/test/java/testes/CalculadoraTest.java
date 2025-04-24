package testes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraTest{
    @Test
    public void testMultplication(){
        int result = 4 * 3;
        assertEquals(12, result, "4 * 3 should equal 12");
    }

    @Test
    public void testDivision(){
        int result = 10 / 2;
        assertEquals(5, result, "10 / 2 should equal 5");
    }

}