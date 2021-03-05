import org.junit.Test;
import org.kurodev.command.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author kuro
 **/
public class TestCommands {

    @Test
    public void argsContainsIdentifiesExistingArguments() {
        String[] args = {"hello", "world"};
        Command com = new DebugCommand();
        assertTrue(com.argsContain(args, "hello"));
        assertTrue(com.argsContain(args, "world"));
        assertTrue(com.argsContain(args, "worLd"));

        assertFalse(com.argsContain(args, "someValue"));
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
        assertFalse(com.argsContain(args, "hello"));
        assertFalse(com.argsContain(args, "someValue"));

        args = new String[3];
        assertFalse(com.argsContain(args, "hello"));
        assertFalse(com.argsContain(args, "someValue"));
    }

    @Test(expected = NullPointerException.class)
    public void argsContainsThrowsNullPointerWhenNoKeyWordGiven() {
        Command com = new DebugCommand();
        String[] args = new String[3];
        com.argsContain(null, args);
    }

    @Test()
    public void argsContainsWithList() {
        Command com = new DebugCommand();
        String[] args = {"hello", "world"};
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("test");
        assertTrue(com.argsContain(args, list, true));
        assertFalse(com.argsContain(args, list, false));
        list.add("world");
        assertFalse(com.argsContain(args, list, false));
        list.remove(1);
        assertTrue(com.argsContain(args, list, false));
    }

}
