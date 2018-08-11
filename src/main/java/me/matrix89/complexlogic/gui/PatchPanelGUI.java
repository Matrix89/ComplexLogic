package me.matrix89.complexlogic.gui;

import me.matrix89.complexlogic.ComplexLogic;
import me.matrix89.complexlogic.network.PacketRegistry;
import me.matrix89.complexlogic.network.packets.PatchPanelPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class PatchPanelGUI extends GuiContainer {
    private ResourceLocation guiTexture = new ResourceLocation(ComplexLogic.MOD_ID, "textures/gui/patch_panel_gui_huge.png");
    private int texWidth = 228, texHeight = 228;
    private PatchPanelContainer container;

    public PatchPanelGUI(PatchPanelContainer inventorySlotsIn) {
        super(inventorySlotsIn);
        this.container = inventorySlotsIn;
    }

    @Override
    public void initGui() {
        super.initGui();
        int marginLeft = (width - texWidth) / 2 + 33;
        int marginTop = (height - texHeight) / 2 + 33;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                PatchPanelCheckBox checkBox = new PatchPanelCheckBox(i + j * 16, marginLeft + i * 12, marginTop + j * 12);
                buttonList.add(checkBox);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(guiTexture);
        drawTexturedModalRect((width - texWidth) / 2, (height - texHeight) / 2, 0, 0, texWidth, texHeight);

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        PacketRegistry.INSTANCE.packetHandler.sendToServer(new PatchPanelPacket(container.connectionGrid));
        super.actionPerformed(button);
    }

    class PatchPanelCheckBox extends GuiButton {
        int rowIdx;
        int columnIdx;

        public PatchPanelCheckBox(int buttonId, int x, int y) {
            super(buttonId, x, y, 6, 6, "");
            rowIdx = buttonId / 16;
            columnIdx = buttonId % 16;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
            if (this.visible) {
                //this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                Minecraft.getMinecraft().renderEngine.bindTexture(guiTexture);
                if (container.connectionGrid[rowIdx][columnIdx] != 0)
                    drawTexturedModalRect(x, y, 9, 239, width, height);
                //this.mouseDragged(mc, mouseX, mouseY);
            }
        }

        public boolean isChecked() {
            return container.connectionGrid[rowIdx][columnIdx] != 0;
        }


        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
                if (container.connectionGrid[rowIdx][columnIdx] != 0){
                    container.connectionGrid[rowIdx][columnIdx] = 0;
                }else {
                    container.connectionGrid[rowIdx][columnIdx] = 15;
                }
                return true;
            }

            return false;
        }
    }

}
