package cn.itrip.controller;

import cn.itrip.dao.itripHotel.ItripHotelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class hotelController {
    @Resource
    ItripHotelMapper mapper;
    @RequestMapping("/list")
    @ResponseBody
    public Object getList() throws Exception {
        return mapper.getItripHotelById(1L);
    }
}
