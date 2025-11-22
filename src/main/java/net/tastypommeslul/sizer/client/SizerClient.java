package net.tastypommeslul.sizer.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moulberry.lattice.Lattice;
import com.moulberry.lattice.element.LatticeElements;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.tastypommeslul.sizer.compat.Config;
import org.lwjgl.glfw.GLFW;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.*;
import java.text.DecimalFormat;

public class SizerClient implements ClientModInitializer {

    private static LatticeElements elements;
    public static Config config;
    private static KeyBinding toggleKey;
    private static KeyBinding biggerKey;
    private static KeyBinding smallerKey;


    @Override
    public void onInitializeClient() {
        registerKeyBindings();
        registerKeyHandlers();
        config = new Config();
        loadConfig();
        try {
            elements = LatticeElements.fromAnnotations(Text.literal("Sizer Config"), config);
        } catch (Exception e) {
            System.err.println("Failed to initialize Lattice config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static final String CATEGORY = "key.category.sizer.sizer";
//    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of("sizer","sizer"));
    private static void registerKeyBindings() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.sizer.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                CATEGORY
        ));
        biggerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sizer.bigger",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_EQUAL,
                CATEGORY
        ));
        smallerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sizer.smaller",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_MINUS,
                CATEGORY
        ));
    }
    private static void registerKeyHandlers() {
        DecimalFormat df = new DecimalFormat("0.00");
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(toggleKey.wasPressed()) {
                // false => true
                config.sizer.enabled = !config.sizer.enabled;
                if (config.sizer.enabled) {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("Enabled Sizer!")
                            .formatted(Formatting.GREEN, Formatting.BOLD), false);
                } else {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("Disabled Sizer!")
                            .formatted(Formatting.RED, Formatting.BOLD), false);
                }
                saveConfig();
            }
            while (biggerKey.wasPressed()) {
                if (config.sizer.shrinkAmount + config.sizer.changeRate <= 2.0f) {
                    config.sizer.shrinkAmount += config.sizer.changeRate;
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(
                            Text.literal("Current Size: " + df.format(config.sizer.shrinkAmount)), false
                    );
                } else {
                    config.sizer.shrinkAmount = 2.0f;
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(
                            Text.literal("Maximum scale reached! " + 2.0), false
                    );
                }
                saveConfig();
            }
            while (smallerKey.wasPressed()) {
                if (config.sizer.shrinkAmount - config.sizer.changeRate >= 0.25f) {
                    config.sizer.shrinkAmount -= config.sizer.changeRate;
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(
                            Text.literal("Current Size: " + df.format(config.sizer.shrinkAmount)), false
                    );
                } else {
                    config.sizer.shrinkAmount = 0.25f;
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(
                            Text.literal("Minimum scale reached! " + 0.25), false
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
