
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
    private static final long DISPLAY_TIME_MS = 5000L;

    private static String lastServerIp = "";
    private static long shownAt = 0L;

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath("server_ip_hud", "ip"),
                ServerIpHud::extract
        );
    }

    private static void extract(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) {
            return;
        }

        ServerData server = mc.getCurrentServer();
        if (server == null || server.ip == null || server.ip.isBlank()) {
            return;
        }

        String currentIp = server.ip;
        long now = System.currentTimeMillis();

        // Show the address when connecting to a different server.
        if (!currentIp.equals(lastServerIp)) {
            lastServerIp = currentIp;
            shownAt = now;
        }

        // Hide the address after five seconds.
        if (now - shownAt >= DISPLAY_TIME_MS) {
            return;
        }

        String text = "Server IP: " + currentIp;
        int width = mc.font.width(text);
        int height = mc.font.lineHeight;

        graphics.fill(
                MARGIN,
                MARGIN,
                MARGIN + width + PADDING * 2,
                MARGIN + height + PADDING * 2,
                0x88000000
        );

        graphics.text(
                mc.font,
                text,
                MARGIN + PADDING,
                MARGIN + PADDING,
                0xFFFFFFFF,
                true
        );
    }
}
