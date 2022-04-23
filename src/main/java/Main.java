import java.util.Date;

/**
 * Calls {@link RentalAgreement#checkout(String, int, int, Date)} and writes the output to the console.
 */
public class Main {
    public static void main(String[] args)
    {
        // the rental date will be "now"
        System.out.println(RentalAgreement.checkout("CHNS", 4, 0, new Date()));
    }
}
