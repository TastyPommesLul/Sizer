package net.tastypommeslul.sizer;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public class SizerCommands {
    public static LiteralArgumentBuilder<FabricClientCommandSource> mainCommand = ClientCommandManager.literal("sizer")
            .then(ClientCommandManager.literal("config").executes(SizerCommands::executeConfig))
            .then(ClientCommandManager.literal("size")
                    .then(ClientCommandManager.argument("scale", FloatArgumentType.floatArg(0.25f, 2f))
                            .executes(SizerCommands::executeSize)));

    private static int executeSize(CommandContext<FabricClientCommandSource> ctx) {
        SizerClient.config.sizer.scale = ctx.getArgument("scale", Float.class);
        ctx.getSource().sendFeedback(Component.literal("Shrink amount set to " + SizerClient.config.sizer.scale));
        return Command.SINGLE_SUCCESS;
    }


    private static int executeConfig(CommandContext<FabricClientCommandSource> ctx) {
        ctx.getSource().getClient().schedule(() -> ctx.getSource().getClient().setScreen(SizerClient.configScreen(null)));
        return Command.SINGLE_SUCCESS;
    }
}
