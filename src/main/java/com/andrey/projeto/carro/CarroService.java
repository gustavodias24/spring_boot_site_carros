package com.andrey.projeto.carro;

import org.springframework.stereotype.Service;

@Service
public class CarroService {

    private final CarroRepository carroRepository;

    // Injeta o repositório de Carro
    public CarroService(CarroRepository carroRepository) {
        this.carroRepository = carroRepository;
    }

    // Método para salvar o carro no banco de dados
    public void salvarCarro(Carro carro) {
        carroRepository.save(carro);
    }
}
