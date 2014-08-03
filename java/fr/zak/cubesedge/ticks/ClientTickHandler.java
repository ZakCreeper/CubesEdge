package fr.zak.cubesedge.ticks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fr.zak.cubesedge.event.KeyHandler;

public class ClientTickHandler {

	int temps = 0;
	boolean ralenti = false;

	public static boolean jump = false, animRight = false, animLeft = false;

	Minecraft minecraft = Minecraft.getMinecraft();

	//	@SideOnly(Side.CLIENT)
	//	private void changeCamera(EntityPlayer player){
	//		if(player.yOffset == 1.62F && !(player instanceof EntityPlayerMP)){
	//			System.out.println("cleh");
	//			player.yOffset = 0.2F;
	//			minecraft.renderViewEntity.yOffset = 0.2F;
	//			player.worldObj.updateEntity(player);
	//			minecraft.renderViewEntity.worldObj.updateEntity(minecraft.renderViewEntity);
	//		}
	//	}
	//	
//	@SubscribeEvent
	//	public void tickPlayer(TickEvent.PlayerTickEvent event) {
	//		boolean b = false;
	//		if(event.phase == TickEvent.Phase.START && event.side == Side.CLIENT){
	//			if(event.player.height != 0.6F){
	//				System.out.println(event.player.height);
	//				b = false;
	//			}
	//			else{
	//				b = true;
	//			}
	//			if(!b){
	//				//				if(!(Boolean)ObfuscationReflectionHelper.getPrivateValue(EntityPlayer.class, event.player, 18) && !event.player.isDead){
	//				this.forceSetSize(Entity.class, event.player, 0.6F, 0.6F);
	//				event.player.eyeHeight = 0.01F;
	////				event.player.yOffset = 0.2F;
	////				if(!event.player.worldObj.isRemote){
	//
	////					event.player.prevCameraPitch = event.player.cameraPitch;
	////					event.player.prevCameraYaw = event.player.cameraYaw;
	////					event.player.prevDistanceWalkedModified = event.player.distanceWalkedModified;
	////					event.player.prevLimbSwingAmount = event.player.limbSwingAmount;
	////					event.player.prevPosX = event.player.posX;
	////					event.player.prevPosY = event.player.posY;
	////					event.player.prevPosZ = event.player.posZ;
	////					event.player.prevRenderYawOffset = event.player.renderYawOffset;
	////					event.player.prevRotationPitch = event.player.rotationPitch;
	////					event.player.prevRotationYaw = event.player.rotationYaw;
	////					event.player.prevRotationYawHead = event.player.rotationYawHead;
	////					event.player.prevSwingProgress = event.player.swingProgress;
	////					
	////					minecraft.renderViewEntity.prevCameraPitch = minecraft.renderViewEntity.cameraPitch;
	////					minecraft.renderViewEntity.prevDistanceWalkedModified = minecraft.renderViewEntity.distanceWalkedModified;
	////					minecraft.renderViewEntity.prevLimbSwingAmount = minecraft.renderViewEntity.limbSwingAmount;
	////					minecraft.renderViewEntity.prevPosX = minecraft.renderViewEntity.posX;
	////					minecraft.renderViewEntity.prevPosY = minecraft.renderViewEntity.posY;
	////					minecraft.renderViewEntity.prevPosZ = minecraft.renderViewEntity.posZ;
	////					minecraft.renderViewEntity.prevRenderYawOffset = minecraft.renderViewEntity.renderYawOffset;
	////					minecraft.renderViewEntity.prevRotationPitch = minecraft.renderViewEntity.rotationPitch;
	////					minecraft.renderViewEntity.prevRotationYaw = minecraft.renderViewEntity.rotationYaw;
	////					minecraft.renderViewEntity.prevRotationYawHead = minecraft.renderViewEntity.rotationYawHead;
	////					minecraft.renderViewEntity.prevSwingProgress = minecraft.renderViewEntity.swingProgress;
	////				}
	//				//			        event.player.setPosition((double)((float)event.player.posX), (double)((float)event.player.posY + event.player.yOffset + 0.1F), (double)((float)event.player.posZ));
	//				//				}
	//			}
	//		}
	//	}

