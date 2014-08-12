package fr.zak.cubesedge.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class PacketPlayerHandler implements IMessageHandler<PacketPlayer, IMessage>{

	public PacketPlayerHandler(){
		
	}
	
	
	@Override
	public IMessage onMessage(PacketPlayer message, MessageContext ctx) {
		System.out.println("isSneaking is : " + message.isSneaking);
		((EntityPlayerCustom)ctx.getServerHandler().playerEntity.getExtendedProperties("Player Custom")).isSneaking = message.isSneaking;
		return null;
	}

}
