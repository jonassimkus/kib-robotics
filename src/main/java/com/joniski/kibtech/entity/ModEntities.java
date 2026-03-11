package com.joniski.kibtech.entity;

import java.util.function.Supplier;

import org.checkerframework.checker.signature.qual.Identifier;

import com.joniski.kibtech.KibTech;
import com.joniski.kibtech.block.ModBlocks;
import com.joniski.kibtech.block.custom.SolarPanelEntity;
import com.joniski.kibtech.entity.custom.DiamondRobotEntity;
import com.joniski.kibtech.entity.custom.IronRobotEntity;
import com.joniski.kibtech.entity.custom.RobotEntity;
import com.joniski.kibtech.entity.custom.StoneRobotEntity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.Blocks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, KibTech.MODID);

    public static final Supplier<EntityType<RobotEntity>> WOOD_ROBOT = 
        ENTITIES.register("wood_robot_entity",  () -> EntityType.Builder.of(RobotEntity::new,
             MobCategory.MISC).sized(.5f, .5f).build("wood_robot_entity"));


    public static final Supplier<EntityType<StoneRobotEntity>> STONE_ROBOT = 
        ENTITIES.register("stone_robot_entity",  () -> EntityType.Builder.of(StoneRobotEntity::new,
             MobCategory.MISC).sized(.5f, .5f).build("stone_robot_entity"));

    public static final Supplier<EntityType<IronRobotEntity>> IRON_ROBOT = 
        ENTITIES.register("iron_robot_entity",  () -> EntityType.Builder.of(IronRobotEntity::new,
             MobCategory.MISC).sized(.5f, .5f).build("iron_robot_entity"));

    public static final Supplier<EntityType<DiamondRobotEntity>> DIAMOND_ROBOT = 
        ENTITIES.register("diamond_robot_entity",  () -> EntityType.Builder.of(DiamondRobotEntity::new,
             MobCategory.MISC).sized(.5f, .5f).build("diamond_robot_entity"));



    public static void register(IEventBus eventBus){
        ENTITIES.register(eventBus);
    }
}
