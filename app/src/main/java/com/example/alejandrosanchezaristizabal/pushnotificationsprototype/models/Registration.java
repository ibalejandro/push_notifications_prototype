package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.models;

/**
 * Created by alejandrosanchezaristizabal on 15/04/16.
 */
public class Registration {

  private String vwId;
  private String addressee;
  private String channel;

  public Registration(String vwId, String addressee, String channel) {
    this.vwId = vwId;
    this.addressee = addressee;
    this.channel = channel;
  }

  public String getVwId() {
    return vwId;
  }

  public void setVwId(String vwId) {
    this.vwId = vwId;
  }

  public String getAddressee() {
    return addressee;
  }

  public void setAddressee(String addressee) {
    this.addressee = addressee;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }
}
