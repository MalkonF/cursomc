package me.malkon.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import me.malkon.cursomc.domain.Cidade;
import me.malkon.cursomc.domain.Cliente;
import me.malkon.cursomc.domain.Endereco;
import me.malkon.cursomc.domain.enums.Perfil;
import me.malkon.cursomc.domain.enums.TipoCliente;
import me.malkon.cursomc.dto.ClienteDTO;
import me.malkon.cursomc.dto.ClienteNewDTO;
import me.malkon.cursomc.repositories.ClienteRepository;
import me.malkon.cursomc.repositories.EnderecoRepository;
import me.malkon.cursomc.resources.exception.AuthorizationException;
import me.malkon.cursomc.security.UserSS;
import me.malkon.cursomc.services.exceptions.DataIntegrityException;
import me.malkon.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;
	// injetando o bean q foi criado em SecurityConfig
	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.prefix.client.profile}")
	private String prefix;

	public Cliente find(Integer id) {

		UserSS user = UserService.authenticated(); // retorna user logado
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		} /*
			 * hasRole verificar se o usuário tem o perfil de admin e id.equals verifica se
			 * o id q ta buscando n é igual ao user q ta logado
			 */

		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));

	}

	@Transactional // vai inserir o cliente e os seus endereços na mesma transação do bd
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());// tem que salvar o cliente e o endereco ja q os demais dados
														// estao contidos nessas duas entidades
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);// atualize os dados do novo objeto criado com base no objeto que veio como
								// argumento. Assim qd atualizar so o nome e email n vai ficar null os outros
								// campos
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDto) {// instanciar cliente básico a partir DTO
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO objDto) {// sobrecarga do método acima
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),
				TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(),
				objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();// verifica se o cliente ta autenticado, pq ele tem q ta autenticado p
													// mudar sua imagem de perfil
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		// recebe a img jpeg a parti do arquivo enviado pelo user
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		// monta o nome do arquivo com base no cliente q esta logado
		String fileName = prefix + user.getId() + ".jpg";

		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}
