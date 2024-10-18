package com.dev.controller;

import com.dev.entity.OurUser;
import com.dev.entity.ReqResp;
import com.dev.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementController {

    @Autowired
    private UserManagementService userManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqResp> register(@RequestBody ReqResp resp){
        return ResponseEntity.ok(userManagementService.register(resp));
    }
    @PostMapping("/auth/login")
    public ResponseEntity<ReqResp> login(@RequestBody ReqResp req){
        return ResponseEntity.ok(userManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqResp> refreshToken(@RequestBody ReqResp req){
        return ResponseEntity.ok(userManagementService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqResp> getAllUser(){
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }
    @GetMapping("/admin/get-user/{id}")
    public ResponseEntity<ReqResp> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userManagementService.getUserById(id));
    }
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ReqResp> updateUser(@RequestBody OurUser user, @PathVariable Long id){
        return ResponseEntity.ok(userManagementService.updateUser(user,id));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqResp> getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ReqResp response = userManagementService.getMyInfo(username);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ReqResp> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userManagementService.deleteUser(id));
    }
}
