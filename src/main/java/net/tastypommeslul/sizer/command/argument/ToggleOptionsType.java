package net.tastypommeslul.sizer.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ToggleOptionsType implements ArgumentType<ToggleOptions> {
    @Override
    public ToggleOptions parse(StringReader reader) throws CommandSyntaxException {
        return switch (reader.readString()) {
            case "enabled" -> ToggleOptions.ENABLED;
            case "useDifferentValues", "use_different_values" -> ToggleOptions.USE_DIFFERENT_VALUES;
            case "aprilfools", "april_fools" -> ToggleOptions.APRIL_FOOLS;
            case "everyone" -> ToggleOptions.EVERYONE;
            default -> throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
        };
    }
}
