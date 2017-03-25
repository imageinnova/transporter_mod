package com.imageinnova.transporter.inventory.guicontainer;

import com.imageinnova.transporter.tileentities.TileEntityTransporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTransporter extends Container {
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int NUM_SLOTS_PER_ROW = 9;
	private static final int INPUT_Y = 39;	// y position of input slot
	private static final int INVENTORY_X = 6;
	private static final int INVENTORY_Y = 138;
	private static final int HOT_BAR_Y = 202;
	private static final int SLOT_WIDTH = 20;	// slot width includes space to next slot

	private TileEntityTransporter te;
	
	public TileEntityTransporter getTe() {
		return te;
	}
	
	public ContainerTransporter(IInventory playerInv, TileEntityTransporter te) {
		this.te = te;
		
		/*
		 * SLOTS:
		 * 
		 * Tile Entity 0-8 ........ 0  - 0
		 * Player Inventory 9-35 .. 1  - 27
		 * Player Inventory 0-8 ... 28 - 35
		 */
		
		// Tile Entity, Slot 0-0, Slot IDs 0-0
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		this.addSlotToContainer(new SlotItemHandler(itemHandler, 0, INVENTORY_X, INPUT_Y));

	    // Player Inventory, Slot 0-27, Slot IDs 1-27
		for(int playerSlotIndexY = 0; playerSlotIndexY < PLAYER_INVENTORY_ROW_COUNT; ++playerSlotIndexY) {
            for(int playerSlotIndexX = 0; playerSlotIndexX < NUM_SLOTS_PER_ROW; ++playerSlotIndexX) {
                addSlotToContainer(new Slot(playerInv, 
                		playerSlotIndexX + playerSlotIndexY * NUM_SLOTS_PER_ROW + HOTBAR_SLOT_COUNT, 
                		INVENTORY_X + playerSlotIndexX * SLOT_WIDTH,
                		INVENTORY_Y + playerSlotIndexY * SLOT_WIDTH));
            }
        }

	    // Player Inventory, Slot 0-8, Slot IDs 28-35
		for(int hotbarSlotIndex = 0; hotbarSlotIndex < HOTBAR_SLOT_COUNT; hotbarSlotIndex++) {
            addSlotToContainer(new Slot(playerInv, hotbarSlotIndex, 
                  INVENTORY_X + hotbarSlotIndex * SLOT_WIDTH, HOT_BAR_Y));
        }
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
	    ItemStack previous = ItemStack.EMPTY;
	    Slot slot = (Slot) this.inventorySlots.get(fromSlot);

	    if (slot != null && slot.getHasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();

	        if (fromSlot < te.SIZE) {
	            // From TE Inventory to Player Inventory
	            if (!this.mergeItemStack(current, 1, 37, true))
	                return ItemStack.EMPTY;
	        } else {
	            // From Player Inventory to TE Inventory
	            if (!this.mergeItemStack(current, 0, 1, false))
	                return ItemStack.EMPTY;
	        }

	        if (current.getCount() == 0)
	            slot.getStack().setCount(0);
	        else
	            slot.onSlotChanged();

	        if (current.getCount() == previous.getCount())
	            return ItemStack.EMPTY;
	        slot.onTake(playerIn, current);
	    }
	    return previous;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.canInteractWith(playerIn);
	}
}
