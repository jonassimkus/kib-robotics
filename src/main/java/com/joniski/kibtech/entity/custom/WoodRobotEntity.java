package com.joniski.kibtech.entity.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.Debug;

import com.joniski.kibtech.KibTech;
import com.joniski.kibtech.block.ModBlocks;
import com.joniski.kibtech.component.ModDataComponents;
import com.joniski.kibtech.component.PowerRecord;
import com.joniski.kibtech.enums.RobotWorkType;
import com.joniski.kibtech.item.ModItems;
import com.joniski.kibtech.item.custom.WeakBatteryItem;
import com.joniski.kibtech.menus.custom.RobotMenu;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner.BlockType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.world.level.block.Blocks;

public class WoodRobotEntity extends Animal{

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private BlockPos targetBlock;
    private BlockPos searchStart;
    private BlockPos searchEnd;
    private boolean moving = false;
    private RobotWorkType workType = RobotWorkType.NONE;
    private List<BlockPos> everyValidBlock = new ArrayList<BlockPos>();

   // Slot 1: Battery; Slot 2: Tool
    public final ItemStackHandler inventory = new ItemStackHandler(2){
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        };


        @Override
        protected void onContentsChanged(int slot) {
            if(!level().isClientSide()){
                if (slot == 1){
                    setJob(getStackInSlot(slot));
                }
            }
        };

        protected void onLoad() {
            setJob(getStackInSlot(1));
        };

        public void setJob(ItemStack stack){
            if (stack == null){
                workType = RobotWorkType.NONE;
                return;
            }

            if (stack.getItem() instanceof AxeItem axe){
                workType = RobotWorkType.LUMBERJACK;
                return;
            }
            if (stack.getItem() instanceof PickaxeItem pickaxe){
                workType = RobotWorkType.MINER;
                return;
            }
            if (stack.getItem() instanceof HoeItem hoe){
                workType = RobotWorkType.FARMER;
                return;
            }

            workType = RobotWorkType.NONE;
        }
    };

    public WoodRobotEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("inventory", inventory.serializeNBT(this.level().registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        inventory.deserializeNBT(this.level().registryAccess(), tag.getCompound("inventory"));
    } 



    @Override
    protected void registerGoals() {
      //  this.goalSelector.addGoal(0, new TemptGoal(this, 1, stack -> stack.is(ModItems.WEAK_BATTERY), false));

    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 4d)
                .add(Attributes.MOVEMENT_SPEED, 0.3d)
                .add(Attributes.FOLLOW_RANGE, 10d)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 2);
    }


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!player.level().isClientSide()){
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider((windId, inv, p) -> new RobotMenu(windId, inv, getId()) , Component.literal("Wood Robot")), buf -> buf.writeInt(getId()));
            }

            if (hand == InteractionHand.MAIN_HAND){
                if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.ACACIA_BOAT){
                    searchStart = getOnPos().subtract(new Vec3i(5,5,5));
                    searchEnd = getOnPos().offset(new Vec3i(5,5,5));
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        dropContents();
    }

    @Override
    public boolean isFood(ItemStack arg0) {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel arg0, AgeableMob arg1) {
        return null;
    }

    private void setupAnimationStates(){
        if (this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = 60;
            this.idleAnimationState.start(this.tickCount);
        }else{
            --this.idleAnimationTimeout;
        }

    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()){
            setupAnimationStates();
        }else{
            work();
        }   
    }


    public void work(){
        ItemStack batteryStack = inventory.getStackInSlot(0);
        if (batteryStack.getItem() instanceof WeakBatteryItem battery){
            PowerRecord power = batteryStack.get(ModDataComponents.POWER_COMPONENT);
            
            if (power == null){
                return;
            }

            if (power.power() > 0){
                batteryStack.set(ModDataComponents.POWER_COMPONENT, new PowerRecord(power.power()-1));

                if (searchStart == null || searchEnd == null){
                    return;
                }

                if (!moving){
                    everyValidBlock.clear();
                    for(int y = searchStart.getY(); y < searchEnd.getY(); ++ y){
                        for(int x = searchStart.getX(); x < searchEnd.getX(); ++ x){
                            for(int z = searchStart.getZ(); z < searchEnd.getZ(); ++ z){
                                BlockPos newPos = new BlockPos(x, y, z);
                                if (validWorkBlock(level(), newPos, workType)){
                                    everyValidBlock.add(newPos);
                                }
                            }
                        }
                    }

                    double closestDistance = 10000;
                    BlockPos closestBlock = null;
                    for (BlockPos pos : everyValidBlock){
                        if (pos.getCenter().distanceTo(getOnPos().getCenter()) < closestDistance){
                            Path p = getNavigation().createPath(pos, 0);
                            if (p != null && p.canReach()){
                                // Make sure robot can get right up, or he tries to path find to blocks behind stuff.
                                if(p.getEndNode().distanceTo(pos) <= 1.5){
                                    closestDistance = pos.getCenter().distanceTo(getOnPos().getCenter());
                                    closestBlock = pos;
                                }
                            }
                            getNavigation().stop();
                        }
                    }

                    if (closestBlock != null){
                        targetBlock = closestBlock;
                        moving = true;
                    }
                }else{
                    if (getNavigation().isStuck()){
                        getNavigation().stop();
                        targetBlock = null;
                        moving = false;
                    }

                    // Use this way instead of doing getNavigation().isDone() as that also returns true if
                    // there is no path, I don't want this or it makes all blocks destroy at same time :)
                    if (getNavigation().getPath() != null && getNavigation().getPath().isDone()){
                        getNavigation().stop();
                        level().destroyBlock(targetBlock, false);
                        level().updateNeighborsAt(searchEnd, null);
                        targetBlock = null;
                        moving = false;
                    }


                    if (!validWorkBlock(level(), targetBlock, workType)){
                        moving = false;
                        targetBlock = null;
                        getNavigation().stop();
                    }else{
                        getNavigation().moveTo(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), 1);
                    }
                }
            }
        }
    }

    public static Boolean validWorkBlock(Level level, BlockPos blockPos, RobotWorkType workType){
        if (blockPos == null){
            return false;
        }

        if (workType == RobotWorkType.NONE){
            return false;
        }

        if (workType == RobotWorkType.LUMBERJACK){
            for (TagKey tag : level.getBlockState(blockPos).getTags().toList()){
                if (tag.toString().contains("minecraft:logs")){
                    return true;
                }
            }
        }

        if (workType == RobotWorkType.MINER){
            for (TagKey tag : level.getBlockState(blockPos).getTags().toList()){
                if (tag.toString().contains("ore")){
                    return true;
                }

                if (tag.toString().contains("stone")){
                    return true;
                }
            }
        }

        return false;
    }

    public void dropContents() {
        SimpleContainer drops = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < drops.getContainerSize(); ++ i){
            drops.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(level(), getOnPos(), drops);
    }
}
    