package com.example.cinema;


// === Import Models / Classes ===
import com.example.cinema.models.*;

// === Import Repositories / Database Tables ===
import com.example.cinema.repositories.*;

// == Import the springboot modules needed to run the data seeder ===
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component //Similar to @Repository or @Controller; springboot will create an instance of @Component
class DataSeeder implements CommandLineRunner {
    @Autowired private UserRepository userRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private SnackRepository snackRepository;

    //CommandLineRunner only has one method "run"
    //  Springboot will look for all instances that implement CommandLineRunner and call their run methods
    @Override
    public void run(String... args) throws Exception {
        //Check if the database was already initialized at some point
        if (userRepository.count() > 0) return;

        // Create a new admin user
        Admin admin = new Admin("admin", "admin123");
        // Save the admin in the database
        adminRepository.save(admin);

        Customer customer = new Customer("John Doe", "jdoe123", 0111231234, "12 Example Street, Sample Town, Test State");
        customerRepository.save(customer);

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
    }
}
