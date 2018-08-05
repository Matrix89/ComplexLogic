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

    @SidedProxy(modId = MOD_ID, clientSide = "me.matrix89.complexlogic.ProxyClient", serverSide = "me.matrix89.complexlogic.ProxyCommon")
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
        PROXY.registerColor();
    }

//    public static void main(String[] args) {
////        System.out.println(new File(".").getAbsolutePath());
//        File dir = new File("src/main/resources/assets/complex-logic/recipes/lamp");
//        StringBuilder sb = new StringBuilder();
//        for (EnumDyeColor c: EnumDyeColor.values()) {
//            File dest = dir.toPath().resolve("color_lamp_" + c.getName() + ".json").toFile();
//            File dest2 = dir.toPath().resolve("color_lamp_" + c.getName() + "_inverted.json").toFile();
//            try {
//                FileWriter fw = new FileWriter(dest);
//                FileWriter fw2 = new FileWriter(dest2);
//                fw.write("{\n" +
//                        "  \"type\": \"charset:shaped\",\n" +
//                        "  \"group\": \"complex-logic:lamp\",\n" +
//                        "  \"pattern\": [\n" +
//                        "    \" d \",\n" +
//                        "    \" l \",\n" +
//                        "    \"   \"\n" +
//                        "  ],\n" +
//                        "  \"key\": {\n" +
//                        "    \"d\": {\n" +
//                        "      \"type\": \"forge:ore_dict\",\n" +
//                        "      \"ore\": \"dye"+ c.getTranslationKey().replace(c.getTranslationKey().substring(0,1), c.getTranslationKey().substring(0,1).toUpperCase())  +"\"\n" +
//                        "    },\n" +
//                        "    \"l\": {\n" +
//                        "      \"item\": \"minecraft:redstone_lamp\"\n" +
//                        "    }\n" +
//                        "  },\n" +
//                        "  \"result\": {\n" +
//                        "    \"type\": \"minecraft:item\",\n" +
//                        "    \"item\": \"complex-logic:color_lamp_" + c.getName()+ "\",\n" +
//                        "    \"data\": 0,\n" +
//                        "    \"count\": 1\n" +
//                        "  }\n" +
//                        "}");
//                fw.close();
//                fw2.write(
//                        "{\n" +
//                                "  \"type\": \"charset:shaped\",\n" +
//                                "  \"group\": \"complex-logic:lamp\",\n" +
//                                "  \"pattern\": [\n" +
//                                "    \" d \",\n" +
//                                "    \" l \",\n" +
//                                "    \" t \"\n" +
//                                "  ],\n" +
//                                "  \"key\": {\n" +
//                                "    \"d\": {\n" +
//                                "      \"type\": \"forge:ore_dict\",\n" +
//                                "      \"ore\": \"dye"+ c.getTranslationKey().replace(c.getTranslationKey().substring(0,1), c.getTranslationKey().substring(0,1).toUpperCase()) +"\"\n" +
//                                "    },\n" +
//                                "    \"l\": {\n" +
//                                "      \"item\": \"minecraft:redstone_lamp\"\n" +
//                                "    },\n" +
//                                "    \"t\": {\n" +
//                                "      \"item\": \"minecraft:redstone_torch\"\n" +
//                                "    }\n" +
//                                "  },\n" +
//                                "  \"result\": {\n" +
//                                "    \"type\": \"minecraft:item\",\n" +
//                                "    \"item\": \"complex-logic:color_lamp_" + c.getName()+ "\",\n" +
//                                "    \"data\": 2,\n" +
//                                "    \"count\": 1\n" +
//                                "  }\n" +
//                                "}"
//                );
//                fw2.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        System.out.println(sb.toString());
//    }
}
