package com.codedrinker.controller;

import com.codedrinker.dto.UserDTO;
import com.codedrinker.entity.ResponseDTO;
import com.codedrinker.service.AuthorizationService;
import com.codedrinker.utils.AESSecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by codedrinker on 06/08/2017.
 */
@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${aes.key}")
    private String key;

    @Autowired
    private AuthorizationService authorizationService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String user = null;
        if (cookies != null && cookies.length != 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (StringUtils.equals(cookies[i].getName(), "user")) {
                    user = cookies[i].getValue();
                }
            }
        }
        if (StringUtils.isNotBlank(user)) {
            ResponseDTO responseDTO = null;
            try {
                responseDTO = authorizationService.getByAccessToken(AESSecurityUtil.decrypt(user, key));
            } catch (Exception e) {
                logger.error("decrypt error -> {}", user, e);
            }
            request.getSession().setAttribute("id", user);
            UserDTO userDTO = (UserDTO) responseDTO.getData();
            request.getSession().setAttribute("name", userDTO.getName());
        }
        model.addAttribute("clientId", clientId);
        return "index";
    }
}