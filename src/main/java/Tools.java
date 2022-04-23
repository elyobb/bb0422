import java.util.HashMap;
import java.util.Map;

/**
 * Storage for all tools offered in the rental system.
 */
public class Tools {

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
