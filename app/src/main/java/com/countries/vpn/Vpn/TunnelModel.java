package com.countries.vpn.Vpn;

public class TunnelModel {

    private int id;
    private String privatekey;
    private String address;
    private String dns;
    private String publickey;
    private String presharedkey;
    private String endpoints;
    private String allowed_ips;
    private String country;
    private String created_at;
    private String updated_at;
    private boolean is_enable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getPresharedkey() {
        return presharedkey;
    }

    public void setPresharedkey(String presharedkey) {
        this.presharedkey = presharedkey;
    }

    public String getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(String endpoints) {
        this.endpoints = endpoints;
    }

    public String getAllowed_ips() {
        return allowed_ips;
    }

    public void setAllowed_ips(String allowed_ips) {
        this.allowed_ips = allowed_ips;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isIs_enable() {
        return is_enable;
    }

    public void setIs_enable(boolean is_enable) {
        this.is_enable = is_enable;
    }
}
