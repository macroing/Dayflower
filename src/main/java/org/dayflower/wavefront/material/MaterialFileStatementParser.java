/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
package org.dayflower.wavefront.material;

import java.util.Optional;

import org.dayflower.utility.TextLine;

/**
 * A {@code MaterialFileStatementParser} can be added to a {@link MaterialFileParser} in order to parse unsupported Wavefront Material statements.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface MaterialFileStatementParser {
	/**
	 * Parses {@code textLine} into an optional {@link MaterialFileStatement}.
	 * <p>
	 * Returns the optional {@code MaterialFileStatement}.
	 * 
	 * @param textLine a {@link TextLine} that contains the text to parse
	 * @return the optional {@code MaterialFileStatement}
	 */
	Optional<MaterialFileStatement> parse(final TextLine textLine);
}