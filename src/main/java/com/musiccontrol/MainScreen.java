package com.musiccontrol.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

/** Small helpers for building option buttons. */
final class Widgets {
	private Widgets() {
	}

	static OptionInstance<Boolean> toggle(String key, boolean value, Consumer<Boolean> onChange) {
		return OptionInstance.createBoolean(key, value, onChange::accept);
	}

	static OptionInstance<Boolean> toggle(String key, String tooltip, boolean value, Consumer<Boolean> onChange) {
		return OptionInstance.createBoolean(key, OptionInstance.cachedConstantTooltip(Component.translatable(tooltip)), value,
				onChange::accept);
	}

	/** A button that cycles through an enum. Value labels come from "{key}.{value}" in the language file. */
	static <E extends Enum<E>> OptionInstance<E> cycle(String key, Class<E> type, E value, Consumer<E> onChange) {
		List<E> values = Arrays.asList(type.getEnumConstants());
		Codec<E> codec = Codec.STRING.xmap(name -> Enum.valueOf(type, name), Enum::name);
		return new OptionInstance<>(key,
				OptionInstance.cachedConstantTooltip(Component.translatable(key + ".tooltip")),
				(caption, current) -> Component.translatable(key + "." + current.name().toLowerCase(Locale.ROOT)),
				new OptionInstance.Enum<>(values, codec),
				value,
				onChange::accept);
	}

	static Button button(Component label, Runnable onPress) {
		return Button.builder(label, b -> onPress.run()).size(150, 20).build();
	}

	static Button button(Component label, Component tooltip, Runnable onPress) {
		Button button = button(label, onPress);
		button.setTooltip(Tooltip.create(tooltip));
		return button;
	}
}
