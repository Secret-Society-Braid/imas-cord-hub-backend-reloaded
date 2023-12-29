package org.braid.society.secret.imascordhubbackend.version;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VersionHelper {

  private final int major = 0;
  private final int minor = 0;
  private final int patch = 0;

  public String getFullVersion() {
    return String.format("%d.%d.%d", major, minor, patch);
  }
}
