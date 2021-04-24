package br.com.devinhouse.thiago_mathias_simon.service;

import br.com.devinhouse.thiago_mathias_simon.dto.ProcessoCriadoDTO;
import br.com.devinhouse.thiago_mathias_simon.dto.ProcessoRemovidoDTO;
import br.com.devinhouse.thiago_mathias_simon.entity.ProcessoEntity;
import br.com.devinhouse.thiago_mathias_simon.exceptions.NullProcessException;
import br.com.devinhouse.thiago_mathias_simon.exceptions.ProcessAlreadyExistException;
import br.com.devinhouse.thiago_mathias_simon.exceptions.ProcessNotFoundException;
import br.com.devinhouse.thiago_mathias_simon.repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessoService {

	@Autowired
	private ProcessoRepository repository;

	public Iterable<ProcessoEntity> listarProcessos() {
		return recuperarProcessos();
	}

	public ProcessoCriadoDTO cadastrarProcesso(ProcessoEntity novoProcesso) throws ProcessAlreadyExistException {

		Iterable<ProcessoEntity> todosOsProcessos = recuperarProcessos();

		long id = 1;
		for (ProcessoEntity processo : todosOsProcessos) {
			if (processo.getId() == novoProcesso.getId()) {
				throw new ProcessAlreadyExistException("O ID que você informou já existe! Por favor, informe outro!");
			}
			id++;
		}

		if ((novoProcesso.getNuProcesso() == null) || (novoProcesso.getChaveProcesso() == null)
				|| (novoProcesso.getSgOrgaoProcesso() == null) || (novoProcesso.getNuAnoProcesso() == null)
				|| (novoProcesso.getDescricao() == null) || (novoProcesso.getCdAssunto() == null)
				|| (novoProcesso.getDescricaoAssunto() == null) || (novoProcesso.getCdInteressado() == null)
				|| (novoProcesso.getNmInteressado() == null)) {

			throw new NullProcessException("Não é possível cadastrar um processo com campos nulos!");
		}

		repository.save(novoProcesso);

		ProcessoCriadoDTO processoCriado = new ProcessoCriadoDTO();
		processoCriado.setId(id);
		processoCriado.setChaveProcesso(novoProcesso.getChaveProcesso());

		return processoCriado;
	}

	public Optional<ProcessoEntity> buscarProcessoPorId(long id) {

		Optional<ProcessoEntity> processo = recuperarProcessoPorId(id);

		return Optional.ofNullable(processo.orElseThrow());
	}

	public Iterable<ProcessoEntity> buscarProcessoPorChave(String chaveProcesso) {

		Iterable<ProcessoEntity> todosOsProcessos = recuperarProcessos();
		List<ProcessoEntity> processoFiltrados = new ArrayList<>();
		boolean encontrouOProcesso = false;
		for (ProcessoEntity processo : todosOsProcessos) {
			if (processo.getChaveProcesso().equals(chaveProcesso)) {
				processoFiltrados.add(processo);
				encontrouOProcesso = true;
			}
		}
		if (encontrouOProcesso){
			return processoFiltrados;
		}
		throw new ProcessNotFoundException("O processo pelo qual buscavas não foi encontrado!");
	}

	public ProcessoEntity atualizarProcessso(long id, ProcessoEntity processoAtualizado) {

		Iterable<ProcessoEntity> todosOsProcessos = repository.findAll();

		for (ProcessoEntity process : todosOsProcessos) {

			if (process.getId() == id) {

				Integer nuProcesso = (processoAtualizado.getNuProcesso() != null) ? processoAtualizado.getNuProcesso()
						: process.getNuProcesso();
				String chaveProcesso = (processoAtualizado.getChaveProcesso() != null)
						? processoAtualizado.getChaveProcesso()
						: process.getChaveProcesso();
				String sgOrgaoProcesso = (processoAtualizado.getSgOrgaoProcesso() != null)
						? processoAtualizado.getSgOrgaoProcesso()
						: process.getSgOrgaoProcesso();
				String nuAnoProcesso = (processoAtualizado.getNuAnoProcesso() != null)
						? processoAtualizado.getNuAnoProcesso()
						: process.getNuAnoProcesso();
				String descricao = (processoAtualizado.getDescricao() != null) ? processoAtualizado.getDescricao()
						: process.getDescricao();
				Integer cdAssunto = (processoAtualizado.getCdAssunto() != null) ? processoAtualizado.getCdAssunto()
						: process.getCdAssunto();
				String descricaoAssunto = (processoAtualizado.getDescricaoAssunto() != null)
						? processoAtualizado.getDescricaoAssunto()
						: process.getDescricaoAssunto();
				Integer cdInteressado = (processoAtualizado.getCdInteressado() != null)
						? processoAtualizado.getCdInteressado()
						: process.getCdInteressado();
				String nmInteressado = (processoAtualizado.getNmInteressado() != null)
						? processoAtualizado.getNmInteressado()
						: process.getNmInteressado();

				process.setNuProcesso(nuProcesso);
				process.setChaveProcesso(chaveProcesso);
				process.setSgOrgaoProcesso(sgOrgaoProcesso);
				process.setNuAnoProcesso(nuAnoProcesso);
				process.setDescricao(descricao);
				process.setCdAssunto(cdAssunto);
				process.setDescricaoAssunto(descricaoAssunto);
				process.setCdInteressado(cdInteressado);
				process.setNmInteressado(nmInteressado);

				repository.save(process);
				return process;
			}
		}
		throw new ProcessNotFoundException("O processo que buscavas atualizar não foi encontrado!");
	}

	public ProcessoRemovidoDTO deletarProcesso(long id) {
		Iterable<ProcessoEntity> todosOsProcessos = repository.findAll();

		for (ProcessoEntity process : todosOsProcessos) {

			if (process.getId() == id) {

				repository.deleteById(id);

				ProcessoRemovidoDTO processoRemovido = new ProcessoRemovidoDTO();
				processoRemovido.setId(id);
				processoRemovido.setMensagem("Processo removido com sucesso!");

				return processoRemovido;
			}
		}
		throw new ProcessNotFoundException("O processo que buscavas apagar não foi encontrado!");
	}

	private Iterable<ProcessoEntity> recuperarProcessos() {
		return repository.findAll();
	}

	private Optional<ProcessoEntity> recuperarProcessoPorId(long id) {
		return repository.findById(id);
	}

}
