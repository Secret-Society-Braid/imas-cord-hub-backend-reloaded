package org.braid.society.secret.imascordhubbackend.health;

import java.lang.management.ManagementFactory;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class HealthService {

  public Map<String, String> health() {
    // get uptime
    long uptime = System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime();
    return Map.of("status", "UP", "uptime", String.valueOf(uptime));
  }
}
