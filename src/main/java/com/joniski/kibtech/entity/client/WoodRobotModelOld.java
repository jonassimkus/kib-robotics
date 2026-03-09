package com.joniski.kibtech.entity.client;

import com.joniski.kibtech.KibTech;
import com.joniski.kibtech.entity.custom.WoodRobotEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class WoodRobotModelOld<T extends WoodRobotEntity> extends HierarchicalModel<T>{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KibTech.MODID, "wood_robot_entity"), "main");
	private final ModelPart Body;
	private final ModelPart bone5;
	private final ModelPart bone6;
	private final ModelPart bone3;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bone4;

	public WoodRobotModelOld(ModelPart root) {
		this.Body = root.getChild("Body");
		this.bone5 = this.Body.getChild("bone5");
		this.bone6 = this.Body.getChild("bone6");
		this.bone3 = this.Body.getChild("bone3");
		this.bone = this.Body.getChild("bone");
		this.bone2 = this.Body.getChild("bone2");
		this.bone4 = this.Body.getChild("bone4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone5 = Body.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(6, 22).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone6 = Body.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(14, 22).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));

		PartDefinition bone3 = Body.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -7.0F, -3.0F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -2.0F, 0.0F));

		PartDefinition bone = Body.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(20, 13).addBox(-5.0F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, -2.0F, 0.0F));

		PartDefinition bone2 = Body.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 22).addBox(-5.0F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -2.0F, 0.0F));

		PartDefinition bone4 = Body.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(22, 22).addBox(-4.0F, -7.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-5.0F, -5.0F, -2.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -9.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(WoodRobotEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch);
		
		this.animate(entity.idleAnimationState, WoodRobotAnimations.ANIM_WOOD_ROBOT_IDLE, ageInTicks, 1f);
	}

	private void applyHeadRotation(float headYaw, float headPitch){
		headYaw = Mth.clamp(headYaw, -30f, 30f);
		headPitch = Mth.clamp(headPitch, -25f, 45f);

		this.bone4.yRot = headYaw * ((float)Math.PI / 180f);
		this.bone4.xRot = headPitch * ((float)Math.PI / 180f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int colour) {
		bone5.render(poseStack, vertexConsumer, packedLight, packedOverlay, colour);
		bone6.render(poseStack, vertexConsumer, packedLight, packedOverlay, colour);
		bone3.render(poseStack, vertexConsumer, packedLight, packedOverlay, colour);
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, colour);
		bone2.render(poseStack, vertexConsumer, packedLight, packedOverlay, colour);
		bone4.render(poseStack, vertexConsumer, packedLight, packedOverlay, colour);
	}

    @Override
    public ModelPart root() {
        return Body;
    }


}
