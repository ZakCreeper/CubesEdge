package fr.zak.cubesedge.movement;

import javax.vecmath.Vector3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.MovementVar;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import fr.zak.cubesedge.packet.PacketPlayer;
import fr.zak.cubesedge.renderer.EntityRendererCustom;

@Movement("Slide")
public class MovementSlide extends MovementVar {

	private EntityRenderer renderer, prevRenderer;

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player) {
		if (!player.capabilities.isFlying) {
			if (!player.isSprinting() && playerCustom.wasSprinting) {
				if (player.isSneaking() && player.onGround
						&& !playerCustom.isRolling) {
					playerCustom.isSneaking = true;
					Util.channel
							.sendToServer(new PacketPlayer.CPacketPlayerSneak(
									true));
				}
			}
			if (playerCustom.isSneaking && player.isSneaking()) {
				if (player.isCollidedHorizontally) {
					playerCustom.sneakTime = 16;
				}
				if (playerCustom.sneakTime < 16 && player.onGround) {
					player.motionX *= (0.98F * 0.91F) + 1;
					player.motionZ *= (0.98F * 0.91F) + 1;
					playerCustom.sneakTime++;
				}
			}
			if (playerCustom.isSneaking && !player.isSneaking()) {
				playerCustom.isSneaking = false;
				Util.channel.sendToServer(new PacketPlayer.CPacketPlayerSneak(
						false));
				playerCustom.sneakTime = 0;
			}
			playerCustom.wasSprinting = player.isSprinting();
		}
	}

	@Override
	public void renderTick(EntityPlayerCustom playerCustom) {
		if (playerCustom.isSneaking
				|| (Util.isCube(Minecraft.getMinecraft().theWorld
						.getBlock(
								MathHelper.floor_double(Minecraft
										.getMinecraft().thePlayer.posX),
								MathHelper.floor_double(Minecraft
										.getMinecraft().thePlayer.posY),
								MathHelper.floor_double(Minecraft
										.getMinecraft().thePlayer.posZ))
						) && playerCustom.wasSliding)) {
			int x1 = MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.posX);
			int y1 = MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.posY);
			int z1 = MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.posZ);
			ExtendedBlockStorage ebs = ((ExtendedBlockStorage[]) ObfuscationReflectionHelper
					.getPrivateValue(Chunk.class,
							Minecraft.getMinecraft().thePlayer.worldObj
									.getChunkFromBlockCoords(x1, z1), 2))[y1 >> 4];
			if (ebs.getExtSkylightValue((x1 & 15), y1 & 15, (z1 & 15)) == 0) {
				ebs.setExtSkylightValue((x1 & 15), y1 & 15, (z1 & 15),
						playerCustom.lastLightValue);
			}
			playerCustom.lastLightValue = (byte) ebs.getExtSkylightValue(
					(x1 & 15), y1 & 15, (z1 & 15));
			KeyBinding.setKeyBindState(
					Minecraft.getMinecraft().gameSettings.keyBindSneak
							.getKeyCode(), true);
			if (renderer == null) {
				renderer = new EntityRendererCustom(Minecraft.getMinecraft());
			}
			if (Minecraft.getMinecraft().entityRenderer != renderer) {
				// be sure to store the previous renderer
				prevRenderer = Minecraft.getMinecraft().entityRenderer;
				Minecraft.getMinecraft().entityRenderer = renderer;
			}
			Util.forceSetSize(Entity.class, Minecraft.getMinecraft().thePlayer,
					0.6F, 0.6F);
		} else if (prevRenderer != null
				&& Minecraft.getMinecraft().entityRenderer != prevRenderer
				&& Minecraft.getMinecraft().theWorld
						.getBlock(MathHelper.floor_double(Minecraft
								.getMinecraft().thePlayer.posX),
								MathHelper.floor_double(Minecraft
										.getMinecraft().thePlayer.posY),
								MathHelper.floor_double(Minecraft
										.getMinecraft().thePlayer.posZ)) == Blocks.air && !playerCustom.isRolling) {
			// reset the renderer
			KeyBinding.setKeyBindState(
					Minecraft.getMinecraft().gameSettings.keyBindSneak
							.getKeyCode(), false);
			Minecraft.getMinecraft().entityRenderer = prevRenderer;
			Util.forceSetSize(Entity.class, Minecraft.getMinecraft().thePlayer,
					0.6F, 1.8F);
			playerCustom.sneakTime = 0;
			playerCustom.wasSliding = false;
		}
		if (!playerCustom.wasSliding) {
			playerCustom.wasSliding = playerCustom.isSneaking;
		}
	}

	@SubscribeEvent
	public void jump(LivingJumpEvent event) {
		if (event.entityLiving instanceof EntityPlayer
				&& ((EntityPlayerCustom) event.entityLiving
						.getExtendedProperties("Cube's Edge Player")).isSneaking) {
			event.entityLiving.motionY = 0;
		}
	}
}
