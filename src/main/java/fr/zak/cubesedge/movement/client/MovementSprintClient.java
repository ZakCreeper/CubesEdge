package fr.zak.cubesedge.movement.client;

import java.awt.Color;
import java.math.BigDecimal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.MovementClient;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class MovementSprintClient extends MovementClient {

	private static long lastTime = -1;
	public static double speed = 0; //actuellement en 2d, enleverles commentaires pour la vitesse en 3d
	private static double prevPosX;
	//private static double prevPosY;
	private static double prevPosZ;
	private Minecraft mc;

	@SubscribeEvent
	public void onRenderInGame(RenderGameOverlayEvent.Post event) {
		if (mc == null)
			mc = Minecraft.getMinecraft();
		calculateSpeed();
		if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
			this.drawString(
				mc.fontRendererObj,
				"Speed : ",
				event.resolution.getScaledWidth() - 115,
				event.resolution.getScaledHeight() - 15,
				new Color(255, 255, 255).getRGB()
			);
			
			this.drawString(
				mc.fontRendererObj,
				speedToStr(),
				event.resolution.getScaledWidth() - 45 - mc.fontRendererObj.getStringWidth(speedToStr()),
				event.resolution.getScaledHeight() - 15,
				new Color(255, 255, 255).getRGB()
			);
			
			this.drawString(
				mc.fontRendererObj,
				"kb/h",
				event.resolution.getScaledWidth() - 40,
				event.resolution.getScaledHeight() - 15,
				new Color(255, 255, 255).getRGB()
			);
		}
	}

	public void drawString(FontRenderer par1FontRenderer, String par2Str,
			int par3, int par4, int par5) {
		par1FontRenderer.drawString(par2Str, par3, par4, par5);
	}

	public static String speedToStr()
	{
		if (speed <= 0)
			return "0";
		String str = "" + round((float)speed, 2);
		String[] tab = str.split("\\.");
		if (tab.length > 1)
		{
			while (tab[1].length() < 2)
			{
				str += "0";
				tab = str.split("\\.");
			}
		}
		return str;
	}

	private void calculateSpeed()
	{
		double dec = !mc.thePlayer.onGround ? 0 : 0.0784000015258789D;
		speed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
		speed = Math.sqrt(speed * speed + (mc.thePlayer.motionY + dec) * (mc.thePlayer.motionY + dec)) * 36D;
	}

	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
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
		return "Sprint Client";
	}

	@Override
	public void controlClient(EntityPlayerCustom playerCustom,
			EntityPlayer player) {

	}

}
