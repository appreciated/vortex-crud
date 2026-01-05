package com.github.appreciated.vortex_crud.jpa.service.reflection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
class TestEntity {
    private String id;
    private String name;
    private List<String> items;
}