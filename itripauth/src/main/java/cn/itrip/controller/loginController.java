package cn.itrip.controller;

import cn.itrip.common.*;
import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.pojo.ItripUser;
import com.alibaba.fastjson.JSONArray;
import cz.mallat.uasparser.UserAgentInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
@Api(value = "appinfo", description = "用户模块")
public class loginController {
    @Resource
    ItripUserMapper dao;
    @Resource
    JredisApi jredisApi;
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",required=true,value="用户名",name="name",defaultValue="itrip@163.com"),
            @ApiImplicitParam(paramType="query",required=true,value="密码",name="password",defaultValue="111111")
    })
    @RequestMapping(value = "/api/dologin")
    @ResponseBody
    public Dto getLogin(String name, String password, HttpServletRequest request) throws Exception {
        String md5pass = MD5.getMd5(password, 32);
        ItripUser user = dao.dologin(name, md5pass);
        if (user != null) {
            // key=token, value=用户实体类
            String token = generateToken(request.getHeader("User-Agent"), user);
            String value = JSONArray.toJSONString(user);
            // 存session这步被redis替换了
            if (jredisApi.getRedis("token") == null) {
                jredisApi.SetRedis(token, value, 7200);
            }
            // 返回数据，客户端通过token来进行登录判断
            ItripTokenVO tokenVO = new ItripTokenVO(token, Calendar.getInstance().getTimeInMillis() + 7200, Calendar.getInstance().getTimeInMillis());

            return DtoUtil.returnDataSuccess(tokenVO);
        }
        return DtoUtil.returnDataSuccess("登录失败！");
    }

    public String generateToken(String agent, ItripUser user) {
        // TODO Auto-generated method stub
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
                    agent);
            StringBuilder sb = new StringBuilder();
            sb.append("token:");//统一前缀
            if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
                if (UserAgentUtil.CheckAgent(agent)) {
                    sb.append("MOBILE-");
                } else {
                    sb.append("PC-");
                }
            } else if (userAgentInfo.getDeviceType()
                    .equals("Personal computer")) {
                sb.append("PC-");
            } else
                sb.append("MOBILE-");
//			sb.append(user.getUserCode() + "-");
            sb.append(MD5.getMd5(user.getUserCode(), 32) + "-");//加密用户名称
            sb.append(user.getId() + "-");
            sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + "-");
            sb.append(MD5.getMd5(agent, 6));// 识别客户端的简化实现——6位MD5码

            return sb.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}