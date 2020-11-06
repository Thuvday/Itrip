package cn.itrip.controller;

import cn.itrip.common.Dto;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.JredisApi;
import cn.itrip.common.MD5;
import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.help.ItripUserVO;
import cn.itrip.help.SMS;
import cn.itrip.pojo.ItripUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class registerController {
    @Resource
    ItripUserMapper dao;
    @Resource
    JredisApi jredisApi;
    @RequestMapping("/api/registerbyphone")
    @ResponseBody
    public Dto registerbyphone(@RequestBody ItripUserVO userVO) throws Exception {
        ItripUser user = new ItripUser();
        String phone = userVO.getUserCode();
        user.setUserCode(phone);
        user.setUserPassword(MD5.getMd5(userVO.getUserPassword(),32));
        user.setUserName(userVO.getUserName());
        int i = dao.insertItripUser(user);
        if (i==1) {
            String confrim = (int)(Math.random()*9000)+1000+"";
            SMS.sentYZM(phone, confrim);
            jredisApi.SetRedis(phone, confrim, 300);
            return DtoUtil.returnDataSuccess("成功");
        }
        return DtoUtil.returnFail("失败", "111");
    }

    @RequestMapping("/api/validatephone")
    @ResponseBody
    public Dto validatephone(String user, String code) throws Exception {
        if (jredisApi.getRedis(user).equals(code)) {
            dao.upActived(user);
            return DtoUtil.returnDataSuccess("激活成功");
        };
        return DtoUtil.returnFail("失败", "111");
    }
}
