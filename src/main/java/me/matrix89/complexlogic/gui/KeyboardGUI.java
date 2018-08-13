package me.matrix89.complexlogic.gui;

import me.matrix89.complexlogic.ComplexLogic;
import me.matrix89.complexlogic.network.KeyboardPacket;
import me.matrix89.complexlogic.utils.HIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import pl.asie.charset.lib.inventory.GuiContainerCharset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class KeyboardGUI extends GuiContainerCharset<KeyboardContainer> {
    private static ResourceLocation keyOutlineTexture = new ResourceLocation(ComplexLogic.MOD_ID, "textures/gui/keyboard_key_outline.png");
    private static ResourceLocation keyFeedbackTexture = new ResourceLocation(ComplexLogic.MOD_ID, "textures/gui/keyboard_key_feedback.png");
    private KeyboardContainer container;

    public KeyboardGUI(KeyboardContainer container) {
        super(container, 100, 100);
        this.container= container;
    }

    @Override
    public void initGui() {
        int buttonSize = 20;
        int fullWidth = 368;
        addButtons(width / 2 - fullWidth / 2, 120, 5, buttonSize, buttonSize);
    }

    private void addButtons(int x, int y, int spacing, int width, int height) {
        HIDUtil.keys = new LinkedHashMap<>();
        HIDUtil.regenerateKeys();

        int[] rowXs = new int[]{x, x, x, x, x};
        HIDUtil.keys.values().forEach(key -> {
            buttonList.add(new Key(key, rowXs[key.row], y + key.row * height + key.row * spacing, key.keyWidth, height));
            rowXs[key.row] += key.keyWidth + spacing;
        });
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.player.closeScreen();
            return;
        }

        if (HIDUtil.keys.get(keyCode) == null) return;
        int btnIdx = HIDUtil.keys.get(keyCode).scanCode;
        System.out.println(btnIdx);
        buttonList.forEach(button -> {
            if (button.id == btnIdx && button instanceof Key) {
                ((Key) button).click();
                this.actionPerformed(button);
            }
        });

    }

    @Override
    protected void actionPerformed(GuiButton button){
        ComplexLogic.registry.sendToServer(new KeyboardPacket(button.id));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    private class Key extends GuiButton {
        private float backgroundAlpha = 0f;
        HIDUtil.HIDEntry key;

        public Key(HIDUtil.HIDEntry key, int x, int y, int width, int height) {
            super(key.scanCode, x, y, width, height, key.lowercase);
            this.key = key;
        }

        public void click() {
            backgroundAlpha = 1f;
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (super.mousePressed(mc, mouseX, mouseY)) {
                click();
                return true;
            }
            return false;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {

                FontRenderer fontrenderer = mc.fontRenderer;
                Minecraft.getMinecraft().renderEngine.bindTexture(keyOutlineTexture);

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

                drawOutline();
                GlStateManager.color(1.0F, 1.0F, 1.0F, backgroundAlpha);
                mc.getTextureManager().bindTexture(keyFeedbackTexture);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

                bufferbuilder.pos((double) (x), (double) (y + height), (double) this.zLevel)
                        .tex(0, 1).endVertex();
                bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel)
                        .tex(1, 1).endVertex();
                bufferbuilder.pos((double) (x + width), (double) (y), (double) this.zLevel)
                        .tex(1, 0).endVertex();
                bufferbuilder.pos((double) (x), (double) (y), (double) this.zLevel)
                        .tex(0, 0).endVertex();
                tessellator.draw();

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1f);

                backgroundAlpha -= Math.max(partialTicks * 0.1f, 0);
                this.mouseDragged(mc, mouseX, mouseY);
                int j = 14737632;

                if (packedFGColour != 0) {
                    j = packedFGColour;
                } else if (!this.enabled) {
                    j = 10526880;
                } else if (this.hovered) {
                    j = 16777120;
                }

                if (displayString.length() != 1) {
                    this.drawString(fontrenderer, this.displayString, this.x + 5, this.y + (this.height - 8) / 2, j);
                } else {
                    this.drawCenteredString(fontrenderer, isShiftKeyDown() ? key.uppercase : key.lowercase, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
                }
            }
        }

        public void drawOutline() {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

            bufferbuilder.pos((double) (x), (double) (y + height), (double) this.zLevel)
                    .tex(0, 1).endVertex();
            bufferbuilder.pos((double) (x + 8), (double) (y + height), (double) this.zLevel)
                    .tex(.5, 1).endVertex();
            bufferbuilder.pos((double) (x + 8), (double) (y), (double) this.zLevel)
                    .tex(.5, 0).endVertex();
            bufferbuilder.pos((double) (x), (double) (y), (double) this.zLevel)
                    .tex(0, 0).endVertex();

            bufferbuilder.pos((double) (x + width - 8), (double) (y + height), (double) this.zLevel)
                    .tex(.5, 1).endVertex();
            bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel)
                    .tex(1, 1).endVertex();
            bufferbuilder.pos((double) (x + width), (double) (y), (double) this.zLevel)
                    .tex(1, 0).endVertex();
            bufferbuilder.pos((double) (x + width - 8), (double) (y), (double) this.zLevel)
                    .tex(.5, 0).endVertex();

            bufferbuilder.pos((double) (x + 8), (double) (y + height), (double) this.zLevel)
                    .tex(.2, 1).endVertex();
            bufferbuilder.pos((double) (x + width - 8), (double) (y + height), (double) this.zLevel)
                    .tex(.8, 1).endVertex();
            bufferbuilder.pos((double) (x + width - 8), (double) (y), (double) this.zLevel)
                    .tex(.8, 0).endVertex();
            bufferbuilder.pos((double) (x + 8), (double) (y), (double) this.zLevel)
                    .tex(.2, 0).endVertex();

            tessellator.draw();
        }
    }
}
