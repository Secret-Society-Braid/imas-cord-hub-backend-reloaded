package org.braid.society.secret.imascordhubbackend.servers;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerRepositoryTest {

  BufferedReader br;
  DatabaseOperation<ServerEntity> op;
  ServerRepository r;

  @BeforeEach
  void setUp() {
    br = new BufferedReader(
      new InputStreamReader(
        Objects.requireNonNull(getClass().getResourceAsStream("/server.model.csv")),
        StandardCharsets.UTF_8));
    op = new ServerDatabaseOperation(br);
    r = new ServerRepository(op);
  }

  @Test
  void getAllShouldReturnAllServers() {
    // setup
    final List<ServerEntity> list = r.getAllServer();

    // assertion
    assertThat(list).isNotNull();
    assertThat(list).isNotEmpty();
    assertThat(list).hasSize(4);

    System.out.println(list);
  }

  @Test
  void searchServersShouldReturnCorrectly() {
    // setup
    final List<ServerEntity> list = r.searchServers("name", "デレマス");

    // assertion
    assertThat(list).hasSize(1); // should only one
    final ServerEntity obj = list.get(0);

    assertThat(obj.getName()).isEqualTo("デレマス13歳組の間");
    assertThat(obj.getIp()).isEqualTo(IP.CINDERELLA);
  }

  @Test
  void findShouldOnlyReturnOneEntity() {
    // setup
    final ServerEntity obj = r.getServerById("9bffce54-d95e-4f51-b8d1-8b68aaea605a");

    // assertion

    assertThat(obj).isNotNull();
    assertThat(obj.getName()).isEqualTo("デレマス13歳組の間");
    assertThat(obj.getIp()).isEqualTo(IP.CINDERELLA);
    assertThat(obj.getId()).isEqualTo("9bffce54-d95e-4f51-b8d1-8b68aaea605a");
  }

  @Test
  void lessRandomShouldReturnSpecifiedNumberOfEntity() {
    // setup
    final List<ServerEntity> lessList = r.getRandomServers(2);

    // assertion
    assertThat(lessList).hasSize(2);
  }

  @Test
  void moreRandomShouldReturnSpecifiedNumberOfEntity() {
    // setup
    final ServerRepository r = new ServerRepository(op);
    final List<ServerEntity> lessList = r.getRandomServers(10);

    // assertion
    assertThat(lessList).hasSize(10);
  }
}
