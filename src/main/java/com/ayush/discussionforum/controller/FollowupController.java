package com.ayush.discussionforum.controller;

import com.ayush.discussionforum.dto.FollowupRequest;
import com.ayush.discussionforum.dto.FollowupResponse;
import com.ayush.discussionforum.service.FollowupService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("v1/api/followups")
public class FollowupController {

    private final FollowupService followupService;

    @ApiOperation(value = "Create followup for an answer")
    @PostMapping()
    public ResponseEntity<?> createFollowup(@RequestBody @Valid FollowupRequest followupRequest){
        System.err.println(followupRequest.toString());
        followupService.createFollowup(followupRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get all followups for answer by its id")
    @GetMapping("/{answerId}")
    public List<FollowupResponse> getFromAnswerId(@PathVariable Long answerId){
        return followupService.getByAnswerId(answerId);
    }
}
