import java.util.HashMap;
import java.util.Map;

/**
 * Mock database containing all tools currently offered in the rental system. The 4-letter code acts as a primary key
 * for looking up tool details when checking out.
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
