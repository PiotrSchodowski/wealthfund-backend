//package com.example.wealthFund.controllerTests;
//
//import com.example.wealthFund.repository.UserRepository;
//import com.example.wealthFund.repository.entity.User;
//import com.example.wealthFund.restController.UserController;
//import com.example.wealthFund.service.UserService;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.hasSize;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Mockito.when;
//
//public class UserControllerTest {
//
//    @InjectMocks
//    UserController userController;
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    UserService userService;
//
//    public UserControllerTest() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void should_ReturnListOfUsers() {
//        // Given
//        List<User> expectedUsers = prepareMockData();
//        when(userController.getUsers()).thenReturn(expectedUsers);
//
//        // When
//        List<User> actualUsers = userController.getUsers();
//
//        // Then
//        assertThat(actualUsers, hasSize(expectedUsers.size()));
//    }
//
//    @Test
//    public void should_DeleteUser() {
//        // Given
//        String name = "Piotr";
//        when(userController.deleteUser(name)).thenReturn(true);
//
//        // When
//        boolean result = userController.deleteUser(name);
//
//        // Then
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void should_AddUser() {
//        // Given
//        String name = "Piotr";
//        User user = new User(name);
//        when(userController.addNewUser(name)).thenReturn(user);
//
//        // When
//        User actualUser = userController.addNewUser(name);
//
//        // Then
//        assertNotNull(actualUser);
//        assertEquals(name, actualUser.getName());
//    }
//
//
//    private List<User> prepareMockData() {
//        List<User> users = new ArrayList<>();
//        users.add(new User("Piotr"));
//        users.add(new User("Patryk"));
//        return users;
//    }
//}
