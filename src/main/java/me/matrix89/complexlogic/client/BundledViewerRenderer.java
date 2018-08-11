package me.matrix89.complexlogic.client;

import me.matrix89.complexlogic.gate.BundledViewerLogic;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import pl.asie.charset.lib.render.model.ModelTransformer;
import pl.asie.charset.lib.render.model.SimpleBakedModel;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.render.GateDynamicRenderer;

public class BundledViewerRenderer extends GateDynamicRenderer<BundledViewerLogic> {
    public static final BundledViewerRenderer INSTANCE = new BundledViewerRenderer();
    public IModel viewerLampsModel;
    public IBakedModel viewerLampsBakedModel;

    protected BundledViewerRenderer() {

    }

    @Override
    public Class<BundledViewerLogic> getLogicClass() {
        return BundledViewerLogic.class;
    }

    @Override
    public void appendModelsToItem(PartGate gate, SimpleBakedModel model) {
        if(viewerLampsBakedModel==null){
            initModels();
        }
        model.addModel(getTransformedModel(viewerLampsBakedModel, gate));
        super.appendModelsToItem(gate, model);
    }

    public void initModels(){
        viewerLampsBakedModel = viewerLampsModel.bake(
                TRSRTransformation.identity(),
                DefaultVertexFormats.BLOCK,
                ModelLoader.defaultTextureGetter()
        );
    }

    @Override
    public void render(PartGate partGate, BundledViewerLogic bundledViewerLogic, IBlockAccess iBlockAccess, double x, double y, double z, float v3, int v5, float v4, BufferBuilder bufferBuilder) {
        if (viewerLampsBakedModel == null) {
            initModels();
        }

        float[][] tints = new float[16][4];
        byte[] data = bundledViewerLogic.getOutputValueBundled(EnumFacing.NORTH);
        for (int i = 0; i < 16; i++) {
           int v = data[i];
           float[] color = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(i));
           float minMul = 0.25f;
           float mul = minMul + (v * (1f-minMul) / 15);
           tints[i][0] = 1f;
           tints[i][1] = color[0]*mul;
           tints[i][2] = color[1]*mul;
           tints[i][3] = color[2]*mul;
        }

        renderTransformedModel(
                viewerLampsBakedModel,
                ModelTransformer.IVertexTransformer.tintByIndex(
                        tints
                ),
                partGate,
                iBlockAccess, x, y, z, bufferBuilder
        );
    }
}
