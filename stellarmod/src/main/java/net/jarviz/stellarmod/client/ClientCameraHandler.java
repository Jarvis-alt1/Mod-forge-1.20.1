package net.jarviz.stellarmod.client;

import net.jarviz.stellarmod.entity.custom.GnomEntity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.jarviz.stellarmod.stellarmod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class ClientCameraHandler {

    private static CameraType prevType = null;
    private static boolean enforcing = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean ridingGnome = mc.player.getVehicle() instanceof GnomEntity;
        CameraType current = mc.options.getCameraType();

        if (ridingGnome) {
            if (!enforcing) {
                prevType = current;
                enforcing = true;
                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            } else {
                if (current != CameraType.THIRD_PERSON_BACK) {
                    mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
                }
            }
        } else if (enforcing) {
            mc.options.setCameraType(prevType != null ? prevType : CameraType.FIRST_PERSON);
            prevType = null;
            enforcing = false;
        }
    }
}
