package com.codedrinker.service;

import com.codedrinker.dao.AuthorizationDao;
import com.codedrinker.entity.Authorization;
import com.codedrinker.entity.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by codedrinker on 23/07/2017.
 */
@Component
public class AuthorizationService {
    @Autowired
    private AuthorizationDao authorizationDao;

    public ResponseDTO save(Authorization authorization) {
        authorizationDao.save(authorization);
        return ResponseDTO.ok(authorization);
    }

    public ResponseDTO update(Authorization authorization) {
        authorizationDao.update(authorization);
        return ResponseDTO.ok(authorization);
    }

    public void delete(Integer id) {
        authorizationDao.delete(id);
    }

    public ResponseDTO get(Integer id) {
        Authorization authorization = authorizationDao.get(id);
        return ResponseDTO.ok(authorization);
    }
}
