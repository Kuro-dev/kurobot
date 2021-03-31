package discord.util;

import org.junit.Test;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.argument.ErrorCode;

import java.util.EnumSet;

import static org.junit.Assert.*;

/**
 * @author kuro
 * @deprecated will soon be replaced}
 **/
@Deprecated(forRemoval = true, since = "1.7.0")
public class ArgumentTest {
    @Test
    public void argParsingTest() {
        String[] args = {"testArg", "-b", "MEEP", "--enableSomething", "otherTestArg"};
        Argument arg = Argument.parse(args);
        assertFalse(arg.hasErrors());
        assertEquals("MEEP", arg.getParam("-b"));
        assertEquals("MEEP", arg.getParam("b"));
        assertTrue(arg.getOpt("--enableSomething"));
        assertTrue(arg.getOpt("enableSomething"));
        assertEquals("testArg", arg.getOtherArgs().get(0));
        assertEquals("otherTestArg", arg.getOtherArgs().get(1));
    }

    @Test
    public void argParsingWillDetectMissingArgumentParam() {
        //-b without additional data should cause an error.
        String[] args = {"-b", "--enableSomething", "otherTestArg"};
        Argument arg = Argument.parse(args);
        assertTrue(arg.hasErrors());
        assertEquals(ErrorCode.OPTION_SYNTAX_ERROR, arg.getErrors().get(0).getCode());
    }

    @Test
    public void argParsingWillNotThrowExceptionsEver() {
        //too short argument should cause error
        String[] args = {"-", "--"};
        Argument arg = Argument.parse(args);
        assertTrue(arg.hasErrors());
        assertEquals(2, arg.getErrors().size());
        assertEquals(ErrorCode.ARGUMENT_TOO_SHORT, arg.getErrors().get(0).getCode());
        assertEquals(ErrorCode.ARGUMENT_TOO_SHORT, arg.getErrors().get(1).getCode());
    }

    @Test
    public void emptyArgsShouldBeIgnored() {
        //too short argument should cause error
        String[] args = {"", ""};
        Argument arg = Argument.parse(args);
        assertFalse(arg.hasErrors());
        assertEquals(0, arg.getOtherArgs().size());
    }

    @Test
    public void edgecase() {
        String[] args = {"-", "--", "-test"};
        Argument arg = Argument.parse(args);
        assertTrue(arg.hasErrors());
        assertEquals(3, arg.getErrors().size());
        assertEquals(ErrorCode.ARGUMENT_TOO_SHORT, arg.getErrors().get(0).getCode());
        assertEquals(ErrorCode.ARGUMENT_TOO_SHORT, arg.getErrors().get(1).getCode());
        assertEquals(ErrorCode.OPTION_SYNTAX_ERROR, arg.getErrors().get(2).getCode());
    }

    @Test
    public void testBulkCheck() {
        String[] args = {"--test1", "-test3", "value", "test6"};
        Argument arg = Argument.parse(args);
        assertFalse(arg.hasErrors());
        EnumSet<TestArgs> content = arg.checkBulk(EnumSet.allOf(TestArgs.class));
        assertTrue(content.contains(TestArgs.BOOL_ARG));
        assertFalse(content.contains(TestArgs.NOT_PRESENT_ARG));
        assertTrue(content.contains(TestArgs.PARAM_ARG));
        assertFalse(content.contains(TestArgs.ARG_WITH_VALUE_SAME_AS_PARAM));
        assertTrue(content.contains(TestArgs.NORMAL_ARG));
    }
}
