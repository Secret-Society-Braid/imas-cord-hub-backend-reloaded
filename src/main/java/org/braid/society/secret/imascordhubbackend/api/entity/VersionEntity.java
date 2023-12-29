package org.braid.society.secret.imascordhubbackend.api.entity;

public record VersionEntity(String full,
                            org.braid.society.secret.imascordhubbackend.api.entity.VersionEntity.VersionNumberEntity numbers,
                            String changes) {

  public record VersionNumberEntity(String major, String minor, String patch) {

  }
}
