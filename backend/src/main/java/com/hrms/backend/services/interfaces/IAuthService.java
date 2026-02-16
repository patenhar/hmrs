package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.request.AuthReqDto;
import com.hrms.backend.dtos.response.LoginResDto;

public interface IAuthService {
    Boolean register(AuthReqDto authReqDto);
    LoginResDto login(AuthReqDto authReqDto);
}
