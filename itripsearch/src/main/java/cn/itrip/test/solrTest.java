package cn.itrip.test;

import cn.itrip.entity.itripHotel;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.List;

public class solrTest {
    static String url = "http://localhost:8080/solr/";
    public static void main(String[] args) {
        //初始化HttpSolrClient
        HttpSolrClient httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrClient.setConnectionTimeout(500); // 建立连接的最长时间
        // 初始化SolrQuery
        SolrQuery query = new SolrQuery("id:1");
        // query.setSort("id", SolrQuery.ORDER.asc);
        query.setStart(0);
        //一页显示多少条
        query.setRows(5);
        QueryResponse queryResponse;
        try {
            queryResponse = httpSolrClient.query("hotel", query);
            List<itripHotel> list;
            list = queryResponse.getBeans(itripHotel.class);
            for (itripHotel hotel : list) {
                System.out.println(hotel.getId()+"-"+hotel.getHotelName()+"-"+hotel.getAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
