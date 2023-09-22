package org.braid.society.secret.imascordhubbackend.internal.database;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.braid.society.secret.imascordhubbackend.api.entity.FansiteEntity;

@Slf4j
public class FansiteDatabaseOperation extends AbstractCsvDatabaseOperation<FansiteEntity> {

  private static final String[] HEADERS = {"is", "name", "description", "waifu", "link"};

  public FansiteDatabaseOperation(Reader reader) {
    super(reader, HEADERS);
  }

  @Override
  @Nonnull
  public List<FansiteEntity> getAll() {
    try (CSVParser p = super.createParser()) {
      List<FansiteEntity> res = p.stream().map(this::parseRecord).toList();
      log.debug("Loaded {} fansites from local csv database", res.size());
      return res;
    } catch (IOException e) {
      log.error("Failed to load fansites from local csv database due to IO error", e);
    }
    return Collections.emptyList();
  }

  @Override
  protected FansiteEntity parseRecord(@Nonnull CSVRecord r) {
    return FansiteEntity.of(
      r.get("id"),
      r.get("name"),
      r.get("description"),
      r.get("waifu"),
      r.get("link")
    );
  }
}
