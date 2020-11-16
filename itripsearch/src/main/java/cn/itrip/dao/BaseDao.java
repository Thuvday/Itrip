package cn.itrip.dao;

import cn.itrip.entity.ItripHotelVO;
import cn.itrip.entity.Page;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.util.List;

public class BaseDao {
    HttpSolrClient httpSolrClient;
    static String url = "http://localhost:8080/solr/hotel";

    public BaseDao() {
        httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrClient.setConnectionTimeout(500); // 建立连接的最长时间
    }

    public List<ItripHotelVO> getlist(SolrQuery solrQuery) throws Exception {
        List<ItripHotelVO> list = httpSolrClient.query(solrQuery).getBeans(ItripHotelVO.class);
        return list;
    }

    public Page<ItripHotelVO> gethot(SolrQuery solrQuery, int curpage, Integer pagesize) throws Exception {
        QueryResponse queryResponse = httpSolrClient.query(solrQuery);
        List<ItripHotelVO> list = queryResponse.getBeans(ItripHotelVO.class);
        SolrDocumentList solrDocuments = ((QueryResponse) queryResponse).getResults();
        Page page = new Page(curpage, pagesize, new Long(solrDocuments.getNumFound()).intValue());
        page.setRows(list);
        return page;
    }
}
