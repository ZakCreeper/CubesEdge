package fr.zak.cubesedge.movement;

import java.awt.Color;
import java.math.BigDecimal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.IMovement;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class MovementSprint extends IMovement {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player) {
	}

	@SubscribeEvent
	public void onRenderInGame(RenderGameOverlayEvent.Post event) {
		if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
			this.drawString(
					Minecraft.getMinecraft().fontRenderer,
					"Speed : "
							+ round((MathHelper.abs((float) Minecraft
									.getMinecraft().thePlayer.motionX) + MathHelper
									.abs((float) Minecraft.getMinecraft().thePlayer.motionZ)) * 20,
									1) + " blocks/s", event.resolution
							.getScaledWidth() - 115, event.resolution
							.getScaledHeight() - 15, new Color(255, 255, 255)
							.getRGB());
		}
	}

	public void drawString(FontRenderer par1FontRenderer, String par2Str,
			int par3, int par4, int par5) {
		par1FontRenderer.drawStringWithShadow(par2Str, par3, par4, par5);
	}

	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	private float speed = 1;

	@SubscribeEvent
	public void walk(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer
				&& event.entityLiving != null) {
			if (((EntityPlayer) event.entityLiving).isSprinting()) {
				if (speed < 1.15) {
					speed += 0.005F;
				}
				if (speed < 1.20 && speed >= 1.15) {
					speed += 0.002F;
				}
				if (speed < 1.30 && speed >= 1.20) {
					speed += 0.001F;
				}
				if (speed < 1.32 && speed >= 1.30) {
					speed += 0.0002F;
				}
				if (((EntityPlayer) event.entityLiving).onGround) {
					((EntityPlayer) event.entityLiving).motionX *= speed;
					((EntityPlayer) event.entityLiving).motionZ *= speed;
				}
				if (!((EntityPlayer) event.entityLiving).onGround) {
					((EntityPlayer) event.entityLiving).motionX *= ((double) ((speed - 1) / 2)) + 0.9;
					((EntityPlayer) event.entityLiving).motionZ *= ((double) ((speed - 1) / 2)) + 0.9;
				}
			} else {
				speed = 1;
			}
		}
	}

	@Override
	public void renderTick(EntityPlayerCustom playerCustom) {
		float f1 = playerCustom.slow ? 0.03F / 4F : 0.03F;
		if (!playerCustom.animLeft && !playerCustom.animRight) {
			if (Minecraft.getMinecraft().thePlayer.isSprinting()) {
				if (playerCustom.tickRunningRight < 0.5F
						&& !playerCustom.animRunnig) {
					playerCustom.tickRunningRight += f1;
				}
				if (playerCustom.tickRunningRight >= 0.5F
						&& !playerCustom.animRunnig) {
					playerCustom.animRunnig = true;
				}
			} else {
				playerCustom.animRunnig = false;
				playerCustom.tickRunningLeft = 0;
				playerCustom.tickRunningRight = 0;
			}
			if (playerCustom.animRunnig) {
				if (playerCustom.tickRunningLeft < 0.5F
						&& !playerCustom.backLeft) {
					playerCustom.tickRunningLeft += f1;
				}
				if (playerCustom.tickRunningLeft >= 0.5F
						&& !playerCustom.backLeft) {
					playerCustom.backLeft = true;
				}
				if (playerCustom.tickRunningLeft > 0 && playerCustom.backLeft) {
					playerCustom.tickRunningLeft -= f1;
				}
				if (playerCustom.tickRunningLeft <= 0 && playerCustom.backLeft) {
					playerCustom.backLeft = false;
				}
				if (playerCustom.tickRunningRight > 0
						&& !playerCustom.backRight) {
					playerCustom.tickRunningRight -= f1;
				}
				if (playerCustom.tickRunningRight <= 0
						&& !playerCustom.backRight) {
					playerCustom.backRight = true;
				}
				if (playerCustom.tickRunningRight < 0.5F
						&& playerCustom.backRight) {
					playerCustom.tickRunningRight += f1;
				}
				if (playerCustom.tickRunningRight >= 0.5F
						&& playerCustom.backRight) {
					playerCustom.backRight = false;
				}
			}
		} else if (playerCustom.animLeft || playerCustom.animRight) {
			playerCustom.tickRunningLeft = 0;
			playerCustom.tickRunningRight = 0;
		}

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Sprint Animation";
	}
}
