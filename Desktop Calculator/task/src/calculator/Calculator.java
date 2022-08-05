package calculator;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Calculator extends JFrame {
    final List<Character> operators;
    final char PLUS_SYMBOL = '\u002B';
    final char MINUS_SYMBOL = '-';
    final char MULTIPLY_SYMBOL = '\u00D7';
    final char DIVIDE_SYMBOL = '\u00F7';
    final char SQRT_SYMBOL = '\u221A';
    final char EXP_SYMBOL = '^';
    JLabel resultLabel;
    JLabel equationLabel;

    {
        resultLabel = new JLabel("0");
        resultLabel.setName("ResultLabel");
        resultLabel.setBounds(50, 20, 80, 20);

        equationLabel = new JLabel();
        equationLabel.setName("EquationLabel");
        equationLabel.setBounds(50, 35, 195, 20);

        operators = new ArrayList<>(List.of(PLUS_SYMBOL, MINUS_SYMBOL, MULTIPLY_SYMBOL, DIVIDE_SYMBOL, SQRT_SYMBOL, EXP_SYMBOL));
    }

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 430);
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
            case '\u221A':
                return (float) Math.sqrt(a);
            case '^':
                return (float) Math.pow(a, b);
            default:
                throw new ArithmeticException("Invalid Operator!");
        }
    }

    private int countChar(String string, char c) {
        int count = 0;
        for (char character : string.toCharArray()) {
            if (c == character) {
                count++;
            }
        }
        return count;
    }

    private char getLastChar(String string) {
        return string.charAt(string.length() - 1);
    }

    private boolean isOperator(Character op) {
        return operators.contains(op);
    }

    private boolean isOperator(String op) {
        return op.length() == 1 && isOperator(op.toCharArray()[0]);
    }

    private boolean isButtonOperator(JButton button) {
        return isOperator(button.getText().toCharArray()[0]);
    }

    private boolean isEquationEndedByOp() {
        return isOperator(getLastChar(equationLabel.getText()));
    }

    private boolean containsDivisionByZero() {
        return equationLabel.getText().contains(DIVIDE_SYMBOL + "0");
    }

    private int getOpPrecedence(Character op) {
        switch (op) {
            case PLUS_SYMBOL:
            case MINUS_SYMBOL:
                return 1;
            case MULTIPLY_SYMBOL:
            case DIVIDE_SYMBOL:
            case SQRT_SYMBOL:
            case EXP_SYMBOL:
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
                while (i < equation.length() && (Character.isDigit(equation.charAt(i)) || equation.charAt(i) == '.')) {
                    buffer += equation.charAt(i);
                    i++;
                }
                i--;
                output.add(buffer);
            } else if (Objects.equals('(', token)) {
                opStack.push(token);
            } else if (Objects.equals(')', token)) {
                while (!opStack.isEmpty()) {
                    Character pop = opStack.pop();
                    if (isOperator(pop)) {
                        output.add(pop.toString());
                    } else if (pop == '(') {
                        break;
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

    private float evaluatePostfix(Deque<String> postfix) {
        Deque<Float> operandStack = new ArrayDeque<>();
        for (String token : postfix) {
            if (isOperator(token) && token.equals(String.valueOf(SQRT_SYMBOL))) {
                float op1 = operandStack.pop();
                float result = calculate(op1, 0, token.toCharArray()[0]);
                operandStack.push(result);
            } else if (isOperator(token)) {
                float op2 = operandStack.pop();
                float result;
                if (operandStack.isEmpty()) {
                    result = calculate(0, op2, token.toCharArray()[0]);
                } else {
                    float op1 = operandStack.pop();
                    result = calculate(op1, op2, token.toCharArray()[0]);
                }
                operandStack.push(result);
            } else {
                operandStack.push(Float.parseFloat(token));
            }
        }
        return operandStack.pop();
    }

    private void evaluate() {
        if (equationLabel.getText().length() == 0) {
            return;
        }
        if (isEquationEndedByOp() || containsDivisionByZero()) {
            equationLabel.setForeground(Color.RED.darker());
            return;
        }
        String equation = equationLabel.getText();
        Deque<String> postfix = convertInfixToPostfix(equation);
        float result = evaluatePostfix(postfix);
        if (Float.isNaN(result)) {
            equationLabel.setForeground(Color.RED.darker());
            return;
        }
        String string = String.valueOf(result);
        String text = string.endsWith(".0") ? string.replace(".0", "") : string;
        resultLabel.setText(text);
        equationLabel.setForeground(Color.BLACK);
    }

    private void appendToEquation(String string) {
        equationLabel.setText(equationLabel.getText() + string);
    }

    private void addActionListener(JButton button) {
        switch (button.getName()) {
            case "Equals":
                button.addActionListener(e -> this.evaluate());
                break;
            case "Clear":
                button.addActionListener(e -> {
                    equationLabel.setForeground(Color.BLACK);
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
            case "Parentheses":
                button.addActionListener(e -> {
                    String equation = equationLabel.getText();
                    if (countChar(equation, '(') == countChar(equation, ')')
                        || equation.charAt(equation.length() - 1) == '('
                        || isOperator(equation.charAt(equation.length() - 1))) {
                        appendToEquation("(");
                    } else {
                        appendToEquation(")");
                    }
                });
                break;
            case "SquareRoot":
                button.addActionListener(e -> appendToEquation(operators.get(4).toString() + "("));
                break;
            case "PowerTwo":
                button.addActionListener(e -> appendToEquation("^(2)"));
                break;
            case "PowerY":
                button.addActionListener(e -> appendToEquation("^("));
                break;
            case "PlusMinus":
                button.addActionListener(actionEvent -> {
                    String text = equationLabel.getText();
                    int length = text.length();
                    if (length == 0) {
                        appendToEquation("(-");
                        return;
                    }
                    boolean lastCharIsDigit = Character.isDigit(getLastChar(text));
                    if (text.contains("(-")) {
                        if (text.endsWith("(-")) {
                            equationLabel.setText(text.substring(0, length - 2));
                        } else if (text.indexOf("(-") == length - 3 && lastCharIsDigit) {
                            equationLabel.setText(text.substring(0, length - 3) + getLastChar(text));
                        }
                    } else if (lastCharIsDigit) {
                        equationLabel.setText(text.substring(0, length - 1) + "(-" + getLastChar(text));
                    } else {
                        appendToEquation("(-");
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
                        char lastChar = getLastChar(text);
                        if (isOperator(lastChar)) {
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
                        appendToEquation(button.getText());
                    });
                } else {
                    button.addActionListener(e -> appendToEquation(button.getText()));
                }
                break;
        }
    }

    private List<JButton> createButtons() {
        String[] names = {
                "Parentheses", "", "Clear", "Delete",
                "PowerTwo", "PowerY", "SquareRoot", "Divide",
                "Seven", "Eight", "Nine", "Multiply",
                "Four", "Five", "Six", "Subtract",
                "One", "Two", "Three", "Add",
                "PlusMinus", "Zero", "Dot", "Equals"
        };
        String[] texts = {
                "( )", "CE", "C", "D",
                "x²", "xⁿ", String.valueOf(SQRT_SYMBOL), String.valueOf(DIVIDE_SYMBOL),
                "7", "8", "9", String.valueOf(MULTIPLY_SYMBOL),
                "4", "5", "6", String.valueOf(MINUS_SYMBOL),
                "1", "2", "3", String.valueOf(PLUS_SYMBOL),
                "±", "0", ".", "=",
        };
        int[] x = {50, 100, 150, 200};
        int[] y = {70, 120, 170, 220, 270, 320};
        List<JButton> buttons = new ArrayList<>();
        int yIndex = 0;
        for (int i = 0; i < names.length; i++) {
            JButton button = new JButton(texts[i]);
            button.setName(names[i]);
            int xIndex = (i) % 4;
            if (xIndex == 0 && i != 0) {
                yIndex++;
            }
            button.setBounds(x[xIndex], y[yIndex], 47, 47);
            addActionListener(button);
            buttons.add(button);
        }
        return buttons;
    }
}
