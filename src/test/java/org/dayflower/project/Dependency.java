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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.macroing.java.io.Files;

public final class Dependency {
	private Dependency() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		Module.generate(new File("./src/main/java"), true);
		Module.findModules("org.dayflower").forEach(module -> module.print());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Module implements Comparable<Module> {
		private static final Map<String, Module> MODULES = new LinkedHashMap<>();
		private static final Pattern IMPORT_DECLARATION = Pattern.compile("import(\\s+static)?\\s+((\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)(\\s*\\.\\s*(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*))*)(\\s*\\.\\s*\\*)?\\s*;");
		private static final Pattern PACKAGE_DECLARATION = Pattern.compile("package\\s+((\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)(\\s*\\.\\s*(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*))*)\\s*;");
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private final Set<Module> modules;
		private final String name;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Module(final String name) {
			this.modules = new LinkedHashSet<>();
			this.name = Objects.requireNonNull(name, "name == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Set<String> getNames(final boolean isIncludingAll) {
			final List<String> names = new ArrayList<>();
			
			for(final Module module : this.modules) {
				names.add(module.getName());
				
				if(isIncludingAll) {
					names.addAll(module.getNames(isIncludingAll));
				}
			}
			
			Collections.sort(names);
			
			return new LinkedHashSet<>(names);
		}
		
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Module)) {
				return false;
			} else if(!Objects.equals(this.name, Module.class.cast(object).name)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public int compareTo(final Module module) {
			return this.name.compareTo(module.name);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(this.name);
		}
		
		public void addModule(final Module module) {
			this.modules.add(Objects.requireNonNull(module, "module == null"));
		}
		
		public void print() {
			System.out.println(getName());
			
			for(final String name : getNames(true)) {
				System.out.println(" - " + name);
			}
		}
		
		public void sort() {
			final List<Module> modules = new ArrayList<>(this.modules);
			
			Collections.sort(modules);
			
			this.modules.clear();
			this.modules.addAll(modules);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public static List<Module> findModules(final String namePrefix) {
			Objects.requireNonNull(namePrefix, "namePrefix == null");
			
			final List<Module> modules = new ArrayList<>();
			
			for(final Entry<String, Module> entry : MODULES.entrySet()) {
				if(entry.getValue().getName().startsWith(namePrefix)) {
					modules.add(entry.getValue());
				}
			}
			
			Collections.sort(modules);
			
			return modules;
		}
		
		public static Module getModule(final String name) {
			return MODULES.computeIfAbsent(Objects.requireNonNull(name, "name == null"), newName -> new Module(newName));
		}
		
		public static void generate(final File directory, final boolean isDiscardingJavaAPIs) {
			final List<File> files = Files.findFilesFromDirectory(directory, currentFile -> currentFile.getName().matches(".*(java)$"));
			
			for(final File file : files) {
				final String string = Files.readAllBytesToString(file);
				
				final Optional<String> optionalPackageName = doFindPackageName(string);
				
				if(optionalPackageName.isPresent()) {
					final String packageName = optionalPackageName.get();
					
					final Module module = getModule(packageName);
					
					final List<String> importedTypeNames = doFindImportedTypeNames(string);
					
					for(final String importedTypeName : importedTypeNames) {
						final String importedPackageName = doToImportedPackageName(importedTypeName);
						
						if(importedPackageName.equals(packageName) || isDiscardingJavaAPIs && doIsJavaAPI(importedPackageName)) {
							continue;
						}
						
						module.addModule(getModule(importedPackageName));
					}
				}
			}
			
			for(final Module module : MODULES.values()) {
				module.sort();
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private static List<String> doFindImportedTypeNames(final String string) {
			final List<String> importedTypeNames = new ArrayList<>();
			
			final Matcher matcher = IMPORT_DECLARATION.matcher(Objects.requireNonNull(string, "string == null"));
			
			while(matcher.find()) {
				importedTypeNames.add(matcher.group(2));
			}
			
			return importedTypeNames;
		}
		
		private static Optional<String> doFindPackageName(final String string) {
			final Matcher matcher = PACKAGE_DECLARATION.matcher(Objects.requireNonNull(string, "string == null"));
			
			if(matcher.find()) {
				return Optional.of(matcher.group(1));
			}
			
			return Optional.empty();
		}
		
		private static String doToImportedPackageName(final String importedTypeName) {
			final String[] identifiers = importedTypeName.split("\\s*\\.\\s*");
			
			final StringBuilder stringBuilder = new StringBuilder();
			
			for(int i = 0; i < identifiers.length; i++) {
				final String identifier = identifiers[i].trim();
				
				if(identifier.length() == 0 || Character.isUpperCase(identifier.charAt(0))) {
					break;
				}
				
				stringBuilder.append(i > 0 ? "." : "");
				stringBuilder.append(identifier);
			}
			
			return stringBuilder.toString();
		}
		
		private static boolean doIsJavaAPI(final String packageName) {
			return packageName.startsWith("java.") || packageName.startsWith("javafx.") || packageName.startsWith("javax.");
		}
	}
}