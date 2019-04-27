package me.dmillerw.droids.client.render;

import me.dmillerw.droids.common.entity.EntityDroid;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelDroid extends ModelBiped {

    public ModelDroid() {
        super(0.0F, 0.0F, 64, 64);
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        EntityDroid minion = (EntityDroid)entity;

        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        if (!minion.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) {
            this.bipedLeftArm.rotateAngleX = 5;
            this.bipedRightArm.rotateAngleX = 5;
        }

        GlStateManager.pushMatrix();

        float f = 0.5F;
        GlStateManager.scale(f, f, f);
        GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
        this.bipedHead.render(scale);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
        this.bipedBody.render(scale);
        this.bipedRightArm.render(scale);
        this.bipedLeftArm.render(scale);
        this.bipedRightLeg.render(scale);
        this.bipedLeftLeg.render(scale);
        this.bipedHeadwear.render(scale);

        GlStateManager.popMatrix();
    }
}
