package org.braid.society.secret.imascordhubbackend.health;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class HealthServiceTest {

  @Test
  void healthTest() {
    if (!Objects.equals(System.getenv("RUNENV"), "local")) {
      return;
    }
    HealthService healthService = new HealthService();
    Map<String, String> health = healthService.health();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      fail(e);
    }
    Map<String, String> anotherMap = healthService.health();

    assertThat(health.get("up_since")).isEqualTo(anotherMap.get("up_since"));
  }
}
