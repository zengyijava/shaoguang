
package com.montnets.emp.common.advice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "SubToUnionInterfaceSoap", targetNamespace = "http://www.mboss.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface SubToUnionInterfaceSoap {

	@WebMethod(operationName = "SubUnionInterface", action = "http://www.mboss.com/SubUnionInterface")
	@WebResult(name = "SubUnionInterfaceResult", targetNamespace = "http://www.mboss.com")
	public Response subUnionInterface(
			@WebParam(name = "request", targetNamespace = "http://www.mboss.com") Request request);

}
