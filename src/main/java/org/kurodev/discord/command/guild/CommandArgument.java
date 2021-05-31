package org.kurodev.discord.command.guild;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kuro
 * @deprecated to be removed in 1.8.0
 * please use {@link org.apache.commons.cli.CommandLine} instead
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Deprecated(forRemoval = true, since = "1.7.0")
public @interface CommandArgument {
    boolean mandatory() default false;

    boolean requireAdmin() default false;

    String meaning();
}
