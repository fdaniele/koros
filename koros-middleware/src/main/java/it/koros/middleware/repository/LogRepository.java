package it.koros.middleware.repository;

import it.koros.middleware.model.LogMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<LogMessage, String> {

}