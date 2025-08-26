package net.jarviz.stellarmod.entity.client;

import net.jarviz.stellarmod.TutorialMod;
import net.jarviz.stellarmod.entity.custom.GnomEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GnomModel extends GeoModel<GnomEntity> {
    @Override
    public ResourceLocation getModelResource(GnomEntity animatable) {
        return new ResourceLocation(TutorialMod.MOD_ID, "geo/gnom.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GnomEntity animatable) {
        return new ResourceLocation(TutorialMod.MOD_ID, "textures/entity/gnom.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GnomEntity animatable) {
        return new ResourceLocation(TutorialMod.MOD_ID, "animations/gnom.animation.json");
    }
}
