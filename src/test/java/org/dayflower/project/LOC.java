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
package org.dayflower.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.macroing.java.io.Files;

public final class LOC {
	private LOC() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doRun("./src/main/java", "(java)");
		doRun("./src/test/java", "(java)");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doRun(final String directory, final String extension) {
		final
		Counter counter = new Counter(extension);
		counter.count(directory);
		
		final int charCount = counter.getCharCount();
		final int fileCount = counter.getFileCount();
		final int lineCount = counter.getLineCount();
		
		final
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(directory);
		stringBuilder.append("\n  Line Count: " + lineCount);
		stringBuilder.append("\n  File Count: " + fileCount);
		stringBuilder.append("\n  Char Count: " + charCount);
		
		System.out.println(stringBuilder);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Counter {
		private final List<List<String>> files;
		private final String extension;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Counter(final String extension) {
			this.files = new ArrayList<>();
			this.extension = extension;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public int getCharCount() {
			return this.files.stream().mapToInt(file -> file.stream().mapToInt(string -> string.length()).sum()).sum();
		}
		
		public int getFileCount() {
			return this.files.size();
		}
		
		public int getLineCount() {
			return this.files.stream().mapToInt(file -> file.size()).sum();
		}
		
		public void addFileLines(final List<String> fileLines) {
			this.files.add(fileLines);
		}
		
		@SuppressWarnings("unused")
		public void count(final String directory) {
			final List<File> files = Files.findFilesFromDirectory(new File(directory), currentFile -> currentFile.getName().matches(".*" + this.extension + "$"));
			
			for(final File file : files) {
				final List<String> fileLines = new ArrayList<>();
				
				addFileLines(fileLines);
				
				try(final BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
					while(bufferedReader.ready()) {
						fileLines.add(bufferedReader.readLine());
					}
				} catch(final IOException e) {
//					Do nothing!
				}
			}
		}
	}
}