package Pica;

class Checkout {
    public static void main(String[] args) {
        Pizza margherita = new Pizza("Margherita", 9.99);
        Pizza pepperoni = new Pizza("Pepperoni", 11.99);
        Pizza veggie = new Pizza("Veggie", 12.99);

        Order order = new Order();
        order.addPizza(margherita);
        order.addPizza(pepperoni);

        System.out.println("Your order:");
        for (Pizza pizza : order.getPizzas()) {
            System.out.println(pizza.getName() + ": $" + pizza.getPrice());
        }
        System.out.println("Total: $" + order.getTotal());

        String receipt = order.generateReceipt();
        System.out.println(receipt);
    }
}