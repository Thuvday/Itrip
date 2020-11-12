package cn.itrip.controller;

import cn.itrip.common.Dto;
import cn.itrip.common.DtoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class hotelController {
    @RequestMapping()
    @ResponseBody
    public Dto hotelSearch() {

        return DtoUtil.returnDataSuccess("");
    }
}
