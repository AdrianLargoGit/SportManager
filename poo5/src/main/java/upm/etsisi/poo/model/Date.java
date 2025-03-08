package upm.etsisi.poo.model;
import jakarta.persistence.*;

@Entity
@Table(name = "dates")
public class Date {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private long id;
    @Column(name = "year")
    private int year;
    @Column(name = "month")
    private int month;
    @Column(name = "day")
    private int day;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(String date) {
        String[] original = date.split("/");
        day = Integer.parseInt(original[0]);
        month = Integer.parseInt(original[1]);
        year = Integer.parseInt(original[2]);
    }

    /**
     * CONSTRUYE UNA FECHA CON LA FECHA DE HOY
     */
    public Date() {
        year = java.time.LocalDate.now().getYear();
        month = java.time.LocalDateTime.now().getMonthValue();
        day = java.time.LocalDate.now().getDayOfMonth();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public boolean greaterThan(Date date) {
        boolean result = false;
        if (year == date.getYear()) {
            if (month == date.getMonth()) {
                result = day > date.getDay();
            } else {
                result = month > date.getMonth();
            }
        } else {
            result = year > date.getYear();
        }
        return result;
    }

    public boolean lowerThan(Date date) {
        return !greaterThan(date) && !equals(date);
    }

    public static boolean isCorrect(String date) {
        boolean result = false;
        String[] original = date.split("/");
        try {
            result = Integer.parseInt(original[0]) > 0 && Integer.parseInt(original[0]) <= 31 &&
                    Integer.parseInt(original[1]) > 0 && Integer.parseInt(original[1]) <= 12;
        } catch (NumberFormatException ex) {
            result = false;
        }
        return result;
    }

    @Override
    public String toString() {
        return getDay() + "/" + getMonth() + "/" + getYear();
    }
}
