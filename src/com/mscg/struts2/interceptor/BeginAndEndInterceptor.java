package com.mscg.struts2.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class BeginAndEndInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 62150656732419332L;

	private static Log LOG = LogFactory.getLog(BeginAndEndInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		LOG.debug("Before execution");
		String ret = invocation.invoke();
		LOG.debug("After execution");
		return ret;
	}

}
