package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<DeviceEntity, String> {

    List<DeviceEntity> findAllByOrderByUpdatedTimestampDesc();

    List<DeviceEntity> findAllByDeviceInfoContainingIgnoreCaseOrderByUpdatedTimestampDesc(String query);

}