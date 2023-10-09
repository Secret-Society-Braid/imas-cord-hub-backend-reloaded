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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            String.format("Cannot find %s with id %s from local csv database.",
                this.getClass().getSimpleName(), id)
        ));
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
    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        String.format("Filtering %s with %s = %s is not supported.",
            this.getClass().getSimpleName(), fieldName, value)
    );
  }

  @Override
  public void insert(T t) {
    throw new ResponseStatusException(
        HttpStatus.METHOD_NOT_ALLOWED,
        "Insert operation is not supported."
    );
  }

  @Override
  public void update(T t) {
    throw new ResponseStatusException(
        HttpStatus.METHOD_NOT_ALLOWED,
        "Update operation is not supported."
    );
  }

  @Override
  public void delete(String id) {
    throw new ResponseStatusException(
        HttpStatus.METHOD_NOT_ALLOWED,
        "Delete operation is not supported."
    );
  }

  protected abstract T parseRecord(@Nonnull CSVRecord record);
}
