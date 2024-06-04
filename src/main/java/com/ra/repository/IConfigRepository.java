package com.ra.repository;

import com.ra.model.entity.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface IConfigRepository extends JpaRepository<Config,Long> {
    Page<Config> findAllByConfigNameContainingIgnoreCase(Pageable pageable, String name);
    boolean existsByConfigName(String configName);
    boolean findByConfigName(String name);
    @Modifying
    @Query("update Config c set c.status = case when c.status = true then false else true end where c.id =?1 ")
    void changStatus(Long id);

    Config findConfigByConfigName(String name);
    List<Config> findAllByStatus(boolean status);
}
