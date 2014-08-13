package fr.zak.cubesedge.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderPlayerEventCustom {

	@SubscribeEvent
	public void onRenderFirstPerson(RenderPlayerEvent.Pre event){
	}

	@SubscribeEvent
	public void onRenderFirstPerson(RenderPlayerEvent.Post event){
	}	
}
