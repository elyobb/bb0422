/**
 * Represents a tool in the rental system. Each tool has a type and brand. A tool also has an associated daily rental charge,
 * which will depend on the tool's type. Certain tool types may also be exempt from daily charges on holidays and/or weekends.
 */
public class Tool {

    private final ToolType type;
    private final String brand;
    private ToolChargeData chargeData;

    public Tool(ToolType type, String brand)
    {
        this.type = type;
        this.brand = brand;
        setChargeData();
    }

    public ToolType getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    private void setChargeData()
    {
        switch (type) {
            case CHAINSAW -> this.chargeData = new ChainsawChargeData();
            case LADDER -> this.chargeData = new LadderChargeData();
            case JACKHAMMER -> this.chargeData = new JackhammerChargeData();
        }
    }

    public ToolChargeData getChargeData()
    {
        return chargeData;
    }
}
