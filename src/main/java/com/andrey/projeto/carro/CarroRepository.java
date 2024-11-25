package com.andrey.projeto.carro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {
    List<Carro> findByCategoriaIgnoreCase(String categoria);
    List<Carro> findByNomeContainingIgnoreCase(String nome);
}
