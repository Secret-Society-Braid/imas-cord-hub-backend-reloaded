package org.braid.society.secret.imascordhubbackend.internal.database;

import jakarta.annotation.Nonnull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.braid.society.secret.imascordhubbackend.api.database.DatabaseOperation;

@Slf4j
@RequiredArgsConstructor
public class CsvDatabaseOperation<T> implements DatabaseOperation<T> {

  private final String filePath;
  private final Class<T> clazz;

  @Override
  @Nonnull
  public List<T> getAll() {
    log.info("Getting all {} from {}", clazz.getSimpleName(), filePath);
    return List.of();
  }
}
