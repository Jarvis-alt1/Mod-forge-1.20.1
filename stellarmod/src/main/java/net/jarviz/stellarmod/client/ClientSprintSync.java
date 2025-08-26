package net.jarviz.stellarmod.client;

import net.jarviz.stellarmod.entity.custom.GnomEntity;
import net.jarviz.stellarmod.network.ModMessages;
import net.jarviz.stellarmod.network.packet.SprintWhileMountedC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.jarviz.stellarmod.TutorialMod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class ClientSprintSync {
    private static boolean lastSent = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean sprintKey = mc.options.keySprint.isDown() || mc.player.isSprinting();

        if (mc.player.getVehicle() instanceof GnomEntity gnom) {
            gnom.setRiderSprinting(sprintKey);

            if (sprintKey != lastSent) {
                ModMessages.CHANNEL.sendToServer(new SprintWhileMountedC2SPacket(sprintKey));
                lastSent = sprintKey;
            }
        } else {
            lastSent = false;
        }
    }
}
