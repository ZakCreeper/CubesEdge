package fr.zak.cubesedge.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.IMovement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import fr.zak.cubesedge.packet.PacketPlayer.CPacketPlayerAction;
import fr.zak.cubesedge.renderer.EntityRendererCustom;

public class MovementRoll extends IMovement {

	private EntityRenderer renderer, prevRenderer;

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, Side side) {
		if (!player.capabilities.isFlying && !playerCustom.isSneaking) {
			if (player.fallDistance > 3.0F && player.fallDistance < 15F) {
				if (Util.isCube(player.worldObj.getBlock(
						MathHelper.floor_double(player.posX),
						MathHelper.floor_double(player.posY) - 3,
						MathHelper.floor_double(player.posZ)))
						|| Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX),
								MathHelper.floor_double(player.posY) - 4,
								MathHelper.floor_double(player.posZ))
								)) {
					if (player instanceof EntityPlayerSP) {
						if (((EntityPlayerSP) player).movementInput.sneak) {
							playerCustom.prevRolling = true;
						}
					}
				}
			}
			if (playerCustom.prevRolling && player.onGround) {
				playerCustom.isRolling = true;
				Util.channel.sendToServer(new CPacketPlayerAction(
						2));
			}
			if (playerCustom.isRolling) {
				player.setSprinting(false);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindForward
								.getKeyCode(), true);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindLeft
								.getKeyCode(), false);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindRight
								.getKeyCode(), false);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindBack
								.getKeyCode(), false);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindSneak
								.getKeyCode(), false);
				player.motionZ *= 1.5;
				player.motionX *= 1.5;
				if (playerCustom.rotationPitch == 0) {
					playerCustom.rotationPitch = player.rotationPitch;
				}
				playerCustom.rotationPitch += 30;
				float f2 = player.rotationPitch;
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
					player.rotationPitch = (float) ((double) playerCustom.rotationPitch);
					player.prevRotationPitch += playerCustom.rotationPitch - f2;
				}
				if (playerCustom.rotationPitch >= 360) {
					playerCustom.prevRolling = false;
					playerCustom.isRolling = false;
					playerCustom.rotationPitch = 0;
					Util.channel
							.sendToServer(new CPacketPlayerAction(
									3));
					KeyBinding
							.setKeyBindState(
									Minecraft.getMinecraft().gameSettings.keyBindForward
											.getKeyCode(), false);
				}
			}
		}
	}

	@Override
	public void renderTick(EntityPlayerCustom playerCustom) {
		if (playerCustom.isRolling
				|| (Util.isCube(Minecraft.getMinecraft().theWorld
						.getBlock(
								MathHelper.floor_double(Minecraft
										.getMinecraft().thePlayer.posX),
								MathHelper.floor_double(Minecraft
										.getMinecraft().thePlayer.posY),
								MathHelper.floor_double(Minecraft
										.getMinecraft().thePlayer.posZ))
						) && playerCustom.wasRolling)) {
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
				&& playerCustom.wasRolling) {
			// reset the renderer
			KeyBinding.setKeyBindState(
					Minecraft.getMinecraft().gameSettings.keyBindSneak
							.getKeyCode(), false);
			Minecraft.getMinecraft().entityRenderer = prevRenderer;
			playerCustom.wasRolling = false;
			Util.forceSetSize(Entity.class, Minecraft.getMinecraft().thePlayer,
					0.6F, 1.8F);
		}
		if (!playerCustom.wasRolling) {
			playerCustom.wasRolling = playerCustom.isRolling;
		}
	}

	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer
				&& ((EntityPlayerCustom) event.entityLiving
						.getExtendedProperties("Cube's Edge Player")).isRolling) {
			event.distance = 0;
		}
	}

	@SubscribeEvent
	public void jump(LivingJumpEvent event) {
		if (event.entityLiving instanceof EntityPlayer
				&& ((EntityPlayerCustom) event.entityLiving
						.getExtendedProperties("Cube's Edge Player")).isRolling) {
			event.entityLiving.motionY = 0;
		}
	}

	@SubscribeEvent
	public void onClick(MouseEvent event) {
		if (((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
				.getExtendedProperties("Cube's Edge Player")).isRolling) {
			event.setCanceled(true);
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Roll";
	}
}
