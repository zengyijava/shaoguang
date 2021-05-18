package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

public class LfMonWbsbuf
{

	//RPT滞留量
	private Integer rptnosd;

	//MT滞留量
	private Integer mtnosd;

	//Rpt发送缓冲
	private Integer rptsdbuf;

	//MO/RPT瞬时转发速度
	private Integer morptsdspd1;

	//写MTTASK缓冲
	private Integer wrmtbuf;

	//日志文件数量
	private Integer logfilenum;

	//MO/Rpt发送缓冲
	private Integer morptsdbuf;

	//接收缓冲
	private Integer recvbuf;

	//程序编号
	private Long proceid;

	//MO/RPT瞬时接收速度
	private Integer morptrvspd1;

	//MO滞留量
	private Integer monosd;

	//MO/RPT一分钟的平均转发速度
	private Integer morptrvspd2;

	//前端回应临时缓冲
	private Integer prerspbuf;

	//MO/RPT一分钟的平均转发速度
	private Integer morptsdspd2;

	private Long id;

	//补发缓冲
	private Integer suppsdbuf;

	//前端连接数
	private Integer precnt;

	//后端连接数
	private Integer endcnt;

	//MT瞬时转发速度
	private Integer mtsdspd1;

	//MT一分钟的平均转发速度
	private Integer mtsdspd2;

	//日志缓冲
	private Integer logbuf;

	//MO写库缓冲
	private Integer wrrptbuf;

	//MO写库缓冲
	private Integer updmobuf;

	//MO转发总量
	private Integer mosdcnt;

	//写MTLVL缓冲
	private Integer wrmtlvlbuf;

	//后端回应缓冲
	private Integer mtsdbuf;

	//写MTVFY缓冲
	private Integer wrmtvfybuf;

	//更新时间
	private Timestamp updatetime;

	//MT一分钟的平均接收速度
	private Integer mtrvspd2;

	//MT发送等待缓冲
	private Integer mtwaitbuf;

	//MT瞬时接收速度
	private Integer mtrvspd1;

	//MO写库缓冲
	private Integer wrmobuf;

	//重发缓冲
	private Integer resndbuf;

	//MO接收总量
	private Integer mtsdcnt;

	//MO转发总量
	private Integer morvcnt;

	//MO/RPT发送等待缓冲
	private Integer morptwaitbuf;

	//后端回应缓冲
	private Integer endrspbuf;

	//网关编号
	private Long gatewayid;

	//MT接收总量
	private Integer mtrvcnt;

	//前端回应临时缓冲
	private Integer prersptmpbuf;

	public LfMonWbsbuf(){
	} 

	public Integer getRptnosd(){

		return rptnosd;
	}

	public void setRptnosd(Integer rptnosd){

		this.rptnosd= rptnosd;

	}

	public Integer getMtnosd(){

		return mtnosd;
	}

	public void setMtnosd(Integer mtnosd){

		this.mtnosd= mtnosd;

	}

	public Integer getRptsdbuf(){

		return rptsdbuf;
	}

	public void setRptsdbuf(Integer rptsdbuf){

		this.rptsdbuf= rptsdbuf;

	}

	public Integer getMorptsdspd1(){

		return morptsdspd1;
	}

	public void setMorptsdspd1(Integer morptsdspd1){

		this.morptsdspd1= morptsdspd1;

	}

	public Integer getWrmtbuf(){

		return wrmtbuf;
	}

	public void setWrmtbuf(Integer wrmtbuf){

		this.wrmtbuf= wrmtbuf;

	}

	public Integer getLogfilenum(){

		return logfilenum;
	}

	public void setLogfilenum(Integer logfilenum){

		this.logfilenum= logfilenum;

	}

	public Integer getMorptsdbuf(){

		return morptsdbuf;
	}

	public void setMorptsdbuf(Integer morptsdbuf){

		this.morptsdbuf= morptsdbuf;

	}

	public Integer getRecvbuf(){

		return recvbuf;
	}

	public void setRecvbuf(Integer recvbuf){

		this.recvbuf= recvbuf;

	}

	public Long getProceid(){

		return proceid;
	}

	public void setProceid(Long proceid){

		this.proceid= proceid;

	}

	public Integer getMorptrvspd1(){

		return morptrvspd1;
	}

	public void setMorptrvspd1(Integer morptrvspd1){

		this.morptrvspd1= morptrvspd1;

	}

	public Integer getMonosd(){

		return monosd;
	}

	public void setMonosd(Integer monosd){

		this.monosd= monosd;

	}

	public Integer getMorptrvspd2(){

		return morptrvspd2;
	}

	public void setMorptrvspd2(Integer morptrvspd2){

		this.morptrvspd2= morptrvspd2;

	}

	public Integer getPrerspbuf(){

		return prerspbuf;
	}

