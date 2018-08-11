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
import pl.asie.charset.lib.render.model.SimpleBakedModel;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.SimpleLogicGates;
import pl.asie.simplelogic.gates.render.GateDynamicRenderer;

import javax.vecmath.Vector3f;

public class ButtonPannelRenderer extends GateDynamicRenderer<ButtonPanelLogic> {
    public static final ButtonPannelRenderer INSTANCE = new ButtonPannelRenderer();
    public IModel buttonPanelModelOn;
    public IModel buttonPanelModelOff;
    private IBakedModel[] buttonModels;

    private static ModelTransformer.IVertexTransformer[] transformations = new ModelTransformer.IVertexTransformer[16];
    private static float[][][] colors = new float[16][2][4];

    @Override
    public void appendModelsToItem(PartGate gate, SimpleBakedModel model) {
        if(buttonModels==null){
            initModels();
        }

        for (int i = 0; i < 16; i++) {
            model.addModel(getTransformedModel(buttonModels[i], gate));
        }

        super.appendModelsToItem(gate, model);
    }

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

    public void invalidateModels() {
        buttonModels = null;
    }
    public void initModels(){
        IBakedModel buttonPanelBakedModelOn = buttonPanelModelOn.bake(
                TRSRTransformation.identity(),
                DefaultVertexFormats.BLOCK,
                ModelLoader.defaultTextureGetter()
        );

        IBakedModel buttonPanelBakedModelOff = buttonPanelModelOff.bake(
                TRSRTransformation.identity(),
                DefaultVertexFormats.BLOCK,
                ModelLoader.defaultTextureGetter()
        );

        buttonModels = new IBakedModel[32];
        for (int i = 0; i < buttonModels.length; i++) {
            int color = i & 15;
            boolean v = (i & 16) != 0;

            buttonModels[i] = ModelTransformer.transform(
                    v ? buttonPanelBakedModelOn : buttonPanelBakedModelOff,
                    SimpleLogicGates.blockGate.getDefaultState(),
                    0L,
                    ModelTransformer.IVertexTransformer.compose(
                            transformations[color],
                            ModelTransformer.IVertexTransformer.tint(v ? colors[color][1] : colors[color][0])
                    )
            );
        }
    }

    @Override
    public Class<ButtonPanelLogic> getLogicClass() {
        return ButtonPanelLogic.class;
    }

    @Override
    public void render(PartGate partGate, ButtonPanelLogic buttonPanelLogic, IBlockAccess iBlockAccess, double x, double y, double z, float v3, int h, float v4, BufferBuilder bufferBuilder) {
        if (buttonModels == null) {
            initModels();
        }

        byte[] data = buttonPanelLogic.value;
        for (int i = 0; i < 16; i++) {
            boolean v = data[i] != 0;

            renderTransformedModel(
                    buttonModels[i | (v ? 16 : 0)],
                    partGate,
                    iBlockAccess, x, y, z, bufferBuilder
            );
        }

    }
}
