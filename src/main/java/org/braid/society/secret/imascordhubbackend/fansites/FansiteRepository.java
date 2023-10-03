package org.braid.society.secret.imascordhubbackend.fansites;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.braid.society.secret.imascordhubbackend.api.database.DatabaseOperation;
import org.braid.society.secret.imascordhubbackend.api.entity.FansiteEntity;
import org.braid.society.secret.imascordhubbackend.internal.database.FansiteDatabaseOperation;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FansiteRepository {

  private final String dbPath;

  public FansiteRepository() {
    this("/static/fansite.model.csv");
  }

  private DatabaseOperation<FansiteEntity> createOperator() {
    BufferedReader br = new BufferedReader(new InputStreamReader(
      Objects.requireNonNull(FansiteRepository.class.getResourceAsStream(this.dbPath)),
      StandardCharsets.UTF_8));
    return new FansiteDatabaseOperation(br);
  }

  public List<FansiteEntity> getAllFansite() {
    final DatabaseOperation<FansiteEntity> op = createOperator();
    return op.getAll();
  }

  public FansiteEntity getFansiteById(String id) {
    final DatabaseOperation<FansiteEntity> op = createOperator();
    return op.get(id);
  }

  public List<FansiteEntity> searchFansites(String fieldName, String term) {
    final DatabaseOperation<FansiteEntity> op = createOperator();
    return op.filter(fieldName, term);
  }

  public List<FansiteEntity> getRandomFansites(int count) {
    if (count <= 0) {
      throw new IllegalArgumentException("count must be positive");
    }
    final DatabaseOperation<FansiteEntity> op = createOperator();
    List<FansiteEntity> fansites = op.getAll();
    if (count > fansites.size()) {
      int size = fansites.size();
      for (int i = 0; i < count - size; i++) {
        fansites.add(fansites.get(i));
      }
    }
    Collections.shuffle(fansites);
    return fansites.subList(0, count);
  }
}
