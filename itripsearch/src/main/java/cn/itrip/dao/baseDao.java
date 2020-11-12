package cn.itrip.dao;

import cn.itrip.help.ItripHotelVO;
import cn.itrip.help.SearchHotCityVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

import java.util.List;

public class baseDao {
    static String url = "http://localhost:8080/solr/";
    HttpSolrClient httpSolrClient;

    public baseDao() {
        //初始化HttpSolrClient
        httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrClient.setConnectionTimeout(3000); // 建立连接的最长时间
    }

    public List<ItripHotelVO> getList(SolrQuery query) throws Exception {
        List<ItripHotelVO> list;
        list = httpSolrClient.query("hotel", query).getBeans(ItripHotelVO.class);
        return list;
    }
}
