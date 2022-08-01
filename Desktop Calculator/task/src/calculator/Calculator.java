package calculator;

import javax.swing.*;
import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.List;

public class Calculator extends JFrame {
    JTextField equationTextField;

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLayout(null);
        equationTextField = new JFormattedTextField();
        equationTextField.setName("EquationTextField");
        equationTextField.setBounds(50, 35, 195, 20);
        add(equationTextField);
        for (JButton button : createButtons()) {
            add(button);
        }
        setVisible(true);
    }

    private void calculateEquation() {
        int result;
        String equation = equationTextField.getText();
        if (equation.contains("/")) {
            String[] numbers = equation.split("/");
            result = Integer.parseInt(numbers[0]) / Integer.parseInt(numbers[1]);
        } else if (equation.contains("x")) {
            String[] numbers = equation.split("x");
            result = Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
        } else if (equation.contains("+")) {
            String[] numbers = equation.split("\\+");
            result = Integer.parseInt(numbers[0]) + Integer.parseInt(numbers[1]);
        } else {
            String[] numbers = equation.split("-");
            result = Integer.parseInt(numbers[0]) - Integer.parseInt(numbers[1]);
        }
        equationTextField.setText(equationTextField.getText() + "=" + result);
    }

    private List<JButton> createButtons() {
        String[] names = {
                "Seven", "Eight", "Nine", "Divide",
                "Four", "Five", "Six", "Multiply",
                "One", "Two", "Three", "Add",
                "", "Zero", "Equals", "Subtract"
        };
        String[] texts = {
                "7", "8", "9", "/",
                "4", "5", "6", "x",
                "1", "2", "3", "+",
                "", "0", "=", "-"
        };
        int[] x = {50, 100, 150, 200};
        int[] y = {120, 170, 220, 270};
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
            if (names[i].equals("Equals")) {
                button.addActionListener(e -> this.calculateEquation());
            } else {
                button.addActionListener(e -> equationTextField.setText(equationTextField.getText() + button.getText()));
            }
            buttons.add(button);
        }
        return buttons;
    }
}
