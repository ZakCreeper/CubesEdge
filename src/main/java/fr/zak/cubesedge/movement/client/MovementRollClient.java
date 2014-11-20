package fr.zak.cubesedge.movement.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.client.event.MouseEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.IMovementClient;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import fr.zak.cubesedge.renderer.EntityRendererCustom;

public class MovementRollClient extends IMovementClient {

	private EntityRenderer renderer, prevRenderer;
	
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
		} else if (prevRenderer != null
				&& Minecraft.getMinecraft().entityRenderer != prevRenderer
				&& playerCustom.wasRolling) {
			// reset the renderer
			KeyBinding.setKeyBindState(
					Minecraft.getMinecraft().gameSettings.keyBindSneak
							.getKeyCode(), false);
			Minecraft.getMinecraft().entityRenderer = prevRenderer;
			playerCustom.wasRolling = false;
		}
		if (!playerCustom.wasRolling) {
			playerCustom.wasRolling = playerCustom.isRolling;
		}
	}
	
	@SubscribeEvent
	public void onClick(MouseEvent event) {
		if (((EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Cube's Edge Player")).isRolling) {
			event.setCanceled(true);
		}
	}

	@Override
	public String getName() {
		return "Roll Client";
	}

	@Override
	public void controlClient(EntityPlayerCustom playerCustom,
			EntityPlayer player) {
		int x = MathHelper.floor_double(player.posX);
		int y = MathHelper.floor_double(player.posY);
		int z = MathHelper.floor_double(player.posZ);
		if (!player.capabilities.isFlying && !playerCustom.isSneaking) {
			if (player.fallDistance > 3.0F && player.fallDistance < 15F) {
				if (Util.isCube(player.worldObj.getBlock(
						x,
						y - 3,
						z))
						|| Util.isCube(player.worldObj.getBlock(
								x,
								y - 4,
								z)
								)) {
					if (player instanceof EntityPlayerSP) {
						if (((EntityPlayerSP) player).movementInput.sneak) {
							playerCustom.prevRolling = true;
						}
					}
				}
			}
		}
	}

}
