package dev.ftb.mods.ftbteams.api.property;

import java.util.function.Consumer;

public final class TeamPropertyValue<T> {
	private final TeamProperty<T> property;
	T value;
	final Consumer<T> consumer;

	public TeamPropertyValue(TeamProperty<T> property, T value) {
		this.property = property;
		this.value = value;
		consumer = val -> this.value = val;
	}

	public TeamPropertyValue(TeamProperty<T> k) {
		this(k, k.getDefaultValue());
	}

	public TeamProperty<T> getProperty() {
		return property;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public TeamPropertyValue<T> copy() {
		return new TeamPropertyValue<>(property, value);
	}

	@Override
	public String toString() {
		return property.id + ":" + value;
	}
}
