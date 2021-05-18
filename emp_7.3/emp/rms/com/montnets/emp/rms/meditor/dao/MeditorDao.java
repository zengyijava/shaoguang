package com.montnets.emp.rms.meditor.dao;

import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTmplRela;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.meditor.dto.PageListDto;
import com.montnets.emp.rms.meditor.entity.LfHfive;
import com.montnets.emp.rms.meditor.entity.LfTempContent;
import com.montnets.emp.rms.meditor.vo.TempHFiveVo;
import com.montnets.emp.rms.meditor.vo.TempsVo;
import com.montnets.emp.util.PageInfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface MeditorDao {

    /**
     * 获取模板列表
     * @param tempsVo
     * @return
     */
    PageListDto getTemps(TempsVo tempsVo, HttpServletRequest request, HttpServletResponse response) throws Exception;
    
    /**
     * 行业用途-查询
     * @param lfIndustryUse
     * @return
     * @throws Exception
     */
    List<LfIndustryUse> getIndustryUseList(LfIndustryUse lfIndustryUse)throws Exception;
    
    /**
     * H5-列表查询
     * @param tempHFiveVo
     * @return
     * @throws Exception
     */
    List<LfHfive> getH5s(TempHFiveVo tempHFiveVo,PageInfo pageinfo)throws Exception;

    /**
     * 模板内容-查询
     * @param tmId
     * @param contType
     * @param tmpType
     * @return
     * @throws Exception
     */
    List<String> getTempContents(String tmId,String contType,String tmpType)throws Exception;

    /**
     * 查询共享模板
     * @param tmId
     * @param lfSysuser
     * @return
     */
    List<LfTmplRela> findSharTmp(Long tmId, LfSysuser lfSysuser);
}
