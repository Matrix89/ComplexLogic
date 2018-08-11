package me.matrix89.complexlogic.network;

import me.matrix89.complexlogic.ComplexLogic;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashSet;
import java.util.Set;

public class PacketRegistry {
    public static PacketRegistry INSTANCE = new PacketRegistry();
    public  SimpleNetworkWrapper packetHandler;
    private Set<RPacket> packetsID = new HashSet<RPacket>();
    public void init(){
        packetHandler = new SimpleNetworkWrapper(ComplexLogic.MOD_ID);
    }

    public <T extends Packet<T,U>, U extends IMessage> void registerPacket(int id, Class<T> clazz, Side handleOn){
        if(!packetsID.contains(new RPacket(id, handleOn))){
            packetsID.add(new RPacket(id, handleOn));
            packetHandler.registerMessage(clazz, clazz, id, handleOn);
        }else{
            ComplexLogic.logger.error("Packet ID:"+ id +"try overwritting other packet!");
        }
    }

    private static class RPacket{
        @SuppressWarnings("unused")
        final int id;
        @SuppressWarnings("unused")
        final Side side;
        public RPacket(int id, Side side) {
            this.side = side;
            this.id = id;
        }
    }
}
