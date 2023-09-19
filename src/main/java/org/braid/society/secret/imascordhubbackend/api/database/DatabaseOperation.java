package org.braid.society.secret.imascordhubbackend.api.database;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.braid.society.secret.imascordhubbackend.internal.database.DatabaseOperationImpl;

public interface DatabaseOperation<T> {

  @Nonnull
  List<T> getAll();

  @Nonnull
  <V> List<T> filter(String fieldName, V value);

  @Nullable
  T get(String id);

  void insert(T t);

  void update(T t);

  void delete(String id);

  static <T> DatabaseOperation<T> of(@Nonnull Class<T> c) {
    return new DatabaseOperationImpl<>(c);
  }
}
