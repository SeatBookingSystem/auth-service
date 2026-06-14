package auth_service.controller;

import auth_service.dto.AuthRequest;
import auth_service.service.UserService;
import auth_service.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_shouldReturnSuccessMessage() {
        AuthRequest request = new AuthRequest();
        request.username = "test";
        request.password = "123";

        ResponseEntity<String> response = authController.register(request);

        assertEquals(200, response.getStatusCode());
        verify(userService).register("test", "123");
    }

    @Test
    void login_shouldReturnToken() {
        AuthRequest request = new AuthRequest();
        request.username = "test";
        request.password = "123";

        when(jwtUtil.generateToken("test")).thenReturn("mock-token");

        ResponseEntity<String> response = authController.login(request);

        assertEquals("mock-token", response.getBody());
        verify(userService).login("test", "123");
    }
}
