package com.oroarmor.slimeblockredstone;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SlimeBlockInRedstoneMod implements ClientModInitializer {

	public static final Map<Identifier, ItemGroup> TAG_MAP = new HashMap<Identifier, ItemGroup>();
	private static final Map<String, ItemGroup> GROUP_MAP = generateGroupMap();
	public static final Map<Identifier, ItemGroup> ITEM_MAP = generateItemMap();

	private static Map<String, ItemGroup> generateGroupMap() {
		Map<String, ItemGroup> groupMap = new HashMap<String, ItemGroup>();

		File groupConfigFile = new File(FabricLoader.getInstance().getConfigDirectory().getAbsolutePath(),
				"slimeblock_group_config.txt");

		if (groupConfigFile.exists()) {
			try {
				FileInputStream stream = new FileInputStream(groupConfigFile);
				byte[] bytes = new byte[stream.available()];
				stream.read(bytes);

				String file = new String(bytes);

				String[] lines = file.split("\\n");

				for (String line : lines) {
					String[] parts = line.split(":");
					if (line.trim().startsWith("//") || parts.length != 4) {
						continue;
					}
					FabricItemGroupBuilder.build(
							new Identifier(parts[0].trim().toLowerCase(), parts[1].trim().toLowerCase()),
							() -> new ItemStack(Registry.ITEM.get(new Identifier(parts[2].trim(), parts[3].trim()))));
				}
				stream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				groupConfigFile.createNewFile();
				FileOutputStream stream = new FileOutputStream(groupConfigFile);
				stream.write(("// Format:\n" + "// your_namespace:group : namespace:logo_item\n").getBytes());
				stream.write(("\nslimeblock:DOORS : minecraft:oak_door").getBytes());
				stream.close();

				return generateGroupMap();
			} catch (Exception e) {
			}
		}

		for (ItemGroup group : ItemGroup.GROUPS) {
			groupMap.put(group.getName().toUpperCase(), group);
		}
		return groupMap;
	}

	private static Map<Identifier, ItemGroup> generateItemMap() {
		File itemConfigFile = new File(FabricLoader.getInstance().getConfigDirectory().getAbsolutePath(),
				"slimeblock_item_config.txt");
		Map<Identifier, ItemGroup> itemMap = new HashMap<Identifier, ItemGroup>();

		try {
			if (!itemConfigFile.exists()) {
				itemConfigFile.createNewFile();
				FileOutputStream stream = new FileOutputStream(itemConfigFile);
				stream.write("// Format:\n// namespace:item : (NAMESPACE).GROUP\n\n".getBytes());
				stream.write("// Possible groups:\n".getBytes());
				for (String key : GROUP_MAP.keySet()) {
					stream.write(("// " + key + "\n").getBytes());
				}
				stream.write("\nminecraft:slime_block : REDSTONE".getBytes());
				stream.write("\nminecraft:honey_block : REDSTONE".getBytes());

				String[] types = new String[] { "spruce", "birch", "jungle", "acacia", "dark_oak", "crimson",
						"warped" };
				String[] blocks = new String[] { "trapdoor", "fence_gate", "button", "door", "pressure_plate" };

				for (String type : types) {
					for (String block : blocks) {
						stream.write(("\nminecraft:" + type + "_" + block + " : SLIMEBLOCK.DOORS").getBytes());
					}
				}

				stream.write("\nminecraft:polished_blackstone_button : SLIMEBLOCK.DOORS".getBytes());
				stream.write("\nminecraft:polished_blackstone_pressure_plate : SLIMEBLOCK.DOORS".getBytes());

				stream.close();
				return generateItemMap();
			} else {
				FileInputStream stream = new FileInputStream(itemConfigFile);
				byte[] bytes = new byte[stream.available()];
				stream.read(bytes);

				String file = new String(bytes);

				String[] lines = file.split("\\n");

				for (String line : lines) {
					String[] parts = line.split(":");
					if (line.trim().startsWith("//") || parts.length != 3) {
						continue;
					}

					if (parts[0].trim().startsWith("#")) {
						TAG_MAP.put(
								new Identifier(parts[0].trim().substring(1, parts[0].trim().length()), parts[1].trim()),
								GROUP_MAP.getOrDefault(parts[2].trim().toUpperCase(), null));
					} else {
						itemMap.put(new Identifier(parts[0].trim(), parts[1].trim()),
								GROUP_MAP.getOrDefault(parts[2].trim().toUpperCase(), null));
					}
				}
				stream.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemMap;
	}

	@Override
	public void onInitializeClient() {
	}

}
