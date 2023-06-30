package it.itj.academy.blogbe.util.db_filler;

import it.itj.academy.blogbe.entity.User;
import it.itj.academy.blogbe.repository.RoleRepository;
import it.itj.academy.blogbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class UserFiller {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void fillUsers() {
        userRepository.deleteAll(userRepository.findAll());
        Set<User> users = Set.of(
            new User("Edoardo Filippo", "Borrello", (byte) 28, "edoardo.borrello@gmail.com", "e.borrello", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get(), roleRepository.findById(3L).get(), roleRepository.findById(4L).get())),
            new User("Daniele", "Bertoldi", (byte) 27, "daniele.bertoldi@gmail.com", "d.bertoldi", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get(), roleRepository.findById(3L).get())),
            new User("Daniele", "Gradassai", (byte) 31, "daniele.gradassai@gmail.com", "d.gradassai", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get(), roleRepository.findById(3L).get())),
            new User("Davide", "Giannetti", (byte) 25, "davide.giannetti@gmail.com", "d.giannetti", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get(), roleRepository.findById(3L).get())),
            new User("Marco", "Fabozzi", (byte) 25, "marco.fabozzi@gmail.com", "m.fabozzi", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get(), roleRepository.findById(3L).get())),
            new User("Francesco", "Pacifico", (byte) 26, "francesco.pacifico@gmail.com", "f.pacifico", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get(), roleRepository.findById(3L).get())),
            new User("Mario", "Rossi", (byte) 43, "mario.rossi@gmail.com", "m.rossi", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get())),
            new User("Luigi", "Bianchi", (byte) 35, "luigi.bianchi@gmail.com", "l.bianchi", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get())),
            new User("Giulia", "Verdi", (byte) 30, "giulia.verdi@gmail.com", "g.verdi", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get())),
            new User("Sofia", "Neri", (byte) 24, "sofia.neri@gmail.com", "s.neri", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get(), roleRepository.findById(2L).get())),
            new User("Ludovica", "Gialli", (byte) 45, "ludovica.gialli@gmail.com", "l.gialli", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get())),
            new User("Andrea", "Viola", (byte) 32, "andrea.viola@gmail.com", "a.viola", bCryptPasswordEncoder.encode("Test123!"), List.of(roleRepository.findById(1L).get()))
        );
        userRepository.saveAll(users);
    }
}
