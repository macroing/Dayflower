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
package org.dayflower.utility;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.macroing.java.lang.Strings;

/**
 * A class that encapsulates a document that can be written to.
 * <p>
 * One of the main goals with this class, is to create {@code String}s containing well-formatted text, that can subsequently be written to disk etc.
 * <p>
 * To write a Hello World program in Java, consider the following example:
 * <pre>
 * Document document = new Document();
 * document.line("public class HelloWorld {", Indentation.INDENT);
 * document.line("public static void main(String[] args) {", Indentation.INDENT);
 * document.line("System.out.println(\"Hello, World!\");", Indentation.OUTDENT);
 * document.line("}", Indentation.OUTDENT);
 * document.line("}");
 * 
 * System.out.print(document);
 * </pre>
 * <p>
 * This class is thread-safe and thus suitable for concurrent use without external synchronization. Although, calling any of the methods, with the exception for {@code toString()}, from multiple {@code Thread}s at the same
 * time, may produce unexpected results.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Document {
	private static final String INDENTATION_STRING = "\t";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String LINE_SEPARATOR_LINUX = "\n";
	private static final String LINE_SEPARATOR_WINDOWS = "\r\n";
	private static final String REGEX_LINE_SEPARATOR = String.format("%s|%s", LINE_SEPARATOR_WINDOWS, LINE_SEPARATOR_LINUX);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<Node> nodes = new ArrayList<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Document} instance.
	 */
	public Document() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs an indentation operation on this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * 
	 * @return the {@code Document} itself, such that method chaining is possible
	 */
//	TODO: Add Unit Tests!
	public Document indent() {
		synchronized(this.nodes) {
			this.nodes.add(new IndentationNode());
		}
		
		return this;
	}
	
	/**
	 * Appends an empty line of text to this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * <p>
	 * Calling this method is equivalent to calling {@code line("")}.
	 * 
	 * @return the {@code Document} itself, such that method chaining is possible
	 */
