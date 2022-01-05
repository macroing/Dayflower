/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.javafx.scene.layout;

import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code HBox} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class HBoxes {
	private HBoxes() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new bordered, padded and spaced {@code HBox} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * HBoxes.createBorderedPaddedAndSpacedHBox(borderWidthTop, borderWidthRight, borderWidthBottom, borderWidthLeft, 10.0D);
	 * }
	 * </pre>
	 * 
	 * @param borderWidthTop the top border width
	 * @param borderWidthRight the right border width
	 * @param borderWidthBottom the bottom border width
	 * @param borderWidthLeft the left border width
	 * @return a new bordered, padded and spaced {@code HBox} instance
	 */
	public static HBox createBorderedPaddedAndSpacedHBox(final double borderWidthTop, final double borderWidthRight, final double borderWidthBottom, final double borderWidthLeft) {
		return createBorderedPaddedAndSpacedHBox(borderWidthTop, borderWidthRight, borderWidthBottom, borderWidthLeft, 10.0D);
	}
	
	/**
	 * Returns a new bordered, padded and spaced {@code HBox} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * HBoxes.createBorderedPaddedAndSpacedHBox(borderWidthTop, borderWidthRight, borderWidthBottom, borderWidthLeft, padding, 20.0D);
	 * }
	 * </pre>
	 * 
	 * @param borderWidthTop the top border width
	 * @param borderWidthRight the right border width
	 * @param borderWidthBottom the bottom border width
	 * @param borderWidthLeft the left border width
	 * @param padding the top, right, bottom and left padding
	 * @return a new bordered, padded and spaced {@code HBox} instance
	 */
	public static HBox createBorderedPaddedAndSpacedHBox(final double borderWidthTop, final double borderWidthRight, final double borderWidthBottom, final double borderWidthLeft, final double padding) {
		return createBorderedPaddedAndSpacedHBox(borderWidthTop, borderWidthRight, borderWidthBottom, borderWidthLeft, padding, 20.0D);
	}
	
	/**
	 * Returns a new bordered, padded and spaced {@code HBox} instance.
	 * 
	 * @param borderWidthTop the top border width
	 * @param borderWidthRight the right border width
	 * @param borderWidthBottom the bottom border width
	 * @param borderWidthLeft the left border width
	 * @param padding the top, right, bottom and left padding
	 * @param spacing the spacing
	 * @return a new bordered, padded and spaced {@code HBox} instance
	 */
	public static HBox createBorderedPaddedAndSpacedHBox(final double borderWidthTop, final double borderWidthRight, final double borderWidthBottom, final double borderWidthLeft, final double padding, final double spacing) {
		final
		HBox hBox = new HBox();
		hBox.setBorder(new Border(new BorderStroke(Color.rgb(181, 181, 181), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(borderWidthTop, borderWidthRight, borderWidthBottom, borderWidthLeft))));
		hBox.setPadding(new Insets(padding, padding, padding, padding));
		hBox.setSpacing(spacing);
		
		return hBox;
	}
}