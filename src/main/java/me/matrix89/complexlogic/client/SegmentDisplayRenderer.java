package me.matrix89.complexlogic.client;

import me.matrix89.complexlogic.gate.SegmentDisplayLogic;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import pl.asie.charset.lib.render.model.ModelTransformer;
import pl.asie.charset.lib.render.model.SimpleBakedModel;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.render.GateDynamicRenderer;


public class SegmentDisplayRenderer extends GateDynamicRenderer<SegmentDisplayLogic> {
    public static final SegmentDisplayRenderer INSTANCE = new SegmentDisplayRenderer();
    public IModel segmentModel;
    public IBakedModel segmentBakedModel;

    @Override
    public Class<SegmentDisplayLogic> getLogicClass() {
        return SegmentDisplayLogic.class;
    }

    @Override
    public void appendModelsToItem(PartGate gate, SimpleBakedModel model) {
        if (segmentBakedModel == null) {
            segmentBakedModel = segmentModel.bake(
                    TRSRTransformation.identity(),
                    DefaultVertexFormats.BLOCK,
                    ModelLoader.defaultTextureGetter()
            );
        }
        model.addModel(getTransformedModel(segmentBakedModel, gate));
        super.appendModelsToItem(gate, model);
    }


    @Override
    public void render(PartGate partGate, SegmentDisplayLogic segmentDisplayLogic, IBlockAccess iBlockAccess, double x, double y, double z, float v3, int v5, float v4, BufferBuilder bufferBuilder) {
        if (segmentBakedModel == null) {
            segmentBakedModel = segmentModel.bake(
                    TRSRTransformation.identity(),
                    DefaultVertexFormats.BLOCK,
                    ModelLoader.defaultTextureGetter()
            );
        }

        float[][] tints = new float[16][4];
        byte[] data = segmentDisplayLogic.value;
        float[] color = segmentDisplayLogic.color.getColorComponentValues();
        for (int i = 0; i < 16; i++) {
            int v = data[i]!=0?1:0;
            tints[i][0] = 1f;
            tints[i][1] = v*color[0];
            tints[i][2] = v*color[1];
            tints[i][3] = v*color[2];
        }

        renderTransformedModel(
                segmentBakedModel,
                ModelTransformer.IVertexTransformer.tintByIndex(
                        tints
                ),
                partGate,
                iBlockAccess, x, y, z, bufferBuilder
        );
    }
}