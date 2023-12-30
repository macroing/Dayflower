/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
package org.dayflower.wavefront.object;

/**
 * An {@code ObjectFileParserObserver} can be added to an {@link ObjectFileParser} in order to observe its processed {@link ObjectFileStatement}s.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface ObjectFileParserObserver {
	/**
	 * Called by an {@link ObjectFileParser} when it has processed {@code objectFileStatement}.
	 * 
	 * @param objectFileStatement the {@link ObjectFileStatement} that has been processed
	 */
	void onObjectFileStatement(final ObjectFileStatement objectFileStatement);
}