package Pica;

import java.util.ArrayList;
import java.util.List;

class Order {
    private List<Pizza> pizzas;
    private double total;

    public Order() {
        pizzas = new ArrayList<>();
        total = 0;
    }

    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
        total += pizza.getPrice();
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public double getTotal() {
        return total;
    }
    public String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Receipt:\n");
        for (Pizza pizza : pizzas) {
            receipt.append(pizza.getName() + ": $" + pizza.getPrice() + "\n");
        }
        receipt.append("Total: $" + total);
        return receipt.toString();
    }
}