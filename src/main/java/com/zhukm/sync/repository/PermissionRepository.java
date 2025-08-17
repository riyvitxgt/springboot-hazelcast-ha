package com.zhukm.sync.repository;


import com.zhukm.sync.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByCode(String code);

    boolean existsByCode(String code);

    List<Permission> findByResource(String resource);

    List<Permission> findByResourceAndAction(String resource, String action);

    List<Permission> findByIdIn(Set<Long> ids);
}
