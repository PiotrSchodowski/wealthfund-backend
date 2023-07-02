//package com.example.wealthFund.service;
//
//import com.example.wealthFund.dto.WalletDto;
//import com.example.wealthFund.exception.WealthFundSingleException;
//import com.example.wealthFund.repository.UserRepository;
//import com.example.wealthFund.repository.WalletRepository;
//import com.example.wealthFund.repository.entity.UserEntity;
//import com.example.wealthFund.repository.entity.WalletEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class WalletServiceTest {
//
//    @InjectMocks
//    private WalletService walletService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private WalletRepository walletRepository;
//
//    @Mock
//    private TextValidator textValidator;
//
//    @Mock
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void shouldAddNewWallet() {
//        String userName = "Piotr";
//        String walletName = "Xtb";
//        String currency = "USD";
//
//        assertDoesNotThrow(() -> walletService.addNewWallet(userName, walletName, currency));
//
//    }
//}

