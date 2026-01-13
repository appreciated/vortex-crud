package com.github.appreciated.vortex_crud.demo.projectmanagement.service;

import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.CustomFieldDefinitionRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.CUSTOM_FIELD_DEFINITION;

@Service
public class CustomFieldService {
    private final DSLContext dsl;

    public CustomFieldService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<CustomFieldDefinitionRecord> getDefinitions(String entityType) {
        return dsl.selectFrom(CUSTOM_FIELD_DEFINITION)
                .where(CUSTOM_FIELD_DEFINITION.ENTITY_TYPE.eq(entityType))
                .orderBy(CUSTOM_FIELD_DEFINITION.FIELD_ORDER)
                .fetch();
    }
}
