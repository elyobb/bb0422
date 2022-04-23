/**
 * Represents a tool in the rental system. Each tool has a type and brand. A tool also has an associated daily rental charge,
 * which will fluctuate depending on the tool's type. Certain tool types may also be exempt from daily charges on special
 * days such as holidays or weekends.
 */
public class Tool {

    private final ToolType toolType;
    private final String toolBrand;
    private ToolChargeData toolChargeData;

    public Tool(ToolType toolType, String toolBrand)
    {
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        setToolChargeData();
    }

    public String getToolType() {
        return toolType.getValue();
    }

    public String getToolBrand() {
        return toolBrand;
    }

    private void setToolChargeData()
    {
        switch (toolType) {
            case CHAINSAW -> this.toolChargeData = new ChainsawChargeData();
            case LADDER -> this.toolChargeData = new LadderChargeData();
            case JACKHAMMER -> this.toolChargeData = new JackhammerChargeData();
        }
    }

    public ToolChargeData getChargeData()
    {
        return toolChargeData;
    }
}
