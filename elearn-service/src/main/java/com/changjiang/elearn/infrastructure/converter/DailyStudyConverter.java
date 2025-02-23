package com.changjiang.elearn.infrastructure.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.domain.model.DailyStudy;
import com.changjiang.elearn.infrastructure.persistence.po.DailyStudyPO;
import java.util.List;

@Mapper
public interface DailyStudyConverter {
    DailyStudyConverter INSTANCE = Mappers.getMapper(DailyStudyConverter.class);
    
    DailyStudy toDomain(DailyStudyPO po);
    
    DailyStudyPO toPO(DailyStudy domain);
    
    List<DailyStudy> toDomainList(List<DailyStudyPO> poList);
    
    List<DailyStudyPO> toPOList(List<DailyStudy> domainList);
} 