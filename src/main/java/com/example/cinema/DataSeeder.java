package com.example.cinema;


// === Import Models / Classes ===
import com.example.cinema.models.*;

// === Import Repositories / Database Tables ===
import com.example.cinema.repositories.*;

// == Import the springboot modules needed to run the data seeder ===
import com.example.cinema.services.SeatService;
import com.example.cinema.services.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component //Similar to @Repository or @Controller; springboot will create an instance of @Component
class DataSeeder implements CommandLineRunner {
    @Autowired private UserRepository userRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private SnackRepository snackRepository;
    @Autowired private SnackOrderRepository snackOrderRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private CreditCardRepository creditCardRepository;
    @Autowired private OrderRepository orderRepository;

    private final TheaterService theaterService;
    private final SeatService seatService;

    public DataSeeder(TheaterService theaterService, SeatService seatService) {
        this.theaterService = theaterService;
        this.seatService = seatService;
    }

    //CommandLineRunner only has one method "run"
    //  Springboot will look for all instances that implement CommandLineRunner and call their run methods
    @Override
    public void run(String... args) throws Exception {
        Customer customer = new Customer("JohnDoe", "jdoe123", 0111231234, "12 Example Street, Sample Town, Test State");
        CreditCard creditCard = new CreditCard("John Doe", 1234234534564567L, "05/30", 999);
        customer.setCreditCard(creditCard);

        if (userRepository.count() == 0) {
            Admin admin = new Admin("admin", "admin123");
            adminRepository.save(admin);
            creditCardRepository.save(creditCard);
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

        // Theaters
        /* name, location, capacity */
        Object[][] VENUES = {
                {"Colombo Cineplex", "Colombo 03", 320},
                {"Majestic Cinema", "Bambalapitiya", 280},
                {"Royal Grand Cinema", "Kandy", 260},
                {"Liberty Cine Hub", "Negombo", 200},
                {"Ocean View Theater", "Galle", 180},
                /*
                {"Galaxy Multiplex", "Kurunegala", 240},
                {"Platinum Screens", "Colombo 07", 350},
                {"Sapphire Cinema", "Dehiwala", 190},
                {"Empire Movie Hall", "Matara", 210},
                {"Nova Cineplex", "Jaffna", 250},
                {"Skyline Theater", "Nugegoda", 200},
                {"Metro Gold Cinema", "Wattala", 175},
                {"Pearl City Movies", "Batticaloa", 160},
                {"Regal Screen House", "Panadura", 185},
                {"Infinity Multiplex", "Moratuwa", 230},
                {"StarLight Cinema", "Trincomalee", 150},
                {"Golden Frame Theater", "Anuradhapura", 140},
                {"Liberty Lite Multiplex", "Kandy", 220},
                {"Scope Cinemas Negombo", "Negombo", 195},
                {"Savoy Premier", "Wellawatte", 205},
                {"EAP Films Multiplex", "Matara", 215},
                {"Regal Cinema Jaffna", "Jaffna", 170}
                */
        };

        List<String> theaterIDs = new LinkedList<>();

        if (movieRepository.count() == 0) {
            for (Object[] v : VENUES) {
                String name = (String) v[0];
                String location = (String) v[1];
                int capacity = (Integer) v[2];
                String id = theaterService.addTheater(name, location, capacity);
                if (id != null) {
                    theaterIDs.add(id);
                    seedSeatsForTheater(id, capacity);
                }
            }

            String[][] movies = {
                    {"Inception", "Sci-Fi", "inception.jpg", "A thief who steals corporate secrets through dream-sharing technology.", "10:00 AM, 1:00 PM, 6:00 PM"},
                    {"The Dark Knight", "Action", "dark_knight.jpg", "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos.", "11:00 AM, 2:30 PM, 7:00 PM"},
                    {"Interstellar", "Sci-Fi", "interstellar.jpg","A team of explorers travel through a wormhole in space to ensure humanity's survival.", "12:00 PM, 3:30 PM, 8:00 PM"},
                    {"The Grand Budapest Hotel", "Comedy", "grand_budapest.jpg","The adventures of a legendary hotel concierge and his protégé.", "10:30 AM, 1:30 PM, 5:00 PM"},
                    {"Parasite", "Thriller", "parasite.jpg","Greed and class discrimination threaten the symbiotic relationship between two families.", "2:00 PM, 5:30 PM, 9:00 PM"}
            };

            for (int i = 0; i < theaterIDs.size(); i++) {
                for(int j=0 ; j < movies.length ; j++) {
                    movieRepository.save(new Movie(String.format("M%03d%03d", (i+1), (j+1)), movies[j][0], movies[j][1], ("../images/movies/" + movies[j][2]), movies[j][3], movies[j][4], theaterIDs.get(i)));
                }
            }
        }

        if (orderRepository.count() == 0){
            Order order1 = new Order();
            order1.setCustomer(customer);
            order1.setTotalAmount(45.99);
            order1.setStatus("COMPLETED");

            Order order2 = new Order();
            order2.setCustomer(customer);
            order2.setTotalAmount(23.50);
            order2.setStatus("CANCELLED");

            Order order3 = new Order();
            order3.setCustomer(customer);
            order3.setTotalAmount(67.00);
            order3.setStatus("PENDING");

            Order order4 = new Order();
            order4.setCustomer(customer);
            order4.setTotalAmount(12.75);
            order4.setStatus("COMPLETED");

            orderRepository.saveAll(List.of(order1, order2, order3, order4));
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
        order.setUsername(customer.getUsername());
        order.setPlacedAt(placedAt);
        order.setTotalAmount(total);
        order.setItems(new ArrayList<>(lines));
        return order;
    }

    private void seedSeatsForTheater(String theaterId, int capacity) {
        if (capacity >= 300) {
            seatService.bulkAddSeats(theaterId, "A", "L", 14, Seat.TYPE_REGULAR, 820.0);
            seatService.bulkAddSeats(theaterId, "M", "P", 12, Seat.TYPE_VIP, 1250.0);
        } else if (capacity >= 220) {
            seatService.bulkAddSeats(theaterId, "A", "J", 12, Seat.TYPE_REGULAR, 780.0);
            seatService.bulkAddSeats(theaterId, "K", "N", 10, Seat.TYPE_VIP, 1180.0);
        } else if (capacity >= 180) {
            seatService.bulkAddSeats(theaterId, "A", "H", 11, Seat.TYPE_REGULAR, 750.0);
            seatService.bulkAddSeats(theaterId, "I", "K", 9, Seat.TYPE_VIP, 1100.0);
        } else {
            seatService.bulkAddSeats(theaterId, "A", "F", 10, Seat.TYPE_REGULAR, 700.0);
            seatService.bulkAddSeats(theaterId, "G", "J", 8, Seat.TYPE_VIP, 1050.0);
        }
        reserveOne(theaterId, "A", 1);
        reserveOne(theaterId, "A", 5);
        reserveOne(theaterId, "B", 3);
    }

    private void reserveOne(String theaterId, String row, int seatNumber) {
        for (Seat s : seatService.getSeatsByTheater(theaterId)) {
            if (row.equals(s.getRow()) && s.getSeatNumber() == seatNumber) {
                seatService.reserveSeat(s.getSeatId());
                break;
            }
        }
    }
}
