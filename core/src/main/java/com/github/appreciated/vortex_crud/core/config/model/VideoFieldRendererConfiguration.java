package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class VideoFieldRendererConfiguration<ModelClass, FieldType, RepositoryType>
        extends RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {

    public VideoFieldRendererConfiguration(Class<? extends VortexCrudResourceProvider> fileFactory) {
        super();
        this.setFactory(null);
        this.setImageFactory(fileFactory);
    }
}