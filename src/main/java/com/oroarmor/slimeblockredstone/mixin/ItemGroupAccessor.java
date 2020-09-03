package com.oroarmor.slimeblockredstone.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

@Mixin(Item.class)
public interface ItemGroupAccessor {

	@Accessor("group")
	public void setGroup(ItemGroup group);
}
