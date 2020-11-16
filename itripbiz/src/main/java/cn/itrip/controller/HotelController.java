package cn.itrip.controller;

import cn.itrip.common.Dto;
import cn.itrip.common.DtoUtil;
import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import cn.itrip.dao.itripLabelDic.ItripLabelDicMapper;
import cn.itrip.pojo.ItripAreaDic;
import cn.itrip.pojo.ItripLabelDic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class HotelController {
    @Resource
    ItripAreaDicMapper dao;
    @RequestMapping("/api/hotel/queryhotcity/{type}")
    @ResponseBody
    public Dto getHot(@PathVariable("type") int type) throws Exception {
        List<ItripAreaDic> list = dao.getHot(type);
        return DtoUtil.returnDataSuccess(list);
    }

    @Resource
    ItripLabelDicMapper dao1;
    @RequestMapping("/api/hotel/queryhotelfeature")
    @ResponseBody
    public Dto getFeature() throws Exception {
        List<ItripLabelDic> list = dao1.getFeature();
        return DtoUtil.returnDataSuccess(list);
    }

    @RequestMapping("/api/hotel/querytradearea/{id}")
    @ResponseBody
    public Dto getHotArea(@PathVariable("id") int id) throws Exception {
        List<ItripAreaDic> list = dao.getHotArea(id);
        return DtoUtil.returnDataSuccess(list);
    }
}
