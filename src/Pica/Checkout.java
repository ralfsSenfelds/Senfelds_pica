package Pica;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

class Item {
    String name;
    int quantity;
    double price;

    public Item(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public double getTotalPrice() {
        return quantity * price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

public class Checkout {
    public static String generateReceipt(ArrayList<Item> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("PIRKUMA CEKS\n\n");

        double sum = 0;
        for (Item item : items) {
            sb.append(item.name).append("\n");
            sb.append("\t").append(item.quantity).append(" gab. x ").append(item.price).append("\t\n");
            sum += item.getTotalPrice();
        }

        BigDecimal subtotal = new BigDecimal(sum);
        BigDecimal vatRate = new BigDecimal("0.21");
        BigDecimal vatAmount = subtotal.multiply(vatRate);
        BigDecimal total = subtotal.add(vatAmount);

        sb.append("_____________________\n");
        sb.append("Summa bez PVN: ").append(subtotal.setScale(2, RoundingMode.HALF_UP)).append("\n");
        sb.append("PVN:           ").append(vatAmount.setScale(2, RoundingMode.HALF_UP)).append("\n");
        sb.append("KOPA:          ").append(total.setScale(2, RoundingMode.HALF_UP)).append(" EUR\n");

        return sb.toString();
    }
}