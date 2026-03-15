package com.joniski.kibtech.entity.client;

import com.joniski.kibtech.KibTech;
import com.joniski.kibtech.entity.custom.RobotEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NetheriteRobotRenderer extends MobRenderer<RobotEntity, NetheriteRobotModel<RobotEntity>>{

    public NetheriteRobotRenderer(Context context) {
        super(context, new NetheriteRobotModel<>(context.bakeLayer(NetheriteRobotModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(RobotEntity arg0) {
        return ResourceLocation.fromNamespaceAndPath(KibTech.MODID, "textures/entity/netherite_robot.png");
    }

    @Override
    public void render(RobotEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
