package me.timesquared.lowarmournotif.config;

import me.timesquared.lowarmournotif.MainServer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.io.IOException;

public class ConfigScreen extends Screen {
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	
	public ConfigScreen(Text title) {
		super(title);
	}
	
	protected void init() {
		TextFieldWidget armourAlert = new TextFieldWidget(mc.textRenderer, this.width / 2 + 20, this.height / 2, 100, 20, Text.of("intfield"));
		this.addDrawableChild(armourAlert);
		
		ButtonWidget doneButton = ButtonWidget.builder(Text.of("Done"), button -> {
			String content = armourAlert.getText();
			int output;
			
			try {
				output = Integer.parseInt(content);
			} catch (NumberFormatException numberFormatException) {
				mc.setScreen(null);
				MainServer.LOGGER.warn("Could not parse '" + content + "' as an int.");
				mc.player.sendMessage(Text.of("The input you put in was not an integer value!"));
				return; // Don't update the alert
			}
			if (!(output <= 100 && output >= 0)) {
				mc.setScreen(null);
				mc.player.sendMessage(Text.of("Alert percentage must be a value from 0 to 100!"));
				return; // Don't update
			}
			// We have passed all the checks, now we will set the variable
			
			ConfigManager.setArmourPercentAlert(output);
			mc.player.sendMessage(Text.of("Updated armour alert percentage"));
			
			try {
				ConfigManager.save();
			} catch (IOException ioException) {
				throw new RuntimeException("Could not save config file: " + ioException);
			}
			mc.setScreen(null);
		}).dimensions(this.width / 2 - 75, (int) (this.height / 1.1), 150, 20).build();
		this.addDrawableChild(doneButton);
		
		ButtonWidget toggleSound = ButtonWidget.builder(Text.of("Sounds: " + (ConfigManager.isSound() ? "Yes" : "No")), button -> {
			ConfigManager.setSound(!ConfigManager.isSound());
			button.setMessage(Text.of("Sounds: " + (ConfigManager.isSound() ? "Yes" : "No")));
			
			try {
				ConfigManager.save();
			} catch (IOException ioException) {
				throw new RuntimeException("Could not save config file: " + ioException);
			}
		}).dimensions(this.width / 2 - 62, this.height / 2 + 25, 125, 20).build();
		this.addDrawableChild(toggleSound);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackgroundTexture(matrices);
		this.textRenderer.drawWithShadow(matrices, "Alert percentage: ", (float) (this.width / 2 - mc.textRenderer.getWidth("Alert percentage: ")), (float) (this.height / 2 + 4.5), 0xFFFFFF);
		super.render(matrices, mouseX, mouseY, delta);
	}
}
