package it.itj.academy.blogbe.util.db_filler;

import it.itj.academy.blogbe.entity.User;
import it.itj.academy.blogbe.repository.RoleRepository;
import it.itj.academy.blogbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
            new User("Edoardo Filippo", "Borrello", LocalDate.parse("1995-07-03"), "edoardo.borrello@gmail.com", "e.borrello", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_SUPER_ADMIN").get()),
            new User("Daniele", "Bertoldi", LocalDate.parse("1995-07-03"), "daniele.bertoldi@gmail.com", "d.bertoldi", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_ADMIN").get()),
            new User("Daniele", "Gradassai", LocalDate.parse("1995-07-03"), "daniele.gradassai@gmail.com", "d.gradassai", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_ADMIN").get()),
            new User("Davide", "Giannetti", LocalDate.parse("1995-07-03"), "davide.giannetti@gmail.com", "d.giannetti", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_ADMIN").get()),
            new User("Marco", "Fabozzi", LocalDate.parse("1995-07-03"), "marco.fabozzi@gmail.com", "m.fabozzi", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_ADMIN").get()),
            new User("Francesco", "Pacifico", LocalDate.parse("1995-07-03"), "francesco.pacifico@gmail.com", "f.pacifico", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_ADMIN").get()),
            new User("Mario", "Rossi", LocalDate.parse("1995-07-03"), "mario.rossi@gmail.com", "m.rossi", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_MODERATOR").get()),
            new User("Luigi", "Bianchi", LocalDate.parse("1995-07-03"), "luigi.bianchi@gmail.com", "l.bianchi", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_MODERATOR").get()),
            new User("Giulia", "Verdi", LocalDate.parse("1995-07-03"), "giulia.verdi@gmail.com", "g.verdi", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_MODERATOR").get()),
            new User("Sofia", "Neri", LocalDate.parse("1995-07-03"), "sofia.neri@gmail.com", "s.neri", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_MODERATOR").get()),
            new User("Ludovica", "Gialli", LocalDate.parse("1995-07-03"), "ludovica.gialli@gmail.com", "l.gialli", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_USER").get()),
            new User("Andrea", "Viola", LocalDate.parse("1995-07-03"), "andrea.viola@gmail.com", "a.viola", bCryptPasswordEncoder.encode("Test123!"), roleRepository.findByAuthority("ROLE_USER").get())
        );
        userRepository.saveAll(users);
    }
}
