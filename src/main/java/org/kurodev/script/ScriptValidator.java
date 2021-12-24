package org.kurodev.script;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScriptValidator {
    private static final List<String> ARRAY_LIST = new ArrayList<>();

    static {
        String[] items = {"description.txt", "main.py"};
        ARRAY_LIST.addAll(Arrays.asList(items));
    }

    private final boolean hasErrors;
    private final String errors;

    public ScriptValidator(String errors) {
        this.hasErrors = !errors.isEmpty();
        this.errors = errors;
    }

    public static ScriptValidator validate(Path dir) {
        StringBuilder errors = new StringBuilder();
        ARRAY_LIST.forEach(item -> {
            var path = Path.of(dir.toString(), item);
            if (!Files.exists(path)) {
                errors.append("Missing required file: ")
                        .append(item)
                        .append("in ")
                        .append(dir.getFileName())
                        .append(System.lineSeparator());
            }
        });
        return new ScriptValidator(errors.toString());
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public String getErrors() {
        return errors;
    }
}
