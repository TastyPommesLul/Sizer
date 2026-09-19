package net.tastypommeslul.sizer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MultiVersionUtils {
    public static void setOverlayMessage(Minecraft client, Component component, boolean animate) {
        //? if 26.2 {
        client.gui.hud.setOverlayMessage(component, animate);
         //?} else {
        /*client.gui.setOverlayMessage(component, animate);
         *///?}
    }

    public static void setScreen(Minecraft client, Screen screen) {
        //? if 26.2 {
        client.gui.setScreen(screen);
         //?} else {
        /*client.setScreen(screen);
        *///?}
    }
}
