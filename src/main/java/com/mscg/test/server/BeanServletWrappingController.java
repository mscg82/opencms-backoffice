package com.mscg.test.server;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class BeanServletWrappingController extends AbstractController
	implements BeanNameAware, InitializingBean, DisposableBean {

	private String servletName;

	private Properties initParameters = new Properties();

	private String beanName;

	private Servlet servletBean;


	/**
	 * Set the servlet bean of the servlet to wrap.
	 * Needs to implement <code>javax.servlet.Servlet</code>.
	 * @see javax.servlet.Servlet
	 */
	public void setServletBean(Servlet servletBean) {
		this.servletBean = servletBean;
	}

	/**
	 * Set the name of the servlet to wrap.
	 * Default is the bean name of this controller.
	 */
	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	/**
	 * Specify init parameters for the servlet to wrap,
	 * as name-value pairs.
	 */
	public void setInitParameters(Properties initParameters) {
		this.initParameters = initParameters;
	}

	public void setBeanName(String name) {
		this.beanName = name;
	}


	/**
	 * Initialize the wrapped Servlet instance.
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.servletBean == null) {
			throw new IllegalArgumentException("servletBean is required");
		}
		if (this.servletName == null) {
			this.servletName = this.beanName;
		}
		this.servletBean.init(new DelegatingServletConfig());
	}


	/**
	 * Invoke the the wrapped Servlet instance.
	 * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		this.servletBean.service(request, response);
		return null;
	}


	/**
	 * Destroy the wrapped Servlet instance.
	 * @see javax.servlet.Servlet#destroy()
	 */
	public void destroy() {
		this.servletBean.destroy();
	}


	/**
	 * Internal implementation of the ServletConfig interface, to be passed
	 * to the wrapped servlet. Delegates to ServletWrappingController fields
	 * and methods to provide init parameters and other environment info.
	 */
	private class DelegatingServletConfig implements ServletConfig {

		public String getServletName() {
			return servletName;
		}

		public ServletContext getServletContext() {
			return BeanServletWrappingController.this.getServletContext();
		}

		public String getInitParameter(String paramName) {
			return initParameters.getProperty(paramName);
		}

		public Enumeration getInitParameterNames() {
			return initParameters.keys();
		}
	}

}
