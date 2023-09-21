package org.braid.society.secret.imascordhubbackend.api;

import lombok.ToString;

@ToString
public enum IP {
  ALLSTARS("765as"),
  CINDERELLA,
  MILLION,
  SHINY,
  SIDEM,
  OTHERS,
  JOINT;

  private final String ip;

  IP(String ip) {
    this.ip = ip;
  }

  IP() {
    this.ip = this.name().toLowerCase();
  }

  public static IP fromString(String s) {
    for (IP ip : IP.values()) {
      if (ip.getIP().equalsIgnoreCase(s)) {
        return ip;
      }
    }
    return null;
  }

  public String getIP() {
    return this.ip;
  }

}
