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
package org.dayflower.example;

import org.dayflower.change.Change;
import org.dayflower.change.ChangeHistory;

public class ChangeExample {
	public static void main(String[] args) {
		ChangeHistory changeHistory = new ChangeHistory();
		changeHistory.push(new Change(() -> System.out.println("Redo #1"), () -> System.out.println("Undo #1")), true);//Redo #1
		changeHistory.push(new Change(() -> System.out.println("Redo #2"), () -> System.out.println("Undo #2")), true);//Redo #2
		changeHistory.push(new Change(() -> System.out.println("Redo #3"), () -> System.out.println("Undo #3")), true);//Redo #3
		changeHistory.undo();//Undo #3
		changeHistory.undo();//Undo #2
		changeHistory.redo();//Redo #2
		changeHistory.redo();//Redo #3
	}
}