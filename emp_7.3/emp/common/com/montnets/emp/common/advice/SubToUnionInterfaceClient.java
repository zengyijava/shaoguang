
package com.montnets.emp.common.advice;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;

public class SubToUnionInterfaceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public SubToUnionInterfaceClient() {
        create0();
        String suggest_report_url = SystemGlobals.getValue(StaticValue.SUGGEST_REPORT_URL);
        Endpoint SubToUnionInterfaceSoapEP = service0 .addEndpoint(new QName("http://www.mboss.com", "SubToUnionInterfaceSoap"), 
        		new QName("http://www.mboss.com", "SubToUnionInterfaceSoap"), suggest_report_url);
        endpoints.put(new QName("http://www.mboss.com", "SubToUnionInterfaceSoap"), SubToUnionInterfaceSoapEP);
        Endpoint SubToUnionInterfaceSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://www.mboss.com", "SubToUnionInterfaceSoapLocalEndpoint"), new QName("http://www.mboss.com", "SubToUnionInterfaceSoapLocalBinding"), "xfire.local://SubToUnionInterface");
        endpoints.put(new QName("http://www.mboss.com", "SubToUnionInterfaceSoapLocalEndpoint"), SubToUnionInterfaceSoapLocalEndpointEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
        	EmpExecutionContext.error(e, "获取endpoint异常。");
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((SubToUnionInterfaceSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://www.mboss.com", "SubToUnionInterfaceSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://www.mboss.com", "SubToUnionInterfaceSoapLocalBinding"), "urn:xfire:transport:local");
        }
    }

    public SubToUnionInterfaceSoap getSubToUnionInterfaceSoap() {
        return ((SubToUnionInterfaceSoap)(this).getEndpoint(new QName("http://www.mboss.com", "SubToUnionInterfaceSoap")));
    }

    public SubToUnionInterfaceSoap getSubToUnionInterfaceSoap(String url) {
        SubToUnionInterfaceSoap var = getSubToUnionInterfaceSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public SubToUnionInterfaceSoap getSubToUnionInterfaceSoapLocalEndpoint() {
        return ((SubToUnionInterfaceSoap)(this).getEndpoint(new QName("http://www.mboss.com", "SubToUnionInterfaceSoapLocalEndpoint")));
    }

    public SubToUnionInterfaceSoap getSubToUnionInterfaceSoapLocalEndpoint(String url) {
        SubToUnionInterfaceSoap var = getSubToUnionInterfaceSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public static void main(String[] args) 
    {
        SubToUnionInterfaceClient client = new SubToUnionInterfaceClient();
        Request req = new Request();
        req.setBizCode("S004");
        req.setSvcCont("aa");
        
	    Response resp=   client.getSubToUnionInterfaceSoap().subUnionInterface(req);
	
    }

}
