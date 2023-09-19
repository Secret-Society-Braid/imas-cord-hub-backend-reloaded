package org.braid.society.secret.imascordhubbackend.database;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import org.braid.society.secret.imascordhubbackend.api.database.DatabaseOperation;
import org.braid.society.secret.imascordhubbackend.api.entity.ServerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseOperationTest {

  @Test
  void getAllTest() {
    DatabaseOperation<ServerEntity> op = DatabaseOperation.of(ServerEntity.class);
    List<ServerEntity> servers = op.getAll();

    assertThat(servers.size()).isAtLeast(1);
  }
}
