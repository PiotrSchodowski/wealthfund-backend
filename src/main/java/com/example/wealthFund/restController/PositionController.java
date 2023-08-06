package com.example.wealthFund.restController;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import com.example.wealthFund.service.PositionManager;

import org.springframework.web.bind.annotation.*;

@RestController
public class PositionController {

    private final PositionManager positionManager;


    public PositionController(PositionManager positionManager) {
        this.positionManager = positionManager;
    }

    @PostMapping("/user/{userName}/wallet/{walletName}/position/add")
    public AddPositionDto addPosition(@PathVariable String userName,
                                      @PathVariable String walletName,
                                      @RequestBody AddPositionDto addPositionDto) {
        return positionManager.addPosition(userName, walletName, addPositionDto);
    }

    @PostMapping("/user/{userName}/wallet/{walletName}/position/decrease")
    public SubtractPositionDto decreasePosition(@PathVariable String userName,
                                                @PathVariable String walletName,
                                                @RequestBody SubtractPositionDto subtractPositionDto) {
        return positionManager.subtractPosition(userName, walletName, subtractPositionDto);
    }


}
