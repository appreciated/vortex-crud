package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class JooqApplication extends Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public static JooqApplication.JooqApplicationBuilder<?, ?> of() {
        return JooqApplication.builder();
    }
}
