package org.braid.society.secret.imascordhubbackend.home;

import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class HomeService {

  public Map<String, String> home() {
    return Map.of("message", "Hello, world!");
  }
}
