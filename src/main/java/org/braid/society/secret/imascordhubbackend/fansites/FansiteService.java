package org.braid.society.secret.imascordhubbackend.fansites;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.braid.society.secret.imascordhubbackend.api.entity.FansiteEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FansiteService {

  private final FansiteRepository repo;

  public List<FansiteEntity> getAll() {
    return this.repo.getAllFansite();
  }

  public FansiteEntity getFansiteById(String id) {
    return this.repo.getFansiteById(id);
  }

  public List<FansiteEntity> searchFansites(String fieldName, String term) {
    return this.repo.searchFansites(fieldName, term);
  }

  public List<FansiteEntity> getRandomFansites(int count) {
    return this.repo.getRandomFansites(count);
  }
}
