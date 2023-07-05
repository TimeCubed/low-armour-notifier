package me.timesquared.lowarmournotif;

import me.timesquared.lowarmournotif.config.ConfigManager;
import me.timesquared.lowarmournotif.config.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.io.IOException;

public class MainClient implements ClientModInitializer {
	public static final MainClient INSTANCE = new MainClient();
	private final MinecraftClient mc = MinecraftClient.getInstance();
	
	int helmetIndex = 3;
	int chestPlateIndex = 2;
	int leggingsIndex = 1;
	int bootsIndex = 0;
	
	double lastHelmetTickPercent, lastChestTickPercent, lastLeggingsTickPercent, lastBootsTickPercent;
	
	@Override
	public void onInitializeClient() {
		lastHelmetTickPercent = 1;
		lastChestTickPercent = 1;
		lastLeggingsTickPercent = 1;
		lastBootsTickPercent = 1;
		
		try {
			ConfigManager.load();
		} catch (IOException ioException) {
			throw new RuntimeException("Could not load config file: " + ioException);
		}
		
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("armornotifier").executes(context -> {
			mc.send(() -> mc.setScreen(new ConfigScreen(Text.of("config"))));
			return 1;
		})))); // Register config command
	}
	
	public void onTick() {
		if (mc.player != null) {
			PlayerInventory playerInventory = mc.player.getInventory();
			
			ItemStack helmetSlot = playerInventory.getArmorStack(helmetIndex);
			ItemStack chestPlateSlot = playerInventory.getArmorStack(chestPlateIndex);
			ItemStack leggingsSlot = playerInventory.getArmorStack(leggingsIndex);
			ItemStack bootsSlot = playerInventory.getArmorStack(bootsIndex);
			
			int helmetCurrentDura = helmetSlot.getMaxDamage() - helmetSlot.getDamage();
			int chestCurrentDura = chestPlateSlot.getMaxDamage() - chestPlateSlot.getDamage();
			int leggingsCurrentDura = leggingsSlot.getMaxDamage() - leggingsSlot.getDamage();
			int bootsCurrentDura = bootsSlot.getMaxDamage() - bootsSlot.getDamage();
			
			double helmetDuraPercent = 1, chestDuraPercent = 1, leggingsDuraPercent = 1, bootsDuraPercent = 1;
			
			if (helmetSlot.getItem() instanceof ArmorItem) helmetDuraPercent = (double) helmetCurrentDura / helmetSlot.getMaxDamage();
			if (chestPlateSlot.getItem() instanceof ArmorItem) chestDuraPercent = (double) chestCurrentDura / chestPlateSlot.getMaxDamage();
			if (leggingsSlot.getItem() instanceof ArmorItem) leggingsDuraPercent = (double) leggingsCurrentDura / leggingsSlot.getMaxDamage();
			if (bootsSlot.getItem() instanceof ArmorItem) bootsDuraPercent = (double) bootsCurrentDura / bootsSlot.getMaxDamage();
			
			if (helmetSlot.getItem() instanceof ArmorItem) {
				if (!(lastHelmetTickPercent <= ((double) ConfigManager.getArmourPercentAlert() / 100)) && helmetDuraPercent < ((double) ((double) ConfigManager.getArmourPercentAlert() / 100) / 100)) {
					if (ConfigManager.isSound()) mc.player.playSound(SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.MASTER, 1f, 1f);
					mc.getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT,
							Text.of("Armor Durability Low!"),
							Text.of("Your helmet's durability is below " + ConfigManager.getArmourPercentAlert() + "%! Make sure to repair it soon")));
				}
			}
			if (chestPlateSlot.getItem() instanceof ArmorItem) {
				if (!(lastChestTickPercent <= ((double) ConfigManager.getArmourPercentAlert() / 100)) && chestDuraPercent < ((double) ConfigManager.getArmourPercentAlert() / 100)) {
					if (ConfigManager.isSound()) mc.player.playSound(SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.MASTER, 1f, 1f);
					mc.getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT,
							Text.of("Armor Durability Low!"),
							Text.of("Your chestplate's durability is below " + ConfigManager.getArmourPercentAlert() + "%! Make sure to repair it soon")));
				}
			}
			if (leggingsSlot.getItem() instanceof ArmorItem) {
				if (!(lastLeggingsTickPercent <= ((double) ConfigManager.getArmourPercentAlert() / 100)) && leggingsDuraPercent < ((double) ConfigManager.getArmourPercentAlert() / 100)) {
					if (ConfigManager.isSound()) mc.player.playSound(SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.MASTER, 1f, 1f);
					mc.getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT,
							Text.of("Armor Durability Low!"),
							Text.of("Your legging's durability is below " + ConfigManager.getArmourPercentAlert() + "%! Make sure to repair it soon")));
				}
			}
			if (bootsSlot.getItem() instanceof ArmorItem) {
				if (!(lastBootsTickPercent <= ((double) ConfigManager.getArmourPercentAlert() / 100)) && bootsDuraPercent < ((double) ConfigManager.getArmourPercentAlert() / 100)) {
					if (ConfigManager.isSound()) mc.player.playSound(SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.MASTER, 1f, 1f);
					mc.getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT,
							Text.of("Armor Durability Low!"),
							Text.of("Your boot's durability is below " + ConfigManager.getArmourPercentAlert() + "%! Make sure to repair it soon")));
				}
			}
			
			/* MainServer.LOGGER.info(
					"\nHelmet Dura: " + helmetCurrentDura +
					"\nChest Dura: " + chestCurrentDura +
					"\nLeggings Dura: " + leggingsCurrentDura +
					"\nBoots Dura: " + bootsCurrentDura +
					"\nPercent helm: " + helmetDuraPercent + " and " + lastHelmetTickPercent +
					"\nPercent chest: " + chestDuraPercent + " and " + lastChestTickPercent +
					"\nPercent leggings: " + leggingsDuraPercent + " and " + lastLeggingsTickPercent +
					"\nPercent boots: " + bootsDuraPercent + " and " + lastBootsTickPercent
			); */
			
			lastHelmetTickPercent = helmetDuraPercent;
			lastChestTickPercent = chestDuraPercent;
			lastLeggingsTickPercent = leggingsDuraPercent;
			lastBootsTickPercent = bootsDuraPercent;
		}
	}
}