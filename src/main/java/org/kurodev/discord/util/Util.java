package org.kurodev.discord.util;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author kuro
 **/
public class Util {
    /**
     * Generates the Filename, so that if the name already exists it will append a (n) before the file extension.
     * <p>n =  an iterated number</p>
     *
     * @param fileNameRaw Name of the file to use
     * @return a Unique path for the file with the given name.
     */
    public static Path generateFileName(String fileNameRaw, Path parent) {
        var split = fileNameRaw.split("\\.");
        StringBuilder fileNameBuilder = new StringBuilder();
        String fileName, fileExtension = "." + split[split.length - 1];
        for (int i = 0; i < (split.length - 1); i++) {
            fileNameBuilder.append(split[i]);
        }
        fileName = fileNameBuilder.toString();
        Path file = parent.resolve(fileName + fileExtension);

        for (int num = 1; Files.exists(file); num++) {
            String numString = String.format("(%d)", num);
            file = parent.resolve(fileName + numString + fileExtension);
        }
        return file;
    }
}
