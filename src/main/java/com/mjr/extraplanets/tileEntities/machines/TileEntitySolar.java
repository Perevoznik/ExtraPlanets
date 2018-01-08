package com.mjr.extraplanets.tileEntities.machines;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mjr.extraplanets.Constants;
import com.mjr.extraplanets.blocks.machines.BlockSolar;
import com.mjr.mjrlegendslib.util.TranslateUtilities;

public class TileEntitySolar extends TileBaseUniversalElectricalSource implements IMultiBlock, IDisableableMachine, IInventory, ISidedInventory, IConnector {
	@NetworkedField(targetSide = Side.CLIENT)
	public int solarStrength = 0;
	public float targetAngle;
	public float currentAngle;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean disabled = false;
	@NetworkedField(targetSide = Side.CLIENT)
	public int disableCooldown = 0;
	private ItemStack[] containingItems = new ItemStack[1];
	public static final int MAX_GENERATE_WATTS = 1000;
	@NetworkedField(targetSide = Side.CLIENT)
	public int generateWatts = 0;

	@NetworkedField(targetSide = Side.CLIENT)
	public boolean isDaylight = false;

	private boolean initialised = false;
	private boolean initialisedMulti = false;

	public TileEntitySolar() {
		this(2);
	}

	/*
	 * @param tier: 1 = Basic Solar 2 = Advanced Solar
	 */
	public TileEntitySolar(int tier) {
		this.storage.setMaxExtract(TileEntitySolar.MAX_GENERATE_WATTS);
		this.storage.setMaxReceive(TileEntitySolar.MAX_GENERATE_WATTS);
		if (tier == 1) {
			this.storage.setCapacity(60000);
		} else if (tier == 2) {
			this.storage.setCapacity(90000);
		}
		this.setTierGC(tier);
		this.initialised = true;
	}

	@Override
	public void update() {
		if (!this.initialised) {
			int metadata = this.getBlockMetadata();
			if (metadata >= BlockSolar.HYBRID_METADATA) {
				this.storage.setCapacity(30000);
				this.setTierGC(2);
			}
			this.initialised = true;
		}

		if (!this.initialisedMulti) {
			this.initialisedMulti = this.initialiseMultiTiles(this.getPos(), this.worldObj);
		}

		this.receiveEnergyGC(null, this.generateWatts, false);

		super.update();

		if (!this.worldObj.isRemote) {
			if (this.worldObj.isDaytime())
				isDaylight = true;
			else
				isDaylight = false;
			this.recharge(this.containingItems[0]);

			if (this.disableCooldown > 0) {
				this.disableCooldown--;
			}

			if (!this.getDisabled(0) && this.ticks % 20 == 0) {
				this.solarStrength = 0;

				if (this.worldObj.provider instanceof IGalacticraftWorldProvider || !this.worldObj.isRaining() && !this.worldObj.isThundering()) {
					if (this.worldObj.isDaytime()) {
						double distance = 100.0D;
						double sinA = -Math.sin((this.currentAngle - 77.5D) * Constants.RADIANS_TO_DEGREES_D);
						double cosA = Math.abs(Math.cos((this.currentAngle - 77.5D) * Constants.RADIANS_TO_DEGREES_D));

						for (int x = -1; x <= 1; x++) {
							for (int z = -1; z <= 1; z++) {
								if (this.tierGC == 1) {
									if (this.worldObj.canBlockSeeSky(this.getPos().add(x, 2, z))) {
										boolean valid = true;

										for (int y = this.getPos().getY() + 3; y < 256; y++) {
											Block block = this.worldObj.getBlockState(new BlockPos(this.getPos().getX() + x, y, this.getPos().getZ() + z)).getBlock();

											if (block.isOpaqueCube()) {
												valid = false;
												break;
											}
										}

										if (valid) {
											this.solarStrength++;
										}
									}
								} else {
									boolean valid = true;

									BlockVec3 blockVec = new BlockVec3(this).translate(x, 3, z);
									for (double d = 0.0D; d < distance; d++) {
										BlockVec3 blockAt = blockVec.clone().translate((int) (d * sinA), (int) (d * cosA), 0);
										Block block = blockAt.getBlock(this.worldObj);

										if (block.isOpaqueCube()) {
											valid = false;
											break;
										}
									}

									if (valid) {
										this.solarStrength++;
									}
								}
							}
						}
						if (this.tierGC == 2)
							this.solarStrength = this.solarStrength * 2;

					} else {
						if (this.tierGC == 1) {
							this.solarStrength = 30;
						} else {
							this.solarStrength = 60;
						}
					}
				}
			}
		}

		float angle = this.worldObj.getCelestialAngle(1.0F) - 0.7845194F < 0 ? 1.0F - 0.7845194F : -0.7845194F;
		float celestialAngle = (this.worldObj.getCelestialAngle(1.0F) + angle) * 360.0F;

		celestialAngle %= 360;

		if (this.tierGC == 1) {
			if (celestialAngle > 30 && celestialAngle < 150) {
				float difference = this.targetAngle - celestialAngle;
				this.targetAngle -= difference / 20.0F;
			} else if (this.worldObj.isRaining() || this.worldObj.isThundering()) {
				this.targetAngle = 77.5F + 180.0F;
			} else if (celestialAngle < 50 || !this.worldObj.isDaytime()) {
				this.targetAngle = 50;
			} else if (celestialAngle > 150) {
				this.targetAngle = 50;
			} else if (celestialAngle > 160) {
				this.targetAngle = 77;
			}
		} else {
			if (celestialAngle > 30 && celestialAngle < 150) {
				float difference = this.targetAngle - celestialAngle;
				this.targetAngle -= difference / 20.0F;
			} else if (this.worldObj.isRaining() || this.worldObj.isThundering()) {
				this.targetAngle = 77.5F + 180.0F;
			} else if (celestialAngle < 50 || !this.worldObj.isDaytime()) {
				this.targetAngle = 50;
			} else if (celestialAngle > 150) {
				this.targetAngle = 50;
			} else if (celestialAngle > 160) {
				this.targetAngle = 77;
			}
		}

		float difference = this.targetAngle - this.currentAngle;

		this.currentAngle += difference / 20.0F;
		if (!this.worldObj.isRemote) {
			if (this.getGenerate() > 0.0F) {
				this.generateWatts = (Math.min(Math.max(this.getGenerate(), 0), TileEntitySolar.MAX_GENERATE_WATTS));
			} else {
				this.generateWatts = 0;
			}
		}

		this.produce();
	}

