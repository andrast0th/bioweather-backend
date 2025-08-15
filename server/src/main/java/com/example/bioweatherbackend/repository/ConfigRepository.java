package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<ConfigEntity, String> {

}
