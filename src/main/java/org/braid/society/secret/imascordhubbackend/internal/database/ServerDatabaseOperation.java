package org.braid.society.secret.imascordhubbackend.internal.database;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.braid.society.secret.imascordhubbackend.api.IP;
import org.braid.society.secret.imascordhubbackend.api.entity.ServerEntity;

@Slf4j
public class ServerDatabaseOperation extends AbstractCsvDatabaseOperation<ServerEntity> {

  private static final String[] HEADERS = {"id", "name", "ip", "waifu", "description", "invite"};

  public ServerDatabaseOperation(Reader reader) {
    super(reader, HEADERS);
  }

  @Override
  @Nonnull
  public List<ServerEntity> getAll() {
    try (CSVParser p = super.createParser()) {
      List<ServerEntity> res = p.stream().map(this::parseRecord).toList();
      log.debug("Loaded {} servers from local csv database", res.size());
      return res;
    } catch (IOException e) {
      log.error("Failed to load servers from local csv database due to IO error", e);
    }
    return Collections.emptyList();
  }

  @Override
  @Nonnull
  public ServerEntity get(String id) {
    try (CSVParser p = super.createParser()) {
      return p.stream().filter(r -> r.get("id").equals(id)).findFirst().map(this::parseRecord)
        .orElseThrow();
    } catch (IOException e) {
      log.error("Failed to load server {} from local csv database due to IO error", id, e);
      throw new RuntimeException(e);
    }
  }

  @Override
  protected ServerEntity parseRecord(@Nonnull CSVRecord r) {
    return ServerEntity.of(
      r.get("id"),
      r.get("name"),
      IP.fromString(r.get("ip")),
      List.of(r.get("waifu").split(",")),
      r.get("description"),
      r.get("invite")
    );
  }
}
