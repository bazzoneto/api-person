package com.example.impl;

import com.example.model.Pessoa;
import com.example.repository.PessoaRepository;
import com.example.service.PessoaService;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessoaServiceImpl implements PessoaService {

	private final PessoaRepository pessoaRepository;

	@Autowired
	public PessoaServiceImpl(PessoaRepository pessoaRepository) {
		this.pessoaRepository = pessoaRepository;
	}

	@Override
	public Optional<Pessoa> getPessoa(Integer id) {
		return pessoaRepository.findById(id);
	}

	public Pessoa savePessoa(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}

	@Override
	public List<Pessoa> getAll() {
		return pessoaRepository.findAll();
	}

	@Override
	public Optional<Pessoa> getById(Integer id) {
		return pessoaRepository.findById(id);
	}

	@Override
	public List<Pessoa> findByNomeIgnoreCaseContainingOrderByNomeAsc(String nome) {
		return pessoaRepository.findByNomeIgnoreCaseContainingOrderByNomeAsc(nome);
	}

	@Override
	public Pessoa create(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}

	@Override
	@Transactional
	public Pessoa update(Integer id, Pessoa pessoa) {
		// Encontre a pessoa existente pelo id
		Pessoa existingPessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new PessoaNotFoundException("Pessoa not found with id: " + id));

		// Atualiza os campos (evita atualizar o id)
		updatePessoaFields(existingPessoa, pessoa);

		// Salva a pessoa atualizada
		return pessoaRepository.save(existingPessoa);
	}

	// Exceção customizada
	public class PessoaNotFoundException extends RuntimeException {
		public PessoaNotFoundException(String message) {
			super(message);
		}
	}

	// Método para atualizar os campos de uma pessoa existente
	private void updatePessoaFields(Pessoa existingPessoa, Pessoa pessoa) {
		try {
			// Itera sobre os métodos set da classe Pessoa
			for (Method method : Pessoa.class.getDeclaredMethods()) {
				if (method.getName().startsWith("set")) {
					// Acesse o método getter correspondente
					Method getMethod = Pessoa.class.getMethod("get" + method.getName().substring(3));
					if (getMethod != null) {
						// Verifica se o campo não é o id para evitar alteração do id
						if (!method.getName().equals("setId")) {
							// Atualiza os campos não-íd
							method.invoke(existingPessoa, getMethod.invoke(pessoa));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Integer id) {
		pessoaRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		pessoaRepository.deleteAll();
	}
}
