package fr.zak.cubesedge.packet;

import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketPlayer {

	public static class CPacketPlayerAction implements IMessage {

		protected int actionId;

		public CPacketPlayerAction() {

		}

		public CPacketPlayerAction(int id) {
			this.actionId = id;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			this.actionId = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(this.actionId);
		}

		public static class Handler implements
		IMessageHandler<CPacketPlayerAction, IMessage> {

			public Handler() {

			}

			@Override
			public IMessage onMessage(CPacketPlayerAction message,
					MessageContext ctx) {
				EntityPlayerCustom playerEntity = ((EntityPlayerCustom) ctx.getServerHandler().playerEntity.getExtendedProperties("Cube's Edge Player"));
				if(message.actionId == 0){
					playerEntity.isGrabbing = true;
					System.out.println("2 : " + playerEntity.isGrabbing);
				}
				if(message.actionId == 1){
					playerEntity.isGrabbing = false;
					System.out.println("3 : " + playerEntity.isGrabbing);
				}
				if(message.actionId == 2){
					playerEntity.isRolling = true;
					Util.forceSetSize(Entity.class,
							ctx.getServerHandler().playerEntity, 0.6F, 0.6F);
				}
				if(message.actionId == 3){
					playerEntity.isRolling = false;
					Util.forceSetSize(Entity.class,
							ctx.getServerHandler().playerEntity, 0.6F, 1.8F);
				}
				if(message.actionId == 4){
					playerEntity.isSneaking = true;
					Util.forceSetSize(Entity.class,
							ctx.getServerHandler().playerEntity, 0.6F, 0.6F);
				}
				if(message.actionId == 5){
					playerEntity.isSneaking = false;
					Util.forceSetSize(Entity.class,
							ctx.getServerHandler().playerEntity, 0.6F, 1.8F);
				}
				
				return null;
			}
		}
	}
}