	protected boolean initialiseMultiTiles(BlockPos pos, World world) {
		// Client can create its own fake blocks and tiles - no need for networking in 1.8+
		if (world.isRemote)
			this.onCreate(world, pos);

		List<BlockPos> positions = new ArrayList<BlockPos>();
		this.getPositions(pos, positions);
		boolean result = true;
		for (BlockPos vecToAdd : positions) {
			TileEntity tile = world.getTileEntity(vecToAdd);
			if (tile instanceof TileEntityMulti) {
				((TileEntityMulti) tile).mainBlockPosition = pos;
			} else {
				result = false;
			}
		}
		return result;
	}

	public int getGenerate() {
		if (this.getDisabled(0)) {
			return 0;
		}
		float angle = this.worldObj.getCelestialAngle(1.0F) - 0.784690560F < 0 ? 1.0F - 0.784690560F : -0.784690560F;
		float celestialAngle = (this.worldObj.getCelestialAngle(1.0F) + angle) * 360.0F;

		celestialAngle %= 360;

		float difference = (180.0F - Math.abs(this.currentAngle % 180 - celestialAngle)) / 180.0F;
		int sum = MathHelper.floor_float(0.01F * difference * difference * (this.solarStrength * (Math.abs(difference) * 500.0F)) * this.getSolarBoost());
		if (this.worldObj.isDaytime() == false && !this.worldObj.isRaining() && !this.worldObj.isThundering()) {
			if (this.tierGC == 1 && sum <= 5) {
				return MathHelper.floor_float(5);
			} else if (this.tierGC == 2 && sum <= 10) {
				return MathHelper.floor_float(10);
			}
		} else
			return sum;
		return sum;
	}

	public float getSolarBoost() {
		return (float) (this.worldObj.provider instanceof ISolarLevel ? ((ISolarLevel) this.worldObj.provider).getSolarEnergyMultiplier() : 1.0F);
	}

	@Override
	public boolean onActivated(EntityPlayer entityPlayer) {
		return false;
	}

	@Override
	public void onCreate(World world, BlockPos placedPosition) {
		List<BlockPos> positions = new LinkedList<BlockPos>();
		this.getPositions(placedPosition, positions);
		if (positions.size() > 0) {
			((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions.get(0), placedPosition, EnumBlockMultiType.SOLAR_PANEL_0.getMeta());
			positions.remove(0);
		}
		((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, getMultiType());
	}

	@Override
	public BlockMulti.EnumBlockMultiType getMultiType() {
		return (this.getTierGC() == 1) ? EnumBlockMultiType.SOLAR_PANEL_1 : EnumBlockMultiType.SOLAR_PANEL_0;
	}

	@Override
	public void getPositions(BlockPos placedPosition, List<BlockPos> positions) {
		int buildHeight = this.worldObj.getHeight() - 1;
		int y = placedPosition.getY() + 1;
		if (y > buildHeight) {
			return;
		}
		positions.add(new BlockPos(placedPosition.getX(), y, placedPosition.getZ()));

		y++;
		if (y > buildHeight) {
			return;
		}
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
			}
		}
	}

