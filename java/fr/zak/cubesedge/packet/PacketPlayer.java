package fr.zak.cubesedge.packet;

import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketPlayer{

	public static class CPacketPlayerSneak implements IMessage{

		protected boolean isSneaking;

		public CPacketPlayerSneak() {

		}

		public CPacketPlayerSneak(boolean isSneaking){
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

		public static class Handler implements IMessageHandler<CPacketPlayerSneak, IMessage>{

			public Handler(){

			}

			@Override
			public IMessage onMessage(CPacketPlayerSneak message, MessageContext ctx) {
				((EntityPlayerCustom)ctx.getServerHandler().playerEntity.getExtendedProperties("Cube's Edge Player")).isSneaking = message.isSneaking;
				if(message.isSneaking){
					Util.forceSetSize(Entity.class, ctx.getServerHandler().playerEntity, 0.6F, 0.6F);
				}
				else {
					Util.forceSetSize(Entity.class, ctx.getServerHandler().playerEntity, 0.6F, 1.8F);
				}
				return null;
			}
		}
	}

	public static class CPacketPlayerRoll implements IMessage{

		protected boolean isRolling;

		public CPacketPlayerRoll() {

		}

		public CPacketPlayerRoll(boolean isRolling){
			this.isRolling = isRolling;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			isRolling = buf.readBoolean();
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeBoolean(isRolling);
		}

		public static class Handler implements IMessageHandler<CPacketPlayerRoll, IMessage>{

			public Handler(){

			}

			@Override
			public IMessage onMessage(CPacketPlayerRoll message, MessageContext ctx) {
				((EntityPlayerCustom)ctx.getServerHandler().playerEntity.getExtendedProperties("Cube's Edge Player")).isRolling = message.isRolling;
				if(message.isRolling){
					Util.forceSetSize(Entity.class, ctx.getServerHandler().playerEntity, 0.6F, 0.6F);
				}
				else {
					Util.forceSetSize(Entity.class, ctx.getServerHandler().playerEntity, 0.6F, 1.8F);
				}
				return null;
			}
		}
	}
}
