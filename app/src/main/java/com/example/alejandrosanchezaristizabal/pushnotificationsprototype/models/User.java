package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.models;

/**
 * Created by alejandrosanchezaristizabal on 05/04/16.
 */
public class User {

  private String name;
  private String registrationId;

  public User(String name, String registrationId) {
    this.name = name;
    this.registrationId = registrationId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRegistrationId() {
    return registrationId;
  }

  public void setRegistrationId(String registrationId) {
    this.registrationId = registrationId;
  }
}