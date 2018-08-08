package me.matrix89.complexlogic;

import me.matrix89.complexlogic.gate.*;
import me.matrix89.complexlogic.lights.ColorLampBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.addon.GateRegisterEvent;

import java.util.Map;

public class ProxyCommon {
    public void init() {

    }

    @SubscribeEvent
    public void onRegisterGates(GateRegisterEvent event) {
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "adder"), AdderLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "viewer"), BundledViewerLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "and"), AndLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "xor"), XorLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "memory"), MemoryLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "display"), SegmentDisplayLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "counter"), CounterLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "shifter"), ShifterLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "rtc"), RTCLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "division"), DivisionLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "modulo"), ModuloLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "multiplication"), MultiplicationLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "random"), RandomLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "bintobcd"), BinToBCDLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "globalor"), GlobalORLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "bitreorderer"), BitReordererLogic.class);
        event.registerLogicType(new ResourceLocation(ComplexLogic.MOD_ID, "subtractor"), SubtractorLogic.class);

        event.registerPartForCreativeTab(new PartGate(new AdderLogic()));
        event.registerPartForCreativeTab(new PartGate(new BundledViewerLogic()));
        event.registerPartForCreativeTab(new PartGate(new AndLogic()));
        event.registerPartForCreativeTab(new PartGate(new XorLogic()));
        event.registerPartForCreativeTab(new PartGate(new MemoryLogic()));
        event.registerPartForCreativeTab(new PartGate(new SegmentDisplayLogic()));
        event.registerPartForCreativeTab(new PartGate(new CounterLogic()));
        event.registerPartForCreativeTab(new PartGate(new ShifterLogic()));
        event.registerPartForCreativeTab(new PartGate(new RTCLogic()));
        event.registerPartForCreativeTab(new PartGate(new DivisionLogic()));
        event.registerPartForCreativeTab(new PartGate(new ModuloLogic()));
        event.registerPartForCreativeTab(new PartGate(new MultiplicationLogic()));
        event.registerPartForCreativeTab(new PartGate(new RandomLogic()));
        event.registerPartForCreativeTab(new PartGate(new BinToBCDLogic()));
        event.registerPartForCreativeTab(new PartGate(new GlobalORLogic()));
        event.registerPartForCreativeTab(new PartGate(new BitReordererLogic()));
        event.registerPartForCreativeTab(new PartGate(new SubtractorLogic()));
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {

    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> register) {
        for (Map.Entry<Block, Item> entry : ColorLampBlock.LampRegistry.entrySet()) {
            register.getRegistry().register(entry.getKey());
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> register) {
        for (Map.Entry<Block, Item> entry : ColorLampBlock.LampRegistry.entrySet()) {
            register.getRegistry().register(entry.getValue());
        }
    }


    public void registerColor() {

    }

}
