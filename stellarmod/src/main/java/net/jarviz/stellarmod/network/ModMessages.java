package net.jarviz.stellarmod.network;

import net.jarviz.stellarmod.TutorialMod;
import net.jarviz.stellarmod.network.packet.SprintWhileMountedC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(TutorialMod.MOD_ID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        CHANNEL.registerMessage(id(), SprintWhileMountedC2SPacket.class,
                SprintWhileMountedC2SPacket::encode,
                SprintWhileMountedC2SPacket::decode,
                SprintWhileMountedC2SPacket::handle);
    }
}