	public void setPrerspbuf(Integer prerspbuf){

		this.prerspbuf= prerspbuf;

	}

	public Integer getMorptsdspd2(){

		return morptsdspd2;
	}

	public void setMorptsdspd2(Integer morptsdspd2){

		this.morptsdspd2= morptsdspd2;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Integer getSuppsdbuf(){

		return suppsdbuf;
	}

	public void setSuppsdbuf(Integer suppsdbuf){

		this.suppsdbuf= suppsdbuf;

	}

	public Integer getPrecnt(){

		return precnt;
	}

	public void setPrecnt(Integer precnt){

		this.precnt= precnt;

	}

	public Integer getEndcnt(){

		return endcnt;
	}

	public void setEndcnt(Integer endcnt){

		this.endcnt= endcnt;

	}

	public Integer getMtsdspd1(){

		return mtsdspd1;
	}

	public void setMtsdspd1(Integer mtsdspd1){

		this.mtsdspd1= mtsdspd1;

	}

	public Integer getMtsdspd2(){

		return mtsdspd2;
	}

	public void setMtsdspd2(Integer mtsdspd2){

		this.mtsdspd2= mtsdspd2;

	}

	public Integer getLogbuf(){

		return logbuf;
	}

	public void setLogbuf(Integer logbuf){

		this.logbuf= logbuf;

	}

	public Integer getWrrptbuf(){

		return wrrptbuf;
	}

	public void setWrrptbuf(Integer wrrptbuf){

		this.wrrptbuf= wrrptbuf;

	}

	public Integer getUpdmobuf(){

		return updmobuf;
	}

	public void setUpdmobuf(Integer updmobuf){

		this.updmobuf= updmobuf;

	}

	public Integer getMosdcnt(){

		return mosdcnt;
	}

	public void setMosdcnt(Integer mosdcnt){

		this.mosdcnt= mosdcnt;

	}

	public Integer getWrmtlvlbuf(){

		return wrmtlvlbuf;
	}

	public void setWrmtlvlbuf(Integer wrmtlvlbuf){

		this.wrmtlvlbuf= wrmtlvlbuf;

	}

	public Integer getMtsdbuf(){

		return mtsdbuf;
	}

	public void setMtsdbuf(Integer mtsdbuf){

		this.mtsdbuf= mtsdbuf;

	}

	public Integer getWrmtvfybuf(){

		return wrmtvfybuf;
	}

	public void setWrmtvfybuf(Integer wrmtvfybuf){

		this.wrmtvfybuf= wrmtvfybuf;

	}

	public Timestamp getUpdatetime(){

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime){

		this.updatetime= updatetime;

	}

	public Integer getMtrvspd2(){

		return mtrvspd2;
	}

	public void setMtrvspd2(Integer mtrvspd2){

		this.mtrvspd2= mtrvspd2;

	}

	public Integer getMtwaitbuf(){

		return mtwaitbuf;
	}

	public void setMtwaitbuf(Integer mtwaitbuf){

		this.mtwaitbuf= mtwaitbuf;

	}

	public Integer getMtrvspd1(){

		return mtrvspd1;
	}

	public void setMtrvspd1(Integer mtrvspd1){

		this.mtrvspd1= mtrvspd1;

	}

	public Integer getWrmobuf(){

		return wrmobuf;
	}

	public void setWrmobuf(Integer wrmobuf){

		this.wrmobuf= wrmobuf;

	}

	public Integer getResndbuf(){

		return resndbuf;
	}

	public void setResndbuf(Integer resndbuf){

		this.resndbuf= resndbuf;

	}

	public Integer getMtsdcnt(){

		return mtsdcnt;
	}

	public void setMtsdcnt(Integer mtsdcnt){

		this.mtsdcnt= mtsdcnt;

	}

	public Integer getMorvcnt(){

		return morvcnt;
	}

	public void setMorvcnt(Integer morvcnt){

		this.morvcnt= morvcnt;

	}

	public Integer getMorptwaitbuf(){

		return morptwaitbuf;
	}

	public void setMorptwaitbuf(Integer morptwaitbuf){

		this.morptwaitbuf= morptwaitbuf;

	}

	public Integer getEndrspbuf(){

		return endrspbuf;
	}

	public void setEndrspbuf(Integer endrspbuf){

		this.endrspbuf= endrspbuf;

	}

	public Long getGatewayid(){

		return gatewayid;
	}

	public void setGatewayid(Long gatewayid){

		this.gatewayid= gatewayid;

	}

	public Integer getMtrvcnt(){

		return mtrvcnt;
	}

	public void setMtrvcnt(Integer mtrvcnt){

		this.mtrvcnt= mtrvcnt;

	}

	public Integer getPrersptmpbuf(){

		return prersptmpbuf;
	}

	public void setPrersptmpbuf(Integer prersptmpbuf){

		this.prersptmpbuf= prersptmpbuf;

	}
}
