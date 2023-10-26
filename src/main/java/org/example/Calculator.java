package org.example;

import java.util.Stack;

public class Calculator {
    /**
     * 当前计算结果
     */
    private double currentValue;
    /**
     * 历史操作栈
     */
    private final Stack<Command> history;
    /**
     * redo操作栈
     */
    private final Stack<Command> redoStack;

    public Calculator() {
        currentValue = 0.0;
        history = new Stack<>();
        redoStack = new Stack<>();
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void executeOperation(Operation operation, double operand) {
        Command command = new Command(operation, operand);
        command.execute();
        history.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            history.push(command);
        }
    }

    private class Command {
        private final Operation operation;
        private final double operand;
        private double previousValue;

        public Command(Operation operation, double operand) {
            this.operation = operation;
            this.operand = operand;
        }

        public void execute() {
            previousValue = currentValue;
            switch (operation) {
                case ADD:
                    currentValue += operand;
                    break;
                case SUBTRACT:
                    currentValue -= operand;
                    break;
                case MULTIPLY:
                    currentValue *= operand;
                    break;
                case DIVIDE:
                    if (operand != 0) {
                        currentValue /= operand;
                    }
                    break;
            }
        }

        public void undo() {
            currentValue = previousValue;
        }
    }

    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.executeOperation(Calculator.Operation.ADD, 8);
        calculator.executeOperation(Calculator.Operation.SUBTRACT, 3);
        calculator.executeOperation(Calculator.Operation.MULTIPLY, 4);
        calculator.executeOperation(Calculator.Operation.DIVIDE, 2);

        System.out.println("Current Result: " + calculator.getCurrentValue());  // 输出：Current Result: 10.0

        calculator.undo();  // 撤销除法操作
        System.out.println("After Undo: " + calculator.getCurrentValue());  // 输出：After Undo: 20.0

        calculator.redo();  // 重做除法操作
        System.out.println("After Redo: " + calculator.getCurrentValue());  // 输出：After Redo: 10.0
    }
}
