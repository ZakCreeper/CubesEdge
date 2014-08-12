package fr.zak.cubesedge.packet;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PacketPlayer implements IMessage{

	public PacketPlayer(){
		
	}
	
	boolean isSneaking;
	
	public PacketPlayer(boolean isSneaking){
		this.isSneaking = isSneaking;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		isSneaking = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(isSneaking);
	}

}
