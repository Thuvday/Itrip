package cn.itrip.controller;

import cn.itrip.common.Dto;
import cn.itrip.common.DtoUtil;
import cn.itrip.dao.BaseDao;
import cn.itrip.entity.*;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SeachHotHotelController {

    @RequestMapping("/api/hotellist/searchItripHotelListByHotCity")
    @ResponseBody
    public Dto seach(@RequestBody SearchHotCityVO searchHotCityVO) throws Exception {
        BaseDao baseDao = new BaseDao();
        SolrQuery solrQuery = new SolrQuery("cityId:" + searchHotCityVO.getCityId());
        solrQuery.setStart(0);
        solrQuery.setRows(searchHotCityVO.getCount());
        List<ItripHotelVO> list = baseDao.getlist(solrQuery);
        return DtoUtil.returnDataSuccess(list);
    }

    @RequestMapping("/api/hotellist/searchItripHotelPage")
    @ResponseBody
    public Dto seachhot(@RequestBody SearchHotelVO searchHotelVO) throws Exception {
        BaseDao baseDao = new BaseDao();
        SolrQuery solrQuery = new SolrQuery("*:*");
        //目的地
        if (searchHotelVO.getDestination() != null) {
            solrQuery.addFilterQuery("destination:" + searchHotelVO.getDestination());
        }
        //关键词
        if (searchHotelVO.getKeywords() != null && searchHotelVO.getKeywords() != "") {
            solrQuery.addFilterQuery("keyword:" + searchHotelVO.getKeywords());
        }
        //tradingAreaIds:*,3668,*
        //位置
        if (searchHotelVO.getTradeAreaIds() != null && searchHotelVO.getTradeAreaIds() != "") {
            String num[] = searchHotelVO.getTradeAreaIds().split(",");
            String str = "";
            for (int i = 0; i < num.length; i++) {
                if (i == 0) {
                    str = "tradingAreaIds:*," + num[i] + ",*";
                } else {
                    str += " or tradingAreaIds:*," + num[i] + ",*";
                }
            }
            solrQuery.addFilterQuery(str);
        }
        //星级
        if (searchHotelVO.getHotelLevel() != null) {
            solrQuery.addFilterQuery("hotelLevel:" + searchHotelVO.getHotelLevel());
        }
        //特色
        if (searchHotelVO.getFeatureIds() != null && searchHotelVO.getFeatureIds() != "") {
            String num[] = searchHotelVO.getFeatureIds().split(",");
            String str = "";
            for (int i = 0; i < num.length; i++) {
                if (i == 0) {
                    str = "featureIds:*," + num[i] + ",*";
                } else {
                    str += " or featureIds:*," + num[i] + ",*";
                }
            }
            solrQuery.addFilterQuery(str);
        }
        //根据价格，评分，星级及各种排序
        if (searchHotelVO.getAscSort() != null && searchHotelVO.getAscSort() != "") {
            solrQuery.setSort(searchHotelVO.getAscSort(), SolrQuery.ORDER.asc);
        }
        if (searchHotelVO.getDescSort() != null && searchHotelVO.getDescSort() != "") {
            solrQuery.setSort(searchHotelVO.getDescSort(), SolrQuery.ORDER.desc);
        }
        if (searchHotelVO.getMinPrice() != null) {
            solrQuery.addFilterQuery("minPrice:[" + searchHotelVO.getMinPrice() + " TO *]");
        }
        if (searchHotelVO.getMaxPrice() != null) {
            solrQuery.addFilterQuery("minPrice:[* TO " + searchHotelVO.getMaxPrice() + "]");
        }
        searchHotelVO.setPageNo(searchHotelVO.getPageNo() == null ? 1 : searchHotelVO.getPageNo());
        solrQuery.setStart((searchHotelVO.getPageNo() - 1) * 6);
        solrQuery.setRows(6);
        Page page = baseDao.gethot(solrQuery, searchHotelVO.getPageNo(), 6);
        return DtoUtil.returnDataSuccess(page);
    }
}
