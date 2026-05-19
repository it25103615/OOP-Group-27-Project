package com.example.cinema;


// === Import Models / Classes ===
import com.example.cinema.models.*;

// === Import Repositories / Database Tables ===
import com.example.cinema.repositories.*;

// == Import the springboot modules needed to run the data seeder ===
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component //Similar to @Repository or @Controller; springboot will create an instance of @Component
class DataSeeder implements CommandLineRunner {
    @Autowired private UserRepository userRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private SnackRepository snackRepository;
    @Autowired private SnackOrderRepository snackOrderRepository;

    //CommandLineRunner only has one method "run"
    //  Springboot will look for all instances that implement CommandLineRunner and call their run methods
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            Admin admin = new Admin("admin", "admin123");
            adminRepository.save(admin);

            Customer customer = new Customer("John Doe", "jdoe123", 0111231234,
                    "12 Example Street, Sample Town, Test State");
            customerRepository.save(customer);
        }

        if (snackRepository.count() == 0) {

            // Food
            snackRepository.save(new Snack("S001", "Popcorn",        350.00, "Food",     "images/popcorn.png"));
            snackRepository.save(new Snack("S002", "Nachos",         450.00, "Food",     "images/nachos.png"));
            snackRepository.save(new Snack("S003", "Hot Dog",        550.00, "Food",     "images/hotdog.png"));
            snackRepository.save(new Snack("S009", "French Fries",   400.00, "Food",     "images/fries.png"));
            snackRepository.save(new Snack("S010", "Pizza Slice",    600.00, "Food",     "images/pizza.png"));
            snackRepository.save(new Snack("S011", "Chicken Wings",  700.00, "Food",     "images/wings.png"));
            snackRepository.save(new Snack("S012", "Mini Burger",    650.00, "Food",     "images/burger.png"));
            snackRepository.save(new Snack("S013", "Pretzel",        300.00, "Food",     "images/pretzel.png"));

// Beverages
            snackRepository.save(new Snack("S004", "Coca Cola",      250.00, "Beverage", "images/cola.png"));
            snackRepository.save(new Snack("S005", "Orange Juice",   300.00, "Beverage", "images/juice.png"));
            snackRepository.save(new Snack("S006", "Water Bottle",   150.00, "Beverage", "images/water.png"));
            snackRepository.save(new Snack("S014", "Lemonade",       280.00, "Beverage", "images/lemonade.png"));
            snackRepository.save(new Snack("S015", "Iced Tea",       270.00, "Beverage", "images/icedtea.png"));
            snackRepository.save(new Snack("S016", "Sprite",         250.00, "Beverage", "images/sprite.png"));
            snackRepository.save(new Snack("S017", "Milkshake",      350.00, "Beverage", "images/milkshake.png"));
            snackRepository.save(new Snack("S018", "Hot Chocolate",  320.00, "Beverage", "images/hotchoco.png"));

// Sweets
            snackRepository.save(new Snack("S007", "Candy",          100.00, "Sweets",   "images/candy.png"));
            snackRepository.save(new Snack("S008", "Chocolate Bar",  200.00, "Sweets",   "images/chocbar.png"));
            snackRepository.save(new Snack("S019", "Gummy Bears",    150.00, "Sweets",   "images/gummy.png"));
            snackRepository.save(new Snack("S020", "Cotton Candy",   180.00, "Sweets",   "images/cottoncanddy.png"));
            snackRepository.save(new Snack("S021", "Ice Cream Cup",  250.00, "Sweets",   "images/icecream.png"));
            snackRepository.save(new Snack("S022", "Brownie",        220.00, "Sweets",   "images/brownie.png"));
            snackRepository.save(new Snack("S023", "Caramel Apple",  300.00, "Sweets",   "images/caramelapple.png"));
        }

        if (snackOrderRepository.count() == 0) {
            seedSampleSnackOrders();
        }
    }

    /** Example orders in H2 tables snack_orders + snack_order_items (for demo / H2 console). */
    private void seedSampleSnackOrders() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty() || snackRepository.count() == 0) {
            return;
        }

        Customer customer = customers.get(0);

        SnackOrder order1 = buildOrder(
                customer,
                Instant.now().minus(2, ChronoUnit.DAYS),
                List.of(
                        new OrderLineItem("S001", "Popcorn", 2, 350.00),
                        new OrderLineItem("S004", "Coca Cola", 1, 250.00)
                )
        );

        SnackOrder order2 = buildOrder(
                customer,
                Instant.now().minus(1, ChronoUnit.HOURS),
                List.of(
                        new OrderLineItem("S007", "Candy", 3, 100.00),
                        new OrderLineItem("S010", "Pizza Slice", 1, 600.00),
                        new OrderLineItem("S006", "Water Bottle", 2, 150.00)
                )
        );

        snackOrderRepository.save(order1);
        snackOrderRepository.save(order2);
    }

    private static SnackOrder buildOrder(Customer customer, Instant placedAt, List<OrderLineItem> lines) {
        double total = lines.stream().mapToDouble(OrderLineItem::getLineTotal).sum();

        SnackOrder order = new SnackOrder();
        order.setUserId(customer.getId());
        order.setUsername(customer.getUserName());
        order.setPlacedAt(placedAt);
        order.setTotalAmount(total);
        order.setItems(new ArrayList<>(lines));
        return order;
    }
}
