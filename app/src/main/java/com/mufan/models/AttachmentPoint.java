package com.mufan.models;

/**
 * AttachmentPoint class
 * Created by mufan on 2016/5/22.
 */
public class AttachmentPoint {

    protected String switchDPID;
    protected int port;
    protected String errorStatus;

    public AttachmentPoint(String switchDPID){
        this.switchDPID = switchDPID;
        this.port = -1;
        this.errorStatus = null;
    }
    public String getSwitchDPID() {
        return switchDPID;
    }
    public void setSwitchDPID(String switchDPID) {
        this.switchDPID = switchDPID;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getErrorStatus() {
        return errorStatus;
    }
    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    @Override
    public String toString() {
        return "AttachmentPoint{" +
                "switchDPID='" + switchDPID + '\'' +
                ", port=" + port +
                ", errorStatus='" + errorStatus + '\'' +
                '}';
    }
}
