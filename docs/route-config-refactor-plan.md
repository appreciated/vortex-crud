# Route Configuration Refactor Plan

1. Replace monolithic `RouteRendererConfiguration` inheritance with a minimal `RouteConfig` interface.
2. Provide dedicated configuration models per renderer (Kanban, Grid, MultiForm, etc.).
3. Decouple field configuration from renderer config through a separate `FieldConfig` abstraction.
4. Update `RouteRenderer` to accept the new `RouteConfig` interface.
5. Introduce tailored builders for each configuration type.
6. Add migration utilities and tests ensuring type safety.
