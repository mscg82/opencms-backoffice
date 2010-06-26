/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.connection;

/**
 *
 * @author Giuseppe Miscione
 */
public interface HttpMethodExecutionListener {

    public void onMethodExecuted(HttpMethod method);

    public void onMethodExecutionError(Exception e, HttpMethod method);

    public void onSendingData(HttpMethod method);

    public void onResponse(HttpMethod method);
    
}
