package br.com.Letterbook.Letterbook.service;

import br.com.Letterbook.Letterbook.model.DTO.UsersDTO;
import br.com.Letterbook.Letterbook.model.Users;
import br.com.Letterbook.Letterbook.repository.UsersRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users autenticar(String email, String senha) {
        return usersRepository.findByEmail(email)
                .filter(c -> c.getSenha().equals(senha)) // com senha simples
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));
    }


    public void addUsers(UsersDTO usuarioDTO) {
        // Validação do e-mail
        if (usersRepository.findByEmail(usuarioDTO.getEmail()) != null) {
            throw new ValidationException("Este e-mail já está em uso.");
        }

        // Validação do CPF
        if (usersRepository.findByCpf(usuarioDTO.getCpf()) != null) {
            throw new ValidationException("Este CPF já está cadastrado.");
        }

        // Validação das senhas
        if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmarSenha())) {
            throw new ValidationException("As senhas não conferem.");
        }

        // Criando cliente a partir do DTO
        Users users = new Users();
        users.setEmail(usuarioDTO.getEmail());
        users.setSenha(usuarioDTO.getSenha());
        users.setNome(usuarioDTO.getNome());
        users.setCpf(usuarioDTO.getCpf());
        users.setGenero(usuarioDTO.getGenero());
        users.setDataNascimento(usuarioDTO.getDataNascimento());

        usersRepository.save(users);
    }
}


