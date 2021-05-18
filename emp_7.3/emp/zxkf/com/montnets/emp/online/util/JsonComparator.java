package com.montnets.emp.online.util;
import java.util.Comparator;

import org.json.simple.JSONObject;

public class JsonComparator implements Comparator<JSONObject>
{
    public int compare(JSONObject lbs1, JSONObject lbs2)
    {
        return ((Integer) lbs1.get("count")).compareTo((Integer) lbs2.get("count"));
    }
}
