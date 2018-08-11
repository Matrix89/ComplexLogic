package me.matrix89.complexlogic.client;

import me.matrix89.complexlogic.gate.ButtonPanelLogic;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import pl.asie.charset.lib.render.model.ModelTransformer;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.render.GateDynamicRenderer;

public class ButtonPannelRenderer extends GateDynamicRenderer<ButtonPanelLogic> {
    public static final ButtonPannelRenderer INSTANCE = new ButtonPannelRenderer();
    public IModel buttonPanelModelOn;
    public IModel buttonPanelModelOff;
    public IBakedModel buttonPanelBakedModelOn;
    public IBakedModel buttonPanelBakedModelOff;

    @Override
    public Class<ButtonPanelLogic> getLogicClass() {
        return ButtonPanelLogic.class;
    }

    @Override
    public void render(PartGate partGate, ButtonPanelLogic buttonPanelLogic, IBlockAccess iBlockAccess, double x, double y, double z, float v3, int h, float v4, BufferBuilder bufferBuilder) {
        if (buttonPanelBakedModelOn == null) {
            buttonPanelBakedModelOn = buttonPanelModelOn.bake(
                    TRSRTransformation.identity(),
                    DefaultVertexFormats.BLOCK,
                    ModelLoader.defaultTextureGetter()
            );
        }

        if (buttonPanelBakedModelOff == null) {
            buttonPanelBakedModelOff = buttonPanelModelOff.bake(
                    TRSRTransformation.identity(),
                    DefaultVertexFormats.BLOCK,
                    ModelLoader.defaultTextureGetter()
            );
        }

        byte[] data = buttonPanelLogic.value;
        for (int i = 0; i < 16; i++) {
            boolean v = data[i] != 0;
            float[] color = new float[4];
            System.arraycopy(EnumDyeColor.values()[i].getColorComponentValues(), 0, color, 1, 3);
            if (!v) {
                color[1] *= 0.3;
                color[2] *= 0.3;
                color[3] *= 0.3;
            }
            color[0] = 1;
            renderTransformedModel(
                    v ? buttonPanelBakedModelOn : buttonPanelBakedModelOff,
                    ModelTransformer.IVertexTransformer.tint(color),
                    partGate,
                    iBlockAccess, x + ((i % 4) * 3 + 2.5) / 16, y, z + ((i / 4) * 3 + 2.5) / 16, bufferBuilder
            );
        }

    }
}
