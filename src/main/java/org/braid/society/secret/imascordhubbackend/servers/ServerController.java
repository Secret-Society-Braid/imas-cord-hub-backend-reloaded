package org.braid.society.secret.imascordhubbackend.servers;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.braid.society.secret.imascordhubbackend.api.entity.ServerEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/find/{id}")
  public ServerEntity getServerById(@PathVariable(name = "id") String id) {
    return this.service.getServerById(id);
  }

  @GetMapping("/search/{term}")
  public List<ServerEntity> searchServers(
    @RequestParam(value = "type", required = false) Optional<String> type,
    @PathVariable(name = "term") String term) {
    return this.service.searchServers(type.orElse("name"), term);
  }

  @GetMapping("/random")
  public List<ServerEntity> getRandomServers(
    @RequestParam(value = "count", required = false) Optional<Integer> count) {
    return this.service.getRandomServers(count.orElse(2));
  }
}
