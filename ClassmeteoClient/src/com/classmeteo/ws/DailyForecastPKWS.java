// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JSR-172 Reference Implementation wscompile 1.0, using: JAX-RPC Standard Implementation (1.1, build R59)

package com.classmeteo.ws;


public class DailyForecastPKWS {
    protected java.lang.String coopId;
    protected int dyNum;
    
    public DailyForecastPKWS() {
    }
    
    public DailyForecastPKWS(java.lang.String coopId, int dyNum) {
        this.coopId = coopId;
        this.dyNum = dyNum;
    }
    
    public java.lang.String getCoopId() {
        return coopId;
    }
    
    public void setCoopId(java.lang.String coopId) {
        this.coopId = coopId;
    }
    
    public int getDyNum() {
        return dyNum;
    }
    
    public void setDyNum(int dyNum) {
        this.dyNum = dyNum;
    }
}
