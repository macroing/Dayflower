/**
 * Provides the Change API.
 * <p>
 * The Change API provides functionality to perform undo and redo operations.
 * <h3>Overview</h3>
 * <p>
 * The following list contains information about the classes and interfaces in this API.
 * <ul>
 * <li>{@link org.dayflower.change.Change Change} represents a change that can be undone and redone.</li>
 * <li>{@link org.dayflower.change.ChangeCombiner ChangeCombiner} combines multiple {@code Change} instances into a single {@code Change} instance.</li>
 * <li>{@link org.dayflower.change.ChangeHistory ChangeHistory} represents a history of changes.</li>
 * <li>{@link org.dayflower.change.RedoAction RedoAction} represents an action to be performed when redoing changes.</li>
 * <li>{@link org.dayflower.change.UndoAction UndoAction} represents an action to be performed when undoing changes.</li>
 * </ul>
 * <h3>Examples</h3>
 * <p>
 * The following example demonstrates how this API may be used.
 * <pre>
 * <code>
 * import org.dayflower.change.Change;
 * import org.dayflower.change.ChangeHistory;
 * 
 * public class Example {
 *     public static void main(String[] args) {
 *         ChangeHistory changeHistory = new ChangeHistory();
 *         changeHistory.push(new Change(() -&gt; System.out.println("Redo #1"), () -&gt; System.out.println("Undo #1")), true);//Redo #1
 *         changeHistory.push(new Change(() -&gt; System.out.println("Redo #2"), () -&gt; System.out.println("Undo #2")), true);//Redo #2
 *         changeHistory.push(new Change(() -&gt; System.out.println("Redo #3"), () -&gt; System.out.println("Undo #3")), true);//Redo #3
 *         changeHistory.undo();//Undo #3
 *         changeHistory.undo();//Undo #2
 *         changeHistory.redo();//Redo #2
 *         changeHistory.redo();//Redo #3
 *     }
 * }
 * </code>
 * </pre>
 */
package org.dayflower.change;