	@SubscribeEvent
	public void tick(TickEvent.RenderTickEvent event) {
		if(event.phase == TickEvent.Phase.END && minecraft.theWorld != null){
//			if(!minecraft.theWorld.isRemote){
//				minecraft.thePlayer.cameraPitch = c;
//				minecraft.thePlayer.cameraPitch+= 10;
//				c = minecraft.thePlayer.cameraPitch;
//			}
//			System.out.println(minecraft.thePlayer.cameraPitch);
			float f5 = event.renderTickTime - ((Float)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, minecraft.entityRenderer, "smoothCamPartialTicks"));
			float f3 = ((Float)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, minecraft.entityRenderer, "smoothCamFilterX")) * f5;
//			minecraft.thePlayer.setAngles(f3, 30);
			ralenti();

			int heading = MathHelper.floor_double((double)(minecraft.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			wallJumping(heading);
			jump(heading);

			if(minecraft.thePlayer.onGround){
				jump = false;
				animRight = false;
				animLeft = false;
			}
			if(minecraft.thePlayer.capabilities.isFlying){
				animRight = false;
				animLeft = false;
			}
		}
	}

	private void ralenti(){
		if(KeyHandler.keyPressedRalenti && !ralenti){
			ObfuscationReflectionHelper.setPrivateValue(Timer.class, ((Timer)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 15)), 5F, 0);
			ralenti = true;
		}
		if(ralenti){
			temps++;
			System.out.println(temps);
		}
		if(temps == 125 && ralenti){
			ObfuscationReflectionHelper.setPrivateValue(Timer.class, ((Timer)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 15)), 20F, 0);
			temps = 0;
			Minecraft.getMinecraft().gameSettings.mouseSensitivity = KeyHandler.defaultSensitivity;
			KeyHandler.keyPressedRalenti = false;
			ralenti = false;
		}
	}

	private void wallJumping(int heading){
		if(minecraft.thePlayer.fallDistance > 0){
			if((minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1 + 1, (int)minecraft.thePlayer.posY, (int)minecraft.thePlayer.posZ - 1) != Blocks.air || minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1 + 1, (int)minecraft.thePlayer.posY - 1, (int)minecraft.thePlayer.posZ - 1) != Blocks.air) && ((minecraft.thePlayer.movementInput.moveStrafe > 0 && heading == 0) || (minecraft.thePlayer.movementInput.moveStrafe < 0 && heading == 2))){
				minecraft.thePlayer.motionZ *= 0.95D;
				minecraft.thePlayer.motionX *= 0.95D;
				minecraft.thePlayer.motionY *= 0.75D;
				if(minecraft.thePlayer.movementInput.jump && !jump){
					animRight = false;
					animLeft = false;
					if(heading == 0){
						minecraft.thePlayer.motionZ = 0.7D;
						minecraft.thePlayer.motionX = -0.2D;
					}
					if(heading == 2){
						minecraft.thePlayer.motionZ = -0.7D;
						minecraft.thePlayer.motionX = -0.2D;
					}
					minecraft.thePlayer.motionY = 0.41999998688697815D;
					jump = true;
				}
				if(!minecraft.thePlayer.movementInput.jump){
					if(heading == 2){
						animRight = true;
					}
					if(heading == 0){
						animLeft = true;

					}
				}
			}
			if((minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1 - 1, (int)minecraft.thePlayer.posY, (int)minecraft.thePlayer.posZ - 1) != Blocks.air || minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1 - 1, (int)minecraft.thePlayer.posY - 1, (int)minecraft.thePlayer.posZ - 1) != Blocks.air) && ((minecraft.thePlayer.movementInput.moveStrafe < 0 && heading == 0) || (minecraft.thePlayer.movementInput.moveStrafe > 0 && heading == 2))){
				minecraft.thePlayer.motionZ *= 0.95D;
				minecraft.thePlayer.motionX *= 0.95D;
				minecraft.thePlayer.motionY *= 0.75D;
				if(minecraft.thePlayer.movementInput.jump && !jump){
					animRight = false;
					animLeft = false;
					if(heading == 0){
						minecraft.thePlayer.motionZ = 0.7D;
						minecraft.thePlayer.motionX = 0.2D;
					}
					if(heading == 2){
						minecraft.thePlayer.motionZ = -0.7D;
						minecraft.thePlayer.motionX = 0.2D;
					}
					minecraft.thePlayer.motionY = 0.41999998688697815D;
					jump = true;
				}
				if(!minecraft.thePlayer.movementInput.jump){
					if(heading == 2){
						animLeft = true;
					}
					if(heading == 0){
						animRight = true;

					}
				}
			}
			if((minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1, (int)minecraft.thePlayer.posY, (int)minecraft.thePlayer.posZ - 1 + 1) != Blocks.air || minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1, (int)minecraft.thePlayer.posY - 1, (int)minecraft.thePlayer.posZ - 1 + 1) != Blocks.air) && ((minecraft.thePlayer.movementInput.moveStrafe < 0 && heading == 3) || (minecraft.thePlayer.movementInput.moveStrafe > 0 && heading == 1))){
				minecraft.thePlayer.motionZ *= 0.95D;
				minecraft.thePlayer.motionX *= 0.95D;
				minecraft.thePlayer.motionY *= 0.75D;
				if(minecraft.thePlayer.movementInput.jump && !jump){
					animRight = false;
					animLeft = false;
					if(heading == 3){
						minecraft.thePlayer.motionX = 0.7D;
						minecraft.thePlayer.motionZ = -0.2D;
					}
					if(heading == 1){
						minecraft.thePlayer.motionX = -0.7D;
						minecraft.thePlayer.motionZ = -0.2D;
					}
					minecraft.thePlayer.motionY = 0.41999998688697815D;
					jump = true;
				}
				if(!minecraft.thePlayer.movementInput.jump){
					if(heading == 3){
						animRight = true;
					}
					if(heading == 1){
						animLeft = true;
					}
				}
			}
			if((minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1, (int)minecraft.thePlayer.posY, (int)minecraft.thePlayer.posZ - 1 - 1) != Blocks.air || minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1, (int)minecraft.thePlayer.posY - 1, (int)minecraft.thePlayer.posZ - 1 - 1) != Blocks.air) && ((minecraft.thePlayer.movementInput.moveStrafe > 0 && heading == 3) || (minecraft.thePlayer.movementInput.moveStrafe < 0 && heading == 1))){
				minecraft.thePlayer.motionZ *= 0.95D;
				minecraft.thePlayer.motionX *= 0.95D;
				minecraft.thePlayer.motionY *= 0.75D;
				if(minecraft.thePlayer.movementInput.jump && !jump){
					animRight = false;
					animLeft = false;
					if(heading == 3){
						minecraft.thePlayer.motionX = 0.7D;
						minecraft.thePlayer.motionZ = 0.2D;
					}
					if(heading == 1){
						minecraft.thePlayer.motionX = -0.7D;
						minecraft.thePlayer.motionZ = 0.2D;
					}
					minecraft.thePlayer.motionY = 0.41999998688697815D;
					jump = true;
				}
				if(!minecraft.thePlayer.movementInput.jump){
					if(heading == 3){
						animLeft = true;
					}
					if(heading == 1){
						animRight = true;

					}
				}
			}
		}
	}

	private void jump(int heading){
		if((minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1, (int)minecraft.thePlayer.posY - 1, (int)minecraft.thePlayer.posZ - 1 - 1) != Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1, (int)minecraft.thePlayer.posY - 1, (int)minecraft.thePlayer.posZ - 1 + 1) != Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1 - 1, (int)minecraft.thePlayer.posY - 1, (int)minecraft.thePlayer.posZ - 1) != Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1 + 1, (int)minecraft.thePlayer.posY - 1, (int)minecraft.thePlayer.posZ - 1) != Blocks.air && heading == 3)){
			if((minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1, (int)minecraft.thePlayer.posY, (int)minecraft.thePlayer.posZ - 1 - 1) == Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1, (int)minecraft.thePlayer.posY, (int)minecraft.thePlayer.posZ - 1 + 1) == Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1 - 1, (int)minecraft.thePlayer.posY, (int)minecraft.thePlayer.posZ - 1) == Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)minecraft.thePlayer.posX - 1 + 1, (int)minecraft.thePlayer.posY, (int)minecraft.thePlayer.posZ - 1) == Blocks.air && heading == 3)){
				if(ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)minecraft.thePlayer, 41)){
					minecraft.thePlayer.motionY = 0.41999998688697815D;
				}
			}
		}
	}

	public static void forceSetSize(Class clz, Entity ent, float width, float height)
	{
		try
		{
			Method m = clz.getDeclaredMethods()[3];
			m.setAccessible(true);
			m.invoke(ent, width, height);
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e) 
		{
			e.printStackTrace();
		}
	}
}
