package org.braid.society.secret.imascordhubbackend.api.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
@Builder(buildMethodName = "create")
public class FansiteEntity {
  private String id;
  private String name;
  private String waifu;
  private String description;
  private String link;
}
