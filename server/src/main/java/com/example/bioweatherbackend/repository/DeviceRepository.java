package com.example.bioweatherbackend.repository;

import com.example.bioweatherbackend.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<DeviceEntity, String> {

    List<DeviceEntity> findAllByOrderByUpdatedTimestampDesc();

    List<DeviceEntity> findAllByDeviceInfoContainingIgnoreCaseOrderByUpdatedTimestampDesc(String query);

    @Query("SELECT d FROM DeviceEntity d WHERE d.isDisabled = false ORDER BY d.updatedTimestamp DESC")
    List<DeviceEntity> findAllNotDisabled();

}