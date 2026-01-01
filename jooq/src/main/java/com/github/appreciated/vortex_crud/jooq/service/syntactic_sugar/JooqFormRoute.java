package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import lombok.experimental.SuperBuilder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

@SuperBuilder
public class JooqFormRoute extends FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
}