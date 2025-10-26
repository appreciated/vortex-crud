package com.github.appreciated.vortex_crud.example.jpa.repository;

import com.github.appreciated.vortex_crud.example.jpa.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

}