package org.braid.society.secret.imascordhubbackend.version;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class VersionService {

  private static final VersionHelper helper = new VersionHelper();

  public String getFullVersion() {
    return helper.getFullVersion();
  }

  public String getMajorVersion() {
    return String.valueOf(helper.getMajor());
  }

  public String getMinorVersion() {
    return String.valueOf(helper.getMinor());
  }

  public String getPatchVersion() {
    return String.valueOf(helper.getPatch());
  }
}
