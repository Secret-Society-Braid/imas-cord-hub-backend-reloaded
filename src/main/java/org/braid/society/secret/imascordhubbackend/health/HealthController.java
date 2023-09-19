package org.braid.society.secret.imascordhubbackend.health;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor()
public class HealthController {

  private final HealthService service;

  @GetMapping()
  public Map<String, String> health() {
    return this.service.health();
  }
}
