package com.ayush.discussionforum.controller;

import com.ayush.discussionforum.dto.ProfileResponse;
import com.ayush.discussionforum.service.ProfileService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("v1/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @ApiOperation(value = "Get profile info by public id of the student")
    @GetMapping("/{publicStudentId}")
    public ProfileResponse getById(@PathVariable String publicStudentId){
        return profileService.getById(publicStudentId);
    }

}