//	TODO: Add Unit Tests!
	public Document line() {
		return line("");
	}
	
	/**
	 * Appends an empty line of text to this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * <p>
	 * If {@code indentation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to calling {@code line("", indentation)}.
	 * 
	 * @param indentation the indentation operation to be used at the end
	 * @return the {@code Document} itself, such that method chaining is possible
	 * @throws NullPointerException thrown if, and only if, {@code indentation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Document line(final Indentation indentation) {
		return line("", indentation);
	}
	
	/**
	 * Appends a line of text to this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * <p>
	 * If {@code textAfterIndentation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The line of text consists of {@code textAfterIndentation}, which is appended after the indentation of the given line.
	 * <p>
	 * If {@code textAfterIndentation} contains one or more line separators, it will be split into multiple lines of text.
	 * <p>
	 * Calling this method is equivalent to calling {@code line(textAfterIndentation, "")}.
	 * 
	 * @param textAfterIndentation the text to append after the indentation
	 * @return the {@code Document} itself, such that method chaining is possible
	 * @throws NullPointerException thrown if, and only if, {@code textAfterIndentation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Document line(final String textAfterIndentation) {
		return line(textAfterIndentation, "");
	}
	
	/**
	 * Appends a line of text to this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * <p>
	 * If either {@code textAfterIndentation} or {@code indentation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The line of text consists of {@code textAfterIndentation}, which is appended after the indentation of the given line.
	 * <p>
	 * If {@code textAfterIndentation} contains one or more line separators, it will be split into multiple lines of text.
	 * <p>
	 * Calling this method is equivalent to calling {@code line(textAfterIndentation, "", indentation)}.
	 * 
	 * @param textAfterIndentation the text to append after the indentation
	 * @param indentation the indentation operation to be used at the end
	 * @return the {@code Document} itself, such that method chaining is possible
	 * @throws NullPointerException thrown if, and only if, either {@code textAfterIndentation} or {@code indentation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Document line(final String textAfterIndentation, final Indentation indentation) {
		return line(textAfterIndentation, "", indentation);
	}
	
	/**
	 * Appends a line of text to this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * <p>
	 * If either {@code textAfterIndentation} or {@code textBeforeIndentation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The line of text consists of three parts. The first part comes from {@code textBeforeIndentation}. The second part is the indentation itself, if any. The third part comes from {@code textAfterIndentation}.
	 * <p>
	 * If either {@code textAfterIndentation} or {@code textBeforeIndentation} contains one or more line separators, they will be split into multiple lines of text. The one of the two with most line separators will decide
	 * how many lines to create. If one of the two has less line separators than the other, an empty {@code String} literal will be used where no corresponding text can be found.
	 * <p>
	 * Calling this method is equivalent to calling {@code line(textAfterIndentation, textBeforeIndentation, Indentation.SKIP)}.
	 * 
	 * @param textAfterIndentation the text to append after the indentation
	 * @param textBeforeIndentation the text to prepend before the indentation
	 * @return the {@code Document} itself, such that method chaining is possible
	 * @throws NullPointerException thrown if, and only if, either {@code textAfterIndentation} or {@code textBeforeIndentation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Document line(final String textAfterIndentation, final String textBeforeIndentation) {
		return line(textAfterIndentation, textBeforeIndentation, Indentation.SKIP);
	}
	
	/**
	 * Appends a line of text to this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * <p>
	 * If either {@code textAfterIndentation}, {@code textBeforeIndentation} or {@code indentation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The line of text consists of three parts. The first part comes from {@code textBeforeIndentation}. The second part is the indentation itself, if any. The third part comes from {@code textAfterIndentation}.
	 * <p>
	 * If either {@code textAfterIndentation} or {@code textBeforeIndentation} contains one or more line separators, they will be split into multiple lines of text. The one of the two with most line separators will decide
	 * how many lines to create. If one of the two has less line separators than the other, an empty {@code String} literal will be used where no corresponding text can be found.
	 * <p>
	 * The parameter {@code indentation} is used to perform an optional indent- or outdent operation at the end. To perform an indent operation, use {@code Indentation.INDENT}. To perform an outdent operation, use
	 * {@code Indentation.OUTDENT}. To perform no indent- or outdent operation, use {@code Indentation.SKIP}.
	 * 
	 * @param textAfterIndentation the text to append after the indentation
	 * @param textBeforeIndentation the text to prepend before the indentation
	 * @param indentation the indentation operation to be used at the end
	 * @return the {@code Document} itself, such that method chaining is possible
	 * @throws NullPointerException thrown if, and only if, either {@code textAfterIndentation}, {@code textBeforeIndentation} or {@code indentation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Document line(final String textAfterIndentation, final String textBeforeIndentation, final Indentation indentation) {
		final String[] textsAfterIndentation = textAfterIndentation.split(REGEX_LINE_SEPARATOR);
		final String[] textsBeforeIndentation = textBeforeIndentation.split(REGEX_LINE_SEPARATOR);
		
		final int maximumLines = Math.max(textsAfterIndentation.length, textsBeforeIndentation.length);
		
		final LineNode[] lineNodes = new LineNode[maximumLines];
		
		for(int i = 0; i < maximumLines; i++) {
			lineNodes[i] = new LineNode(i < textsAfterIndentation.length ? textsAfterIndentation[i] : "", i < textsBeforeIndentation.length ? textsBeforeIndentation[i] : "");
		}
		
		synchronized(this.nodes) {
			for(final LineNode lineNode : lineNodes) {
				this.nodes.add(lineNode);
			}
		}
		
		switch(indentation) {
			case INDENT:
				indent();
				
				break;
			case OUTDENT:
				outdent();
				
				break;
			case SKIP:
			default:
				break;
		}
		
		return this;
	}
	
	/**
	 * Appends a line of text to this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * <p>
	 * If either {@code textAfterIndentationFormat} or {@code objects} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The line of text consists of {@code String.format(textAfterIndentationFormat, objects)}, which is appended after the indentation of the given line.
	 * <p>
	 * If {@code textAfterIndentation} contains one or more line separators, it will be split into multiple lines of text.
	 * <p>
	 * Calling this method is equivalent to calling {@code line(String.format(textAfterIndentationFormat, objects))}, with the exception that {@code null} references are not allowed.
	 * 
	 * @param textAfterIndentationFormat a format {@code String} for the text to append after the indentation
	 * @param objects an array of {@code Object}s to use in the format {@code String}
	 * @return the {@code Document} itself, such that method chaining is possible
	 * @throws NullPointerException thrown if, and only if, either {@code textAfterIndentationFormat} or {@code objects} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Document linef(final String textAfterIndentationFormat, final Object... objects) {
		return line(String.format(Objects.requireNonNull(textAfterIndentationFormat, "textAfterIndentationFormat == null"), Objects.requireNonNull(objects, "objects == null")));
	}
	
	/**
	 * Performs an outdentation operation on this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * 
	 * @return the {@code Document} itself, such that method chaining is possible
	 */
