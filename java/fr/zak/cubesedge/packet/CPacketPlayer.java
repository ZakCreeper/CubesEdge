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

public class CPacketPlayer{

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

		public static class CPacketPlayerHandler implements IMessageHandler<CPacketPlayerSneak, IMessage>{

			public CPacketPlayerHandler(){

			}

			@Override
			public IMessage onMessage(CPacketPlayerSneak message, MessageContext ctx) {
				System.out.println("Sneaking is : " + message.isSneaking);
				((EntityPlayerCustom)ctx.getServerHandler().playerEntity.getExtendedProperties("Player Custom")).isSneaking = message.isSneaking;
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

	public static class CPacketPlayerBounds implements IMessage{

		public float height, width;
		public int entityId;

		public CPacketPlayerBounds() {

		}

		public CPacketPlayerBounds(float height, float width, int id) {
			this.height = height;
			this.width = width;
			this.entityId = id;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			this.height = buf.readFloat();
			this.width = buf.readFloat();
			this.entityId = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeFloat(this.height);
			buf.writeFloat(this.width);
			buf.writeInt(this.entityId);
		}

		public static class CPacketPlayerHandler implements IMessageHandler<CPacketPlayerBounds, IMessage>{

			public CPacketPlayerHandler(){

			}

			@Override
			public IMessage onMessage(CPacketPlayerBounds message, MessageContext ctx) {
				
				return null;
			}
		}
	}
}
