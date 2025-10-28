package net.tastypommeslul.sizer.client;

import com.moulberry.lattice.Lattice;
import com.moulberry.lattice.element.LatticeElements;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.tastypommeslul.sizer.compat.Config;

public class SizerClient implements ClientModInitializer {

    private static LatticeElements elements;
    public static Config config;

    @Override
    public void onInitializeClient() {
        config = new Config();
        try {
            elements = LatticeElements.fromAnnotations(Text.literal("Sizer Config"), config);
        } catch (Exception e) {
            System.err.println("Failed to initialize Lattice config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Screen configScreen(Screen parent) {
        if (elements == null) {
            System.err.println("Lattice elements not initialized!");
            return null;
        }
        return Lattice.createConfigScreen(elements, null, parent);
    }
}
