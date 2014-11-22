package fr.zak.cubesedge.movement.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import fr.zak.cubesedge.IMovementClient;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import fr.zak.cubesedge.renderer.EntityRendererCustom;

public class MovementSlideClient extends IMovementClient{

	private EntityRenderer renderer, prevRenderer;
	ExtendedBlockStorage ebs;
	
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
			ebs = ((ExtendedBlockStorage[]) ObfuscationReflectionHelper
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
			playerCustom.sneakTime = 0;
			playerCustom.wasSliding = false;
		}
		if (!playerCustom.wasSliding) {
			playerCustom.wasSliding = playerCustom.isSneaking;
		}
	}

	@Override
	public String getName() {
		return "Slide Client";
	}

	@Override
	public void controlClient(EntityPlayerCustom playerCustom,
			EntityPlayer player) {
		
	}

}
