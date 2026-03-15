package com.joniski.kibtech.block.custom;

import javax.annotation.Nullable;

import com.joniski.kibtech.KibTech;
import com.joniski.kibtech.block.ModBlockEntity;
import com.joniski.kibtech.block.ModBlocks;
import com.joniski.kibtech.item.custom.BatteryItem;
import com.joniski.kibtech.menus.custom.BatteryChargerMenu;
import com.joniski.kibtech.menus.custom.SolarPanelMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

@EventBusSubscriber(modid =  KibTech.MODID)
public class RobotStationEntity extends BlockEntity implements MenuProvider{

    public final ItemStackHandler inventory = new ItemStackHandler(3){
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        };


        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };

        public boolean isItemValid(int slot, ItemStack stack) {
            return true;
        };
    };

    private EnergyStorage energyStorage;

    public RobotStationEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntity.ROBOT_STATION_BE.get(), pos, blockState);
        energyStorage = new EnergyStorage(5000);
    }


    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("robot_station.inventory", inventory.serializeNBT(registries));
        tag.put("robot_station.storage", energyStorage.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("robot_station.inventory"));

        if (tag.get("robot_station.storage") != null){
            energyStorage.deserializeNBT(registries, tag.get("robot_station.storage"));
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int arg0, Inventory arg1, Player arg2) {
      //  return new BatteryChargerMenu(arg0, arg1, this);
        return null;
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("block.kibtech.robot_station");
    }


    public static IEnergyStorage getCapabilities(RobotStationEntity robotStationEntity, Direction direction){
        return robotStationEntity.energyStorage;
    }

    public void tick(Level level, BlockPos pos, BlockState state){
//        setChanged();
  //      level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }


    @SubscribeEvent
    public static void onCapabilitiesRegister(final RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntity.ROBOT_STATION_BE.get(), RobotStationEntity::getCapabilities);
    }


    public EnergyStorage getEnergyStorage(){
        return energyStorage;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(Provider registries) {
        return saveWithoutMetadata(registries);
    }


    public void dropContents() {
        SimpleContainer drops = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < drops.getContainerSize(); ++ i){
            drops.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(level, worldPosition, drops);
    }

}
