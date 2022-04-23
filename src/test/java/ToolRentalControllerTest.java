import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Tests the business logic handled by {@link ToolRentalController}.
 */
public class ToolRentalControllerTest {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy");

    /**
     * Tests that the expected exception is thrown when the provided discount exceeds 100%.
     */
    @Test
    public void testExcessiveDiscount()
    {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Date checkoutDate = simpleDateFormat.parse("09-03-15");
            RentalAgreement.checkout("JAKR", 5, 101, checkoutDate);
        } );
        assertEquals("Discount percent must be between 0 and 100.", thrown.getMessage());
    }

    /**
     * Tests that the expected exception is thrown when the provided discount is less than 0%.
     */
    @Test
    public void testNegativeDiscount()
    {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Date checkoutDate = simpleDateFormat.parse("09-03-15");
            RentalAgreement.checkout("JAKR", 5, -1, checkoutDate);
        });
        assertEquals("Discount percent must be between 0 and 100.", thrown.getMessage());
    }

    /**
     * Tests that the expected exception is thrown when the number of rental days is not a positive number.
     */
    @Test
    public void testZeroRentalDays()
    {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Date checkoutDate = simpleDateFormat.parse("09-03-15");
            RentalAgreement.checkout("JAKR", 0, 10, checkoutDate);
        }, "Rental day count must be at least 1." );
        assertEquals("Rental day count must be at least 1.", thrown.getMessage());
    }

    /**
     * Tests that the expected exception is thrown when the provided tool code does not represent a known tool in the system.
     */
    @Test
    public void testInvalidToolCode()
    {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Date checkoutDate = simpleDateFormat.parse("09-03-15");
            RentalAgreement.checkout("XXXX", 5, 10, checkoutDate);
        });
        assertEquals("Tool code does not correspond to a known tool type.", thrown.getMessage());
    }

    /**
     * Tests that the expected output is generated when a ladder is rented with a 10% discount over a time period
     * containing a holiday. There is no charge for holidays when renting a ladder.
     *
     * @throws ParseException if the checkout date fails to be parsed by SimpleDateFormat
     */
    @Test
    public void testJulyFourthLadder() throws ParseException {

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

        String actualOutput = RentalAgreement.checkout("LADW", 2, 10, simpleDateFormat.parse("07-02-20"));

        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests that the expected output is generated when a chainsaw is rented with a 25% discount over a time period
     * containing a full weekend and also a holiday. There is no charge for weekends when renting a chainsaw, but there is
     * for the holiday.
     *
     * @throws ParseException if the checkout date fails to be parsed by SimpleDateFormat
     */
    @Test
    public void testWeekendAndJulyFourthChainsaw() throws ParseException {

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

        String actualOutput = RentalAgreement.checkout("CHNS", 5, 25, simpleDateFormat.parse("07-02-15"));

        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests that the expected output is generated when a jackhammer is rented (no discount) over a time period
     * containing both a full weekend and a holiday. There is no charge for holidays or weekends when renting a jackhammer.
     *
     * @throws ParseException if the checkout date fails to be parsed by SimpleDateFormat
     */
    @Test
    public void testWeekendAndLaborDayJackhammer() throws ParseException {

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

        String actualOutput = RentalAgreement.checkout("JAKD", 6, 0, simpleDateFormat.parse("09-03-15"));

        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests that the expected output is generated when a jackhammer is rented (no discount) over a time period
     * containing a holiday which falls on a weekend. There is no charge for holidays or weekends when renting a jackhammer,
     * and the weekend holiday should count as a single day of not being charged.
     *
     * @throws ParseException if the checkout date fails to be parsed by SimpleDateFormat
     */
    @Test
    public void testWeekendJulyFourthJackhammer() throws ParseException {

        String expectedOutput = """
                Tool code: JAKR
                Tool type: Jackhammer
                Tool brand: Ridgid
                Rental days: 9
                Checkout date: 07-02-15
                Due date: 07-11-15
                Daily rental charge: $2.99
                Charge days: 6
                Pre-discount charge: $17.94
                Discount percent: 0%
                Discount amount: $0.00
                Final charge: $17.94""";

        String actualOutput = RentalAgreement.checkout("JAKR", 9, 0, simpleDateFormat.parse("07-02-15"));

        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests that the expected output is generated when a jackhammer is rented with a 50% discount over a time period
     * containing a holiday which falls on a weekend. There is no charge for holidays or weekends when renting a jackhammer,
     * and the weekend holiday should count as a single day of not being charged.
     *
     * @throws ParseException if the checkout date fails to be parsed by SimpleDateFormat
     */
    @Test
    public void testWeekendJulyFourthJackhammerHalfOff() throws ParseException {

        String expectedOutput = """
                Tool code: JAKR
                Tool type: Jackhammer
                Tool brand: Ridgid
                Rental days: 4
                Checkout date: 07-02-20
                Due date: 07-06-20
                Daily rental charge: $2.99
                Charge days: 2
                Pre-discount charge: $5.98
                Discount percent: 50%
                Discount amount: $2.99
                Final charge: $2.99""";

        String actualOutput = RentalAgreement.checkout("JAKR", 4, 50, simpleDateFormat.parse("07-02-20"));

        assertEquals(expectedOutput, actualOutput);
    }
}