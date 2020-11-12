package cn.itrip.controller;

import cn.itrip.common.Dto;
import cn.itrip.common.DtoUtil;
import cn.itrip.dao.baseDao;
import cn.itrip.help.SearchHotCityVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HotelSearchController {
    @RequestMapping(value = "/api/hotellist/searchItripHotelListByHotCity", produces = "application/json")
    @ResponseBody
    public Dto hotelSearch(@RequestBody SearchHotCityVO vo) throws Exception {
        baseDao dao = new baseDao();
        // 初始化SolrQuery
        SolrQuery query = new SolrQuery("cityId:"+vo.getCityId());
        // query.setSort("id", SolrQuery.ORDER.asc);
        query.setStart(0);
        //一页显示多少条
        query.setRows(vo.getCount());
        return DtoUtil.returnDataSuccess(dao.getList(query));
    }
}
