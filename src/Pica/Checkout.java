package Pica;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
        table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Pica", "Daudzums"}));

        JPanel summaryPanel = new JPanel(new GridLayout(3, 1));

        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
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
            }
        });

        for (Item item : items) {
            Object[] row = {item.getName(), item.getQuantity()};
            for (int i = 1; i < row.length; i++) {
                if (row[i] instanceof Number) {
                    row[i] = ((Number) row[i]).intValue();
                } else if (row[i] instanceof String) {
                    try {
                        row[i] = Integer.parseInt((String) row[i]);
                    } catch (NumberFormatException e) {
                        row[i] = 0;
                    }
                } else {
                    row[i] = 0;
                }
            }
            ((DefaultTableModel)table.getModel()).addRow(row);
        }

        table.setPreferredScrollableViewportSize(new Dimension(500, 200));

        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column = columnModel.getColumn(0);
        column.setPreferredWidth(400);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JFormattedTextField formattedTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        DefaultCellEditor integerEditor = new DefaultCellEditor(formattedTextField) {
           
			/**
			 * 
			 */
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

        Object[] options = {"Piegāde", "Izņemšana"};
        int deliveryChoice = JOptionPane.showOptionDialog(null, "Vai vēlaties pasūtījumu saņemt uz mājām vai izņemt uz vietas?",
                "Pasūtījuma veids", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        BigDecimal deliveryCharge = new BigDecimal("0");
        if (deliveryChoice == JOptionPane.YES_OPTION) {
            deliveryCharge = new BigDecimal("5.99");
        }

        double sum = 0;
        for (Item item : items) {
            sum += item.getTotalPrice();
        }

        BigDecimal subtotal = new BigDecimal(sum);
        BigDecimal vatRate = new BigDecimal("0.21");
        BigDecimal vatAmount = subtotal.multiply(vatRate);
        BigDecimal total = subtotal.add(vatAmount).add(deliveryCharge);

        JLabel subtotalLabel = new JLabel("Bez PVN: " + subtotal.setScale(2, RoundingMode.HALF_UP) + " EUR");
        JLabel vatLabel = new JLabel("PVN (" + vatRate.multiply(new BigDecimal("100")) + "%): " + vatAmount.setScale(2, RoundingMode.HALF_UP) + " EUR");
    	JLabel totalLabel = new JLabel("KOPA: " + total.setScale(2, RoundingMode.HALF_UP) + "EUR");
    	
    	summaryPanel.add(subtotalLabel);
    	summaryPanel.add(vatLabel);
    	summaryPanel.add(totalLabel);

    	JOptionPane.showMessageDialog(null, panel, "Pasutijuma apkopojums", JOptionPane.PLAIN_MESSAGE);
    	JOptionPane.showMessageDialog(null, summaryPanel, "Pasutijums kopa", JOptionPane.PLAIN_MESSAGE);
	}
    
    public static void updateSummaryPanel(JPanel summaryPanel, ArrayList<Item> items) {
        JLabel subtotalLabel = (JLabel) summaryPanel.getComponent(0);
        JLabel deliveryLabel = (JLabel) summaryPanel.getComponent(1);
        JLabel vatLabel = (JLabel) summaryPanel.getComponent(2);
        JLabel totalLabel = null;
        for (Component c : summaryPanel.getComponents()) {
            if (c instanceof JLabel) {
                totalLabel = (JLabel) c;
                break;
            }
        }
        totalLabel.setText(String.format("Total: $%.2f", getTotal(null)));
        subtotalLabel.setText("bez PVN: " + getSubtotal(items) + "EUR");
        deliveryLabel.setText("Piegāde: " + "5.99" + "EUR");
        vatLabel.setText("PVN: " + getVatAmount(items) + "EUR");
        totalLabel.setText("KOPA: " + getTotal(items) + "EUR");
    }
    
    public static double getSubtotal(ArrayList<Item> items) {
        double subtotal = 0;
        if (items == null || items.isEmpty()) {
            return subtotal;
        }
        for (Item item : items) {
            subtotal += item.getTotalPrice();
        }
        return subtotal;
}
    public static double getVatAmount(ArrayList<Item> items) {
        double subtotal = getSubtotal(items);
        double vatRate = 0.21;
        double vatAmount = subtotal * vatRate;
        return vatAmount;
    }

    public static double getTotal(ArrayList<Item> items) {
        double subtotal = getSubtotal(items);
        double vatAmount = getVatAmount(items);
        double total = subtotal + vatAmount;
        return total;
    }
}