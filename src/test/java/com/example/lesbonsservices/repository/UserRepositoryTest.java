package com.example.lesbonsservices.repository;

import com.example.lesbonsservices.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


        @Test
        void shouldFindExistingUserInDatabase_byEmail() {
            // Act
            User found = userRepository.findByEmail("hamid.ach@gmail.com");

            // Assert
            assertThat(found)
                    .as("L'utilisateur 'hamid.ach@gmail.com' doit exister dans la base de données")
                    .isNotNull();

            assertThat(found.getEmail()).isEqualTo("hamid.ach@gmail.com");
        }

}