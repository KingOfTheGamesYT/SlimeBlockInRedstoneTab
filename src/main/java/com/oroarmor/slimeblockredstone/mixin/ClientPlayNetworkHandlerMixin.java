package com.oroarmor.slimeblockredstone.mixin;

import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.slimeblockredstone.SlimeBlockInRedstoneMod;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.packet.s2c.play.SynchronizeTagsS2CPacket;
import net.minecraft.tag.RegistryTagManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Shadow
	@Final
	private RegistryTagManager tagManager;

	@SuppressWarnings("unused")
	@Inject(method = "onSynchronizeTags", at = @At("RETURN"), cancellable = true)
	private void updateItemGroups(SynchronizeTagsS2CPacket packet, CallbackInfo ci) {
		SlimeBlockInRedstoneMod.reload();

		for (Entry<RegistryKey<Item>, Item> entry : Registry.ITEM.getEntries()) {

			Item item = entry.getValue();
			Identifier id = entry.getKey().getValue();

			for (Entry<Identifier, ItemGroup> tagEntry : SlimeBlockInRedstoneMod.TAG_MAP.entrySet()) {
				boolean hasItemTag = tagManager.items().getTagsFor(item).contains(tagEntry.getKey());
				if (hasItemTag) {
					if (tagEntry.getValue() == null) {
						return;
					}
					((ItemGroupAccessor) item).setGroup(tagEntry.getValue());
					break;
				}
			}

			if (SlimeBlockInRedstoneMod.ITEM_MAP.containsKey(id)) {
				ItemGroup group = SlimeBlockInRedstoneMod.ITEM_MAP.get(id);
				if (group == null) {
					return;
				}
				((ItemGroupAccessor) item).setGroup(group);
			}
		}
	}
}
