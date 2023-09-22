package org.braid.society.secret.imascordhubbackend.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
}
