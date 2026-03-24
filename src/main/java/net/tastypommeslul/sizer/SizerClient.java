package net.tastypommeslul.sizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.InputConstants;
import com.moulberry.lattice.Lattice;
import com.moulberry.lattice.element.LatticeElements;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.tastypommeslul.sizer.command.SizerCommands;
import net.tastypommeslul.sizer.compat.Config;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.*;
import java.text.DecimalFormat;

public class SizerClient implements ClientModInitializer {
    private static LatticeElements elements;
    public static Config config;
    private static KeyMapping toggleKey;
    private static KeyMapping biggerKey;
    private static KeyMapping smallerKey;
    public static Logger LOGGER = LoggerFactory.getLogger("sizer");

    @Override
    public void onInitializeClient() {
        registerKeyBindings();
        registerKeyHandlers();
        config = new Config();
        loadConfig();
        try {
            elements = LatticeElements.fromAnnotations(Component.literal("Sizer Config"), config);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Lattice config: {}", e.getMessage());
        }

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> dispatcher.register(SizerCommands.mainCommand));
    }

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("sizer","sizer"));
    private static void registerKeyBindings() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.sizer.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                CATEGORY
        ));
        biggerKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.sizer.bigger",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_EQUAL,
                CATEGORY
        ));
        smallerKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.sizer.smaller",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_MINUS,
                CATEGORY
        ));
    }
    private static void registerKeyHandlers() {
        DecimalFormat df = new DecimalFormat("0.00");
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(toggleKey.consumeClick()) {
                config.enabled = !config.enabled;
                if (config.enabled) {
                    Minecraft.getInstance().gui.setOverlayMessage(Component.literal("Enabled Sizer!")
                            .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD), false);
                } else {
                    Minecraft.getInstance().gui.setOverlayMessage(Component.literal("Disabled Sizer!")
                            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), false);
                }
                saveConfig();
            }
            while (biggerKey.consumeClick()) {
                if (config.scale + config.changeRate <= 2.0f) {
                    config.scale += config.changeRate;
                    Minecraft.getInstance().gui.setOverlayMessage(
                            Component.literal("Current Size: " + df.format(config.scale)), false
                    );
                } else {
                    config.scale = 2.0f;
                    Minecraft.getInstance().gui.setOverlayMessage(
                            Component.literal("Maximum scale reached! " + 2.0), false
                    );
                }
                saveConfig();
            }
            while (smallerKey.consumeClick()) {
                if (config.scale - config.changeRate >= 0.25f) {
                    config.scale -= config.changeRate;
                    Minecraft.getInstance().gui.setOverlayMessage(
                            Component.literal("Current Size: " + df.format(config.scale)), false
                    );
                } else {
                    config.scale = 0.25f;
                    Minecraft.getInstance().gui.setOverlayMessage(
                            Component.literal("Minimum scale reached! " + 0.25), false
                    );
                }
                saveConfig();
            }
        });
    }

    public static Screen configScreen(Screen parent) {
        if (elements == null) {
            System.err.println("Lattice elements not initialized!");
            return null;
        }
        return Lattice.createConfigScreen(elements, SizerClient::saveConfig, parent);
    }

    private static final Path FILE = Paths.get("config", "sizer.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void loadConfig() {
        try {
            if (Files.notExists(FILE)) {
                saveConfig();
                return;
            }
            try (Reader r = Files.newBufferedReader(FILE)) {
                config = GSON.fromJson(r, Config.class);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load config: {}", e.getMessage());
        }
    }

    public static void saveConfig() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer w = Files.newBufferedWriter(FILE)) {
                GSON.toJson(config, w);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }
}