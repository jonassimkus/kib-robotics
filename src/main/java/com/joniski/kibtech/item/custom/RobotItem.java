package com.joniski.kibtech.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class RobotItem extends Item{

    EntityType<?> spawnType;

    public RobotItem(Properties properties, EntityType<?> spawnType) {
        super(properties);
        this.spawnType = spawnType;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedPos() != null){
    
            if (!context.getLevel().isClientSide){
                spawnType.spawn(((ServerLevel)(context.getLevel())), context.getClickedPos().above(), MobSpawnType.SPAWN_EGG);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
