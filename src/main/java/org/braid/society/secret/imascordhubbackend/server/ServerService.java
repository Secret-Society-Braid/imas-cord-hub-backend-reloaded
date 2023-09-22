package org.braid.society.secret.imascordhubbackend.server;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.braid.society.secret.imascordhubbackend.api.entity.ServerEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerService {

  private final ServerRepository repo;

  public List<ServerEntity> getAll() {
    return this.repo.getAllServer();
  }

  public ServerEntity getServerById(String id) {
    return this.repo.getServerById(id);
  }
}
