package org.braid.society.secret.imascordhubbackend.api;

import lombok.ToString;

@ToString
public enum IP {
  ALLSTARS("765as"),
  CINDRELLA,
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

  public String getIP() {
    return this.ip;
  }

}
