package me.matrix89.complexlogic.gui;

import me.matrix89.complexlogic.ComplexLogic;
import me.matrix89.complexlogic.gate.PatchPanelLogic;
import me.matrix89.complexlogic.network.PatchPanelPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.asie.charset.lib.inventory.GuiContainerCharset;
import pl.asie.simplelogic.gates.gui.ContainerGate;

import java.io.IOException;

public class PatchPanelGUI extends GuiContainerCharset<ContainerGate> {
    private ResourceLocation guiTexture = new ResourceLocation(ComplexLogic.MOD_ID, "textures/gui/patch_panel_gui_huge.png");
    private ContainerGate container;

    public PatchPanelGUI(ContainerGate inventorySlotsIn) {
        super(inventorySlotsIn, 239, 239);
        this.container = inventorySlotsIn;
    }

    @Override
    public void initGui() {
        super.initGui();

        int marginLeft = ((width - xSize) / 2) + 33;
        int marginTop = ((height - ySize) / 2) + 33;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                PatchPanelCheckBox checkBox = new PatchPanelCheckBox(i + j * 16, marginLeft + i * 12, marginTop + j * 12);
                buttonList.add(checkBox);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(guiTexture);
        drawTexturedModalRect(xBase, yBase, 0, 0, xSize, ySize);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        ComplexLogic.registry.sendToServer(new PatchPanelPacket(((PatchPanelLogic)container.getGate().getLogic()).getGateConnectionGrid()));
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
                if (((PatchPanelLogic)container.getGate().getLogic()).getGateConnectionGrid()[rowIdx][columnIdx] != 0)
                    drawTexturedModalRect(x, y, 9, 239, width, height);
                //this.mouseDragged(mc, mouseX, mouseY);
            }
        }

        public boolean isChecked() {
            return ((PatchPanelLogic)container.getGate()).getGateConnectionGrid()[rowIdx][columnIdx] != 0;
        }


        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
                if (((PatchPanelLogic)container.getGate().getLogic()).getGateConnectionGrid()[rowIdx][columnIdx] != 0){
                    ((PatchPanelLogic)container.getGate().getLogic()).getGateConnectionGrid()[rowIdx][columnIdx] = 0;
                }else {
                    ((PatchPanelLogic)container.getGate().getLogic()).getGateConnectionGrid()[rowIdx][columnIdx] = 15;
                }
                return true;
            }

            return false;
        }
    }

}
