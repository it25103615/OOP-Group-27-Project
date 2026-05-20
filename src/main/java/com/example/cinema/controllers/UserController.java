package com.example.cinema.controllers;

// === Import Models / Classes ===
import com.example.cinema.models.Admin;
import com.example.cinema.models.Customer;
import com.example.cinema.models.User;

// === Import Repositories / Database Tables ===
import com.example.cinema.repositories.AdminRepository;
import com.example.cinema.repositories.UserRepository;
import com.example.cinema.repositories.CustomerRepository;

// === Imports Specific to Controller ===
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// === Normal Java Imports ===
import java.util.List;
import java.util.Map;


@RestController //Declare that this is a controller class
@RequestMapping("/api/users") //Declare how parts of this controller can be accessed
class UserController {
    // === Declare the repositories ===
    // @Autowired is a shortcut to declare and initialize the database connection
    @Autowired private UserRepository userRepository; // access to the users table
    @Autowired private AdminRepository adminRepository; // access to the admins table
    @Autowired private CustomerRepository customerRepository; //access to the customers table

    //Get all users
    // Returns a list of ALL the users in the database (i.e. Admins AND Customers)
    @GetMapping // /api/users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Get all the customers
    // Returns a list of all the users with role CUSTOMER in the database
    @GetMapping("/admins") // /api/users/admins
    public List<Admin> getAllCustomers() {
        return adminRepository.findAll();
    }

    //Check if user is registered
    //  Expects a mapping of
    //      {"username": [username], "password": [password]}
    //  Returns a mapping of
    //      {"userId": [user ID], "username": [username], "type": [user type]}
    @PostMapping("/login") // /api/users/login
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username"); // extract the username from the request body
        String password = body.get("password"); // extract the password from the request body

        // Use the user database connection to find the user by using their username
        return userRepository.findByUserName(username)
                .filter(u -> u.getPassword().equals(password)) //Use "filter" to check if the passwords match
                //If the passwords match
                //  In the response entity of OK (HTML code 200: OK)
                //  Return a map with user ID, username and user type (CUSTOMER or ADMIN)
                .map(u -> ResponseEntity.ok(Map.of(
                        "userId", u.getId(),
                        "username", u.getUserName(),
                        "type", u.getClass().getSimpleName()
                )))
                //If the passwords don't match
                //  In the response entity of 401 (HTML code 401: Unauthorized)
                //  Return a map with an error message
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));
        }
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
        }
        if (userRepository.findByUserName(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        int phoneNumber = parsePhoneNumber(body.get("phoneNumber"));
        if (phoneNumber < 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid phone number"));
        }

        String billingAddress = body.getOrDefault("billingAddress", "");

        Customer customer = new Customer(username, password, phoneNumber, billingAddress);
        Customer saved = customerRepository.save(customer);
        return ResponseEntity.ok(Map.of(
                "message", "Registered successfully",
                "userId", saved.getId(),
                "username", saved.getUserName(),
                "type", "Customer"
        ));
    }

    private static int parsePhoneNumber(String raw) {
        if (raw == null || raw.isBlank() || "0".equals(raw.trim())) {
            return 0;
        }
        String digits = raw.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            return 0;
        }
        try {
            long value = Long.parseLong(digits);
            if (value > Integer.MAX_VALUE) {
                return -1;
            }
            return (int) value;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}