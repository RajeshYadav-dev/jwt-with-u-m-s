package com.dev.service;

import com.dev.entity.OurUser;
import com.dev.entity.ReqResp;
import com.dev.repository.OurUserRepository;
import com.dev.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private OurUserRepository ourUserRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqResp register(ReqResp registerRequest){
        ReqResp resp = new ReqResp();
        try{
            OurUser ourUser = new OurUser();
            ourUser.setFirstName(registerRequest.getFirstName());
            ourUser.setLastName(registerRequest.getLastName());
            ourUser.setEmail(registerRequest.getEmail());
            ourUser.setUsername(registerRequest.getUsername());
            ourUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            ourUser.setCity(registerRequest.getCity());
            ourUser.setRole(registerRequest.getRole());
            OurUser ourUserResult = ourUserRepository.save(ourUser);

            if(ourUserResult.getId() > 0){
                resp.setOurUser(ourUserResult);
                resp.setMessage("User save successfully.");
                resp.setStatusCode(200);
            }
        }catch (Exception ex){
            resp.setStatusCode(500);
            resp.setError(ex.getMessage());
        }
        return resp;
    }

    public ReqResp login(ReqResp loginRequest){
        ReqResp response = new ReqResp();
        try {
            authenticationManager
                    .authenticate(new
                            UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
            var user = ourUserRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
            var jwt = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hours");
            response.setMessage("Successfully Log In");

        }catch (Exception ex){
            response.setStatusCode(500);
            response.setError(ex.getMessage());
        }
        return response;
    }

    public ReqResp refreshToken(ReqResp refreshTokenRegister){
        ReqResp response = new ReqResp();
        try{
            String ourUsername = jwtService.extractUserName(refreshTokenRegister.getToken());
            OurUser user = ourUserRepository.findByUsername(ourUsername).orElseThrow();

        }catch (Exception ex){
            response.setStatusCode(500);
            response.setError(ex.getMessage());
        }

        return response;
    }

    public ReqResp getAllUsers(){
        ReqResp response = new ReqResp();
        try{
           List<OurUser> allUser =ourUserRepository.findAll();
           if (!allUser.isEmpty()){
               response.setOurUserList(allUser);
               response.setStatusCode(200);
               response.setMessage("Successful..");
           }else {
               response.setStatusCode(404);
               response.setMessage("Users not found..");
               return response;
           }
        }catch (Exception ex){
            response.setStatusCode(500);
            response.setError(ex.getMessage());
            return response;
        }
        return response;
    }

    public ReqResp getUserById(Long id){
        ReqResp response = new ReqResp();
        try {
           OurUser userById = ourUserRepository.findById(id).orElseThrow(()->new RuntimeException("User Not Found.."));
           response.setOurUser(userById);
           response.setStatusCode(200);
           response.setMessage("User Found By Id: "+id+" successfully..");
        }catch (Exception ex){
            response.setStatusCode(500);
            response.setError(ex.getMessage());
        }
        return response;
    }

public ReqResp deleteUser(Long id){
        ReqResp response = new ReqResp();
        try{
            Optional<OurUser> user =ourUserRepository.findById(id);
            if (user.isPresent()){
                ourUserRepository.deleteById(id);
                response.setStatusCode(200);
                response.setMessage("User deleted successfully..");
            }else{
                response.setStatusCode(404);
                response.setMessage("User not found for deletion..");
            }

        }catch (Exception ex){
            response.setStatusCode(500);
            response.setError(ex.getMessage());
        }
        return response;
       }

       public ReqResp updateUser(OurUser ourUser,Long id){
            ReqResp response = new ReqResp();
            try{
                Optional<OurUser> userOptional =ourUserRepository.findById(id);
                if (userOptional.isPresent()){
                    OurUser exitingUser = userOptional.get();
                    exitingUser.setFirstName(ourUser.getFirstName());
                    exitingUser.setLastName(ourUser.getLastName());
                    exitingUser.setEmail(ourUser.getEmail());
                    exitingUser.setUsername(ourUser.getUsername());
                    exitingUser.setRole(ourUser.getRole());
                    exitingUser.setCity(ourUser.getCity());

                    //check if password present in request
                    if(ourUser.getPassword() != null && !ourUser.getPassword().isEmpty()){
                        //encode the password
                        exitingUser.setPassword(passwordEncoder.encode(ourUser.getPassword()));
                    }
                    OurUser saveUser = ourUserRepository.save(exitingUser);
                    response.setOurUser(saveUser);
                    response.setStatusCode(200);
                    response.setMessage("User updated successfully..");
                }else{
                    response.setStatusCode(404);
                    response.setMessage("User not found for update..");
                }

            }catch (Exception ex){
                response.setStatusCode(500);
                response.setError(ex.getMessage());
            }
            return response;
       }

       public ReqResp getMyInfo(String username){
        ReqResp response = new ReqResp();
        try{
            Optional<OurUser> user = ourUserRepository.findByUsername(username);
            if (user.isPresent()){
                response.setOurUser(user.get());
                response.setStatusCode(200);
                response.setMessage("User Successfully..");
            }else {
                response.setStatusCode(404);
                response.setMessage("User not found for update..");
            }
        }catch (Exception ex){
            response.setStatusCode(500);
            response.setError(ex.getMessage());
        }
        return response;
    }

}
