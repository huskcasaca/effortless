package dev.ftb.mods.ftbteams.api.property;

import java.util.function.BiConsumer;

/**
 * Represents the collection of properties that a team has. All teams have the same properties, but of course
 * the values of each property will vary from team to team.
 * <p>
 * Instances of this class can be retrieved via {@link dev.ftb.mods.ftbteams.api.Team#getProperties()}.
 * </p>
 */
public interface TeamPropertyCollection {
	/**
	 * Iterate over all the properties in this collection,
	 * @param consumer the consumer to call for each property and value pair
	 * @param <T> the property type
	 */
	<T> void forEach(BiConsumer<TeamProperty<T>, TeamPropertyValue<T>> consumer);

	/**
	 * Create a new TeamPropertyCollection, which is a copy of this one.
	 *
	 * @return a copy of this collection
	 */
	TeamPropertyCollection copy();

	/**
	 * Update this collection's properties from the supplied property collection.
	 *
	 * @param otherProperties the collection to update from
	 */
	void updateFrom(TeamPropertyCollection otherProperties);

	/**
	 * Retrieve the value for the given property. All built-in properties are available at {@link TeamProperties}, but
	 * other mods may register extra properties (via {@link dev.ftb.mods.ftbteams.api.event.TeamCollectPropertiesEvent}).
	 *
	 * @param key the property to retrieve
	 * @return the value for this property
	 * @param <T> the property type
	 */
	<T> T get(TeamProperty<T> key);

	/**
	 * Set a new value for the given property.
	 *
	 * @param key the property to update
	 * @param value the new value
	 * @param <T> the property type
	 */
	<T> void set(TeamProperty<T> key, T value);

	/**
	 * Get the number of properties in this collection.
	 *
	 * @return the number of properties
	 */
	int size();
}
