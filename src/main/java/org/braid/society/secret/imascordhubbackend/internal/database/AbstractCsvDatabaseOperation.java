package org.braid.society.secret.imascordhubbackend.internal.database;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
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
      .build()
      .parse(this.reader);
  }

  @Override
  @Nonnull
  public abstract List<T> getAll();

  @Override
  @Nonnull
  public abstract T get(String id);

  @Override
  @Nonnull
  public <V> List<T> filter(@Nonnull String fieldName, @Nonnull V value) {
    throw new UnsupportedOperationException("Not supported yet.");
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
