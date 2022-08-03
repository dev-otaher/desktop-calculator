package calculator;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Calculator extends JFrame {
    final List<Character> operators;
    JLabel resultLabel;
    JLabel equationLabel;

    {
        resultLabel = new JLabel("0");
        resultLabel.setName("ResultLabel");
        resultLabel.setBounds(50, 20, 80, 20);

        equationLabel = new JLabel();
        equationLabel.setName("EquationLabel");
        equationLabel.setBounds(50, 35, 195, 20);

        operators = new ArrayList<>(List.of('\u002B', '-', '\u00D7', '\u00F7'));
    }

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLayout(null);
        add(equationLabel);
        add(resultLabel);
        for (JButton button : createButtons()) {
            add(button);
        }
        setVisible(true);
    }

    private float calculate(float a, float b, char op) {
        switch (op) {
            case '\u002B':
                return a + b;
            case '-':
                return a - b;
            case '\u00D7':
                return a * b;
            case '\u00F7':
                return a / b;
            default:
                throw new ArithmeticException("Invalid Operator!");
        }
    }

    private boolean isOperator(Character op) {
        return operators.contains(op);
    }

    private boolean isOperator(String op) {
        if (op.length() > 1) return false;
        return operators.contains(op.toCharArray()[0]);
    }

    private boolean isButtonOperator(JButton button) {
        return operators.contains(button.getText().toCharArray()[0]);
    }

    private boolean isEquationEndByOp() {
        String text = equationLabel.getText();
        int length = text.length();
        return operators.contains(text.charAt(length - 1));
    }

    private int getOpPrecedence(Character op) {
        switch (op) {
            case '\u002B':
            case '-':
                return 1;
            case '\u00D7':
            case '\u00F7':
                return 2;
            default:
                return 0;
        }
    }

    private Deque<String> convertInfixToPostfix(String equation) {
        Deque<Character> opStack = new ArrayDeque<>();
        Deque<String> output = new ArrayDeque<>();
        for (int i = 0; i < equation.length(); i++) {
            char token = equation.charAt(i);
            if (Character.isDigit(token)) {
                String buffer = "";
                while (i < equation.length() && !isOperator(equation.charAt(i))) {
                    buffer += equation.charAt(i);
                    i++;
                }
                i--;
                output.add(buffer);
            } else if (Objects.equals('(', token)) {
                opStack.push(token);
            } else if (Objects.equals(')', token)) {
                while (!opStack.isEmpty() && !opStack.peek().equals('(')) {
                    Character pop = opStack.pop();
                    if (isOperator(pop)) {
                        output.add(pop.toString());
                    }
                }
            } else if (isOperator(token)) {
                while (!opStack.isEmpty() && getOpPrecedence(opStack.peek()) >= getOpPrecedence(token)) {
                    output.add(opStack.pop().toString());
                }
                opStack.push(token);
            }
        }
        while (!opStack.isEmpty()) {
            output.add(opStack.pop().toString());
        }
        return output;
    }

    private void evaluate() {
        if (equationLabel.getText().length() == 0) {
            return;
        }
        if (isEquationEndByOp() || equationLabel.getText().contains("\u00F70")) {
            equationLabel.setForeground(Color.RED.darker());
            return;
        }
        String equation = equationLabel.getText();
        Deque<String> postfix = convertInfixToPostfix(equation);
        Deque<Float> operandStack = new ArrayDeque<>();
        for (String token : postfix) {
            if (isOperator(token)) {
                float op2 = operandStack.pop();
                float op1 = operandStack.pop();
                float result = calculate(op1, op2, token.toCharArray()[0]);
                operandStack.push(result);
            } else {
                operandStack.push(Float.parseFloat(token));
            }
        }
        float result = operandStack.pop();
        int roundedResult = Math.round(result);
        if (result == roundedResult) {
            resultLabel.setText(String.valueOf(roundedResult));
        } else {
            resultLabel.setText(String.valueOf(result));
        }
        equationLabel.setForeground(Color.BLACK);
    }

    private void handleOperatorClick() {

    }

    private List<JButton> createButtons() {
        String[] names = {
                "Seven", "Eight", "Nine", "Divide",
                "Four", "Five", "Six", "Multiply",
                "One", "Two", "Three", "Add",
                "Dot", "Zero", "Equals", "Subtract",
                "Clear", "Delete"
        };
        String[] texts = {
                "7", "8", "9", operators.get(3).toString(),
                "4", "5", "6", operators.get(2).toString(),
                "1", "2", "3", operators.get(0).toString(),
                ".", "0", "=", "-",
                "C", "D"
        };
        int[] x = {50, 100, 150, 200};
        int[] y = {70, 120, 170, 220, 270};
        List<JButton> buttons = new ArrayList<>();
        int yIndex = 0;
        for (int i = 0; i < names.length; i++) {
            JButton button = new JButton(texts[i]);
            button.setName(names[i]);
            int xIndex = (i) % 4;
            if (xIndex == 0 && i != 0) {
                yIndex++;
            }
            button.setBounds(x[xIndex], y[yIndex], 45, 45);
            switch (names[i]) {
                case "Equals":
                    button.addActionListener(e -> this.evaluate());
                    break;
                case "Clear":
                    button.addActionListener(e -> {
                        resultLabel.setForeground(Color.BLACK);
                        equationLabel.setText("");
                    });
                    break;
                case "Delete":
                    button.addActionListener(e -> {
                        String text = equationLabel.getText();
                        if (!text.isEmpty()) {
                            equationLabel.setText(text.substring(0, text.length() - 1));
                        }
                    });
                    break;
                default:
                    if (isButtonOperator(button)) {
                        button.addActionListener(e -> {
                            String text = equationLabel.getText();
                            int length = text.length();
                            if (length == 0) {
                                return;
                            }
                            char lastChar = text.charAt(length - 1);
                            if (operators.contains(lastChar)) {
                                equationLabel.setText(text.substring(0, length - 1) + button.getText());
                                return;
                            }
                            if (lastChar == '.') {
                                equationLabel.setText(text + "0" + button.getText());
                                return;
                            }
                            if (length == 2 && text.charAt(0) == '.' && Character.isDigit(lastChar)) {
                                equationLabel.setText("0." + lastChar + button.getText());
                                return;
                            }
                            equationLabel.setText(text + button.getText());
                        });
                    } else {
                        button.addActionListener(e -> equationLabel.setText(equationLabel.getText() + button.getText()));
                    }
                    break;
            }
            buttons.add(button);
        }
        return buttons;
    }
}
