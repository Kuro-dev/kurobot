package org.kurodev.script;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public enum MessageType {
    LOG,
    MSG,
    ATTACH_FILE,
    SEND;

    private static final Pattern number = Pattern.compile("\\d+");

    public static MessageType identify(String strType) {
        Predicate<MessageType> typePredicate;
        if (number.matcher(strType).matches()) {
            int intType = Integer.parseInt(strType);
            typePredicate = messageType -> messageType.ordinal() == intType;
        } else {
            typePredicate = messageType -> messageType.name().equals(strType);
        }
        var collected = Arrays.stream(MessageType.values()).filter(typePredicate).findFirst();
        return collected.orElse(null);
    }
}
