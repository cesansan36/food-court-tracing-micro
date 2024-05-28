package plazadecomidas.tracing.adapters.driven.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import plazadecomidas.tracing.adapters.driven.mongo.entity.RecordEntity;

import java.util.List;

public interface IRecordRepository extends MongoRepository<RecordEntity, Long> {

    @Query("{ 'idClient' : ?0 }.")
    List<RecordEntity> findByClientIdOrderByCreatedAtDesc(Long clientId);
}
