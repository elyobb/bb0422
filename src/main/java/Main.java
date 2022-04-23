import java.time.LocalDate;

/**
 * Creates a sample {@link ToolRentalAgreement} and calls {@link ToolRentalAgreement#print()} to print to console.
 */
public class Main {
    public static void main(String[] args)
    {
        // the rental date will be "now"
        Checkout.createRentalAgreement("CHNS", 4, 0, LocalDate.now()).print();
    }
}
