package com.github.appreciated.vortex_crud.example.jpa.repository;

import com.github.appreciated.vortex_crud.example.jpa.entity.User;
import com.github.appreciated.vortex_crud.jpa.repository.JpaUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaUserRepository, JpaRepository<User, Long> {
}
