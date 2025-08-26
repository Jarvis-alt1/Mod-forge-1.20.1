package net.jarviz.stellarmod.entity;

import net.jarviz.stellarmod.TutorialMod;
import net.jarviz.stellarmod.entity.custom.GnomEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TutorialMod.MOD_ID);

    public static final RegistryObject<EntityType<GnomEntity>> GNOM = ENTITY_TYPES.register(
            "gnom",
            () -> EntityType.Builder.of(GnomEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.2f)
                    .build(new ResourceLocation(TutorialMod.MOD_ID, "gnom").toString())
    );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
