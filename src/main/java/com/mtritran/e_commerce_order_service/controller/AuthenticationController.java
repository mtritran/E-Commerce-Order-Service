package com.mtritran.e_commerce_order_service.controller;

import com.mtritran.e_commerce_order_service.dto.request.AuthenticationRequest;
import com.mtritran.e_commerce_order_service.dto.request.InspectTokenRequest;
import com.mtritran.e_commerce_order_service.dto.request.LogoutRequest;
import com.mtritran.e_commerce_order_service.dto.response.ApiResponse;
import com.mtritran.e_commerce_order_service.dto.response.AuthenticationResponse;
import com.mtritran.e_commerce_order_service.dto.response.InspectTokenResponse;
import com.mtritran.e_commerce_order_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authResponse = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("Success")
                .result(authResponse)
                .build();
    }

    @PostMapping("/inspect")
    ApiResponse<InspectTokenResponse> inspectToken(@RequestBody InspectTokenRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.inspectToken(request);

        return ApiResponse.<InspectTokenResponse>builder()
                .code(200)
                .message("Success")
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Logout successful")
                .result("Token has been invalidated")
                .build();
    }
}
