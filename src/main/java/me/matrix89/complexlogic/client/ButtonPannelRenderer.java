package me.matrix89.complexlogic.client;

import me.matrix89.complexlogic.gate.ButtonPanelLogic;
import me.matrix89.complexlogic.utils.ColorUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import pl.asie.charset.lib.render.model.ModelTransformer;
import pl.asie.simplelogic.gates.SimpleLogicGates;
import pl.asie.simplelogic.gates.logic.IGateContainer;
import pl.asie.simplelogic.gates.render.GateCustomRenderer;

import javax.vecmath.Vector3f;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ButtonPannelRenderer extends GateCustomRenderer<ButtonPanelLogic> {
    public static final ButtonPannelRenderer INSTANCE = new ButtonPannelRenderer();
    public IModel buttonPanelModelOn;
    public IModel buttonPanelModelOff;
    private IBakedModel[] buttonModels;

    private static ModelTransformer.IVertexTransformer[] transformations = new ModelTransformer.IVertexTransformer[16];
    private static float[][][] colors = new float[16][2][4];

    static {
        colors[0][0] = ColorUtils.WHITE.getColor(0.2f);
        colors[1][0] = ColorUtils.ORANGE.getColor(0.2f);
        colors[2][0] = ColorUtils.MAGENTA.getColor(0.2f);
        colors[3][0] = ColorUtils.LIGHT_BLUE.getColor(0.2f);
        colors[4][0] = ColorUtils.YELLOW.getColor(0.2f);
        colors[5][0] = ColorUtils.LIME.getColor(0.2f);
        colors[6][0] = ColorUtils.PINK.getColor(0.2f);
        colors[7][0] = ColorUtils.GRAY.getColor(0.2f);
        colors[8][0] = ColorUtils.SILVER.getColor(0.2f);
        colors[9][0] = ColorUtils.CYAN.getColor(0.2f);
        colors[10][0] = ColorUtils.PURPLE.getColor(0.2f);
        colors[11][0] = ColorUtils.BLUE.getColor(0.2f);
        colors[12][0] = ColorUtils.BROWN.getColor(0.2f);
        colors[13][0] = ColorUtils.GREEN.getColor(0.2f);
        colors[14][0] = ColorUtils.RED.getColor(0.2f);
        colors[15][0] = ColorUtils.BLACK.getColor(0.1f);

        colors[0][1] = ColorUtils.WHITE.getColor(1);
        colors[1][1] = ColorUtils.ORANGE.getColor(1);
        colors[2][1] = ColorUtils.MAGENTA.getColor(1);
        colors[3][1] = ColorUtils.LIGHT_BLUE.getColor(1);
        colors[4][1] = ColorUtils.YELLOW.getColor(1);
        colors[5][1] = ColorUtils.LIME.getColor(1);
        colors[6][1] = ColorUtils.PINK.getColor(1);
        colors[7][1] = ColorUtils.GRAY.getColor(0.5f);
        colors[8][1] = ColorUtils.SILVER.getColor(0.5f);
        colors[9][1] = ColorUtils.CYAN.getColor(1);
        colors[10][1] = ColorUtils.PURPLE.getColor(1);
        colors[11][1] = ColorUtils.BLUE.getColor(1);
        colors[12][1] = ColorUtils.BROWN.getColor(1);
        colors[13][1] = ColorUtils.GREEN.getColor(1);
        colors[14][1] = ColorUtils.RED.getColor(1);
        colors[15][1] = ColorUtils.BLACK.getColor(0.3f);

        for (int i = 0; i < 16; i++) {
            TRSRTransformation transformation = new TRSRTransformation(new Vector3f(((i % 4) * 3f + 2.5f) / 16f, 0, ((i / 4) * 3f + 2.5f) / 16f), null, null, null);
            transformations[i] = ModelTransformer.IVertexTransformer.transform(transformation, (ItemCameraTransforms.TransformType) null);
        }
    }

    @Override
    public boolean hasDynamic() {
        return true;
    }

    @Override
    public void renderStatic(IGateContainer gate, ButtonPanelLogic logic, boolean isItem, Consumer<IBakedModel> modelConsumer, BiConsumer<BakedQuad, EnumFacing> quadConsumer) {
        super.renderStatic(gate, logic, isItem, modelConsumer, quadConsumer);
        if (isItem) {
            if (buttonModels == null) {
                initModels();
            }

            for (int i = 0; i < 16; i++) {
                modelConsumer.accept(getTransformedModel(buttonModels[i], gate));
            }

            super.renderStatic(gate, logic, isItem, modelConsumer, quadConsumer);
        }
    }


    public void invalidateModels() {
        buttonModels = null;
    }

    public void initModels() {
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
    public void renderDynamic(IGateContainer gate, ButtonPanelLogic logic, IBlockAccess world, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        super.renderDynamic(gate, logic, world, x, y, z, partialTicks, destroyStage, partial, buffer);
        if (buttonModels == null) {
            initModels();
        }

        byte[] data = logic.getOutputValueBundled(EnumFacing.NORTH);
        for (int i = 0; i < 16; i++) {
            boolean v = data != null && data[i] != 0;

            renderTransformedModel(
                    buttonModels[i | (v ? 16 : 0)],
                    gate,
                    world, x, y, z, buffer
            );
        }
    }

}
