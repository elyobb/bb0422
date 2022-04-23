public class Tool {

    public Tool(String toolCode, ToolType toolType, String toolBrand)
    {
        this.toolCode = toolCode;
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

    public void setToolChargeData()
    {
        switch(toolType)
        {
            case CHAINSAW :
                this.toolChargeData = new ChainsawChargeData();
                break;
            case LADDER:
                this.toolChargeData = new LadderChargeData();
                break;
            case JACKHAMMER:
                this.toolChargeData = new JackhammerChargeData();
                break;
        }
    }

    public ToolChargeData getChargeData()
    {
        return toolChargeData;
    }

    private String toolCode;
    private ToolType toolType;
    private String toolBrand;
    private ToolChargeData toolChargeData;
}
