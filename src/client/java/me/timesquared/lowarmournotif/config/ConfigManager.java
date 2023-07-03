package me.timesquared.lowarmournotif.config;

import me.timesquared.lowarmournotif.MainServer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
	public static boolean sound = true;
	public static int armourPercentAlert = 15;
	
	static String configPath = FabricLoader.getInstance().getConfigDir().toString() + "\\lowarmornotifs.properties";
	
	public static void setSound(boolean sound) {
		ConfigManager.sound = sound;
	}
	public static void setArmourPercentAlert(int armourPercentAlert) {
		ConfigManager.armourPercentAlert = armourPercentAlert;
	}
	
	public static boolean isSound() {
		return sound;
	}
	public static int getArmourPercentAlert() {
		return armourPercentAlert;
	}
	
	
	public static void save() throws IOException {
		MainServer.LOGGER.info("Saving config to path '" + configPath + "'...");
		
		if (!fileExists(configPath)) {
			Properties newProperties = dataToProperty();
			MainServer.LOGGER.warn("Config file does not exist, creating a new one with default config data.");
			createFileWithProperties(newProperties, configPath);
		}
		
		Properties newProperties = dataToProperty();
		createFileWithProperties(newProperties, configPath);
	}
	
	public static void load() throws IOException {
		MainServer.LOGGER.info("Loading config from path '" + configPath + "'...");
		
		if (!fileExists(configPath)) {
			MainServer.LOGGER.warn("Config file does not exist, creating a new one with default config data.");
			Properties properties = dataToProperty();
			createFileWithProperties(properties, configPath);
		}
		Properties properties = new Properties();
		
		File file = new File(configPath);
		
		properties.load(new FileInputStream(file));
		
		loadFromProperty(properties);
	}
	
	public static void loadFromProperty(Properties properties) {
		armourPercentAlert = Integer.parseInt(properties.getProperty("armourPercentAlert"));
		sound = Boolean.parseBoolean(properties.getProperty("sounds"));
	}
	
	public static Properties dataToProperty() {
		Properties newProperties = new Properties();
		
		newProperties.setProperty("armourPercentAlert", String.valueOf(armourPercentAlert));
		newProperties.setProperty("sounds", String.valueOf(sound));
		
		return newProperties;
	}
	
	public static void createFileWithProperties(Properties properties, String path) throws IOException {
		File configFile = new File(path);
		
		if (!configFile.exists()) {
			configFile.createNewFile();
		}
		
		properties.store(new FileWriter(path), null);
	}
	
	public static boolean fileExists(String path) throws IOException {
		File file = new File(path);
		
		return file.exists();
	}
}
