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
		Pessoa existingPessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new PessoaNotFoundException("Pessoa not found with id: " + id));

		updatePessoaFields(existingPessoa, pessoa);

		return pessoaRepository.save(existingPessoa);
	}

	public class PessoaNotFoundException extends RuntimeException {
		public PessoaNotFoundException(String message) {
			super(message);
		}
	}

	private void updatePessoaFields(Pessoa existingPessoa, Pessoa pessoa) {
		try {
			for (Method method : Pessoa.class.getDeclaredMethods()) {
				if (method.getName().startsWith("set")) {
					Method getMethod = Pessoa.class.getMethod("get" + method.getName().substring(3));
					if (getMethod != null) {
						method.invoke(existingPessoa, getMethod.invoke(pessoa));
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
