package me.matrix89.complexlogic.client;

import me.matrix89.complexlogic.gate.SegmentDisplayLogic;
import me.matrix89.complexlogic.gate.TextDisplayLogic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.util.vector.Vector3f;
import pl.asie.charset.lib.render.CharsetFaceBakery;
import pl.asie.charset.lib.render.model.ModelTransformer;
import pl.asie.charset.lib.render.model.SimpleBakedModel;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.render.GateDynamicRenderer;

public class TextDisplayRenderer extends GateDynamicRenderer<TextDisplayLogic> {
    public static final TextDisplayRenderer INSTANCE = new TextDisplayRenderer();
    public TextureAtlasSprite font;

    @Override
    public Class<TextDisplayLogic> getLogicClass() {
        return TextDisplayLogic.class;
    }

    @Override
    public void render(PartGate partGate, TextDisplayLogic logic, IBlockAccess iBlockAccess, double x, double y, double z, float v3, int v5, float v4, BufferBuilder bufferBuilder) {
        int codepoint = logic.codepointClient;
        if (codepoint == 0) {
            return;
        }

        int charLeft = codepoint >> 8;
        int charRight = codepoint & 0xFF;

        SimpleBakedModel model = new SimpleBakedModel();
        float height = 4 + 1/128f;
        if (charLeft != 0) {
            float cx = (charLeft & 0x0F);
            float cy = (charLeft >> 4);
            model.addQuad(null, CharsetFaceBakery.INSTANCE.makeBakedQuad(
                    new Vector3f(0.5f, height, 4), new Vector3f(7.5f, height, 12),
                    -1, new float[] {
                            cx, cy, cx+(7/8f), cy+1
                    }, font, EnumFacing.UP, TRSRTransformation.identity(), false
            ));
        }

        if (charRight != 0) {
            float cx = (charRight & 0x0F);
            float cy = (charRight >> 4);
            model.addQuad(null, CharsetFaceBakery.INSTANCE.makeBakedQuad(
                    new Vector3f(8.5f, height, 4), new Vector3f(15.5f, height, 12),
                    -1, new float[] {
                            cx, cy, cx+(7/8f), cy+1
                    }, font, EnumFacing.UP, TRSRTransformation.identity(), false
            ));
        }

        renderTransformedModel(
                model,
                partGate,
                iBlockAccess, x, y, z, bufferBuilder
        );
    }
}