package org.braid.society.secret.imascordhubbackend.database;

import static com.google.common.truth.Truth.assertThat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import org.braid.society.secret.imascordhubbackend.api.IP;
import org.braid.society.secret.imascordhubbackend.api.database.DatabaseOperation;
import org.braid.society.secret.imascordhubbackend.api.entity.ServerEntity;
import org.braid.society.secret.imascordhubbackend.internal.database.ServerDatabaseOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerEntityDatabaseTest {

  @Test
  void getAllTest() {
    BufferedReader br = new BufferedReader(
      new InputStreamReader(
        Objects.requireNonNull(getClass().getResourceAsStream("/server.model.test.csv")),
        StandardCharsets.UTF_8));
    DatabaseOperation<ServerEntity> op = new ServerDatabaseOperation(br);
    List<ServerEntity> l = op.getAll();

    assertThat(l.size()).isAtLeast(1);

    l.stream().filter(e -> e.getId().equals("9bffce54-d95e-4f51-b8d1-8b68aaea605a")).findFirst()
      .ifPresentOrElse(e -> {
        assertThat(e.getName()).isEqualTo("デレマス13歳組の間");
        assertThat(e.getIp()).isEqualTo(IP.CINDERELLA);
      }, Assertions::fail);
  }
}
