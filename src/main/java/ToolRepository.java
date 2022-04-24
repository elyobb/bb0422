import java.util.HashMap;
import java.util.Map;

/**
 * A collection of all tools offered in the rental system. The 4-letter code acts as a primary key for looking up tool
 * details when attempting to check the tool out for rent.
 */
public class ToolRepository {

    private static final Map<String, Tool> tools;

    static
    {
        tools = new HashMap<>();
        tools.put("CHNS", new Tool(ToolType.CHAINSAW, "Stihl"));
        tools.put("LADW", new Tool(ToolType.LADDER, "Werner"));
        tools.put("JAKD", new Tool(ToolType.JACKHAMMER, "DeWalt"));
        tools.put("JAKR", new Tool(ToolType.JACKHAMMER, "Ridgid"));
    }

    public static Tool getTool(String toolCode)
    {
        return tools.get(toolCode);
    }
}
