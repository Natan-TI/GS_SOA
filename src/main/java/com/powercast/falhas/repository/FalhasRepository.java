package com.powercast.falhas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.powercast.falhas.model.Falha;

@Repository
public interface FalhasRepository extends JpaRepository<Falha, Long> {
	List<Falha> findByBairro(String bairro);
}
