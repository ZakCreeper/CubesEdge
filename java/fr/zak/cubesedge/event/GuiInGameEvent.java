package fr.zak.cubesedge.event;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiInGameEvent {

	@SubscribeEvent
	public void onRenderInGame(RenderGameOverlayEvent.Post event){
		if(event.type == RenderGameOverlayEvent.ElementType.ALL){
			this.drawString(Minecraft.getMinecraft().fontRenderer, "Vitesse : " + (int)((SpeedEvent.speed - 1) * 100) + " blocks/h", event.resolution.getScaledWidth() - 110, event.resolution.getScaledHeight() - 15, new Color(255, 255, 255).getRGB());
		}
	}

	public void drawString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5)
	{
		par1FontRenderer.drawStringWithShadow(par2Str, par3, par4, par5);
	}

}
