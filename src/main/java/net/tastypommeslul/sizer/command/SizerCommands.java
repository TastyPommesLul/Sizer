package net.tastypommeslul.sizer.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import net.tastypommeslul.sizer.SizerClient;
import net.tastypommeslul.sizer.command.argument.ToggleOptions;
import net.tastypommeslul.sizer.command.argument.ToggleOptionsType;
import net.tastypommeslul.sizer.command.suggestion.ToggleOptionsSuggestion;

public class SizerCommands {
    public static LiteralArgumentBuilder<FabricClientCommandSource> mainCommand = ClientCommands.literal("sizer")
            .then(ClientCommands.literal("config").executes(SizerCommands::executeConfig))
            .then(ClientCommands.literal("size")
                    .then(ClientCommands.argument("scale", FloatArgumentType.floatArg(0.25f, 2f))
                            .executes(SizerCommands::executeSize)))
            .then(ClientCommands.literal("toggle")
                    .then(ClientCommands.argument("toggle", new ToggleOptionsType())
                            .suggests(new ToggleOptionsSuggestion())
                            .executes(ctx -> {
                                ToggleOptions opt = ctx.getArgument("toggle", ToggleOptions.class);
                                opt.toggleConfigValue();
                                ctx.getSource().sendFeedback(Component.literal(opt + " set to " + opt.getReadable(opt.getConfigValue()) + " (" + opt.getConfigValue() + ")"));
                                return Command.SINGLE_SUCCESS;
                            })));

    private static int executeSize(CommandContext<FabricClientCommandSource> ctx) {
        SizerClient.config.scale = ctx.getArgument("scale", Float.class);
        ctx.getSource().sendFeedback(Component.literal("Scale set to " + SizerClient.config.scale));
        return Command.SINGLE_SUCCESS;
    }

    private static int executeConfig(CommandContext<FabricClientCommandSource> ctx) {
        ctx.getSource().getClient().schedule(() -> ctx.getSource().getClient().setScreen(SizerClient.configScreen(null)));
        return Command.SINGLE_SUCCESS;
    }
}