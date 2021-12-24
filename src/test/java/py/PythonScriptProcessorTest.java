package py;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kurodev.discord.util.py.PythonScriptCommand;

import java.nio.file.Path;

public class PythonScriptProcessorTest {
    @Test
    public void testDescriptionWillBeFound() {
        PythonScriptCommand cmd = new PythonScriptCommand("test", Path.of("./examplePythonCommand"));
        cmd.prepare();
        Assertions.assertNotNull(cmd.getDescription());
    }
}
