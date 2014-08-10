package fr.zak.cubesedge.event;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.Util;

public class RenderPlayerEventCustom {

	@SubscribeEvent
	public void onRenderFirstPerson(RenderPlayerEvent.Pre event){
	}
	
	@SubscribeEvent
	public void onRenderFirstPerson(RenderPlayerEvent.Post event){
		if(Util.animLeft){
			((ModelBiped)ObfuscationReflectionHelper.getPrivateValue(RenderPlayer.class, event.renderer, 1)).heldItemLeft = 50;
		}
		else{
			((ModelBiped)ObfuscationReflectionHelper.getPrivateValue(RenderPlayer.class, event.renderer, 1)).heldItemLeft = 0;
		}
//		((ModelBiped)ObfuscationReflectionHelper.getPrivateValue(RenderPlayer.class, event.renderer, 1)).bipedHead.offsetY = 1.4F;
//		((ModelBiped)ObfuscationReflectionHelper.getPrivateValue(RenderPlayer.class, event.renderer, 1)).bipedLeftLeg.postRender(0.0625F);
	}	
}
