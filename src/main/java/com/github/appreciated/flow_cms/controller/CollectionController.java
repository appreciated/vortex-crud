package com.github.appreciated.flow_cms.controller;

import com.github.appreciated.flow_cms.entity.Collection;
    import com.github.appreciated.flow_cms.service.CollectionService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/api/collections")
    public class CollectionController {
        @Autowired
        private CollectionService collectionService;

        @GetMapping
        public List<Collection> getAllCollections() {
            return collectionService.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Collection> getCollectionById(@PathVariable Long
  id) {
            Optional<Collection> collection = collectionService.findById(id);
            return collection.map(ResponseEntity::ok).orElseGet(() ->
  ResponseEntity.notFound().build());
        }

        @PostMapping
        public Collection createCollection(@RequestBody Collection collection)
  {
            return collectionService.save(collection);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Collection> updateCollection(@PathVariable Long
  id, @RequestBody Collection collectionDetails) {
            Optional<Collection> collection = collectionService.findById(id);
            if (collection.isPresent()) {
                collectionDetails.setId(id);
                return
  ResponseEntity.ok(collectionService.save(collectionDetails));
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
            collectionService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
