package com.ww.mall.tiny.comom.utlis;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-25 10:55
 * @describe:
 */

@Component
public class ElasticsearchUtils<T> {


    @Autowired
    private RestHighLevelClient restHighLevelClient;



    //1.索引操作client.indices().

    //新建索引
    public boolean createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        //System.out.println(response.isAcknowledged());
        return response.isAcknowledged();
    }

    //判断索引是否存在
    public boolean existsIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        //System.out.println(exists);
        return exists;
    }

    //删除索引
    public boolean deleteIndex(String index) throws  IOException{
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        //System.out.println(response.isAcknowledged());
        return response.isAcknowledged();

    }





    //2.文档操作client.没有client.index了

    //新建文档
    public boolean createDocument(String index, String id, T data){
        //和新建索引请求有点像但是新建索引请求是new CreateIndexRequest
        IndexRequest request = new IndexRequest(index);
        //把写入信息封装到request
        request.id(id);         //设置文档id
        request.timeout("1s");  //设置超时时间1秒
        request.source(JSON.toJSONString(data), XContentType.JSON);    //文档数据和指定类型
        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(response);
        return response.status() == RestStatus.OK || response.status() == RestStatus.CREATED;   //创建成功或者新建
    }

    //根据id查询文档
    public GetResponse getDocument(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index, id);
        GetResponse documentFields = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        //System.out.println(documentFields);
        //System.out.println(documentFields.getSourceAsString());  //可以asString asMap……
        return documentFields;
    }

    //判断文档是否存在
    public boolean existsDocument(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index, id);
        //只判断索引是否存在不需要获取_source
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        //System.out.println(exists);
        return exists;
    }

    //删除文档
    public boolean deleteDocument(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index,id);
        request.timeout("1s");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        //System.out.println(response);
        return response.status() == RestStatus.OK;
    }

    //修改文档
    public boolean updateDocument(String index, String id, T data) throws IOException {
        //返回值最好还是写updateRequest不要写quest 有分明
        UpdateRequest updateRequest = new UpdateRequest(index, id);
        updateRequest.doc(JSON.toJSONString(data),XContentType.JSON);
        updateRequest.timeout("1s");
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        //System.out.println(updateRequest);
        return updateResponse.status() == RestStatus.OK;

    }


    /**
     * 批量新增文档
     * bulk是一个桶，里面装的是各种Request然后一起执行 新增就是IndexRequest
     * 更新就是UpdateRequest 删除就是DeleteRequest 查询就是GetRequest
     * （批量修改删除也是一样 可以单独放到一个bulk也可以全部放到bulk）
     * @param index  索引
     * @param list   插入的数据
     */
    public boolean bulkCreateDocument(String index, List<T> list){
        BulkRequest bulkRequest = new BulkRequest(index);
        bulkRequest.timeout(TimeValue.timeValueSeconds(5)); //用5s也是一样
        for (int i = 0; i < list.size(); i++) {
            //bulkRequest里面装的就是一个IndexRequest请求，没有分配id，会随机生成一个
            bulkRequest.add(new IndexRequest(index).source(JSON.toJSONString(list.get(i)), XContentType.JSON));
        }
        BulkResponse bulkResponse = null;
        try {
            bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(bulkResponse);
        return !bulkResponse.hasFailures(); //不失败就是成功
    }





    //3.花样查询 you know, for search
    /**
     *    可以全部查询，精确查询，模糊匹配都可以
     *    更具体可以看官网安排的明明白白 京东项目实战
     */
    public void searchDocument(String index) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //QueryBuilders.点一下就可以设置好多查询条件
        //sourceBuilder.query(QueryBuilders.matchAllQuery());                       //查询所有
        sourceBuilder.query(QueryBuilders.termQuery("name", "ww1"));     //精确查找
        sourceBuilder.from(0);  //分页
        sourceBuilder.size(2);

        sourceBuilder.timeout(TimeValue.timeValueSeconds(60));                      //设置超时时间
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println(searchResponse);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        for (SearchHit documentFields:searchResponse.getHits().getHits()) {
            System.out.println(documentFields.getSourceAsMap());
        }
    }

}
