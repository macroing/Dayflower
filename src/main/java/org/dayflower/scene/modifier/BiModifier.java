/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
 * 
 * This file is part of Dayflower.
 * 
 * Dayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Dayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.scene.modifier;

import java.util.Objects;

import org.dayflower.scene.Intersection;

/**
 * A {@code BiModifier} is a {@link Modifier} implementation that does what its two other {@code Modifier} instances does.
 * <p>
 * This class is immutable and thread-safe if both of its {@code Modifier} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BiModifier implements Modifier {
	/**
	 * The ID of this {@code BiModifier} class.
	 */
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifierA;
	private final Modifier modifierB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BiModifier} instance.
	 * <p>
	 * If either {@code modifierA} or {@code modifierB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param modifierA a {@link Modifier} instance
	 * @param modifierB a {@code Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code modifierA} or {@code modifierB} are {@code null}
	 */
	public BiModifier(final Modifier modifierA, final Modifier modifierB) {
		this.modifierA = Objects.requireNonNull(modifierA, "modifierA == null");
		this.modifierB = Objects.requireNonNull(modifierB, "modifierB == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Modifier} instance that is associated with this {@code BiModifier} instance and is denoted by {@code A}.
	 * 
	 * @return the {@code Modifier} instance that is associated with this {@code BiModifier} instance and is denoted by {@code A}
	 */
	public Modifier getModifierA() {
		return this.modifierA;
	}
	
	/**
	 * Returns the {@link Modifier} instance that is associated with this {@code BiModifier} instance and is denoted by {@code B}.
	 * 
	 * @return the {@code Modifier} instance that is associated with this {@code BiModifier} instance and is denoted by {@code B}
	 */
	public Modifier getModifierB() {
		return this.modifierB;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BiModifier} instance.
	 * 
	 * @return a {@code String} representation of this {@code BiModifier} instance
	 */
	@Override
	public String toString() {
		return String.format("new BiModifier(%s, %s)", this.modifierA, this.modifierB);
	}
	
	/**
	 * Compares {@code object} to this {@code BiModifier} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BiModifier}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BiModifier} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BiModifier}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BiModifier)) {
			return false;
		} else if(!Objects.equals(this.modifierA, BiModifier.class.cast(object).modifierA)) {
			return false;
		} else if(!Objects.equals(this.modifierB, BiModifier.class.cast(object).modifierB)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code BiModifier} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code BiModifier} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code BiModifier} instance.
	 * 
	 * @return a hash code for this {@code BiModifier} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.modifierA, this.modifierB);
	}
	
	/**
	 * Modifies the surface at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public void modify(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		this.modifierA.modify(intersection);
		this.modifierB.modify(intersection);
	}
}