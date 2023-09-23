package org.braid.society.secret.imascordhubbackend.servers;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
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
}
