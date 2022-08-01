package calculator;

import javax.swing.*;

public class Calculator extends JFrame {

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLayout(null);

        JTextField equationTextField = new JFormattedTextField();
        equationTextField.setName("EquationTextField");
        equationTextField.setBounds(50, 35, 100, 20);
        add(equationTextField);

        JButton solve = new JButton("Solve");
        solve.setName("Solve");
        solve.setBounds(70, 150, 80, 20);
        solve.addActionListener(e -> {
            String[] numbers = equationTextField.getText().split("\\+");
            if (numbers.length == 2) {
                int sum = Integer.parseInt(numbers[0]) + Integer.parseInt(numbers[1]);
                equationTextField.setText(equationTextField.getText() + "=" + sum);
            }
        });
        add(solve);

        setVisible(true);
    }
}
