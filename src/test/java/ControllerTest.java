import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ControllerTest {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy");

    @Test
    public void testExcessiveDiscount()
    {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Date checkoutDate = simpleDateFormat.parse("09-03-15");
            RentalAgreement.checkout("JAKR", 5, 101, checkoutDate);
        } );
        assertEquals("Discount percent must be between 0 and 100.", thrown.getMessage());
    }

    @Test
    public void testNegativeDiscount()
    {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Date checkoutDate = simpleDateFormat.parse("09-03-15");
            RentalAgreement.checkout("JAKR", 5, -1, checkoutDate);
        });
        assertEquals("Discount percent must be between 0 and 100.", thrown.getMessage());
    }

    @Test
    public void testZeroRentalDays()
    {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Date checkoutDate = simpleDateFormat.parse("09-03-15");
            RentalAgreement.checkout("JAKR", 0, 10, checkoutDate);
        }, "Rental day count must be at least 1." );
        assertEquals("Rental day count must be at least 1.", thrown.getMessage());
    }

    // one weekend day is not charged
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

    // holiday does not affect charge
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

    // full weekend and holiday are not charged
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

    @Test
    public void testJulyFourthJackhammer() throws ParseException {

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

    @Test
    public void testJulyFourthJackhammerHalfOff() throws ParseException {

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