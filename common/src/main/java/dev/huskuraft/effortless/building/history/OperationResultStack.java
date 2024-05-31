package dev.huskuraft.effortless.building.history;

import java.util.Stack;

import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.session.BuildSession;

public class OperationResultStack {

    private final Stack<OperationResult> undoStack = new Stack<>();
    private final Stack<OperationResult> redoStack = new Stack<>();

    public OperationResultStack() {
    }

    public OperationResult pushSession(BuildSession buildSession) {
        redoStack.clear();
        return undoStack.push(buildSession.commit());
    }

    public synchronized OperationResult undo() {
        return redoStack.push(undoStack.pop().getReverseOperation().commit());
    }

    public synchronized OperationResult redo() {
        return undoStack.push(redoStack.pop().getReverseOperation().commit());
    }

    public int undoSize() {
        return undoStack.size();
    }

    public int redoSize() {
        return redoStack.size();
    }

}
