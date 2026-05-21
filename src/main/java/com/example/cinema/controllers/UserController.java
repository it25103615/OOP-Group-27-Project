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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

// === Normal Java Imports ===
import java.util.List;
import java.util.Map;

//Summary Of all API Endpoints in the file:
//  /api/users - get all the users in the database
//  /api/users/admins - get all the admin users in the database
//  /api/users/login - accepts a username and password map and will return a mapping of userId, username or will return a 401 if the user is not in the database
//  /api/users/register - accepts a username and password map and will return a mapping with a message and the userID or will return a bad request if the user already exists or if the username fails a requirements check (e.g: no spaces)
//  /api/users/register/admin - accepts a username and password map and will return a mapping with a message and the userID or will return a bad request if the user already exists, if the username fails a requirements check (e.g: no spaces), or if the user trying to make the admin account is not an admin themselves
//  /api/users/delete - accepts a user ID and will return a message if the user was successfully deleted or an error message if there was an issue
//  /api/users/updateUser - accepts userID, newUsername, newPassword and makeAdmin, will update the existing user with id userID to have the newUsername and newPassword. Will delete the user and create a new one with the correct type depending on the makeAdmin boolean value

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

    //Get a specific user by ID
    //	Returns the entry of a specified user
    @PostMapping("/userid")
    public ResponseEntity<?> getUser(@RequestBody Map<String, String> body) {
        Long id = Long.valueOf(body.get("userID"));
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        } else {
            return ResponseEntity.ok(userRepository.findById(id).get());
        }
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
        String username = (body.get("username")).strip(); // extract the username from the request body
        String password = (body.get("password")).strip(); // extract the password from the request body

        // Use the user database connection to find the user by using their username
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password)) //Use "filter" to check if the passwords match
                //If the passwords match
                //  In the response entity of OK (HTML code 200: OK)
                //  Return a map with user ID, username and user type (CUSTOMER or ADMIN)
                .map(u -> ResponseEntity.ok(Map.of(
                        "userId", u.getId(),
                        "username", u.getUsername(),
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
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }
        username = username.strip();
        password = password.strip();

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }
        if (username.contains(" ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username cannot contain spaces"));
        }

        int phoneNumber = parsePhoneNumber(body.getOrDefault("phoneNumber", "0"));
        String billingAddress = body.getOrDefault("billingAddress", "");

        Customer saved = customerRepository.save(
                new Customer(username, password, phoneNumber, billingAddress));

        return ResponseEntity.ok(Map.of(
                "message", "Registered successfully",
                "userId", saved.getId(),
                "username", saved.getUsername(),
                "type", "Customer"
        ));
    }

    /** Parses phone from register form; non-digits stripped; invalid values become 0. */
    private static int parsePhoneNumber(String raw) {
        if (raw == null || raw.isBlank()) {
            return 0;
        }
        try {
            String digits = raw.replaceAll("\\D", "");
            return digits.isEmpty() ? 0 : Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    //Create a new Admin User
    //  Checks if the person making the account is an admin
    //      If they are then create a new Admin account and save it to the database
    //      If not then return an error with an appropriate message
    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Map<String, String> body){
        String newUserName = (body.get("username")).strip(); // extract the new username from the request body
        String newPassword = (body.get("password")).strip(); // extract the new password from the request body
        Long userIDofPersonMakingAdmin = Long.valueOf(body.get("creatorID")); //extract the userId of the person making the admin account

        //Check if the person making the admin account is actually an Admin
        if(adminRepository.findById(userIDofPersonMakingAdmin).isPresent()){
            //Check if there are any space in the middle of the username
            if(newUserName.contains(" ")){
                //If there is then return an error
                return ResponseEntity.badRequest().body(Map.of("error", "Username cannot contain spaces"));
            } else {
                //If everything is OK then create a new Admin user and save it in the database
                Admin saved = adminRepository.save(new Admin(newUserName, newPassword));
                //Once done return a success message as well as the userID of the newly created Admin
                return ResponseEntity.ok(Map.of("message", "Registered successfully", "userID", saved.getId()));
            }
        } else {
            //If the person trying to make the admin account is not an admin then return an error
            return ResponseEntity.badRequest().body(Map.of("error", "Need to be an Admin to create Admin users"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> body) {
        Long id = Long.valueOf(body.get("userID"));
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @Transactional //Forces all database actions in the method to be done in a single session
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, String> body) {
        Long id = Long.valueOf(body.get("userID"));

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        User user = userRepository.findById(id).get();

        if (body.containsKey("newUsername")) {
            String newUsername = body.get("newUsername").strip();
            if( !(user.getUsername().equals(newUsername)) ){
                if (newUsername.contains(" ")) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Username cannot contain spaces"));
                }
                if (userRepository.findByUsername(newUsername).isPresent()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
                }
                user.setUsername(newUsername);
            }
        }

        if(body.containsKey("newPassword")) {
            String newPassword = body.get("newPassword").strip();
            if( !(user.getPassword().equals(newPassword)) ){
                user.setPassword(newPassword);
            }
        }

        userRepository.save(user);
        userRepository.flush(); // force the save to commit before moving on

        if (body.containsKey("makeAdmin")) {
            boolean makeAdmin = Boolean.parseBoolean(body.get("makeAdmin"));
            boolean changedType = false;
            String username = user.getUsername();
            String password = user.getPassword();

            if(user.getType().equals("CUSTOMER") && makeAdmin){
                adminRepository.save(new Admin(username, password));
                changedType = true;
            } else if(user.getType().equals("ADMIN") && !makeAdmin){
                customerRepository.save(new Customer(username, password));
                changedType = true;
            }

            userRepository.flush(); // force any pending commits before moving on
            if(changedType){ userRepository.deleteById(id); }

            //update any tables that referenced the old ID below
            //  ORDER HISTORY?
            //  TICKETS?
            //  SNACKS?
        }

        return ResponseEntity.ok(Map.of("message", "User updated successfully", "userID", user.getId()));
    }
}