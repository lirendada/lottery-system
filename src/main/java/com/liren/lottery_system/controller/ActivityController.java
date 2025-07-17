package com.liren.lottery_system.controller;

import com.liren.lottery_system.common.pojo.dto.CreateActivityRequestDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {
    @PostMapping("/activity/create")
    public Long createActivity(@RequestBody @Validated CreateActivityRequestDTO req) {
        // TODO

        return null;
    }
}
