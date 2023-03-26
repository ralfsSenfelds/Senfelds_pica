package Pica;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PizzaMenu {

    public static void pizzaBuilder(JFrame frame, ArrayList<Item> items) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Izvēlieties picas izmēru: "), BorderLayout.NORTH);

        String[] sizes = {"Maza (25 cm)", "Vidēja (35 cm)", "Liela (45 cm)"};
        JPanel radioButtonsPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup sizeGroup = new ButtonGroup();
        for (int i = 0; i < sizes.length; i++) {
            JRadioButton radioButton = new JRadioButton(sizes[i]);
            radioButtonsPanel.add(radioButton);
            sizeGroup.add(radioButton);
        }
        panel.add(radioButtonsPanel, BorderLayout.CENTER);

        String[] toppings = {"Siers", "Pepperoni", "Sēnes", "Tomāti", "Sarkanā paprika"};
        JPanel checkboxPanel = new JPanel(new GridLayout(0, 1));
        for (int i = 0; i < toppings.length; i++) {
            JCheckBox checkBox = new JCheckBox(toppings[i]);
            checkboxPanel.add(checkBox);
        }
        panel.add(checkboxPanel, BorderLayout.EAST);

        int result = JOptionPane.showOptionDialog(frame, panel, "Izveidot picu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            String size = "";
            for (Enumeration<AbstractButton> buttons = sizeGroup.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    size = button.getText().split(" ")[0];
                    break;
                }
            }

            ArrayList<String> selectedToppings = new ArrayList<String>();
            for (Component component : checkboxPanel.getComponents()) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    if (checkBox.isSelected()) {
                        selectedToppings.add(checkBox.getText());
                    }
                }
            }
            double basePrice = 0.0;

            if (size.equals("Maza")) {
                basePrice = 5.0;
            } else if (size.equals("Vidēja")) {
                basePrice = 7.5;
            } else if (size.equals("Liela")) {
                basePrice = 10.0;
            }

            double toppingPrice = 0.5 * selectedToppings.size();

            double pizzaPrice = basePrice + toppingPrice;

            Item pizza = new Item(size + " pica ar " + String.join(", ", selectedToppings), 1, pizzaPrice);
            items.add(pizza);
        }
    }
}