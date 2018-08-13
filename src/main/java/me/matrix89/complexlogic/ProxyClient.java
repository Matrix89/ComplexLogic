package me.matrix89.complexlogic;

import me.matrix89.complexlogic.client.BundledViewerRenderer;
import me.matrix89.complexlogic.client.ButtonPannelRenderer;
import me.matrix89.complexlogic.client.SegmentDisplayRenderer;
import me.matrix89.complexlogic.client.TextDisplayRenderer;
import me.matrix89.complexlogic.lights.ColorLampBlock;
import me.matrix89.complexlogic.lights.LampBlockColorHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.asie.charset.lib.utils.RenderUtils;
import pl.asie.simplelogic.gates.SimpleLogicGatesClient;

import java.util.Map;

public class ProxyClient extends ProxyCommon {
    public void init() {
        super.init();
        SimpleLogicGatesClient.INSTANCE.registerRenderer(BundledViewerRenderer.INSTANCE);
        SimpleLogicGatesClient.INSTANCE.registerRenderer(SegmentDisplayRenderer.INSTANCE);
        SimpleLogicGatesClient.INSTANCE.registerRenderer(ButtonPannelRenderer.INSTANCE);
        SimpleLogicGatesClient.INSTANCE.registerRenderer(TextDisplayRenderer.INSTANCE);
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

        ButtonPannelRenderer.INSTANCE.buttonPanelModelOn =
                RenderUtils.getModelWithTextures(
                        new ResourceLocation("complex-logic:block/buttonindicator_on"),
                        event.getMap()
                );
        ButtonPannelRenderer.INSTANCE.buttonPanelModelOff =
                RenderUtils.getModelWithTextures(
                        new ResourceLocation("complex-logic:block/buttonindicator_off"),
                        event.getMap()
                );
        ButtonPannelRenderer.INSTANCE.invalidateModels();

        TextDisplayRenderer.INSTANCE.font = event.getMap().registerSprite(new ResourceLocation(
                "minecraft", "font/ascii"
        ));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event){
        for (Map.Entry<Block, Item> entry : ColorLampBlock.LampRegistry.entrySet()){
            ModelLoader.setCustomModelResourceLocation(entry.getValue(), 0, new ModelResourceLocation(entry.getValue().getRegistryName(), "facing=up,is_on=false"));
        }
    }

    @SubscribeEvent
    @Override
    public void registerBlocks(RegistryEvent.Register<Block> register) {
        super.registerBlocks(register);
    }

    @SubscribeEvent
    @Override
    public void registerItems(RegistryEvent.Register<Item> register) {
        super.registerItems(register);
    }

    @Override
    public void registerColor() {
        BlockColors colorsRegistry = Minecraft.getMinecraft().getBlockColors();
        ItemColors colorsItemRegistry = Minecraft.getMinecraft().getItemColors();
        colorsRegistry.registerBlockColorHandler(new LampBlockColorHandler(), ColorLampBlock.LampRegistry.keySet().toArray(new Block[0]));
        colorsItemRegistry.registerItemColorHandler(new LampBlockColorHandler(), ColorLampBlock.LampRegistry.values().toArray(new Item[0]));
    }
}
