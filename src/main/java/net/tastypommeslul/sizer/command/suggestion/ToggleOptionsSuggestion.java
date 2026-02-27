package net.tastypommeslul.sizer.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.tastypommeslul.sizer.command.argument.ToggleOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ToggleOptionsSuggestion implements SuggestionProvider<FabricClientCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        List<String> args = new ArrayList<>();
        Arrays.stream(ToggleOptions.values()).toList().forEach(opt -> args.add(opt.toString()));

        for (String arg : args) {
            if (arg.equalsIgnoreCase("april_fools")) continue;
            builder.suggest(arg);
        }
        return builder.buildFuture();
    }
}