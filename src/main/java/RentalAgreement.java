import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RentalAgreement {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy");
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private static final DecimalFormat finalDecimalFormat =  new DecimalFormat("0.00");
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    public static String checkout(String toolCode, int rentalDayCount, int discountPercent, Date checkoutDate)
    {
        // ● Rental day count is not 1 or greater
        if(rentalDayCount < 1)
        {
            throw new IllegalArgumentException("Rental day count must be at least 1.");
        }

        if(discountPercent < 0 || discountPercent > 100)
        {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
        //● Discount percent is not in the range 0-100
        decimalFormat.setRoundingMode(RoundingMode.UP);

        final Tool tool = Tools.getTool(toolCode);
        final ToolChargeData toolChargeData = tool.getChargeData();

        final Date dueDate = Controller.calculateDueDate(checkoutDate, rentalDayCount);
        final int chargeDays = Controller.calculateChargeDays(toolChargeData, checkoutDate, dueDate, rentalDayCount);
        final double dailyCharge = toolChargeData.getDailyRentalCharge();
        final double preDiscountCharge = Double.parseDouble(decimalFormat.format(chargeDays * dailyCharge));
        final double discountAmount = Double.parseDouble(decimalFormat.format(preDiscountCharge * ((double)discountPercent / 100)));
        final double finalCharge = Double.parseDouble(finalDecimalFormat.format(preDiscountCharge - discountAmount));

        String consoleOutput = "Tool code: " + toolCode + "\n" +
                "Tool type: " + tool.getToolType() + "\n" +
                "Tool brand: " + tool.getToolBrand() + "\n" +
                "Rental days: " + rentalDayCount + "\n" +
                "Checkout date: " + simpleDateFormat.format(checkoutDate) + "\n" +
                "Due date: " + simpleDateFormat.format(dueDate) + "\n" +
                "Daily rental charge: " + currencyFormat.format(dailyCharge) + "\n" +
                "Charge days: " + chargeDays + "\n" +
                "Pre-discount charge: " + currencyFormat.format(preDiscountCharge) + "\n" +
                "Discount percent: " + discountPercent + "%\n" +
                "Discount amount: " + currencyFormat.format(discountAmount) + "\n" +
                "Final charge: " + currencyFormat.format(finalCharge);

        return consoleOutput;
    }
}
