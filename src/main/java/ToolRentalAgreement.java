import java.util.Objects;

/**
 * Represents a tool rental agreement.
 */
public class ToolRentalAgreement {
    private String code;
    private String type;
    private String brand;
    private int numberOfDays;
    private String checkoutDate;
    private String dueDate;
    private String dailyRentalCharge;
    private int chargeDays;
    private String preDiscountCharge;
    private int discountPercent;
    private String discountAmount;
    private String finalCharge;

    public ToolRentalAgreement(String code, String type, String brand, int numberOfDays, String checkoutDate,
                               String dueDate, String dailyRentalCharge, int chargeDays, String preDiscountCharge,
                               int discountPercent, String discountAmount, String finalCharge) {
        this.code = code;
        this.type = type;
        this.brand = brand;
        this.numberOfDays = numberOfDays;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyRentalCharge = dailyRentalCharge;
        this.chargeDays = chargeDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;
    }

    /**
     * Prints out the rental agreement to the console.
     */
    public void print()
    {
        System.out.println(getOutput());
    }

    /**
     * Constructs the rental agreement text.
     *
     * @return the constructed rental agreement text
     */
    public String getOutput()
    {
        return "Tool code: " + code + "\n" +
                "Tool type: " + type + "\n" +
                "Tool brand: " + brand + "\n" +
                "Rental days: " + numberOfDays + "\n" +
                "Checkout date: " + checkoutDate + "\n" +
                "Due date: " + dueDate + "\n" +
                "Daily rental charge: " + dailyRentalCharge + "\n" +
                "Charge days: " + chargeDays + "\n" +
                "Pre-discount charge: " + preDiscountCharge + "\n" +
                "Discount percent: " + discountPercent + "%\n" +
                "Discount amount: " + discountAmount + "\n" +
                "Final charge: " + finalCharge;
    }
}
