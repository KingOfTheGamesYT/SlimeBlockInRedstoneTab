package com.oroarmor.slimeblockredstone.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

@Mixin(Items.class)
public abstract class ItemsMixin {

	@Inject(method = "register(Lnet/minecraft/block/Block;Lnet/minecraft/item/ItemGroup;)Lnet/minecraft/item/Item;", at = @At("HEAD"), cancellable = true)
	private static void register(Block block, ItemGroup group, CallbackInfoReturnable<Item> cir) {
		if (block == Blocks.SLIME_BLOCK) {
			cir.setReturnValue(register(new BlockItem(block, new Item.Settings().group(ItemGroup.REDSTONE))));
		}
	}

	private static Item register(BlockItem item) {
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return Registry.register(Registry.ITEM, Registry.BLOCK.getId(item.getBlock()), item);
	}

}
