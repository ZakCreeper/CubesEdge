package fr.zak.cubesedge.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EntityPlayerCustom implements IExtendedEntityProperties {

	public byte lastLightValue;

	public boolean wasSneaking = false;

	public float prevRotationYaw = 0;
	public float prevRotationPitch = 0;
	public float rotationYaw = 0;
	public float rotationPitch = 0;

	public byte isJumping = 0;
	public byte jumpTime = 0;
	public boolean isJumpingOnWall = false;

	public boolean prevRolling = false;
	public float lastPitch = 0;
	public boolean isRolling = false;
	public boolean wasRolling = false;

	public boolean isGrabbing = false;
	public boolean grabbingDamage = false;
	public boolean[] grabbingDirections = { false, false, false, false };
	private byte[] grabbingDirectionsByte = { 0, 0, 0, 0 };

	public float tickRunningLeft = 0;
	public float tickRunningRight = 0;
	public boolean animRunnig = false;
	public boolean backLeft = false;
	public boolean backRight = false;

	public byte slowTime = 0;
	public boolean slow = false;

	public boolean isOnWall = false;
	public boolean wallJump = false;
	public boolean animRight = false;
	public boolean animLeft = false;

	public boolean isSneaking = false;
	public byte sneakTime = 0;
	public boolean wasSprinting = false;
	public boolean wasSliding = false;

	public boolean isTurning = false;
	public boolean isTurningOnWall = false;
	public byte turningTime = 0;

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setByte("lastLightValue", this.lastLightValue);
		compound.setBoolean("wasSneaking", this.wasSneaking);
		compound.setFloat("prevRotationYaw", this.prevRotationYaw);
		compound.setFloat("prevRotationPitch", this.prevRotationPitch);
		compound.setFloat("rotationYaw", this.rotationYaw);
		compound.setFloat("rotationPitch", this.rotationPitch);
		compound.setByte("isJumping", this.isJumping);
		compound.setByte("jumpTime", this.jumpTime);
		compound.setBoolean("isJumpingOnWall", this.isJumpingOnWall);
		compound.setBoolean("prevRolling", this.prevRolling);
		compound.setFloat("lastPitch", this.lastPitch);
		compound.setBoolean("isRolling", this.isRolling);
		compound.setBoolean("wasRolling", this.wasRolling);
		compound.setBoolean("isGrabbing", this.isGrabbing);
		compound.setBoolean("grabbingDamage", this.grabbingDamage);
		for (int i = 0; i < this.grabbingDirections.length; i++) {
			if (this.grabbingDirections[i]) {
				this.grabbingDirectionsByte[i] = 1;
			}
			if (!this.grabbingDirections[i]) {
				this.grabbingDirectionsByte[i] = 0;
			}
		}
		compound.setByteArray("grabbingDirections", this.grabbingDirectionsByte);
		compound.setFloat("tickRunningLeft", this.tickRunningLeft);
		compound.setFloat("tickRunningRight", this.tickRunningRight);
		compound.setBoolean("animRunning", this.animRunnig);
		compound.setBoolean("backLeft", this.backLeft);
		compound.setBoolean("backRight", this.backRight);
		compound.setByte("slowTime", this.slowTime);
		compound.setBoolean("slow", this.slow);
		compound.setBoolean("isOnWall", this.isOnWall);
		compound.setBoolean("wallJump", this.wallJump);
		compound.setBoolean("animLeft", this.animLeft);
		compound.setBoolean("animRight", this.animRight);
		compound.setBoolean("isSneaking", this.isSneaking);
		compound.setByte("sneakTime", this.sneakTime);
		compound.setBoolean("wasSprinting", this.wasSprinting);
		compound.setBoolean("wasSliding", this.wasSliding);
		compound.setBoolean("isTurning", this.isTurning);
		compound.setBoolean("isTurningOnWall", this.isTurningOnWall);
		compound.setByte("turningTime", this.turningTime);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.lastLightValue = compound.getByte("lastLightValue");
		this.wasSneaking = compound.getBoolean("wasSprinting");
		this.prevRotationYaw = compound.getFloat("prevRotationYaw");
		this.prevRotationPitch = compound.getFloat("prevRotationPitch");
		this.rotationYaw = compound.getFloat("rotationYaw");
		this.rotationPitch = compound.getFloat("rotationPitch");
		this.isJumping = compound.getByte("isJumping");
		this.jumpTime = compound.getByte("jumpTime");
		this.isJumpingOnWall = compound.getBoolean("isJumpingOnWall");
		this.prevRolling = compound.getBoolean("prevRolling");
		this.lastPitch = compound.getFloat("lastPitch");
		this.isRolling = compound.getBoolean("isRolling");
		this.wasRolling = compound.getBoolean("wasRolling");
		this.isGrabbing = compound.getBoolean("isGrabbing");
		this.grabbingDamage = compound.getBoolean("grabbingDamage");
		for (int i = 0; i < compound.getByteArray("grabbingDirections").length; i++) {
			if (compound.getByteArray("grabbingDirections")[i] == 0) {
				this.grabbingDirections[i] = false;
			}
			if (compound.getByteArray("grabbingDirections")[i] == 1) {
				this.grabbingDirections[i] = true;
			}
		}
		this.tickRunningLeft = compound.getFloat("tickRunningLeft");
		this.tickRunningRight = compound.getFloat("tickRunningRight");
		this.animRunnig = compound.getBoolean("animRunning");
		this.backLeft = compound.getBoolean("backLeft");
		this.backRight = compound.getBoolean("backRight");
		this.slowTime = compound.getByte("slowTime");
		this.slow = compound.getBoolean("slow");
		this.isOnWall = compound.getBoolean("isOnWall");
		this.wallJump = compound.getBoolean("wallJump");
		this.animLeft = compound.getBoolean("animLeft");
		this.animRight = compound.getBoolean("animRight");
		this.isSneaking = compound.getBoolean("isSneaking");
		this.sneakTime = compound.getByte("sneakTime");
		this.wasSprinting = compound.getBoolean("wasSprinting");
		this.wasSliding = compound.getBoolean("wasSliding");
		this.isTurning = compound.getBoolean("isTurning");
		this.isTurningOnWall = compound.getBoolean("isTurningOnWall");
		this.turningTime = compound.getByte("turningTime");
	}

	@Override
	public void init(Entity entity, World world) {

	}
}
