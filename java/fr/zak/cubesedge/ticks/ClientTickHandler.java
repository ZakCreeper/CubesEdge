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
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.event.KeyHandler;
import fr.zak.cubesedge.event.SpeedEvent;
import fr.zak.cubesedge.renderer.EntityRendererCustom;

public class ClientTickHandler {

	int temps = 0;
	boolean ralenti = false;

	public static boolean jump = false, animRight = false, animLeft = false;

	Minecraft minecraft = Minecraft.getMinecraft();

	private EntityRenderer renderer, prevRenderer;
	boolean backLeft = false, backRight = false;
	
	@SubscribeEvent
	public void tick(TickEvent.RenderTickEvent event) {
		//		if(event.phase == TickEvent.Phase.START && minecraft.theWorld != null){
		//			if(!Util.b){
		//				if (renderer == null) {
		//					renderer = new EntityRendererCustom(minecraft);
		//				}
		//				if (minecraft.entityRenderer != renderer) {
		//					// be sure to store the previous renderer
		//					prevRenderer = minecraft.entityRenderer;
		//					minecraft.entityRenderer = renderer;
		//				}
		//				forceSetSize(Entity.class, minecraft.thePlayer, 0.6F, 0.6F);
		//			} else if (prevRenderer != null && minecraft.entityRenderer != prevRenderer) {
		//				// reset the renderer
		//				minecraft.entityRenderer = prevRenderer;
		//			}
		//		}
		if(event.phase == TickEvent.Phase.END && minecraft.theWorld != null){
			if(minecraft.thePlayer.isSprinting()){
				Util.animRunnig = true;
			}
			else{
				Util.animRunnig = false;
				Util.tickRunningLeft = 0;
				Util.tickRunningRight = 0.085F;
			}
			if(Util.animRunnig){
				if(Util.tickRunningLeft < 0.085F && !backLeft){
					Util.tickRunningLeft += (SpeedEvent.speed - 1) * 0.05;
				}
				if(Util.tickRunningLeft >= 0.085F && !backLeft){
					backLeft = true;
				}
				if(Util.tickRunningLeft > 0 && backLeft){
					Util.tickRunningLeft -= (SpeedEvent.speed - 1) * 0.05;
				}
				if(Util.tickRunningLeft <= 0 && backLeft){
					backLeft = false;
				}
				if(Util.tickRunningRight > 0 && !backRight){
					Util.tickRunningRight -= (SpeedEvent.speed - 1) * 0.05;
				}
				if(Util.tickRunningRight <= 0 && !backRight){
					backRight = true;
				}
				if(Util.tickRunningRight < 0.085F && backRight){
					Util.tickRunningRight += (SpeedEvent.speed - 1) * 0.05;
				}
				if(Util.tickRunningRight >= 0.085F && backRight){
					backRight = false;
				}
			}
			ralenti();
			int heading = MathHelper.floor_double((double)(minecraft.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			if(!minecraft.thePlayer.capabilities.isFlying){
				roll(heading);
			}
			grab(heading);
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

	private void roll(int heading){
		if(minecraft.thePlayer.fallDistance > 4.5F && minecraft.thePlayer.fallDistance < 15F){
			if (minecraft.gameSettings.keyBindSneak.isPressed()){
				Util.prevRolling = true;
			}
		}
		if(Util.prevRolling && minecraft.thePlayer.onGround){
			Util.isRolling = true;
		}
		if(Util.isRolling){
			if(heading == 0){
				minecraft.thePlayer.motionZ = 0.5;
				minecraft.thePlayer.motionX = 0;
			}
			if(heading == 1){
				minecraft.thePlayer.motionX = -0.5;
				minecraft.thePlayer.motionZ = 0;
			}
			if(heading == 2){
				minecraft.thePlayer.motionZ = -0.5;
				minecraft.thePlayer.motionX = 0;
			}
			if(heading == 3){
				minecraft.thePlayer.motionX = 0.5;
				minecraft.thePlayer.motionZ = 0;
			}
			if(Util.rollingTime == 0){
				minecraft.thePlayer.fallDistance = 0;
			}
			if(Util.rollingTime < 27){
				float f2 = minecraft.thePlayer.rotationPitch;
				minecraft.thePlayer.rotationPitch = (float)((double)minecraft.thePlayer.rotationPitch + 10);
				minecraft.thePlayer.prevRotationPitch += minecraft.thePlayer.rotationPitch - f2;
				Util.rollingTime++;
			}
			if(Util.rollingTime == 27){
				minecraft.thePlayer.rotationPitch = 0F;
				Util.rollingTime = 0;
				Util.prevRolling = false;
				Util.isRolling = false;
			}
		}
	}

	private void wallJumping(int heading){
		if(minecraft.thePlayer.fallDistance > 0){
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) != Blocks.air || minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) != Blocks.air) && ((minecraft.thePlayer.movementInput.moveStrafe > 0 && heading == 0) || (minecraft.thePlayer.movementInput.moveStrafe < 0 && heading == 2))){
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
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) != Blocks.air || minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) != Blocks.air) && ((minecraft.thePlayer.movementInput.moveStrafe < 0 && heading == 0) || (minecraft.thePlayer.movementInput.moveStrafe > 0 && heading == 2))){
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
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ) + 1) != Blocks.air || minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ) + 1) != Blocks.air) && ((minecraft.thePlayer.movementInput.moveStrafe < 0 && heading == 3) || (minecraft.thePlayer.movementInput.moveStrafe > 0 && heading == 1))){
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
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ) - 1) != Blocks.air || minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) , (int)MathHelper.floor_double(minecraft.thePlayer.posY) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ) - 1) != Blocks.air) && ((minecraft.thePlayer.movementInput.moveStrafe > 0 && heading == 3) || (minecraft.thePlayer.movementInput.moveStrafe < 0 && heading == 1))){
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
		if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ) - 1) != Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ) + 1) != Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) != Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) != Blocks.air && heading == 3)){
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ) - 1) == Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ) + 1) == Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) == Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) == Blocks.air && heading == 3)){
				if(ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)minecraft.thePlayer, 41)){
					minecraft.thePlayer.motionY = 0.41999998688697815D;
					Util.isGrabbing = true;
				}
				else{
					Util.isGrabbing = false;
				}
			}
		}
	}

	private void grab(int heading){
		if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ) - 1) != Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ) + 1) != Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) != Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY), (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) != Blocks.air && heading == 3)){
			if((minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ) - 1) == Blocks.air && heading == 2) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX), (int)MathHelper.floor_double(minecraft.thePlayer.posY) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ) + 1) == Blocks.air && heading == 0) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) - 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) == Blocks.air && heading == 1) || (minecraft.theWorld.getBlock((int)MathHelper.floor_double(minecraft.thePlayer.posX) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posY) + 1, (int)MathHelper.floor_double(minecraft.thePlayer.posZ)) == Blocks.air && heading == 3)){
				if(!minecraft.thePlayer.isSneaking() && !(Boolean)ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)minecraft.thePlayer, 41)){
					minecraft.thePlayer.motionY = 0.0;
				}
				else if(ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)minecraft.thePlayer, 41)){
					minecraft.thePlayer.motionY = 0.41999998688697815D;
				}
			}
		}
	}

	public static void forceSetSize(Class clz, Entity ent, float width, float height)
	{
		try
		{
			Method m = clz.getDeclaredMethod(Util.obfuscation ? "func_70105_a" : "setSize", float.class, float.class);
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
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		} catch (SecurityException e) 
		{
			e.printStackTrace();
		} 
	}
}
