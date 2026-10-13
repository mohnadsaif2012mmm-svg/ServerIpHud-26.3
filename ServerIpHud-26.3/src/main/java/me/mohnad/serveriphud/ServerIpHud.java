package me.mohnad.serveriphud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.resources.Identifier;

public class ServerIpHud implements ClientModInitializer {
    private static final int MARGIN = 4;
    private static final int PADDING = 3;

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath("server_ip_hud", "ip"),
                ServerIpHud::extract
        );
    }

    private static void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        // Only show in multiplayer, and respect F1 (hide HUD)
        if (mc.player == null || mc.level == null || mc.options.hideGui) {
            return;
        }

        ServerData server = mc.getCurrentServer();
        if (server == null || server.ip == null || server.ip.isBlank()) {
            return;
        }

        String text = server.ip;
        int width = mc.font.width(text);
        int height = mc.font.lineHeight;

        graphics.fill(MARGIN, MARGIN, MARGIN + width + PADDING * 2, MARGIN + height + PADDING * 2, 0x88000000);
        graphics.text(mc.font, text, MARGIN + PADDING, MARGIN + PADDING, 0xFFFFFFFF, true);
    }
}
