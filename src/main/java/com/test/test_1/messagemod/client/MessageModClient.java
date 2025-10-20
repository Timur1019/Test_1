package com.test.test_1.messagemod.client;


import com.example.messagemod.gui.MessageScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class MessageModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("message")
                    .executes(context -> {
                        MinecraftClient.getInstance().setScreen(new MessageScreen());
                        return 1;
                    })
            );
        });
    }
}