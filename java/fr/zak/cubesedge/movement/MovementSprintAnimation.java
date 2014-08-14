package fr.zak.cubesedge.movement;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import fr.zak.cubesedge.event.SpeedEvent;

@Movement(Side.CLIENT)
public class MovementSprintAnimation {

	public void control(EntityPlayerCustom playerCustom, EntityPlayer player){
		if(player.isSprinting()){
			if(playerCustom.tickRunningRight < 0.5F && !playerCustom.beginingRunning){
				playerCustom.tickRunningRight += (SpeedEvent.speed - 1) * 0.05;
			}
			if(playerCustom.tickRunningRight >= 0.5F && !playerCustom.beginingRunning){
				playerCustom.beginingRunning = true;
			}
			if(playerCustom.beginingRunning){
				playerCustom.animRunnig = true;
			}
		}
		else{
			playerCustom.animRunnig = false;
			playerCustom.beginingRunning = false;
			playerCustom.tickRunningLeft = 0;
			playerCustom.tickRunningRight = 0;
		}
		if(playerCustom.animRunnig){
			if(playerCustom.tickRunningLeft < 0.5F && !playerCustom.backLeft){
				playerCustom.tickRunningLeft += (SpeedEvent.speed - 1) * 0.2;
			}
			if(playerCustom.tickRunningLeft >= 0.5F && !playerCustom.backLeft){
				playerCustom.backLeft = true;
			}
			if(playerCustom.tickRunningLeft > 0 && playerCustom.backLeft){
				playerCustom.tickRunningLeft -= (SpeedEvent.speed - 1) * 0.2;
			}
			if(playerCustom.tickRunningLeft <= 0 && playerCustom.backLeft){
				playerCustom.backLeft = false;
			}
			if(playerCustom.tickRunningRight > 0 && !playerCustom.backRight){
				playerCustom.tickRunningRight -= (SpeedEvent.speed - 1) * 0.2;
			}
			if(playerCustom.tickRunningRight <= 0 && !playerCustom.backRight){
				playerCustom.backRight = true;
			}
			if(playerCustom.tickRunningRight < 0.5F && playerCustom.backRight){
				playerCustom.tickRunningRight += (SpeedEvent.speed - 1) * 0.2;
			}
			if(playerCustom.tickRunningRight >= 0.5F && playerCustom.backRight){
				playerCustom.backRight = false;
			}
		}
	}
	
}
