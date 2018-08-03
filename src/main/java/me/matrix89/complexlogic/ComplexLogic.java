package me.matrix89.complexlogic;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = ComplexLogic.MOD_ID,
        name = ComplexLogic.MOD_NAME,
        version = ComplexLogic.VERSION,
        dependencies = "before:charset"
)
public class ComplexLogic {

    public static final String MOD_ID = "complex-logic";
    public static final String MOD_NAME = "Complex Logic";
    public static final String VERSION = "1.0-SNAPSHOT";

    @SidedProxy(modId=MOD_ID, clientSide = "me.matrix89.complexlogic.ProxyClient", serverSide = "me.matrix89.complexlogic.ProxyCommon")
    public static ProxyCommon PROXY;

    @Mod.Instance(MOD_ID)
    public static ComplexLogic INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PROXY);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init();
    }
}
