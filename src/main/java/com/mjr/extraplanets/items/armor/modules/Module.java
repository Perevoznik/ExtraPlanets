package com.mjr.extraplanets.items.armor.modules;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public abstract class Module {
	private String name;
	private List<ItemStack> requirements = new ArrayList<ItemStack>();
	private int slotType;
	private ItemStack icon;
	private boolean active;
	private int passivePowerCost = 0;
	private int usePowerCost = 0;

	public Module(String name, List<ItemStack> requirements, int slotType, ItemStack icon, boolean active, int passivePowerCost, int usePowerCost) {
		super();
		this.name = name;
		this.requirements = requirements;
		this.slotType = slotType;
		this.icon = icon;
		this.active = active;
		this.passivePowerCost = passivePowerCost;
		this.usePowerCost = usePowerCost;
	}

	public Module(String name, int slotType, ItemStack icon, boolean active, int passivePowerCost, int usePowerCost) {
		super();
		this.name = name;
		this.slotType = slotType;
		this.setIcon(icon);
		this.active = active;
		this.passivePowerCost = passivePowerCost;
		this.usePowerCost = usePowerCost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ItemStack> getRequirements() {
		return this.requirements;
	}

	public void setRequirements(List<ItemStack> requirements) {
		this.requirements = requirements;
	}

	public int getSlotType() {
		return slotType;
	}

	public void setSlotType(int slotType) {
		this.slotType = slotType;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return this.getName() + ".desc";
	}

	public int getPassivePowerCost() {
		return passivePowerCost;
	}

	public void setPassivePowerCost(int passivePowerCost) {
		this.passivePowerCost = passivePowerCost;
	}

	public int getUsePowerCost() {
		return usePowerCost;
	}

	public void setUsePowerCost(int usePowerCost) {
		this.usePowerCost = usePowerCost;
	}

	public abstract void tickServer(EntityPlayerMP player);

	public abstract void tickClient(EntityPlayerSP player);

	public abstract void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks);

}
