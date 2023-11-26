package com.example.wealthFund.configuration;


import com.example.wealthFund.service.ActualizationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final ActualizationService actualizationService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 */15 * * * *")
    public void actualizeWalletData() {
        actualizationService.actualizeEachWallet();
        log.info("The wallets have updated {}", dateFormat.format(new Date()));
    }
}