package org.braid.society.secret.imascordhubbackend.server;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.braid.society.secret.imascordhubbackend.api.entity.ServerEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/servers")
public class ServerController {

  private final ServerService service;

  @GetMapping()
  public List<ServerEntity> getAllServer() {
    return this.service.getAll();
  }

  @GetMapping("/{id}")
  public ServerEntity getServerById(@PathVariable(name = "id") String id) {
    return this.service.getServerById(id);
  }
}
