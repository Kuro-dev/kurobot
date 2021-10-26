package org.kurodev.discord.util;

import org.kurodev.discord.message.command.Command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public static List<String> partitionString(String string) {
        return partitionString(string, 1950);
    }

    /**
     * splits a big string into multiple smaller ones.
     * <P>Respects line-breaks, does not cut off mid line.</P>
     *
     * @param string        the string to split
     * @param partitionSize maximum size of each list item (default: 1950)
     * @throws IllegalArgumentException if partitionSize is less than or equal to 0
     */
    public static List<String> partitionString(String string, final int partitionSize) {
        if (partitionSize <= 0) {
            throw new IllegalArgumentException("PartitionSize must be above 0");
        }
        List<String> parts = new ArrayList<>();
        StringBuilder part = new StringBuilder(2000);
        final int newLine = "\n".length();
        string.lines().forEachOrdered(s -> {
            if (part.length() + s.length() + newLine >= partitionSize) {
                parts.add(part.toString());
                part.delete(0, part.length());
            }
            part.append(s).append("\n");
        });
        parts.add(part.toString());
        return parts;
    }

}
