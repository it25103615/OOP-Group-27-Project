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
    }
}
