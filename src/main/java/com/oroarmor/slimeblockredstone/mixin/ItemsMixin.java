package com.oroarmor.slimeblockredstone.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.oroarmor.slimeblockredstone.SlimeBlockInRedstoneMod;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Mixin(Items.class)
public abstract class ItemsMixin {

	@Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("HEAD"), cancellable = true)
	private static void register(Identifier id, Item item, CallbackInfoReturnable<Item> cir) {
		if (item instanceof BlockItem) {
			((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
		}

		if (SlimeBlockInRedstoneMod.ITEM_MAP.containsKey(id)) {
			ItemGroup group = SlimeBlockInRedstoneMod.ITEM_MAP.get(id);
			if (group == null) {
				return;
			}
			((ItemGroupAccessor) item).setGroup(group);
		}

		cir.setReturnValue(Registry.register(Registry.ITEM, id, item));
	}

}