	@Override
	public void onDestroy(TileEntity callingBlock) {
		final BlockPos thisBlock = getPos();
		List<BlockPos> positions = new ArrayList<BlockPos>();
		this.getPositions(thisBlock, positions);

		for (BlockPos pos : positions) {
			IBlockState stateAt = this.worldObj.getBlockState(pos);

			if (stateAt.getBlock() == GCBlocks.fakeBlock) {
				EnumBlockMultiType type = (EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE);
				if ((type == EnumBlockMultiType.SOLAR_PANEL_0 || type == EnumBlockMultiType.SOLAR_PANEL_1)) {
					if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D) {
						FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, GCBlocks.solarPanel.getDefaultState());
					}

					this.worldObj.setBlockToAir(pos);
				}
			}
		}

		this.worldObj.destroyBlock(getPos(), true);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.storage.setCapacity(nbt.getFloat("maxEnergy"));
		this.currentAngle = nbt.getFloat("currentAngle");
		this.targetAngle = nbt.getFloat("targetAngle");
		this.setDisabled(0, nbt.getBoolean("disabled"));
		this.disableCooldown = nbt.getInteger("disabledCooldown");

		final NBTTagList var2 = nbt.getTagList("Items", 10);
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final int var5 = var4.getByte("Slot") & 255;

			if (var5 < this.containingItems.length) {
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		this.initialised = false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setFloat("maxEnergy", this.getMaxEnergyStoredGC());
		nbt.setFloat("currentAngle", this.currentAngle);
		nbt.setFloat("targetAngle", this.targetAngle);
		nbt.setInteger("disabledCooldown", this.disableCooldown);
		nbt.setBoolean("disabled", this.getDisabled(0));

		final NBTTagList list = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3) {
			if (this.containingItems[var3] != null) {
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				list.appendTag(var4);
			}
		}

		nbt.setTag("Items", list);
	}

	@Override
	public EnumSet<EnumFacing> getElectricalInputDirections() {
		return EnumSet.noneOf(EnumFacing.class);
	}

	public EnumFacing getFront() {
		IBlockState state = this.worldObj.getBlockState(getPos());
		if (state.getBlock() instanceof BlockSolar) {
			return state.getValue(BlockSolar.FACING);
		}
		return EnumFacing.NORTH;
	}

	@Override
	public EnumSet<EnumFacing> getElectricalOutputDirections() {
		return EnumSet.of(getFront());
	}

	@Override
	public EnumFacing getElectricOutputDirection() {
		return getFront();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 4, getPos().getZ() + 2);
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public String getName() {
		return TranslateUtilities.translate(this.tierGC == 1 ? "container.solarhybrid.name" : "container.solarultimate.name");
	}

	@Override
	public void setDisabled(int index, boolean disabled) {
		if (this.disableCooldown == 0) {
			this.disabled = disabled;
			this.disableCooldown = 20;
		}
	}

	@Override
	public boolean getDisabled(int index) {
		return this.disabled;
	}

	public int getScaledElecticalLevel(int i) {
		return (int) Math.floor(this.getEnergyStoredGC() * i / this.getMaxEnergyStoredGC());
	}

	@Override
	public int getSizeInventory() {
		return this.containingItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) {
		return this.containingItems[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.containingItems[par1] != null) {
			ItemStack var3;

			if (this.containingItems[par1].stackSize <= par2) {
				var3 = this.containingItems[par1];
				this.containingItems[par1] = null;
				return var3;
			} else {
				var3 = this.containingItems[par1].splitStack(par2);

				if (this.containingItems[par1].stackSize == 0) {
					this.containingItems[par1] = null;
				}

				return var3;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int par1) {
		if (this.containingItems[par1] != null) {
			final ItemStack var2 = this.containingItems[par1];
			this.containingItems[par1] = null;
			return var2;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.containingItems[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return this.isItemValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return slotID == 0;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		return slotID == 0 && ItemElectricBase.isElectricItem(itemstack.getItem());
	}

	@Override
	public boolean canConnect(EnumFacing direction, NetworkType type) {
		if (direction == null || type != NetworkType.POWER) {
			return false;
		}

		return direction == this.getElectricOutputDirection();
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {

	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}
}
