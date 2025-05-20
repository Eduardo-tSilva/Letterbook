<<<<<<< HEAD
package br.com.Letterbook.Letterbook.repository;

import br.com.Letterbook.Letterbook.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Users findByCpf(String cpf);
    boolean existsByEmail(String email);
}
=======
package br.com.Letterbook.Letterbook.repository;

import br.com.Letterbook.Letterbook.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Users findByCpf(String cpf);
    boolean existsByEmail(String email);
}
>>>>>>> c0af6527fa296ac171a74ac582a10fd37a463b03
