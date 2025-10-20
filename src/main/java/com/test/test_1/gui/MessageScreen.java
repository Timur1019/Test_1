package com.test.test_1.gui;

import com.example.messagemod.network.NetworkConstants;
import com.example.messagemod.proto.Messages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.client.util.math.MatrixStack;

public class MessageScreen extends Screen {
    private TextFieldWidget textField;

    public MessageScreen() {
        super(Text.literal("Message Sender"));
    }

    @Override
    protected void init() {
        super.init();

        this.textField = new TextFieldWidget(
                this.textRenderer,
                this.width / 2 - 150, this.height / 2 - 20, 300, 20,
                Text.literal("Enter message")
        );
        this.textField.setMaxLength(256);
        this.addSelectableChild(this.textField);
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 75, this.height / 2 + 10, 150, 20,
                Text.literal("Send Message"),
                button -> this.sendMessage()
        ));

        this.setInitialFocus(this.textField);
    }

    private void sendMessage() {
        String text = this.textField.getText().trim();
        if (!text.isEmpty()) {
            try {
                Messages.Message message = Messages.Message.newBuilder()
                        .setText(text)
                        .build();

                byte[] packetData = message.toByteArray();
                ClientPlayNetworking.send(NetworkConstants.MESSAGE_PACKET_ID,
                        net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create().writeBytes(packetData));

                this.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(matrices, this.textRenderer, this.title,
                this.width / 2, this.height / 2 - 50, 0xFFFFFF);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC
            this.close();
            return true;
        } else if (keyCode == 257) { // Enter
            this.sendMessage();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}