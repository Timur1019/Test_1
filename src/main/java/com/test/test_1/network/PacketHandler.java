package com.test.test_1.network;

import com.example.messagemod.MessageMod;
import com.example.messagemod.db.MessageEntity;
import com.example.messagemod.db.MessageRepository;
import com.example.messagemod.proto.Messages;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class PacketHandler {

    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkConstants.MESSAGE_PACKET_ID,
                (server, player, handler, buf, responseSender) -> {
                    try {
                        byte[] packetData = new byte[buf.readableBytes()];
                        buf.readBytes(packetData);

                        server.execute(() -> {
                            try {
                                Messages.Message protoMessage = Messages.Message.parseFrom(packetData);
                                handleMessage(player, protoMessage);
                            } catch (Exception e) {
                                MessageMod.LOGGER.error("Failed to parse message packet", e);
                            }
                        });
                    } catch (Exception e) {
                        MessageMod.LOGGER.error("Failed to read packet data", e);
                    }
                });
    }

    private static void handleMessage(ServerPlayerEntity player, Messages.Message protoMessage) {
        MessageRepository repository = MessageMod.getMessageRepository();
        try {
            UUID playerUuid = player.getUuid();
            String text = protoMessage.getText();
            if (text.length() > 256) {
                text = text.substring(0, 256);
            }
            MessageEntity entity = new MessageEntity(playerUuid, text);
            repository.saveMessage(entity);

            MessageMod.LOGGER.info("Message saved from player {}: {}", playerUuid, text);
        } catch (Exception e) {
            MessageMod.LOGGER.error("Failed to save message to database", e);
        }
    }
}