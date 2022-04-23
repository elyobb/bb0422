import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Tests that {@link Checkout#createRentalAgreement(String, int, int, LocalDate)} creates the expected {@link ToolRentalAgreement},
 * and that the expected output is produced when printing the agreement to the console.
 */
public class ToolRentalAgreementTest {

    /**
     * Tests that the expected exception is thrown when the discount exceeds 100%.
     */
    @Test
    public void testExcessiveDiscount() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            LocalDate checkoutDate = LocalDate.of(9, 3, 15);
            Checkout.createRentalAgreement("JAKR", 5, 101, checkoutDate);
        });
        assertEquals("Discount percent must be between 0 and 100.", thrown.getMessage());
    }

    /**
     * Tests that the expected exception is thrown when the discount is less than 0%.
     */
    @Test
    public void testNegativeDiscount() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            LocalDate checkoutDate = LocalDate.of(9, 3, 15);
            Checkout.createRentalAgreement("JAKR", 5, -1, checkoutDate);
        });
        assertEquals("Discount percent must be between 0 and 100.", thrown.getMessage());
    }

    /**
     * Tests that the expected exception is thrown when the number of rental days is not a positive number.
     */
    @Test
    public void testZeroRentalDays() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            LocalDate checkoutDate = LocalDate.of(9, 3, 15);
            Checkout.createRentalAgreement("JAKR", 0, 10, checkoutDate);
        }, "Rental day count must be at least 1.");
        assertEquals("Rental day count must be at least 1.", thrown.getMessage());
    }

    /**
     * Tests that the expected exception is thrown when the tool code is not in the system.
     */
    @Test
    public void testInvalidToolCode() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            LocalDate checkoutDate = LocalDate.of(9, 3, 15);
            Checkout.createRentalAgreement("XXXXXXXXXX", 5, 10, checkoutDate);
        });
        assertEquals("Tool code does not correspond to an existing tool.", thrown.getMessage());
    }

    /**
     * Tests that the expected output is generated when a ladder is rented with a 10% discount over a time period
     * containing a holiday. There is no charge for holidays when renting a ladder.
     */
    @Test
    public void testJulyFourthLadder() {

        String expectedOutput = """
                Tool code: LADW
                Tool type: Ladder
                Tool brand: Werner
                Rental days: 2
                Checkout date: 07-02-20
                Due date: 07-04-20
                Daily rental charge: $1.99
                Charge days: 1
                Pre-discount charge: $1.99
                Discount percent: 10%
                Discount amount: $0.20
                Final charge: $1.79""";

        ToolRentalAgreement rentalAgreement = Checkout.createRentalAgreement("LADW", 2,
                10, LocalDate.of(20, 7, 2));

        assertEquals(expectedOutput, rentalAgreement.getOutput());
    }

    /**
     * Tests that the expected output is generated when a chainsaw is rented with a 25% discount over a time period
     * containing a full weekend and also a holiday. There is no charge for weekends when renting a chainsaw, but there is
     * for the holiday.
     */
    @Test
    public void testWeekendAndJulyFourthChainsaw() {

        String expectedOutput = """
                Tool code: CHNS
                Tool type: Chainsaw
                Tool brand: Stihl
                Rental days: 5
                Checkout date: 07-02-15
                Due date: 07-07-15
                Daily rental charge: $1.49
                Charge days: 3
                Pre-discount charge: $4.47
                Discount percent: 25%
                Discount amount: $1.12
                Final charge: $3.35""";

        ToolRentalAgreement rentalAgreement = Checkout.createRentalAgreement("CHNS", 5,
                25, LocalDate.of(15, 7, 2));

        assertEquals(expectedOutput, rentalAgreement.getOutput());
    }

    /**
     * Tests that the expected output is generated when a jackhammer is rented over a time period containing both a
     * full weekend and a holiday. There is no charge for holidays or weekends when renting a jackhammer.
     */
    @Test
    public void testWeekendAndLaborDayJackhammer() {

        String expectedOutput = """
                Tool code: JAKD
                Tool type: Jackhammer
                Tool brand: DeWalt
                Rental days: 6
                Checkout date: 09-03-15
                Due date: 09-09-15
                Daily rental charge: $2.99
                Charge days: 3
                Pre-discount charge: $8.97
                Discount percent: 0%
                Discount amount: $0.00
                Final charge: $8.97""";

        ToolRentalAgreement rentalAgreement = Checkout.createRentalAgreement("JAKD", 6,
                0, LocalDate.of(15, 9, 3));

        assertEquals(expectedOutput, rentalAgreement.getOutput());
    }

    /**
     * Tests that the expected output is generated when a jackhammer is rented over a time period containing a holiday
     * which falls on a weekend. There is no charge for holidays or weekends when renting a jackhammer, and since the
     * holiday falls on Saturday, it is celebrated on the closest weekday (Friday). This means Friday through Sunday
     * are non-charge days.
     */
    @Test
    public void testWeekendJulyFourthJackhammer() {

        String expectedOutput = """
                Tool code: JAKR
                Tool type: Jackhammer
                Tool brand: Ridgid
                Rental days: 9
                Checkout date: 07-02-15
                Due date: 07-11-15
                Daily rental charge: $2.99
                Charge days: 5
                Pre-discount charge: $14.96
                Discount percent: 0%
                Discount amount: $0.00
                Final charge: $14.96""";

        ToolRentalAgreement rentalAgreement = Checkout.createRentalAgreement("JAKR", 9,
                0, LocalDate.of(15, 7, 2));

        assertEquals(expectedOutput, rentalAgreement.getOutput());
    }

    /**
     * Tests that the expected output is generated when a jackhammer is rented with a 50% discount over a time period
     * containing a holiday which falls on a weekend. There is no charge for holidays or weekends when renting a jackhammer,
     * and since the holiday falls on Saturday, it is celebrated on the closest weekday (Friday). This means Friday
     * through Sunday are non-charge days.
     */
    @Test
    public void testWeekendJulyFourthJackhammerHalfOff() {

        String expectedOutput = """
                Tool code: JAKR
                Tool type: Jackhammer
                Tool brand: Ridgid
                Rental days: 4
                Checkout date: 07-02-20
                Due date: 07-06-20
                Daily rental charge: $2.99
                Charge days: 1
                Pre-discount charge: $2.99
                Discount percent: 50%
                Discount amount: $1.50
                Final charge: $1.49""";

        ToolRentalAgreement rentalAgreement = Checkout.createRentalAgreement("JAKR", 4,
                50, LocalDate.of(20, 7, 2));

        assertEquals(expectedOutput, rentalAgreement.getOutput());
    }
}