package net.tastypommeslul.sizer.client;

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
import net.tastypommeslul.sizer.compat.Config;
import org.lwjgl.glfw.GLFW;

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
        try {
            elements = LatticeElements.fromAnnotations(Text.literal("Sizer Config"), config);
        } catch (Exception e) {
            System.err.println("Failed to initialize Lattice config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void registerKeyBindings() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.sizer.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "key.categories.sizer"
        ));
        biggerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sizer.bigger",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_EQUAL,
                "key.categories.sizer"
        ));
        smallerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sizer.smaller",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_MINUS,
                "key.categories.sizer"
        ));
    }
    private static void registerKeyHandlers() {
        DecimalFormat df = new DecimalFormat("0.00");
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(toggleKey.wasPressed()) {
                config.sizer.enabled = !config.sizer.enabled;
                if (config.sizer.enabled) {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("Enabled Sizer!")
                            .formatted(Formatting.GREEN, Formatting.BOLD), false);
                } else {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("Disabled Sizer!")
                            .formatted(Formatting.RED, Formatting.BOLD), false);
                }
            }
            while (biggerKey.wasPressed()) {
                if (config.sizer.shrinkAmount + 0.1f <= 2.0f) {
                    config.sizer.shrinkAmount += 0.1f;
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("Current Size: " + df.format(config.sizer.shrinkAmount)), false);
                } else {
                    config.sizer.shrinkAmount = 2.0f;
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("Maximum scale reached! " + 2.0), false);
                }
            }
            while (smallerKey.wasPressed()) {
                if (config.sizer.shrinkAmount - 0.1f >= 0.25f) {
                    config.sizer.shrinkAmount -= 0.1f;
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("Current Size: " + df.format(config.sizer.shrinkAmount)), false);
                } else {
                    config.sizer.shrinkAmount = 0.25f;
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("Minimum scale reached! " + 0.25), false);
                }
            }
        });
    }


    public static Screen configScreen(Screen parent) {
        if (elements == null) {
            System.err.println("Lattice elements not initialized!");
            return null;
        }
        return Lattice.createConfigScreen(elements, null, parent);
    }
}
