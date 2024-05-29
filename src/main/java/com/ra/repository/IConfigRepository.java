package com.ra.repository;

import com.ra.model.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IConfigRepository extends JpaRepository<Config,Long> {

}
