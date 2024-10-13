package com.github.appreciated.turbo_crud.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrudControllerFactory {
/*
    @Autowired
    private ApplicationContext context;

    @RequestMapping(value = "/{collection}", method = RequestMethod.GET)
    public Object getAll(@PathVariable String collection) {
        return getController(collection).getAll();
    }

    @RequestMapping(value = "/{collection}/{id}", method = RequestMethod.GET)
    public Object getOne(@PathVariable String collection, @PathVariable Long id) {
        return getController(collection).getOne(id);
    }

    @RequestMapping(value = "/{collection}", method = RequestMethod.POST)
    public Object create(@PathVariable String collection, @RequestBody Map<String, Object> body) {
        return getController(collection).create(body);
    }

    @RequestMapping(value = "/{collection}/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable String collection, @PathVariable Long id, @RequestBody Map<String, Object> body) {
        return getController(collection).update(id, body);
    }

    @RequestMapping(value = "/{collection}/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String collection, @PathVariable Long id) {
        getController(collection).delete(id);
    }*/

}
