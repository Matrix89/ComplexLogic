package me.matrix89.complexlogic.client;

import me.matrix89.complexlogic.gate.ButtonPanelLogic;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import pl.asie.charset.lib.render.model.ModelTransformer;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.render.GateDynamicRenderer;

import javax.vecmath.Vector3f;

public class ButtonPannelRenderer extends GateDynamicRenderer<ButtonPanelLogic> {
    public static final ButtonPannelRenderer INSTANCE = new ButtonPannelRenderer();
    public IModel buttonPanelModelOn;
    public IModel buttonPanelModelOff;
    public IBakedModel buttonPanelBakedModelOn;
    public IBakedModel buttonPanelBakedModelOff;


    private static ModelTransformer.IVertexTransformer[] transformations = new ModelTransformer.IVertexTransformer[16];
    private static float[][][] colors = new float[16][2][4];

    static {

        for (int i = 0; i < 16; i++) {
            float[] color = EnumDyeColor.values()[i].getColorComponentValues();
            colors[i][0][0] = 1;
            colors[i][0][1] = color[0] * 0.3f;
            colors[i][0][2] = color[1] * 0.3f;
            colors[i][0][3] = color[2] * 0.3f;

            colors[i][1][0] = 1;
            colors[i][1][1] = color[0];
            colors[i][1][2] = color[1];
            colors[i][1][3] = color[2];

            TRSRTransformation transformation = new TRSRTransformation(new Vector3f(((i % 4) * 3f + 2.5f) / 16f, 0, ((i / 4) * 3f + 2.5f) / 16f), null, null, null);
            transformations[i] = ModelTransformer.IVertexTransformer.transform(transformation, (ItemCameraTransforms.TransformType) null);
        }
    }

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

            renderTransformedModel(
                    v ? buttonPanelBakedModelOn : buttonPanelBakedModelOff,
                    ModelTransformer.IVertexTransformer.compose(
                            transformations[i],
                            ModelTransformer.IVertexTransformer.tint(v ? colors[i][1] : colors[i][0])
                    ),
                    partGate,
                    iBlockAccess, x, y, z, bufferBuilder
            );
        }

    }
}
