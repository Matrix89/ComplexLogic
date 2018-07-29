package me.matrix89.complexlogic;

import me.matrix89.complexlogic.gate.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import pl.asie.simplelogic.gates.ItemGate;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.SimpleLogicGates;
import pl.asie.simplelogic.gates.logic.GateLogic;

@Mod(
        modid = ComplexLogic.MOD_ID,
        name = ComplexLogic.MOD_NAME,
        version = ComplexLogic.VERSION,
        dependencies = "after:charset"
)
public class ComplexLogic {

    public static final String MOD_ID = "complex-logic";
    public static final String MOD_NAME = "Complex Logic";
    public static final String VERSION = "1.0-SNAPSHOT";

    @SidedProxy(modId=MOD_ID, clientSide = "me.matrix89.complexlogic.ProxyClient", serverSide = "me.matrix89.complexlogic.ProxyCommon")
    public static ProxyCommon PROXY;

    @Mod.Instance(MOD_ID)
    public static ComplexLogic INSTANCE;

    private static SimpleLogicGates SIMPLE_LOGIC_GATES = new SimpleLogicGates();

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        registerGate(new ResourceLocation(MOD_ID, "adder"), AdderLogic.class);
        registerGate(new ResourceLocation(MOD_ID, "viewer"), BundledViewerLogic.class);
        registerGate(new ResourceLocation(MOD_ID, "and"), AndLogic.class);
        registerGate(new ResourceLocation(MOD_ID, "xor"), XorLogic.class);
        registerGate(new ResourceLocation(MOD_ID, "memory"), MemoryLogic.class);
        registerGate(new ResourceLocation(MOD_ID, "display"), SegmentDisplayLogic.class);
        registerGate(new ResourceLocation(MOD_ID, "counter"), CounterLogic.class);

        MinecraftForge.EVENT_BUS.register(PROXY);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SIMPLE_LOGIC_GATES.registerGateStack(ItemGate.getStack(new PartGate(new AdderLogic())));
        SIMPLE_LOGIC_GATES.registerGateStack(ItemGate.getStack(new PartGate(new BundledViewerLogic())));
        SIMPLE_LOGIC_GATES.registerGateStack(ItemGate.getStack(new PartGate(new AndLogic())));
        SIMPLE_LOGIC_GATES.registerGateStack(ItemGate.getStack(new PartGate(new XorLogic())));
        SIMPLE_LOGIC_GATES.registerGateStack(ItemGate.getStack(new PartGate(new MemoryLogic())));
        SIMPLE_LOGIC_GATES.registerGateStack(ItemGate.getStack(new PartGate(new SegmentDisplayLogic())));
        SIMPLE_LOGIC_GATES.registerGateStack(ItemGate.getStack(new PartGate(new CounterLogic())));

        PROXY.init();
    }

    private void registerGate(ResourceLocation name, Class<? extends GateLogic> clazz) {
        SIMPLE_LOGIC_GATES.registerGate(name, clazz, new ResourceLocation(name.getNamespace(), "gatedefs/" + name.getPath() + ".json"),
                "tile." + name.getNamespace() + ".gate." + name.getPath());
    }
}
