package fr.zak.cubesedge.event;

import java.awt.Color;
import java.math.BigDecimal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiInGameEvent {

	@SubscribeEvent
	public void onRenderInGame(RenderGameOverlayEvent.Post event){
		if(event.type == RenderGameOverlayEvent.ElementType.ALL){
			this.drawString(Minecraft.getMinecraft().fontRenderer, "Vitesse : " + round((MathHelper.abs((float)Minecraft.getMinecraft().thePlayer.motionX) + MathHelper.abs((float) Minecraft.getMinecraft().thePlayer.motionZ)) * 20, 1) + " blocks/s", event.resolution.getScaledWidth() - 115, event.resolution.getScaledHeight() - 15, new Color(255, 255, 255).getRGB());
		}
	}

	public void drawString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5)
	{
		par1FontRenderer.drawStringWithShadow(par2Str, par3, par4, par5);
	}

	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
