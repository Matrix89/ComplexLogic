package me.matrix89.complexlogic;

import me.matrix89.complexlogic.lights.ColorLampBlock;
import me.matrix89.complexlogic.network.PacketRegistry;
import me.matrix89.complexlogic.network.packets.PatchPanelPacket;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    public static Logger logger = LogManager.getLogger(ComplexLogic.MOD_ID);

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PROXY);
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GUIHandler());
        PacketRegistry.INSTANCE.init();
        PacketRegistry.INSTANCE.registerPacket(1, PatchPanelPacket.class, Side.SERVER);
        PacketRegistry.INSTANCE.registerPacket(2, PatchPanelPacket.class, Side.CLIENT);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init();
        PROXY.registerColor();
    }

   public static void main(String[] args) throws IOException {
       String recipeNormal = "{\n" +
               "  \"type\": \"charset:shaped\",\n" +
               "  \"group\": \"complex-logic:lamp\",\n" +
               "  \"pattern\": [\n" +
               "    \" d \",\n" +
               "    \" l \",\n" +
               "    \"   \"\n" +
               "  ],\n" +
               "  \"key\": {\n" +
               "    \"d\": {\n" +
               "      \"type\": \"forge:ore_dict\",\n" +
               "      \"ore\": \"$dye\"\n" +
               "    },\n" +
               "    \"l\": {\n" +
               "      \"item\": \"minecraft:redstone_lamp\"\n" +
               "    }\n" +
               "  },\n" +
               "  \"result\": {\n" +
               "    \"type\": \"minecraft:item\",\n" +
               "    \"item\": \"complex-logic:$gateuuid\",\n" +
               "    \"data\": 0,\n" +
               "    \"count\": 1\n" +
               "  }\n" +
               "}";
       String recipeInverted = "{\n" +
               "  \"type\": \"charset:shaped\",\n" +
               "  \"group\": \"complex-logic:lamp\",\n" +
               "  \"pattern\": [\n" +
               "    \" d \",\n" +
               "    \" l \",\n" +
               "    \" t \"\n" +
               "  ],\n" +
               "  \"key\": {\n" +
               "    \"d\": {\n" +
               "      \"type\": \"forge:ore_dict\",\n" +
               "      \"ore\": \"$dye\"\n" +
               "    },\n" +
               "    \"l\": {\n" +
               "      \"item\": \"minecraft:redstone_lamp\"\n" +
               "    },\n" +
               "    \"t\": {\n" +
               "      \"item\": \"minecraft:redstone_torch\"\n" +
               "    }\n" +
               "  },\n" +
               "  \"result\": {\n" +
               "    \"type\": \"minecraft:item\",\n" +
               "    \"item\": \"complex-logic:$gateuuid\",\n" +
               "    \"data\": 0,\n" +
               "    \"count\": 1\n" +
               "  }\n" +
               "}";

       String recipeNormalCage = "{\n" +
               "  \"type\": \"charset:shaped\",\n" +
               "  \"group\": \"complex-logic:lamp\",\n" +
               "  \"pattern\": [\n" +
               "    \" d \",\n" +
               "    \"flf\",\n" +
               "    \"   \"\n" +
               "  ],\n" +
               "  \"key\": {\n" +
               "    \"d\": {\n" +
               "      \"type\": \"forge:ore_dict\",\n" +
               "      \"ore\": \"$dye\"\n" +
               "    },\n" +
               "    \"l\": {\n" +
               "      \"item\": \"minecraft:redstone_lamp\"\n" +
               "    },\n" +
               "    \"f\": {\n" +
               "      \"item\": \"minecraft:iron_bars\"\n" +
               "    }\n" +
               "  },\n" +
               "  \"result\": {\n" +
               "    \"type\": \"minecraft:item\",\n" +
               "    \"item\": \"complex-logic:$gateuuid\",\n" +
               "    \"data\": 0,\n" +
               "    \"count\": 1\n" +
               "  }\n" +
               "}";
       String recipeInvertedCage = "{\n" +
               "  \"type\": \"charset:shaped\",\n" +
               "  \"group\": \"complex-logic:lamp\",\n" +
               "  \"pattern\": [\n" +
               "    \" d \",\n" +
               "    \"flf\",\n" +
               "    \" t \"\n" +
               "  ],\n" +
               "  \"key\": {\n" +
               "    \"d\": {\n" +
               "      \"type\": \"forge:ore_dict\",\n" +
               "      \"ore\": \"$dye\"\n" +
               "    },\n" +
               "    \"l\": {\n" +
               "      \"item\": \"minecraft:redstone_lamp\"\n" +
               "    },\n" +
               "    \"t\": {\n" +
               "      \"item\": \"minecraft:redstone_torch\"\n" +
               "    },\n" +
               "    \"f\": {\n" +
               "      \"item\": \"minecraft:iron_bars\"\n" +
               "    }\n" +
               "  },\n" +
               "  \"result\": {\n" +
               "    \"type\": \"minecraft:item\",\n" +
               "    \"item\": \"complex-logic:$gateuuid\",\n" +
               "    \"data\": 0,\n" +
               "    \"count\": 1\n" +
               "  }\n" +
               "}";

       String recipeNormalFlat = "{\n" +
               "  \"type\": \"charset:shaped\",\n" +
               "  \"group\": \"complex-logic:lamp\",\n" +
               "  \"pattern\": [\n" +
               "    \" d \",\n" +
               "    \"lll\",\n" +
               "    \"   \"\n" +
               "  ],\n" +
               "  \"key\": {\n" +
               "    \"d\": {\n" +
               "      \"type\": \"forge:ore_dict\",\n" +
               "      \"ore\": \"$dye\"\n" +
               "    },\n" +
               "    \"l\": {\n" +
               "      \"item\": \"minecraft:redstone_lamp\"\n" +
               "    }\n" +
               "  },\n" +
               "  \"result\": {\n" +
               "    \"type\": \"minecraft:item\",\n" +
               "    \"item\": \"complex-logic:$gateuuid\",\n" +
               "    \"data\": 0,\n" +
               "    \"count\": 6\n" +
               "  }\n" +
               "}";
       String recipeInvertedFlat = "{\n" +
               "  \"type\": \"charset:shaped\",\n" +
               "  \"group\": \"complex-logic:lamp\",\n" +
               "  \"pattern\": [\n" +
               "    \" d \",\n" +
               "    \"lll\",\n" +
               "    \" t \"\n" +
               "  ],\n" +
               "  \"key\": {\n" +
               "    \"d\": {\n" +
               "      \"type\": \"forge:ore_dict\",\n" +
               "      \"ore\": \"$dye\"\n" +
               "    },\n" +
               "    \"l\": {\n" +
               "      \"item\": \"minecraft:redstone_lamp\"\n" +
               "    },\n" +
               "    \"t\": {\n" +
               "      \"item\": \"minecraft:redstone_torch\"\n" +
               "    }\n" +
               "  },\n" +
               "  \"result\": {\n" +
               "    \"type\": \"minecraft:item\",\n" +
               "    \"item\": \"complex-logic:$gateuuid\",\n" +
               "    \"data\": 0,\n" +
               "    \"count\": 6\n" +
               "  }\n" +
               "}";

        Map<String, String[]> lamps = new HashMap<>();
        lamps.put("color_lamp_", new String[]{recipeNormal, recipeInverted});
        lamps.put("cage_lamp_", new String[]{recipeNormalCage, recipeInvertedCage});
        lamps.put("flat_lamp_", new String[]{recipeNormalFlat, recipeInvertedFlat});

       File blockStateDir = new File("src/main/resources/assets/complex-logic/blockstates/");
       File recipeDir = new File("src/main/resources/assets/complex-logic/recipes/lamp/");
       String blockStateTemplate =  "{\n" +
               "  \"variants\":{\n" +
               "    \"facing=up,is_on=true\": {\"model\":\"complex-logic:lamp/$lamp_$on\"},\n" +
               "    \"facing=down,is_on=true\": {\"model\":\"complex-logic:lamp/$lamp_$on\", \"x\":180},\n" +
               "    \"facing=east,is_on=true\": {\"model\":\"complex-logic:lamp/$lamp_$on\", \"x\":90, \"y\":90},\n" +
               "    \"facing=west,is_on=true\": {\"model\":\"complex-logic:lamp/$lamp_$on\", \"x\":90, \"y\":270},\n" +
               "    \"facing=north,is_on=true\": {\"model\":\"complex-logic:lamp/$lamp_$on\", \"x\":90},\n" +
               "    \"facing=south,is_on=true\": {\"model\":\"complex-logic:lamp/$lamp_$on\", \"x\":270},\n" +
               "    \"facing=up,is_on=false\": {\"model\":\"complex-logic:lamp/$lamp_$off\"},\n" +
               "    \"facing=down,is_on=false\": {\"model\":\"complex-logic:lamp/$lamp_$off\", \"x\":180},\n" +
               "    \"facing=east,is_on=false\": {\"model\":\"complex-logic:lamp/$lamp_$off\", \"x\":90, \"y\":90},\n" +
               "    \"facing=west,is_on=false\": {\"model\":\"complex-logic:lamp/$lamp_$off\", \"x\":90, \"y\":270},\n" +
               "    \"facing=north,is_on=false\": {\"model\":\"complex-logic:lamp/$lamp_$off\", \"x\":90},\n" +
               "    \"facing=south,is_on=false\": {\"model\":\"complex-logic:lamp/$lamp_$off\", \"x\":270}\n" +
               "  }\n" +
               "}";


       for (Map.Entry<String, String[]> lamp: lamps.entrySet()){
           for (EnumDyeColor c: EnumDyeColor.values()) {
                String filenameNormal = lamp.getKey() + c.getName() + ".json";
                String filenameInverted = lamp.getKey() + c.getName() + "_inverted.json";
               Path blockStateNormalPath = blockStateDir.toPath().resolve(filenameNormal);
               Path blockStateInvertedPath = blockStateDir.toPath().resolve(filenameInverted);
               Path recipeInvertedPath = recipeDir.toPath().resolve(filenameInverted);
               Path recipeNormalPath = recipeDir.toPath().resolve(filenameNormal);
               FileWriter FWblockStateNormal = new FileWriter(blockStateNormalPath.toFile());
               FileWriter FWblockStateInverted = new FileWriter(blockStateInvertedPath.toFile());
               FileWriter FWrecipeInverted = new FileWriter(recipeInvertedPath.toFile());
               FileWriter FWrecipeNormal = new FileWriter(recipeNormalPath.toFile());
               String lampUUID = lamp.getKey() + c.getName();
               String lampUUIDInverted = lampUUID + "_inverted";
               String lampON = lamp.getKey() + "on";
               String lampOFF = lamp.getKey() + "off";
               String color = "dye" + StringUtils.capitalize(c.getTranslationKey().replace("silver", "lightGray"));
               String lampBlockStateNormal = blockStateTemplate.replace("$lamp_$off", lampOFF).replace("$lamp_$on", lampON);
               String lampBlockStateInverted = blockStateTemplate.replace("$lamp_$off", lampON).replace("$lamp_$on", lampOFF);
               FWblockStateNormal.write(lampBlockStateNormal);
               FWblockStateInverted.write(lampBlockStateInverted);
               FWblockStateNormal.close();
               FWblockStateInverted.close();
               String lampRecipeNormal = lamp.getValue()[0].replace("$gateuuid", lampUUID).replace("$dye", color);
               String lampRecipeInverted = lamp.getValue()[1].replace("$gateuuid", lampUUIDInverted).replace("$dye", color);
               FWrecipeNormal.write(lampRecipeNormal);
               FWrecipeInverted.write(lampRecipeInverted);
               FWrecipeNormal.close();
               FWrecipeInverted.close();
           }
       }
   }
}
