package dev.huskuraft.effortless.building.history;

import dev.huskuraft.effortless.building.operation.OperationResult;

import java.util.Stack;

public class OperationResultStack {

    private final Stack<OperationResult> undoStack = new Stack<>();
    private final Stack<OperationResult> redoStack = new Stack<>();

    public OperationResultStack() {
    }

    public OperationResult push(OperationResult result) {
        redoStack.clear();
        return undoStack.push(result);
    }

    public synchronized OperationResult undo() {
        return redoStack.push(undoStack.pop().getReverseOperation().commit());
    }

    public synchronized OperationResult redo() {
        return undoStack.push(redoStack.pop().getReverseOperation().commit());
    }

}
