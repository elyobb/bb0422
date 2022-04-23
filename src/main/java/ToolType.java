/**
 * Represents all the different types of tools offered in the rental system.
 */
public enum ToolType
{
    CHAINSAW("Chainsaw"),
    LADDER("Ladder"),
    JACKHAMMER("Jackhammer");

    private final String value;

    ToolType(final String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}