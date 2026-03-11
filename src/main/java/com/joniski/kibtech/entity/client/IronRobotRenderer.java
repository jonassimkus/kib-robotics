package com.joniski.kibtech.entity.client;

import com.joniski.kibtech.KibTech;
import com.joniski.kibtech.entity.custom.IronRobotEntity;
import com.joniski.kibtech.entity.custom.RobotEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class IronRobotRenderer extends MobRenderer<IronRobotEntity, IronRobotModel<IronRobotEntity>>{

    public IronRobotRenderer(Context context) {
        super(context, new IronRobotModel<>(context.bakeLayer(IronRobotModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(IronRobotEntity arg0) {
        return ResourceLocation.fromNamespaceAndPath(KibTech.MODID, "textures/entity/iron_robot.png");
    }

    @Override
    public void render(IronRobotEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
