package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<DeviceEntity, String> {
}