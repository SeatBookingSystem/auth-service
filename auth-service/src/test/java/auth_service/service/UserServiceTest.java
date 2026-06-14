package auth_service.service;

import auth_service.entity.User;
import auth_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void register_shouldSaveUser_whenUserNotExists() {
		when(userRepository.findByUsername("test")).thenReturn(Optional.empty());

		userService.register("test", "123");

		verify(userRepository).save(any(User.class));
	}

	@Test
	void register_shouldThrowException_whenUserExists() {
		when(userRepository.findByUsername("test"))
				.thenReturn(Optional.of(new User()));

		assertThrows(RuntimeException.class, () ->
				userService.register("test", "123")
		);
	}

	@Test
	void login_shouldReturnUser_whenCredentialsValid() {
		User user = new User();
		user.setUsername("test");
		user.setPassword("123");

		when(userRepository.findByUsername("test"))
				.thenReturn(Optional.of(user));

		User result = userService.login("test", "123");

		assertEquals("test", result.getUsername());
	}

	@Test
	void login_shouldThrowException_whenUserNotFound() {
		when(userRepository.findByUsername("test"))
				.thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () ->
				userService.login("test", "123")
		);
	}

	@Test
	void login_shouldThrowException_whenPasswordWrong() {
		User user = new User();
		user.setUsername("test");
		user.setPassword("123");

		when(userRepository.findByUsername("test"))
				.thenReturn(Optional.of(user));

		assertThrows(RuntimeException.class, () ->
				userService.login("test", "wrong")
		);
	}

}
