package com.joniski.kibtech.menus.custom;

import com.joniski.kibtech.block.ModBlocks;
import com.joniski.kibtech.block.custom.SolarPanelEntity;
import com.joniski.kibtech.entity.client.WoodRobotAnimations;
import com.joniski.kibtech.entity.custom.WoodRobotEntity;
import com.joniski.kibtech.menus.ModMenus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class RobotMenu extends AbstractContainerMenu{

    private final Level level;
    public WoodRobotEntity entity;

    public RobotMenu(int containerId, Inventory inventory, int entityId){
        super(ModMenus.ROBOT_MENU.get(), containerId);
        this.level = inventory.player.level();
        entity = (WoodRobotEntity) level.getEntity(entityId);
        
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    
        this.addSlot(new SlotItemHandler(entity.inventory, 0, 8, 63));
        this.addSlot(new SlotItemHandler(entity.inventory, 1, 8, 42));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        if (slotIndex >= 36){
            if (!this.moveItemStackTo(slots.get(slotIndex).getItem(), 0, 36, false)){
                return ItemStack.EMPTY;
            }

            return slots.get(slotIndex).getItem().copy();
        }else{
            if (!this.moveItemStackTo(slots.get(slotIndex).getItem(), 36, 38, false)){
                return ItemStack.EMPTY;
            }
            return slots.get(slotIndex).getItem().copy();
        }
    }

    @Override
    public boolean stillValid(Player arg0) {
        return true;
    }

    private void addPlayerInventory(Inventory playerInventory){
        for (int i = 0; i < 3; ++i){
            for (int v = 0; v < 9; ++v){
                this.addSlot(new Slot(playerInventory, v + i * 9 + 9, 8 + v * 18, 84 + i * 18));
            }
        }
    }
    
    private void addPlayerHotbar(Inventory playerInventory){
        for (int i = 0; i < 9; ++i){
            this.addSlot(new Slot(playerInventory, i, 8+i *18, 142));
        }
    }
}
