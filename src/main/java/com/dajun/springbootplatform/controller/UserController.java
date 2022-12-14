package com.dajun.springbootplatform.controller;

import com.dajun.springbootplatform.entities.indexInfo;
import com.dajun.springbootplatform.entities.indexProduct;
import com.dajun.springbootplatform.repository.indexRepository;
import com.dajun.springbootplatform.repository.specialistRepository;
import com.dajun.springbootplatform.repository.userRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Collection;


@Controller
public class UserController {

    @Resource
    private userRepository userRepository;

    @Resource
    private indexRepository indexRepository;

    @Resource specialistRepository specialistRepository;

    @RequestMapping("/")
    public String hello()
    {
        return "forward:index";
    }

    @RequestMapping("logout")
    public String logOut(HttpSession session){
        session.invalidate();
        return "loginOrRegister/loginTest";
    }

    @GetMapping("/index")
    public String index(Model model){
        Collection<indexProduct> indexProducts = indexRepository.findAllProduct();
        Collection<indexInfo> indexInfos = indexRepository.findAllInfo();
        model.addAttribute("indexProducts",indexProducts);
        model.addAttribute("indexInfo",indexInfos);
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "loginOrRegister/loginTest";
    }

    @PostMapping(value = "/UserLogin")
    public String UserLogin(@RequestParam("phone") String phone,
                          @RequestParam("password") String password,
                          @RequestParam("inlineRadioOptions") String option,
                          Model model,
                          HttpSession session){
        if (password.isEmpty()||phone.isEmpty()){
            model.addAttribute("errorMsg","??????????????????????????????");
//            session.invalidate();     ??????session
//            session.getLastAccessedTime();???????????????????????????
//            session.setMaxInactiveInterval();??????session??????????????????????????????
            return "loginOrRegister/loginTest";
        }
        if (option.equals("1")){
            String username = userRepository.findName(phone);
            if (userRepository.findByAccount(phone)!=null && userRepository.findByAccount(phone).getUser_pass().equals(password)){
                //session?????????????????????7200??? ????????????  -> ?????????????????????
//                session.setMaxInactiveInterval(60 * 60 * 24 * 7);
                session.setAttribute("userName",username);
                session.setAttribute("userPhone",phone);
                session.setAttribute("userMsg",3);
                return "redirect:/index";
            }else {
                model.addAttribute("errorMsg","?????????????????????");
                return "loginOrRegister/loginTest";
            }
        }
        else {
            String username = specialistRepository.findName(phone);
            if(specialistRepository.findByAccount(phone)!=null&&specialistRepository.findByAccount(phone).getSpecialist_pass().equals(password)){
                session.setAttribute("userName",username);
                session.setAttribute("userPhone",phone);
                int specialist_id = specialistRepository.findIdByPhone(phone);
                session.setAttribute("specialist_id",specialist_id);
                return "specialistIndex";
            }else {
                model.addAttribute("errorMsg","?????????????????????");
                return "loginOrRegister/loginTest";
            }
        }
    }

}

