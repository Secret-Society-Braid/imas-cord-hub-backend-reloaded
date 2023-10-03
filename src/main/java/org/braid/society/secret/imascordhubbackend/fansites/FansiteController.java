package org.braid.society.secret.imascordhubbackend.fansites;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.braid.society.secret.imascordhubbackend.api.entity.FansiteEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fansites")
public class FansiteController {

  private final FansiteService service;

  @GetMapping()
  public List<FansiteEntity> getAllFansites() {
    return this.service.getAll();
  }

  @GetMapping("/find/{id}")
  public FansiteEntity getFansiteById(@PathVariable(name = "id") String id) {
    return this.service.getFansiteById(id);
  }

  @GetMapping("/search/{term}")
  public List<FansiteEntity> searchFansites(
    @RequestParam(value = "type", required = false) Optional<String> type,
    @PathVariable(name = "term") String term) {
    return this.service.searchFansites(type.orElse("name"), term);
  }

  @GetMapping("/random")
  public List<FansiteEntity> getRandomFansites(
    @RequestParam(value = "count", required = false) Optional<Integer> count) {
    return this.service.getRandomFansites(count.orElse(2));
  }
}
