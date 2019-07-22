package me.matrix89.complexlogic.client;

import me.matrix89.complexlogic.gate.SegmentDisplayLogic;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import pl.asie.charset.lib.render.model.ModelTransformer;
import pl.asie.simplelogic.gates.logic.IGateContainer;
import pl.asie.simplelogic.gates.render.GateCustomRenderer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class SegmentDisplayRenderer extends GateCustomRenderer<SegmentDisplayLogic> {
    public static final SegmentDisplayRenderer INSTANCE = new SegmentDisplayRenderer();
    public IModel segmentModel;
    public IBakedModel segmentBakedModel;

    @Override
    public Class<SegmentDisplayLogic> getLogicClass() {
        return SegmentDisplayLogic.class;
    }

    @Override
    public boolean hasDynamic() {
        return true;
    }

    @Override
    public void renderStatic(IGateContainer gate, SegmentDisplayLogic logic, boolean isItem, Consumer<IBakedModel> modelConsumer, BiConsumer<BakedQuad, EnumFacing> quadConsumer) {
        if (isItem) {
            if (segmentBakedModel == null) {
                segmentBakedModel = segmentModel.bake(
                        TRSRTransformation.identity(),
                        DefaultVertexFormats.BLOCK,
                        ModelLoader.defaultTextureGetter()
                );
            }
            try {
                modelConsumer.accept(getTransformedModel(segmentBakedModel, gate));
            } catch (ModelTransformer.TransformationFailedException e) {
                e.printStackTrace();
            }
            super.renderStatic(gate, logic, isItem, modelConsumer, quadConsumer);
        }
    }

    @Override
    public void renderDynamic(IGateContainer gate, SegmentDisplayLogic logic, IBlockAccess world, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        super.renderDynamic(gate, logic, world, x, y, z, partialTicks, destroyStage, partial, buffer);
        if (segmentBakedModel == null) {
            segmentBakedModel = segmentModel.bake(
                    TRSRTransformation.identity(),
                    DefaultVertexFormats.BLOCK,
                    ModelLoader.defaultTextureGetter()
            );
        }

        float[][] tints = new float[16][4];
        byte[] data = logic.value;
        float[] color = logic.color.getColorComponentValues();
        for (int i = 0; i < 16; i++) {
            int v = data[i] != 0 ? 1 : 0;
            tints[i][0] = 1f;
            tints[i][1] = v * color[0];
            tints[i][2] = v * color[1];
            tints[i][3] = v * color[2];
        }

        renderTransformedModel(
                segmentBakedModel,
                ModelTransformer.IVertexTransformer.tintByIndex(
                        tints
                ),
                gate,
                world, x, y, z, buffer
        );
    }

}