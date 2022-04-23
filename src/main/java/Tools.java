import java.util.HashMap;
import java.util.Map;

public class Tools {
    private static Map<String, Tool> tools;
    static
    {
        tools = new HashMap<>();
        tools.put("CHNS", new Tool("CHNS", ToolType.CHAINSAW, "Stihl"));
        tools.put("LADW", new Tool("LADW", ToolType.LADDER, "Werner"));
        tools.put("JAKD", new Tool("JAKD", ToolType.JACKHAMMER, "DeWalt"));
        tools.put("JAKR", new Tool("JAKR", ToolType.JACKHAMMER, "Ridgid"));
    }

    public static Tool getTool(String toolCode)
    {
        return tools.get(toolCode);
    }
}
