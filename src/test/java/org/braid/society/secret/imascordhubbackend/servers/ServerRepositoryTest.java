package org.braid.society.secret.imascordhubbackend.servers;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import org.braid.society.secret.imascordhubbackend.api.IP;
import org.braid.society.secret.imascordhubbackend.api.entity.ServerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerRepositoryTest {

  @Test
  void getAllShouldReturnAllServers() {
    // setup
    final ServerRepository r = new ServerRepository();
    final List<ServerEntity> list = r.getAllServer();

    // assertion
    assertThat(list).isNotNull();
    assertThat(list).isNotEmpty();
    assertThat(list).hasSize(5);

    System.out.println(list);
  }

  //TODO: add rest of tests
  @Test
  void searchServersShouldReturnCorrectly() {
    // setup
    final ServerRepository r = new ServerRepository();
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
    final ServerRepository r = new ServerRepository();
    final ServerEntity obj = r.getServerById("9bffce54-d95e-4f51-b8d1-8b68aaea605a");

    // assertion

    assertThat(obj).isNotNull();
    assertThat(obj.getName()).isEqualTo("デレマス13歳組の間");
    assertThat(obj.getIp()).isEqualTo(IP.CINDERELLA);
    assertThat(obj.getId()).isEqualTo("9bffce54-d95e-4f51-b8d1-8b68aaea605a");
  }

  @Test
  void randomShouldReturnSpecifiedNumberOfEntity() {
    // setup
    final ServerRepository r = new ServerRepository();
    final List<ServerEntity> lessList = r.getRandomServers(2);
    final List<ServerEntity> moreList = r.getRandomServers(10);

    // assertion
    assertThat(lessList).hasSize(2);
    assertThat(moreList).hasSize(10);
  }
}
