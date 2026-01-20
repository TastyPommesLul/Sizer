package net.tastypommeslul.sizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.InputConstants;
import com.moulberry.lattice.Lattice;
import com.moulberry.lattice.element.LatticeElements;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.tastypommeslul.sizer.compat.Config;
import org.lwjgl.glfw.GLFW;

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


    @Override
    public void onInitializeClient() {
        registerKeyBindings();
        registerKeyHandlers();
        config = new Config();
        loadConfig();
        try {
            elements = LatticeElements.fromAnnotations(Component.literal("Sizer Config"), config);
        } catch (Exception e) {
            System.err.println("Failed to initialize Lattice config: " + e.getMessage());
            e.printStackTrace();
        }
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
                // false => true
                config.sizer.enabled = !config.sizer.enabled;
                if (config.sizer.enabled) {
                    Minecraft.getInstance().gui.setOverlayMessage(Component.literal("Enabled Sizer!")
                            .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD), false);
                } else {
                    Minecraft.getInstance().gui.setOverlayMessage(Component.literal("Disabled Sizer!")
                            .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), false);
                }
                saveConfig();
            }
            while (biggerKey.consumeClick()) {
                if (config.sizer.shrinkAmount + config.sizer.changeRate <= 2.0f) {
                    config.sizer.shrinkAmount += config.sizer.changeRate;
                    Minecraft.getInstance().gui.setOverlayMessage(
                            Component.literal("Current Size: " + df.format(config.sizer.shrinkAmount)), false
                    );
                } else {
                    config.sizer.shrinkAmount = 2.0f;
                    Minecraft.getInstance().gui.setOverlayMessage(
                            Component.literal("Maximum scale reached! " + 2.0), false
                    );
                }
                saveConfig();
            }
            while (smallerKey.consumeClick()) {
                if (config.sizer.shrinkAmount - config.sizer.changeRate >= 0.25f) {
                    config.sizer.shrinkAmount -= config.sizer.changeRate;
                    Minecraft.getInstance().gui.setOverlayMessage(
                            Component.literal("Current Size: " + df.format(config.sizer.shrinkAmount)), false
                    );
                } else {
                    config.sizer.shrinkAmount = 0.25f;
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
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
    public static void saveConfig() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer w = Files.newBufferedWriter(FILE)) {
                GSON.toJson(config, w);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
