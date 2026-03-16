package com.joniski.kibtech.block.custom;

import java.util.List;

import com.joniski.kibtech.KibTech;
import com.joniski.kibtech.block.ModBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RobotStation extends BaseEntityBlock{

    public static final MapCodec<RobotStation> CODEC = simpleCodec(RobotStation::new);

    public RobotStation(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos arg0, BlockState arg1) {
        return new RobotStationEntity(arg0, arg1);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }


    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        level.updateNeighbourForOutputSignal(pos, this);

        if (level.getBlockEntity(pos) instanceof RobotStationEntity robotStationEntity){
            robotStationEntity.dropContents();
        }
       
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
    
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!level.isClientSide){
            if (level.getBlockEntity(pos) instanceof RobotStationEntity robotStationEntity){
                if (robotStationEntity.getEnergyStorage().getEnergyStored() <= 0){
                    player.sendSystemMessage(Component.literal("Station has no power."));
                    return ItemInteractionResult.FAIL;
                }

                ((ServerPlayer) player).openMenu(new SimpleMenuProvider(robotStationEntity, Component.literal("Robot Station")), pos);
                return ItemInteractionResult.SUCCESS;
            }
        }


        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntityType) {
            
        if (level.isClientSide()){
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntity.ROBOT_STATION_BE.get(), 
                (levels, blockPos, blockState, blockEntity) -> blockEntity.tick(levels, blockPos, blockState));
    }

}
