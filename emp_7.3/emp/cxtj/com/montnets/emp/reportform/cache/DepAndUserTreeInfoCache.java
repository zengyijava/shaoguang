package com.montnets.emp.reportform.cache;

import com.montnets.emp.reportform.dto.DepOrUserTreeDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作员跟机构树的缓存
 * @author chen guang
 * @date 2018-1-3 08:42:00
 */
public class DepAndUserTreeInfoCache {

    public final static List<DepOrUserTreeDto> USER_TREE = new ArrayList<DepOrUserTreeDto>();

    public final static List<DepOrUserTreeDto> DEP_TREE = new ArrayList<DepOrUserTreeDto>();


    public static void clear() {
        USER_TREE.clear();
        DEP_TREE.clear();
    }
}
