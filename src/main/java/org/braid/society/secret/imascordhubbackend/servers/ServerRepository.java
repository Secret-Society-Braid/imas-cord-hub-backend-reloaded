package org.braid.society.secret.imascordhubbackend.servers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

  private final DatabaseOperation<ServerEntity> op;

  public ServerRepository() {
    BufferedReader br = new BufferedReader(new InputStreamReader(
      Objects.requireNonNull(this.getClass().getResourceAsStream("/static/server.model.csv")),
      StandardCharsets.UTF_8));
    this.op = new ServerDatabaseOperation(br);
  }

  public List<ServerEntity> getAllServer() {
    return this.op.getAll();
  }

  public ServerEntity getServerById(String id) {
    return this.op.get(id);
  }

  public List<ServerEntity> searchServers(String fieldName, String term) {
    return this.op.filter(fieldName, term);
  }

  public List<ServerEntity> getRandomServers(int count) {
    if (count <= 0) {
      throw new IllegalArgumentException("count must be positive");
    }
    List<ServerEntity> servers = this.op.getAll();
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
