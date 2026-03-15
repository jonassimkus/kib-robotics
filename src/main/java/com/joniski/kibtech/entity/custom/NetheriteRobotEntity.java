package com.joniski.kibtech.entity.custom;

import com.joniski.kibtech.item.ModItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

public class NetheriteRobotEntity extends RobotEntity{

    public NetheriteRobotEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        maxToolTier = Tiers.STONE;
        moveSpeed = 1.3f;
        maxArea = 13;
        dropItem = ModItems.NETHERITE_ROBOT_ITEM.asItem();
        inventory.setSize(29);
    }
}
    