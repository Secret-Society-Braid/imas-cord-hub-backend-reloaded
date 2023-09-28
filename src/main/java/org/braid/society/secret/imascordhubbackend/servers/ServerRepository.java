package org.braid.society.secret.imascordhubbackend.servers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.braid.society.secret.imascordhubbackend.api.database.DatabaseOperation;
import org.braid.society.secret.imascordhubbackend.api.entity.ServerEntity;
import org.braid.society.secret.imascordhubbackend.internal.database.ServerDatabaseOperation;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ServerRepository {

  private final String dbPath;

  public ServerRepository() {
    this("/static/server.model.csv");
  }

  private DatabaseOperation<ServerEntity> createOperator() {
    BufferedReader br = new BufferedReader(new InputStreamReader(
      Objects.requireNonNull(ServerRepository.class.getResourceAsStream(this.dbPath)),
      StandardCharsets.UTF_8));
    return new ServerDatabaseOperation(br);
  }

  public List<ServerEntity> getAllServer() {
    final DatabaseOperation<ServerEntity> op = createOperator();
    return op.getAll();
  }

  public ServerEntity getServerById(String id) {
    final DatabaseOperation<ServerEntity> op = createOperator();
    return op.get(id);
  }

  public List<ServerEntity> searchServers(String fieldName, String term) {
    final DatabaseOperation<ServerEntity> op = createOperator();
    return op.filter(fieldName, term);
  }

  public List<ServerEntity> getRandomServers(int count) {
    if (count <= 0) {
      throw new IllegalArgumentException("count must be positive");
    }
    final DatabaseOperation<ServerEntity> op = createOperator();
    List<ServerEntity> servers = op.getAll();
    if (count > servers.size()) {
      int size = servers.size();
      for (int i = 0; i < count - size; i++) {
        servers.add(servers.get(i));
      }
    }
    Collections.shuffle(servers);
    return servers.subList(0, count);
  }
}