//	TODO: Add Unit Tests!
	public Document outdent() {
		synchronized(this.nodes) {
			this.nodes.add(new OutdentationNode());
		}
		
		return this;
	}
	
	/**
	 * Appends text to this {@code Document} instance.
	 * <p>
	 * Returns the {@code Document} itself, such that method chaining is possible.
	 * <p>
	 * If {@code text} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param text the text to append
	 * @return the {@code Document} itself, such that method chaining is possible
	 * @throws NullPointerException thrown if, and only if, {@code text} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Document text(final String text) {
		synchronized(this.nodes) {
			this.nodes.add(new TextNode(Objects.requireNonNull(text, "text == null")));
		}
		
		return this;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Document} instance.
	 * <p>
	 * The {@code String} representation is the text that you appended to it with all lines and indentations etc.
	 * <p>
	 * This method and the {@code String} returned are both part of the contract for this class and will not change.
	 * 
	 * @return a {@code String} representation of this {@code Document} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		
		int indentation = 0;
		
		synchronized(this.nodes) {
			for(final Node node : this.nodes) {
				if(node instanceof IndentationNode) {
					indentation++;
				} else if(node instanceof LineNode) {
					final String string = stringBuilder.toString();
					
					if(string.length() > 0) {
						stringBuilder.append(LINE_SEPARATOR);
					}
					
					final LineNode lineNode = LineNode.class.cast(node);
					
					stringBuilder.append(lineNode.getTextBeforeIndentation());
					stringBuilder.append(Strings.repeat(INDENTATION_STRING, indentation));
					stringBuilder.append(lineNode.getTextAfterIndentation());
				} else if(node instanceof OutdentationNode) {
					indentation--;
					
					if(indentation < 0) {
						indentation = 0;
					}
				} else if(node instanceof TextNode) {
					final TextNode textNode = TextNode.class.cast(node);
					
					stringBuilder.append(textNode.getText());
				}
			}
		}
		
		return stringBuilder.toString();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * An {@code Indentation} is used by some methods in a {@link Document} instance to perform optional indent and outdent operations.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static enum Indentation {
		/**
		 * Represents an indent operation.
		 */
		INDENT,
		
		/**
		 * Represents an outdent operation.
		 */
		OUTDENT,
		
		/**
		 * Represents no indent or outdent operation.
		 */
		SKIP;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Indentation() {
			
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class IndentationNode implements Node {
		public IndentationNode() {
			
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class LineNode implements Node {
		private final String textAfterIndentation;
		private final String textBeforeIndentation;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public LineNode(final String textAfterIndentation, final String textBeforeIndentation) {
			this.textAfterIndentation = Objects.requireNonNull(textAfterIndentation, "textAfterIndentation == null");
			this.textBeforeIndentation = Objects.requireNonNull(textBeforeIndentation, "textBeforeIndentation == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public String getTextAfterIndentation() {
			return this.textAfterIndentation;
		}
		
		public String getTextBeforeIndentation() {
			return this.textBeforeIndentation;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static interface Node {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class OutdentationNode implements Node {
		public OutdentationNode() {
			
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class TextNode implements Node {
		private final String text;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public TextNode(final String text) {
			this.text = Objects.requireNonNull(text, "text == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public String getText() {
			return this.text;
		}
	}
}