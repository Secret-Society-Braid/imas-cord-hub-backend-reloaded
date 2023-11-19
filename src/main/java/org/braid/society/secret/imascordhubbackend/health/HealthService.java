package org.braid.society.secret.imascordhubbackend.health;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@NoArgsConstructor
public class HealthService {

  private static final AtomicLong UP_SINCE = new AtomicLong(System.currentTimeMillis());

  public Map<String, String> health() {
    // Environment variable
    String stat = System.getenv("STAT");
    return switch (stat) {
      case "up" -> Map.of("status", "up", "up_since", String.valueOf(UP_SINCE.get()));
      case "maintenance" -> throw new ResponseStatusException(HttpStatusCode.valueOf(503),
        "The API Server is under maintenance.");
      default -> throw new ResponseStatusException(HttpStatusCode.valueOf(500),
        "There may be a problem with the API server");
    };
  }
}
