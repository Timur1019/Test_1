package com.test.test_1.messagemod;


import com.example.messagemod.db.MessageRepository;
import com.example.messagemod.network.PacketHandler;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageMod implements ModInitializer {
    public static final String MOD_ID = "messagemod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MessageRepository messageRepository;
    @Getter
    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Message Mod");

        PacketHandler.registerServerReceivers();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            MessageMod.server = server;
            try {
                messageRepository = new MessageRepository();
                LOGGER.info("Database connection initialized");
            } catch (Exception e) {
                LOGGER.error("Failed to initialize database connection", e);
            }
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (messageRepository != null) {
                messageRepository.close();
                LOGGER.info("Database connection closed");
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("message")
                    .executes(context -> {
                        if (context.getSource().getPlayer() != null) {
                            context.getSource().sendMessage(Text.literal("Use /message on client side"));
                        }
                        return 1;
                    })
            );
        });
    }

}