package org.braid.society.secret.imascordhubbackend.api.entity;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.braid.society.secret.imascordhubbackend.api.IP;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
@Builder(buildMethodName = "create")
public class ServerEntity {
  private String id;
  private String name;
  private IP ip;
  private List<String> waifu;
  private String description;
  private String invite;
}
