package me.matrix89.complexlogic;

import me.matrix89.complexlogic.client.BundledViewerRenderer;
import me.matrix89.complexlogic.client.SegmentDisplayRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.asie.charset.lib.utils.RenderUtils;
import pl.asie.simplelogic.gates.SimpleLogicGatesClient;

public class ProxyClient extends ProxyCommon {
    public void init() {
        super.init();
        SimpleLogicGatesClient.INSTANCE.registerDynamicRenderer(BundledViewerRenderer.INSTANCE);
        SimpleLogicGatesClient.INSTANCE.registerDynamicRenderer(SegmentDisplayRenderer.INSTANCE);
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        BundledViewerRenderer.INSTANCE.viewerLampsModel =
                RenderUtils.getModelWithTextures(
                        new ResourceLocation("complex-logic:block/viewer_lamps"),
                        event.getMap()
                );
        BundledViewerRenderer.INSTANCE.viewerLampsBakedModel = null;

        SegmentDisplayRenderer.INSTANCE.segmentModel =
                RenderUtils.getModelWithTextures(
                        new ResourceLocation("complex-logic:block/display_segments"),
                        event.getMap()
                );
        SegmentDisplayRenderer.INSTANCE.segmentBakedModel = null;
    }
}
