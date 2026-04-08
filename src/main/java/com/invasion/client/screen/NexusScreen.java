package com.invasion.client.screen;

import com.invasion.InvasionMod;
import com.invasion.block.container.NexusScreenHandler;
import com.invasion.nexus.Mode;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NexusScreen extends HandledScreen<NexusScreenHandler> {
    private static final Identifier BACKGROUND = InvasionMod.id("textures/gui/nexus.png");

    public NexusScreen(NexusScreenHandler container, PlayerInventory inventory, Text title) {
        super(container, inventory, title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(textRenderer, "Nexus", titleX, titleY, 0x404040, false);
        context.drawText(textRenderer, "Range: " + handler.getSpawnRadius(), 48, 28, 0x404040, false);

        // Wave Mode
        if (handler.getMode() == Mode.STARTED || handler.getMode() == Mode.WAITING) {
            // Draw Status
            context.drawText(textRenderer, "Nexus - Wave " + handler.getCurrentWave(), titleX, titleY, 0x404040, false);
            context.drawText(textRenderer, "Activated!", 17, 53, 4210752, false);
            context.drawText(textRenderer, "Mobs Alive: " + handler.getRemainingMobs(), 48, 40, 0x404040, false);
        // Stable Mode
        } else if (handler.getMode() == Mode.CONTINUOUS) {
            // Draw Status
            context.drawText(textRenderer, "Power: " + handler.getPowerLevel()/2500, 17, 53, 4210752, false);
            context.drawText(textRenderer, "Mobs Killed: " + handler.getKills(), 48, 40, 0x404040, false);
            context.drawText(textRenderer, "Nexus - Next Raid: " + handler.getNextRaidTime(), titleX, titleY, 0x404040, false);
            // Draw Health
            int l = handler.getActivationProgressScaled(160);
            context.drawTexture(BACKGROUND, 8,     63, 0, 175, l, 9);
        }
        // Activation Warning
        if (handler.isActivating() && handler.getMode() == Mode.STOPPED) {
            context.drawText(textRenderer, "Activating...", 17, 53, 0x404040, false);
            if (handler.getMode() != Mode.STABLE) {
                context.drawText(textRenderer, "Are you sure?", 95, 53, 0x404040, false);
            }
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

        if (InvasionMod.getConfig().debugMode == true) {
            context.drawText(textRenderer, "DEBUG"                                                  ,10, 50 , 5635925, true);
            context.drawText(textRenderer, "- Activation Timer: " + handler.getActivationTimer()    ,10, 60 , 43520, true);
            context.drawText(textRenderer, "- Mode: " + handler.getMode()                           ,10, 70 , 43520, true);
            context.drawText(textRenderer, "- Wave: " + handler.getCurrentWave()                    ,10, 80 , 43520, true);
            context.drawText(textRenderer, "- Level:" + handler.getLevel()                          ,10, 90 , 43520, true);
            context.drawText(textRenderer, "- Kills: " + handler.getKills()                         ,10, 100, 43520, true);
            context.drawText(textRenderer, "- SpawnRadius: " +  handler.getSpawnRadius()            ,10, 110, 43520, true);
            context.drawText(textRenderer, "- Generation: " +  handler.getGeneration()              ,10, 120, 43520, true);
            context.drawText(textRenderer, "- PowerLevel: " + handler.getPowerLevel()               ,10, 130, 43520, true);
            context.drawText(textRenderer, "- CookTime: " + handler.getCookTime()                   ,10, 140, 43520, true);
            context.drawText(textRenderer, "- HP: " + handler.getNexusHealthScaled(100)           ,10, 150, 43520, true);
        }


        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        context.drawTexture(BACKGROUND, j, k, 0, 0, backgroundWidth, backgroundHeight);

        // Flux Generation Progress
        int l = handler.getGenerationProgressScaled(24);
        context.drawTexture(BACKGROUND, j + 36, k + 25 + 24 - l, 0, 216 - l, 7, l);

        // Wave Mode
        if (handler.getMode() == Mode.STARTED || handler.getMode() == Mode.WAITING) {
            // Draw Lock
            context.drawTexture(BACKGROUND, j + 7, k + 52, 7, 193, 9, 9);
            // Draw Health
            l = handler.getNexusHealthScaled(160);
            context.drawTexture(BACKGROUND, j+8,     k+63, 0, 166, l, 9);

        // Stable Mode
        } else if (handler.getMode() == Mode.CONTINUOUS) {
            // Draw Health
            l = handler.getNexusHealthScaled(160);
            context.drawTexture(BACKGROUND, j+8,     k+63, 0, 175, l, 9);
            // Draw Lock
            context.drawTexture(BACKGROUND, j + 7, k + 52, 7, 193, 9, 9);
        }

        // Setup Mode
        if ((handler.getMode() == Mode.STOPPED || handler.getMode() == Mode.CONTINUOUS) && handler.isActivating()) {
            l = handler.getActivationProgressScaled(160);
            context.drawTexture(BACKGROUND, j + 8,     k + 63, 0, 166, l, 9);
        } else if (handler.getMode() == Mode.STABLE && handler.isActivating()) {
            l = handler.getActivationProgressScaled(160);
            context.drawTexture(BACKGROUND, j + 8,     k + 63, 0, 175, l, 9);
        }

    }
}