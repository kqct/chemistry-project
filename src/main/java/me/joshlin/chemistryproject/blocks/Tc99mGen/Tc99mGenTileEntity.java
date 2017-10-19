package me.joshlin.chemistryproject.blocks.Tc99mGen;

import me.joshlin.chemistryproject.blocks.ModBlocks;
import me.joshlin.chemistryproject.fluids.ModFluids;
import me.joshlin.chemistryproject.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;

import javax.annotation.Nullable;
import java.util.Arrays;

public class Tc99mGenTileEntity extends TileEntity implements IInventory, ITickable {

    public static final int INPUT_SLOTS_COUNT = 1;
    public static final int OUTPUT_SLOTS_COUNT = 1;
    public static final int TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

    public static final int FIRST_INPUT_SLOT = 0;
    public static final int FIRST_OUTPUT_SLOT = FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT;

    private short processTime;
    private static final short PROCESS_TIME_FOR_COMPLETION = 600; // 20 tps

    private int cachedNumberOfBurningSlots = -1;

    private ItemStack[] itemStacks;

    public Tc99mGenTileEntity() {
        itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];
        clear();
    }

    /**
     * Returns the amount of processing time already completed on the current item
     * @return fraction of time remaining, between 0 - 1
     */
    public double fractionOfProcessingTimeComplete() {
        double fraction = processTime/(double)PROCESS_TIME_FOR_COMPLETION;
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    @Override
    public void update() {
        if (canProcess()) {
            processTime++;
            if (processTime >= PROCESS_TIME_FOR_COMPLETION) {
                processItem();
                processTime = 0;
            }
        } else {
            processTime = 0;
        }
    }

    private boolean canProcess() {
        return processItem(false);
    }

    private void processItem() {
        processItem(true);
    }

    private boolean processItem(Boolean process){
        Integer firstSuitableInputSlot = null;
        Integer firstSuitableOutputSlot = null;
        ItemStack result = ItemStack.EMPTY;

        // finds the first input slot which is processable and whose result fits into an output slot (stacking if possible)
        for (int inputSlot = FIRST_INPUT_SLOT; inputSlot < FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT; inputSlot++) {

            if (!itemStacks[inputSlot].isEmpty()) {
                result = getProcessResultForItem(itemStacks[inputSlot]);
            }

            if (!result.isEmpty()) {

                // find the first suitable output slot- either empty, or with identical item that has enough space
                for (int outputSlot = FIRST_OUTPUT_SLOT; outputSlot < FIRST_OUTPUT_SLOT + OUTPUT_SLOTS_COUNT; outputSlot++) {
                    ItemStack outputStack = itemStacks[outputSlot];

                    if (outputStack.isEmpty()) {
                        firstSuitableInputSlot = inputSlot;
                        firstSuitableOutputSlot = outputSlot;
                        break;
                    }

                    if (outputStack.getItem() == result.getItem() && (!outputStack.getHasSubtypes() || outputStack.getMetadata() == outputStack.getMetadata())
                            && ItemStack.areItemStackTagsEqual(outputStack, result)) {
                       int combinedSize = itemStacks[outputSlot].getCount() + result.getCount();

                       if (combinedSize <= getInventoryStackLimit() && combinedSize <= itemStacks[outputSlot].getMaxStackSize()) {
                           firstSuitableInputSlot = inputSlot;
                           firstSuitableOutputSlot = outputSlot;
                           break;
                       }
                    }
                }

                if (firstSuitableInputSlot != null) {
                    break;
                }
            }
        }

        if (firstSuitableInputSlot == null) {
            return false;
        }

        if (!process) {
            return true;
        }

        //alter input and output
        itemStacks[firstSuitableInputSlot].shrink(1);

        if (itemStacks[firstSuitableInputSlot].getCount() <= 0) {
            itemStacks[firstSuitableInputSlot] = ItemStack.EMPTY;
        }

        if (itemStacks[firstSuitableOutputSlot].isEmpty()) {
            itemStacks[firstSuitableOutputSlot] = result.copy();
        } else {
            int newStackSize = itemStacks[firstSuitableOutputSlot].getCount() + result.getCount();
            itemStacks[firstSuitableOutputSlot].setCount(newStackSize);
        }

        markDirty();
        return true;
    }

    public static ItemStack getProcessResultForItem(ItemStack stack) {
        if (stack.getItem() == ModItems.ingotMolybdenum) {
            return new ItemStack(ModItems.ingotTechnetium);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    // returns true if all slots in inventory are empty
    @Override
    public boolean isEmpty() {

        for (ItemStack itemStack : itemStacks) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    // Gets stack in given slot
    @Override
    public ItemStack getStackInSlot(int index) {
        return itemStacks[index];
    }

    /**
     * Removes soem of the units from itemstack in given slot, and returns as separate itemstack
     * @param index the slot number to remove items from
     * @param count the number of units to remove
     * @return a new itemstack containing the units removed from the slot
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemStackInSlot = getStackInSlot(index);

        if (itemStackInSlot.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack itemStackRemoved;

        if (itemStackInSlot.getCount() <= count) {
            itemStackRemoved = itemStackInSlot;
            setInventorySlotContents(index, ItemStack.EMPTY);
        } else {
            itemStackRemoved = itemStackInSlot.splitStack(count);

            if (itemStackInSlot.getCount() == 0) {
                setInventorySlotContents(index, ItemStack.EMPTY);
            }
        }

        markDirty();
        return itemStackRemoved;
    }

    // overwrites stack in given index with given stack
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        itemStacks[index] = stack;

        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }

        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    // Return true if the given player is able to use this block (does the tileentity exist? is the player too far away?

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {

        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        }

        final double X_CENTER_OFFSET = 0.5;
        final double Y_CENTER_OFFSET = 0.5;
        final double Z_CENTER_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;

        return player.getDistanceSq(pos.getX() + X_CENTER_OFFSET, pos.getY() + Y_CENTER_OFFSET, pos.getZ() + Z_CENTER_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    // return true if stack is allowed to be inserted (i.e. is molybdenum)
    static public boolean isItemValidForInputSlot(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item == ModItems.ingotMolybdenum;
    }

    static public boolean isItemValidForOutputSlot(ItemStack itemStack) {
        return false;
    }

    // Data saved in meta here
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound); // required

        NBTTagList dataForAllSlots = new NBTTagList();

        for (int i = 0; i < this.itemStacks.length; ++i) {

            if (!this.itemStacks[i].isEmpty()) {
                NBTTagCompound dataForThisSlot = new NBTTagCompound();
                dataForThisSlot.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(dataForThisSlot);
                dataForAllSlots.appendTag(dataForThisSlot);
            }

        }

        compound.setTag("Items", dataForAllSlots);
        compound.setShort("ProcessTime", processTime);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        final byte NBT_TYPE_COMPOUND = 10;
        NBTTagList dataForAllSlots = compound.getTagList("Items", NBT_TYPE_COMPOUND);

        Arrays.fill(itemStacks, ItemStack.EMPTY);

        for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
            NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
            byte slotNumber = dataForOneSlot.getByte("Slot");
            if (slotNumber >= 0 && slotNumber < this.itemStacks.length) {
                this.itemStacks[slotNumber] = new ItemStack(dataForOneSlot);
            }
        }

        processTime = compound.getShort("ProcessTime");
        cachedNumberOfBurningSlots = -1;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
        final int METADATA = 0;
        return new SPacketUpdateTileEntity(this.pos, METADATA, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
        handleUpdateTag(updateTagDescribingTileEntityState);
    }

    // DO NOT REMOVE
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    // DO NOT REMOVE
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    // Back to normal
    // set all slots to empty
    @Override
    public void clear() {
        Arrays.fill(itemStacks, ItemStack.EMPTY);
    }

    // adds key to container so that it can be named
    @Override
    public String getName() {
        return "container.Tc99mGen.name";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    private static final byte PROCESS_FIELD_ID = 0;
    private static final byte NUMBER_OF_FIELDS = 1;

    @Override
    public int getField(int id) {
        if (id == PROCESS_FIELD_ID) {
            return processTime;
        }

        System.err.println("Invalid field ID in TileTc99mGen.getField:" + id);
        return 0;
    }

    @Override
    public void setField(int id, int value) {

        if (id == PROCESS_FIELD_ID) {
            processTime = (short)value;
        } else {
            System.err.println("Invalid field ID in TileTc99mGen.setField:" + id);
        }
    }

    @Override
    public int getFieldCount() {
        return NUMBER_OF_FIELDS;
    }

    // Methods here not strictly necessary for this tileentity, but must be implemented
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack = getStackInSlot(index);
        if (!itemStack.isEmpty()) {
            setInventorySlotContents(index, ItemStack.EMPTY);
        }
        return itemStack;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }
}

// see here for more example: https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/TileInventoryFurnace.java