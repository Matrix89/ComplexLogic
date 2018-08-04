package me.matrix89.complexlogic;

import me.matrix89.complexlogic.gate.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.addon.GateRegisterEvent;

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
    }
}
