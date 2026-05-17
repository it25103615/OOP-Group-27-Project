package theaterid;

public abstract class theatre {
    import java.io.Serializable;

    public abstract class Theater {
        private String theaterId;
        private String name;
        private int totalSeats;

        public Theater(String theaterId, String name, int totalSeats) {
            this.theaterId = theaterId;
            this.name = name;
            this.totalSeats = totalSeats;
        }

        // Getters and Setters (Encapsulation)
        public String getTheaterId() { return theaterId; }
        public String getName() { return name; }
        public int getTotalSeats() { return totalSeats; }

        // Polymorphism: Subclasses will implement their own display logic
        public abstract void displayTheaterDetails();
    }

}
