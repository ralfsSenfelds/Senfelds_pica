package Pica;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

class Item {
    String name;
    int quantity;
    double price;
    double totalPrice;

    public Item(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = quantity * price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = quantity * price;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static void generateReceipt(ArrayList<Item> items) {
    	JTable table = new JTable();
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Pica", "Skaits"}));
        JPanel summaryPanel = new JPanel(new GridLayout(6, 1));
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 1) {
                String value = table.getValueAt(row, col).toString();
                int newQuantity = Integer.parseInt(value);
                Item item = items.get(row);
                item.setQuantity(newQuantity);
                item.setTotalPrice(item.getPrice() * newQuantity);
                updateSummaryPanel(summaryPanel, items);
            }
        });

        for (Item item : items) {
            Object[] row = {item.getName(), item.getQuantity()};
            ((DefaultTableModel) table.getModel()).addRow(row);
        }

        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column = columnModel.getColumn(0);
        column.setPreferredWidth(400);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JFormattedTextField formattedTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        DefaultCellEditor integerEditor = new DefaultCellEditor(formattedTextField) {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getCellEditorValue() {
                Object value = super.getCellEditorValue();
                if (value instanceof Number) {
                    return ((Number) value).intValue();
                } else if (value instanceof String) {
                    try {
                        return Integer.parseInt((String) value);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        };
        table.getColumnModel().getColumn(1).setCellEditor(integerEditor);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        int result = JOptionPane.showOptionDialog(null, panel, "Pasūtījuma informācija",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                new String[]{"Labi", "Atcelt"}, "Labi");

        if (result == JOptionPane.OK_OPTION) {
            Object[] options = {"Piegādāšana", "Izņemšana"};
            int deliveryChoice = JOptionPane.showOptionDialog(null, "Vai vēlaties pasūtījumu saņemt uz mājām vai izņemt uz vietas?",
                    "Pasūtījuma veids", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            String deliveryType;
            String address = "";
            String phone = "";
            if (deliveryChoice == JOptionPane.YES_OPTION) {
                deliveryType = "Piegādāšana";
                do {
                address = JOptionPane.showInputDialog(null, "Lūdzu, ievadiet savu adresi:");
                }while(address.isBlank());
                do {
                phone = JOptionPane.showInputDialog(null, "Lūdzu, ievadiet savu telefona numuru:");
                }while(phone.isBlank());
            } else {
                deliveryType = "Izņemšana";
            }

            BigDecimal deliveryCharge = new BigDecimal(0);
            BigDecimal subtotal = new BigDecimal(0);
            for (Item item : items) {
                BigDecimal itemPrice = new BigDecimal(Double.toString(item.getPrice()));
                BigDecimal itemQuantity = new BigDecimal(Integer.toString(item.getQuantity()));
                BigDecimal itemTotalPrice = itemPrice.multiply(itemQuantity);
                subtotal = subtotal.add(itemTotalPrice);
            }
            BigDecimal total;
            if (deliveryChoice == JOptionPane.YES_OPTION) {
                deliveryCharge = new BigDecimal("5.99");
                total = subtotal.add(deliveryCharge);
            } else {
                total = subtotal;
            }

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
            String formattedSubtotal = currencyFormat.format(subtotal);
            String formattedDeliveryCharge = currencyFormat.format(deliveryCharge);
            BigDecimal pvn = subtotal.multiply(new BigDecimal("0.21"));
            BigDecimal totalWithPvn = total.add(pvn);
            String formattedPvn = currencyFormat.format(pvn);
            String formattedTotalWithPvn = currencyFormat.format(totalWithPvn);
            JOptionPane.showMessageDialog(null,
                "Pasūtījuma veids: " + deliveryType + "\n\n" +
                "Adrese: " + address + "\n" +
                "Telefona numurs: +371 " + phone + "\n\n" +
                "Bez PVN: " + formattedSubtotal + "\n" +
                "PVN: " + formattedPvn + "\n" +
                "Piegādes maksa: " + formattedDeliveryCharge + "\n" +
                "Kopā: " + formattedTotalWithPvn,
                "Pasūtījuma kvīts", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void updateSummaryPanel(JPanel summaryPanel, ArrayList<Item> items) {
        summaryPanel.removeAll();
        summaryPanel.add(new JLabel("Pasūtījuma kopsavilkums"));
        summaryPanel.add(new JLabel(""));

        int totalQuantity = 0;
        BigDecimal subtotal = new BigDecimal(0);
        for (Item item : items) {
            totalQuantity += item.getQuantity();
            BigDecimal itemPrice = new BigDecimal(Double.toString(item.getPrice()));
            BigDecimal itemQuantity = new BigDecimal(Integer.toString(item.getQuantity()));
            BigDecimal itemTotalPrice = itemPrice.multiply(itemQuantity);
            subtotal = subtotal.add(itemTotalPrice);
            summaryPanel.add(new JLabel(item.getName() + " (" + item.getQuantity() + " x " +
                    NumberFormat.getCurrencyInstance().format(item.getPrice()) + "): " +
                    NumberFormat.getCurrencyInstance().format(itemTotalPrice)));
        }

        summaryPanel.add(new JLabel(""));
        summaryPanel.add(new JLabel("Kopējais daudzums: " + totalQuantity));
        summaryPanel.add(new JLabel("Bez PVN: " + NumberFormat.getCurrencyInstance().format(subtotal)));
    }
}    