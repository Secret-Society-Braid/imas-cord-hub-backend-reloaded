package org.braid.society.secret.imascordhubbackend.version;

import lombok.RequiredArgsConstructor;
import org.braid.society.secret.imascordhubbackend.api.entity.VersionEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version")
@RequiredArgsConstructor
public class VersionController {

  private final VersionService service;

  @GetMapping()
  public VersionEntity version() {
    return new VersionEntity(
      this.service.getFullVersion(),
      new VersionEntity.VersionNumberEntity(
        this.service.getMajorVersion(),
        this.service.getMinorVersion(),
        this.service.getPatchVersion()
      ),
      "First release!"
    );
  }
}
