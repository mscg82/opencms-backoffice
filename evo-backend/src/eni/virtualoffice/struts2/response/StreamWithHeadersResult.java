package eni.virtualoffice.struts2.response;

import java.io.InputStream;
import java.util.Map;

import org.apache.struts2.dispatcher.HttpHeaderResult;
import org.apache.struts2.dispatcher.StreamResult;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

public class StreamWithHeadersResult extends StreamResult {

	private static final long serialVersionUID = 2021897356635035346L;

	HttpHeaderResult headersResult;


	public StreamWithHeadersResult() {
		super();
		headersResult = new HttpHeaderResult();
	}

	public StreamWithHeadersResult(InputStream in) {
		super(in);
		headersResult = new HttpHeaderResult();
	}

	/**
	 * @param name
	 * @param value
	 * @see org.apache.struts2.dispatcher.HttpHeaderResult#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String name, String value) {
		headersResult.addHeader(name, value);
	}

	/**
     * @see org.apache.struts2.dispatcher.StrutsResultSupport#doExecute(java.lang.String, com.opensymphony.xwork2.ActionInvocation)
     */
    protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
    	resolveHeadersParamsFromStack(invocation.getStack());
    	headersResult.execute(invocation);
    	super.doExecute(finalLocation, invocation);
    }

	/**
	 * @return
	 * @see org.apache.struts2.dispatcher.HttpHeaderResult#getHeaders()
	 */
	public Map getHeaders() {
		return headersResult.getHeaders();
	}

	/**
     * Tries to lookup the parameters on the stack.  Will override any existing parameters
     *
     * @param stack The current value stack
     */
    protected void resolveHeadersParamsFromStack(ValueStack stack) {
    	Integer status = (Integer) stack.findValue("status", Integer.class);
    	if(status != null) {
    		setStatus(status);
    	}

    	Integer error = (Integer) stack.findValue("error", Integer.class);
    	if(error != null) {
    		setError(error);
    	}

    	String errorMessage = stack.findString("errorMessage");
    	if(errorMessage != null) {
    		setErrorMessage(errorMessage);
    	}

    	Map<String, String> headers = (Map<String, String>) stack.findValue("headers", Map.class);
    	if(headers != null) {
    		for(Map.Entry<String, String> entry : headers.entrySet()) {
    			addHeader(entry.getKey(), entry.getValue());
    		}
    	}
    }

	/**
	 * @param error
	 * @see org.apache.struts2.dispatcher.HttpHeaderResult#setError(int)
	 */
	public void setError(int error) {
		headersResult.setError(error);
	}

	/**
	 * @param errorMessage
	 * @see org.apache.struts2.dispatcher.HttpHeaderResult#setErrorMessage(java.lang.String)
	 */
	public void setErrorMessage(String errorMessage) {
		headersResult.setErrorMessage(errorMessage);
	}

	/**
	 * @param parse
	 * @see org.apache.struts2.dispatcher.HttpHeaderResult#setParse(boolean)
	 */
	public void setParse(boolean parse) {
		headersResult.setParse(parse);
	}

    /**
	 * @param status
	 * @see org.apache.struts2.dispatcher.HttpHeaderResult#setStatus(int)
	 */
	public void setStatus(int status) {
		headersResult.setStatus(status);
	}

}
