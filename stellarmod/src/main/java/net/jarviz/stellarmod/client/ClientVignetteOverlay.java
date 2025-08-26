package net.jarviz.stellarmod.client;

import net.jarviz.stellarmod.entity.custom.GnomEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.jarviz.stellarmod.stellarmod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class ClientVignetteOverlay {
    private static final ResourceLocation VIGNETTE = new ResourceLocation(MOD_ID, "textures/gui/vinette.png");

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!(mc.player.getVehicle() instanceof GnomEntity)) return;

        GuiGraphics g = event.getGuiGraphics();
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        g.setColor(1f, 1f, 1f, 1f);
        g.blit(VIGNETTE, 0, 0, 0, 0, w, h, w, h);
    }
}
