package com.joniski.kibtech.uitl;

import java.util.ArrayList;
import java.util.List;

import com.joniski.kibtech.KibTech;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.Property;

public class TreeUtil {
    public static boolean isLog(Level level, BlockPos pos){
        if (level.getBlockState(pos) == null){
            return false;
        }

        for (TagKey tag : level.getBlockState(pos).getTags().toList()){
            if (tag.toString().contains("minecraft:logs")){
                return true;
            }
        }

        return false;
    }
    
    public static List<BlockPos> getNearbyLogs(Level level, BlockPos pos){
        List<BlockPos> nearby = new ArrayList<BlockPos>();

        for (int x = 0; x < 3; ++x){
            for (int y = 0; y < 3; ++y){
                for (int z = 0; z < 3; ++z){
                    BlockPos newPos = pos.offset(x-1, y-1, z-1);
                    if (isLog(level, newPos)){
                        // Make sure its the same tree, different trees wont have branches that face up
                        if (newPos.getX() != pos.getX() || newPos.getZ() != pos.getZ()){
                            if(level.getBlockState(newPos).hasProperty(RotatedPillarBlock.AXIS)){
                                Direction.Axis axis = level.getBlockState(newPos).getValue(RotatedPillarBlock.AXIS);
                                if (axis == Direction.Axis.Y){
                                    continue;
                                }
                            }
                        }
                        nearby.add(newPos);
                    }
                }
            }
        }

        return nearby;
    }

    public static List<BlockPos> lookForBranch(Level level, BlockPos pos, List<BlockPos> foundLogs){
        List<BlockPos> nearbyLogs = getNearbyLogs(level, pos);
        
        for(BlockPos log : nearbyLogs){
            boolean alreadyFound = false;
            for (BlockPos foundLog: foundLogs){
                if (log.getX() == foundLog.getX() && log.getY() == foundLog.getY() && log.getZ() == foundLog.getZ()){
                    alreadyFound = true;
                    break;
                }
            }

            if (alreadyFound == false){
                foundLogs.add(log);
                lookForBranch(level, log, foundLogs);
            }
        }


        return null;
    }

    // Recursion
    public static List<BlockPos> getTree(Level level, BlockPos pos){
        List<BlockPos> logs = new ArrayList<BlockPos>();     
    
        
        if (isLog(level, pos)){
            logs.add(pos);
        }else{
            return logs;
        }

        lookForBranch(level, pos, logs);

        return logs;
    }
}
