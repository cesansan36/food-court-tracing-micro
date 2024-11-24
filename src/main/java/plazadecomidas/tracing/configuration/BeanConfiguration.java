package plazadecomidas.tracing.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plazadecomidas.tracing.adapters.driven.mongo.adapter.RecordAdapter;
import plazadecomidas.tracing.adapters.driven.mongo.mapper.IRecordEntityMapper;
import plazadecomidas.tracing.adapters.driven.mongo.repository.IRecordRepository;
import plazadecomidas.tracing.domain.primaryport.IRecordPrimaryPort;
import plazadecomidas.tracing.domain.primaryport.usecase.RecordUseCase;
import plazadecomidas.tracing.domain.secondaryport.IRecordPersistencePort;

@Configuration
public class BeanConfiguration {

    @Bean
    public IRecordPrimaryPort recordPrimaryPort(IRecordPersistencePort recordSecondaryPort) {
        return new RecordUseCase(recordSecondaryPort);
    }

    @Bean
    public IRecordPersistencePort recordSecondaryPort(IRecordRepository recordRepository, IRecordEntityMapper recordEntityMapper) {
        return new RecordAdapter(recordRepository, recordEntityMapper);
    }
}
