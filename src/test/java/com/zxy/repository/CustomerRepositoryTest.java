package com.zxy.repository;

import com.zxy.model.Customer;
import com.zxy.service.CustomerService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void saveCustomers() {
        repository.save(new Customer("Bill Alice", "北京",13));
        repository.save(new Customer("Steve Bob", "北京",23));
        repository.save(new Customer("Ashin Chou", "成都",27));
        repository.save(new Customer("Michael Summer", "烟台",22));
    }

    @Test
    public void fetchAllCustomers() {
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        Iterable<Customer> iterable=repository.findAll();
        for (Customer customer :iterable) {
            System.out.println(customer);
        }
    }

    @Test
    public void deleteCustomers() {
        repository.deleteAll();
       // customerService.deleteByUserName("Ashin");
    }

    @Test
    public void updateCustomers() {
        Customer customer= repository.findByUserName("summer");
        System.out.println(customer);
        customer.setAddress("北京市海淀区西直门");
        repository.save(customer);
        Customer xcustomer=repository.findByUserName("summer");
        System.out.println(xcustomer);
    }

    @Test
    public void findByAge() {
        System.out.println("Customer found findByAge(22):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByAge(22));
    }

    @Test
    public void fetchIndividualCustomers() {
        System.out.println("Customer found with findByUserName('summer'):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByUserName("summer"));
        System.out.println("--------------------------------");
        System.out.println("Customers found with findByAddress(\"北京\"):");
        String q="北京";
        for (Customer customer : repository.findByAddress(q)) {
            System.out.println(customer);
        }
    }

    @Test
    public void fetchIndividualCustomersLike() {
        System.out.println("Customer found with findByUserName('summer'):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByUserName("summer"));
        System.out.println("--------------------------------");
        System.out.println("Customers found with findByAddressLike(\"北京\"):");
        String q="北京";
        for (Customer customer : repository.findByAddressLike(q)) {
            System.out.println(customer);
        }
    }

    @Test
    public void regexpQueryCustomer() {
        System.out.println("Customer found with regSearchCustomer():");
        System.out.println("--------------------------------");
        for (Customer customer : customerService.regexpQuery("userName", "ashin.*")) {
            System.out.println(customer);
        }
    }

    @Test
    public void matchPhraseQueryCustomer() {
        System.out.println("Customer found with matchPhraseQueryCustomer():");
        System.out.println("--------------------------------");
        for (Customer customer : customerService.matchPhraseQuery(   "address", "北京市")) {
            System.out.println(customer);
        }
    }

    @Test
    public void fetchPageCustomers() {
        System.out.println("Customers found with fetchPageCustomers:");
        System.out.println("-------------------------------");
        Sort sort = new Sort(Sort.Direction.DESC, "age");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Customer> customers=repository.findByAddress("北京", pageable);
        System.out.println("Page customers "+customers.getContent().toString());
    }

    @Test
    public void fetchPage2Customers() {
        System.out.println("Customers found with fetchPageCustomers:");
        System.out.println("-------------------------------");
       QueryBuilder customerQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("address", "北京"));
        Page<Customer> page = repository.search(customerQuery, PageRequest.of(0, 10));
        System.out.println("Page customers "+page.getContent().toString());
        page = repository.search(customerQuery, PageRequest.of(1, 10));
        System.out.println("Page customers "+page.getContent().toString());
    }

    @Test
    public void fetchAggregation() {
        System.out.println("Customers found with fetchAggregation:");
        System.out.println("-------------------------------");

       QueryBuilder customerQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("address", "北京"));

        SumAggregationBuilder sumBuilder = AggregationBuilders.sum("sumAge").field("age");

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(customerQuery)
                .addAggregation(sumBuilder)
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

       //转换成map集合
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        InternalSum sumAge = (InternalSum) aggregationMap.get("sumAge");
        System.out.println("sum age is "+sumAge.getValue());
    }

}
