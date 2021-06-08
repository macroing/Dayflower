The Change API
==============
The Change API provides functionality to perform undo and redo operations.

Supported Features
------------------
* `Change` represents a change that can be undone and redone.
* `ChangeCombiner` combines multiple `Change` instances into a single `Change` instance.
* `ChangeHistory` represents a history of changes.
* `RedoAction` represents an action to be performed when redoing changes.
* `UndoAction` represents an action to be performed when undoing changes.

Packages
--------
* `org.dayflower.change` - The Change API.

Examples
--------
The following example demonstrates how this API may be used.
```java
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
```