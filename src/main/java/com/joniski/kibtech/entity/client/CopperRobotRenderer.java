package com.joniski.kibtech.entity.client;

import com.joniski.kibtech.KibTech;
import com.joniski.kibtech.entity.custom.RobotEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CopperRobotRenderer extends MobRenderer<RobotEntity, CopperRobotModel<RobotEntity>>{

    public CopperRobotRenderer(Context context) {
        super(context, new CopperRobotModel<>(context.bakeLayer(CopperRobotModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(RobotEntity arg0) {
        return ResourceLocation.fromNamespaceAndPath(KibTech.MODID, "textures/entity/copper_robot.png");
    }

    @Override
    public void render(RobotEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
