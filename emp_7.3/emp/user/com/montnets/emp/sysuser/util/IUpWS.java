/**
 * Program : IUpWS.java
 * Author : zousy
 * Create : 2014-6-9 下午03:19:36
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.sysuser.util;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-9 下午03:19:36
 */
public interface IUpWS
{

	public java.lang.String ESAI(java.lang.String reqMsg) throws java.rmi.RemoteException;

	public java.lang.String EITest() throws java.rmi.RemoteException;

}
