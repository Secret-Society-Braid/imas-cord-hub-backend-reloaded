package org.braid.society.secret.imascordhubbackend.internal.database;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.braid.society.secret.imascordhubbackend.api.database.DatabaseOperation;
import org.braid.society.secret.imascordhubbackend.internal.database.SubscriberHelpers.ObservableSubscriber;
import org.braid.society.secret.imascordhubbackend.internal.database.SubscriberHelpers.OperationSubscriber;
import org.braid.society.secret.imascordhubbackend.internal.database.SubscriberHelpers.PrintToStringSubscriber;

@Slf4j
@RequiredArgsConstructor
public class DatabaseOperationImpl<T> implements DatabaseOperation<T> {

  private static MongoClient client;
  private final Class<T> clazz;

  protected static Optional<MongoClient> getClient() {
    if(client == null) {
      Properties props = new Properties();
      final boolean isLocal = "local".equals(System.getenv("RUNENV"));
      try {
        props.load(Paths.get("/etc/secret/database.properties").toUri().toURL().openStream());
      } catch (IOException e) {
        log.error("Failed to load secret file: {}", "/etc/secret/database.properties", e);
        return Optional.empty();
      }
      final String connectionString = props.getProperty("MONGO_DB_CONNECT_STRING");
      client = MongoClients.create(connectionString);
    }
    return Optional.of(client);
  }

  protected static Optional<MongoDatabase> getDatabase() {
    return getClient().map(client -> client.getDatabase("imascordhubServer")).or(Optional::empty);
  }

  protected Optional<MongoCollection<T>> getCollection() {
    return getDatabase().map(database -> database.getCollection(getCollectionName(), this.clazz)).or(Optional::empty);
  }

  private String getCollectionName() {
    return switch (this.clazz.getSimpleName()) {
      case "FansiteEntity" -> "fansiteData";
      case "ServerEntity" -> "serverData";
      case "donatorEntity" -> "donatorServer";
      default -> throw new IllegalArgumentException("Unknown class: " + this.clazz.getSimpleName());
    };
  }

  @Nonnull
  @Override
  public List<T> getAll() {
    ObservableSubscriber<T> s = new PrintToStringSubscriber<>();
    getCollection()
      .map(MongoCollection::find)
      .ifPresentOrElse(
        p -> p.subscribe(s),
        () -> {
          log.error("An Empty Optional chain was found. The one or more of the following is null: MongoClient, MongoDatabase, MongoCollection");
          log.error("If this error persists, please contact the developer at ImasCordHub project GitHub issues.");
          throw new NoSuchElementException("An empty optional chain was found.");
        });
    return s.get();
  }

  @Nonnull
  @Override
  public <V> List<T> filter(String fieldName, V value) {
    ObservableSubscriber<T> s = new PrintToStringSubscriber<>();
    getCollection()
      .map(c -> c.find(eq(fieldName, value)))
      .ifPresentOrElse(
        p -> p.subscribe(s),
        () -> {
          log.error("An Empty Optional chain was found. The one or more of the following is null: MongoClient, MongoDatabase, MongoCollection");
          log.error("If this error persists, please contact the developer at ImasCordHub project GitHub issues.");
          throw new NoSuchElementException("An empty optional chain was found.");
        });
    return s.get();
  }

  @Nullable
  @Override
  public T get(String id) {
    ObservableSubscriber<T> s = new PrintToStringSubscriber<>();
    getCollection()
      .map(c -> c.find(eq("id", id)))
      .ifPresentOrElse(
        p -> p.subscribe(s),
        () -> {
          log.error("An Empty Optional chain was found. The one or more of the following is null: MongoClient, MongoDatabase, MongoCollection");
          log.error("If this error persists, please contact the developer at ImasCordHub project GitHub issues.");
          throw new NoSuchElementException("An empty optional chain was found.");
        });
    return s.first();
  }

  @Override
  public void insert(T t) {
    ObservableSubscriber<InsertOneResult> s = new OperationSubscriber<>();
    getCollection()
      .map(c -> c.insertOne(t))
      .ifPresent(p -> p.subscribe(s));
    if(s.await().first().getInsertedId() == null) {
      log.error("Failed to insert data: {}", t);
      throw new RuntimeException(
        "Failed to insert data.",
        new NoSuchElementException("Data was acknowledged but id confirmation was failed."));
    }
  }

  @Override
  public void update(T t) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void delete(String id) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
