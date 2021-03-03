import org.junit.AfterClass;
import org.junit.Test;
import org.kurodev.command.Command;

import static org.junit.Assert.*;

/**
 * @author kuro
 **/
public class TestCommands {
    @AfterClass
    public static void shutdownVM() {
        System.exit(0); //for some reason the program doesn't exit on its own
    }

    @Test
    public void argsContainsIdentifiesExistingArguments() {
        String[] args = {"hello", "world"};
        Command com = new DebugCommand();
        assertTrue(com.argsContain("hello", args));
        assertTrue(com.argsContain("world", args));
        assertTrue(com.argsContain("worLd", args));

        assertFalse(com.argsContain("someValue", args));
    }

    @Test
    public void argIndexReturnsCorrectValues() {
        String[] args = {"hello", "world"};
        Command com = new DebugCommand();
        assertEquals(0, com.argIndex("hello", args));
        assertEquals(1, com.argIndex("world", args));
        assertEquals(-1, com.argIndex("someValue", args));
    }

    @Test
    public void argsContainsDoesNotThrowExceptionsOnEmptyArgs() {
        String[] args = new String[0];
        Command com = new DebugCommand();
        assertFalse(com.argsContain("hello", args));
        assertFalse(com.argsContain("someValue", args));

        args = new String[3];
        assertFalse(com.argsContain("hello", args));
        assertFalse(com.argsContain("someValue", args));
    }

    @Test(expected = IllegalArgumentException.class)
    public void argsContainsThrowsNullPointerWhenNoKeyWordGiven() {
        Command com = new DebugCommand();
        String[] args = new String[3];
        com.argsContain(null, args);
    }
}
