package org.braid.society.secret.imascordhubbackend.internal.database;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.braid.society.secret.imascordhubbackend.api.database.DatabaseOperation;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractCsvDatabaseOperation<T> implements DatabaseOperation<T> {

  protected final Reader reader;
  @Setter
  protected String[] headers;

  public AbstractCsvDatabaseOperation(Reader reader, String... headers) {
    this.headers = headers;
    this.reader = reader;
  }

  @Nonnull
  protected CSVParser createParser() throws IOException {
    return CSVFormat
      .Builder
      .create(CSVFormat.DEFAULT)
      .setHeader(this.headers)
      .setSkipHeaderRecord(true)
      .setIgnoreEmptyLines(true)
      .setNullString("")
      .setQuote('\"')
      .setRecordSeparator('\n')
      .build()
      .parse(this.reader);
  }

  @Override
  @Nonnull
  public abstract List<T> getAll();

  @Override
  @Nonnull
  public T get(String id) {
    try (CSVParser p = this.createParser()) {
      return p.stream().filter(r -> r.get("id").equals(id)).findFirst().map(this::parseRecord)
        .orElseThrow();
    } catch (IOException e) {
      log.error("Failed to load {} with id {} from local csv database due to IO error.",
        this.getClass().getSimpleName(), id, e);
      throw new RuntimeException(e);
    }
  }

  @Override
  @Nonnull
  public <V> List<T> filter(@Nonnull String fieldName, @Nonnull V value) {
    if (value instanceof String term) {
      try (CSVParser p = this.createParser()) {
        List<T> res = p.stream().filter(r -> r.get(fieldName).contains(term)).map(this::parseRecord)
          .toList();
        log.debug("Found {} {}s with {} = {} from local csv database.", res.size(),
          this.getClass().getSimpleName(), fieldName, value);
        return res;
      } catch (IOException e) {
        log.error("Failed to filter {} with {} = {} from local csv database due to IO error.",
          this.getClass().getSimpleName(), fieldName, value, e);
        return Collections.emptyList();
      }
    }
    throw new IllegalArgumentException(
      "As this database is based on CSV, only String value type is supported.");
  }

  @Override
  public void insert(T t) {
    throw new UnsupportedOperationException("There is no plan to support this operation.");
  }

  @Override
  public void update(T t) {
    throw new UnsupportedOperationException("There is no plan to support this operation.");
  }

  @Override
  public void delete(String id) {
    throw new UnsupportedOperationException("There is no plan to support this operation.");
  }

  protected abstract T parseRecord(@Nonnull CSVRecord record);
}
