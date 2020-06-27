package com.rolvatech.cgc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CommonResponse implements Serializable
{
    private static final long serialVersionUID = -5647066686699171851L;
    @SerializedName("StatusCode")
    @Expose
    private int StatusCode;
    @SerializedName("StatusMessage")
    @Expose
    private String StatusMessage;
    @SerializedName("Data")
    @Expose
    private String Data;
    @SerializedName("RouteMaster")
    @Expose
    private String RouteMaster;
    @SerializedName("RouteMarker")
    @Expose
    private String RouteMarker;
    @SerializedName("RouteProfile")
    @Expose
    private String RouteProfile;
    @SerializedName("Obervation")
    @Expose
    private String Obervation;//(i.e Incident Marker)
    @SerializedName("DefectiveMarker")
    @Expose
    private String DefectiveMarker;
    @SerializedName("LineWalker")
    @Expose
    private String LineWalker;
    @SerializedName("SOS")
    @Expose
    private String SOS;
    @SerializedName("MarkerAudit")
    @Expose
    private String MarkerAudit;


    /*Login String*/
    @SerializedName("RouteId")
    @Expose
    private String RouteId;
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("token_type")
    @Expose
    private String token_type;
    @SerializedName("expires_in")
    @Expose
    private String expires_in;
    @SerializedName("ControlRoomMaster")
    @Expose
    private String ControlRoomMaster;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public String getControlRoomMaster() {
        return ControlRoomMaster;
    }

    public void setControlRoomMaster(String controlRoomMaster) {
        ControlRoomMaster = controlRoomMaster;
    }

    public int getStatusCode() {
        return this.StatusCode;
    }

    public void setStatusCode(int StatusCode) {
        this.StatusCode = StatusCode;
    }

    public String getStatusMessage() {
        return this.StatusMessage;
    }

    public void setStatusMessage(String StatusMessage) {
        this.StatusMessage = StatusMessage;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getRouteMaster() {
        return RouteMaster;
    }

    public void setRouteMaster(String routeMaster) {
        RouteMaster = routeMaster;
    }

    public String getRouteMarker() {
        return RouteMarker;
    }

    public void setRouteMarker(String routeMarker) {
        RouteMarker = routeMarker;
    }

    public String getRouteProfile() {
        return RouteProfile;
    }

    public void setRouteProfile(String routeProfile) {
        RouteProfile = routeProfile;
    }


    public String getObervation() {
        return Obervation;
    }

    public void setObervation(String obervation) {
        Obervation = obervation;
    }

    public String getDefectiveMarker() {
        return DefectiveMarker;
    }

    public void setDefectiveMarker(String defectiveMarker) {
        DefectiveMarker = defectiveMarker;
    }

    public String getLineWalker() {
        return LineWalker;
    }

    public void setLineWalker(String lineWalker) {
        LineWalker = lineWalker;
    }

    public String getSOS() {
        return SOS;
    }

    public void setSOS(String SOS) {
        this.SOS = SOS;
    }

    public String getMarkerAudit() {
        return MarkerAudit;
    }

    public void setMarkerAudit(String markerAudit) {
        MarkerAudit = markerAudit;
    }

    public String getRouteId() {
        return RouteId;
    }

    public void setRouteId(String routeId) {
        RouteId = routeId;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "StatusCode=" + StatusCode +
                ", StatusMessage='" + StatusMessage + '\'' +
                ", Data='" + Data + '\'' +
                ", RouteMaster='" + RouteMaster + '\'' +
                ", RouteMarker='" + RouteMarker + '\'' +
                ", RouteProfile='" + RouteProfile + '\'' +
                ", Obervation='" + Obervation + '\'' +
                ", DefectiveMarker='" + DefectiveMarker + '\'' +
                ", LineWalker='" + LineWalker + '\'' +
                ", SOS='" + SOS + '\'' +
                ", MarkerAudit='" + MarkerAudit + '\'' +
                ", RouteId='" + RouteId + '\'' +
                ", access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                '}';
    }



}
