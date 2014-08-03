package fr.zak.cubesedge.event;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiInGameEvent {

	@SubscribeEvent
	public void onRenderInGame(RenderGameOverlayEvent.Pre event){
		this.drawString(Minecraft.getMinecraft().fontRenderer, "Vitesse : " + (int)((SpeedEvent.speed - 1) * 100) + "km/h", event.resolution.getScaledWidth() - 100, event.resolution.getScaledHeight() - 15, new Color(255, 255, 255).getRGB());
	}
	
	public void drawString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5)
	{
		par1FontRenderer.drawStringWithShadow(par2Str, par3, par4, par5);
	}
	
}
