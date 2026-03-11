package com.joniski.kibtech.entity.client;

import com.joniski.kibtech.KibTech;
import com.joniski.kibtech.entity.custom.DiamondRobotEntity;
import com.joniski.kibtech.entity.custom.IronRobotEntity;
import com.joniski.kibtech.entity.custom.RobotEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DiamondRobotRenderer extends MobRenderer<DiamondRobotEntity, DiamondRobotModel<DiamondRobotEntity>>{

    public DiamondRobotRenderer(Context context) {
        super(context, new DiamondRobotModel<>(context.bakeLayer(IronRobotModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(DiamondRobotEntity arg0) {
        return ResourceLocation.fromNamespaceAndPath(KibTech.MODID, "textures/entity/diamond_robot.png");
    }

    @Override
    public void render(DiamondRobotEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
