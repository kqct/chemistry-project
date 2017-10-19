package me.joshlin.chemistryproject.blocks.Tc99mGen;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * User: Josh
 * Date: 10/09/2017
 */
public class Tc99mGenContainer extends Container {

    private Tc99mGenTileEntity tc99mGenTileEntity;

    private int [] cachedFields;

    private final int HOTBAR_SLOT_COUNT = 9;
    private final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    public final int INPUT_SLOTS_COUNT = 1;
    public final int OUTPUT_SLOTS_COUNT = 1;
    public final int GENERATOR_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

    private final int VANILLA_FIRST_SLOT_INDEX = 0;
    private final int FIRST_INPUT_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private final int FIRST_OUTPUT_SLOT_INDEX = FIRST_INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT;

    private final int FIRST_INPUT_SLOT_NUMBER = 0;
    private final int FIRST_OUTPUT_SLOT_NUMBER = FIRST_INPUT_SLOT_NUMBER + INPUT_SLOTS_COUNT;

    public Tc99mGenContainer(InventoryPlayer invPlayer, Tc99mGenTileEntity tc99mGenTileEntity) {
        this.tc99mGenTileEntity = tc99mGenTileEntity;

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;

        final int HOTBAR_XPOS = 8;
        final int HOTBAR_YPOS = 109;
        // Add the player's hotbar to the gui - the [xpos, ypos] of each item
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            int slotNumber = x;
            addSlotToContainer(new Slot(invPlayer, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
        }

        final int PLAYER_INVENTORY_XPOS = 8;
        final int PLAYER_INVENTORY_YPOS = 51;
        // Add rest of player inventory
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlotToContainer(new Slot(invPlayer, slotNumber, xpos, ypos));
            }
        }

        final int INPUT_SLOT_XPOS = 8 + SLOT_X_SPACING;
        final int INPUT_SLOT_YPOS = 20;
        for (int x = 0; x < INPUT_SLOTS_COUNT; x++) {
            int slotNumber = x + FIRST_INPUT_SLOT_NUMBER;
            addSlotToContainer(new SlotProcessableInput(tc99mGenTileEntity, slotNumber, INPUT_SLOT_XPOS, INPUT_SLOT_YPOS));
        }


        final int OUTPUT_SLOT_XPOS = 8 + 7 * SLOT_X_SPACING;
        final int OUTPUT_SLOT_YPOS = 20;
        for (int x = 0; x < OUTPUT_SLOTS_COUNT; x++) {
            int slotNumber = x + FIRST_OUTPUT_SLOT_NUMBER;
            addSlotToContainer(new SlotOutput(tc99mGenTileEntity, slotNumber, OUTPUT_SLOT_XPOS, OUTPUT_SLOT_YPOS));
        }
    }

    // Checks to see if the player can still use the inventory
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tc99mGenTileEntity.isUsableByPlayer(playerIn);
    }

    // Shift-click code (MUST BE OVERRIDDEN)
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot sourceSlot = (Slot)inventorySlots.get(index);

        if (sourceSlot == null || !sourceSlot.getHasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index >= VANILLA_FIRST_SLOT_INDEX && index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!Tc99mGenTileEntity.getProcessResultForItem(sourceStack).isEmpty()) {
                if (!mergeItemStack(sourceStack, FIRST_INPUT_SLOT_INDEX, FIRST_INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }
        } else if (index >= FIRST_INPUT_SLOT_INDEX && index < FIRST_INPUT_SLOT_INDEX + GENERATOR_SLOTS_COUNT) {
            if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.err.print("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            sourceSlot.onSlotChanged();
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    // Client Synchronization
    // e.g. Update Progress Bar

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        boolean allFieldsHaveChanged = false;
        boolean fieldHasChanged [] = new boolean[tc99mGenTileEntity.getFieldCount()];
        if (cachedFields == null) {
            cachedFields = new int[tc99mGenTileEntity.getFieldCount()];
            allFieldsHaveChanged = true;
        }

        for (int i = 0; i < cachedFields.length; ++i) {
            if (allFieldsHaveChanged || cachedFields[i] != tc99mGenTileEntity.getField(i)) {
                cachedFields[i] = tc99mGenTileEntity.getField(i);
                fieldHasChanged[i] = true;
            }
        }

        for (IContainerListener listener : this.listeners) {
            for (int fieldID = 0; fieldID < tc99mGenTileEntity.getFieldCount(); ++fieldID) {
                if (fieldHasChanged[fieldID]) {
                    listener.sendProgressBarUpdate(this, fieldID, cachedFields[fieldID]);
                }
            }
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        tc99mGenTileEntity.setField(id, data);
    }

    public class SlotProcessableInput extends Slot {
        public SlotProcessableInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return Tc99mGenTileEntity.isItemValidForInputSlot(stack);
        }
    }

    public class SlotOutput extends Slot {
        public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return Tc99mGenTileEntity.isItemValidForOutputSlot(stack);
        }
    }
}

// See https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/ContainerInventoryFurnace.java
