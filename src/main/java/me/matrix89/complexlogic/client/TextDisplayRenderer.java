package me.matrix89.complexlogic.client;

import me.matrix89.complexlogic.gate.TextDisplayLogic;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.util.vector.Vector3f;
import pl.asie.charset.lib.render.CharsetFaceBakery;
import pl.asie.charset.lib.render.model.ModelTransformer;
import pl.asie.charset.lib.render.model.SimpleBakedModel;
import pl.asie.simplelogic.gates.logic.IGateContainer;
import pl.asie.simplelogic.gates.render.GateCustomRenderer;

public class TextDisplayRenderer extends GateCustomRenderer<TextDisplayLogic> {
    public static final TextDisplayRenderer INSTANCE = new TextDisplayRenderer();
    public TextureAtlasSprite font;
    private float[][] color = new float[][]{{1f, 0, 0, 0}};

    @Override
    public Class<TextDisplayLogic> getLogicClass() {
        return TextDisplayLogic.class;
    }

    @Override
    public boolean hasDynamic() {
        return true;
    }

    @Override
    public void renderDynamic(IGateContainer gate, TextDisplayLogic logic, IBlockAccess world, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        super.renderDynamic(gate, logic, world, x, y, z, partialTicks, destroyStage, partial, buffer);
        int codepoint = logic.codepointClient;
        if (codepoint == 0) {
            return;
        }

        int charLeft = codepoint >> 8;
        int charRight = codepoint & 0xFF;

        SimpleBakedModel model = new SimpleBakedModel();
        float height = 4 + 1 / 128f;
        if (charLeft != 0) {
            float cx = (charLeft & 0x0F);
            float cy = (charLeft >> 4);
            model.addQuad(null, CharsetFaceBakery.INSTANCE.makeBakedQuad(
                    new Vector3f(0.5f, height, 4), new Vector3f(7.5f, height, 12),
                    0, new float[]{
                            cx, cy, cx + (7 / 8f), cy + 1
                    }, font, EnumFacing.UP, TRSRTransformation.identity(), false
            ));
        }

        if (charRight != 0) {
            float cx = (charRight & 0x0F);
            float cy = (charRight >> 4);
            model.addQuad(null, CharsetFaceBakery.INSTANCE.makeBakedQuad(
                    new Vector3f(8.5f, height, 4), new Vector3f(15.5f, height, 12),
                    0, new float[]{
                            cx, cy, cx + (7 / 8f), cy + 1
                    }, font, EnumFacing.UP, TRSRTransformation.identity(), false
            ));
        }

        System.arraycopy(logic.color.getColorComponentValues(), 0, color[0], 1, 3);

        renderTransformedModel(
                model,
                ModelTransformer.IVertexTransformer.tintByIndex(color),
                gate,
                world, x, y, z, buffer
        );
    }
}