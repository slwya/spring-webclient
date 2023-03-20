package com.example.springwebclient.domain.product;

import com.example.springwebclient.global.ApiEndPoint;
import com.example.springwebclient.global.GraphqlWebClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private final WebClient webClient;

    public ProductService(WebClient webClient) {
        this.webClient = webClient;
    }

    Logger log = LoggerFactory.getLogger(getClass());
    public void getProductList() {
        GraphqlWebClientUtil graphqlWebClientUtil = new GraphqlWebClientUtil(webClient);

        String document = """
                       query {
                            products{
                              productCode
                              productName
                              price
                            }
                          }
                          
                """;
        String name = "products";
        Map<String, Object> param = new HashMap<>();

        List<Product> result = graphqlWebClientUtil.postListSync(ApiEndPoint.GET_API_PATH, document, name, param, Product.class);
        log.info(String.valueOf(result));
    }

    public void getProduct() {
        GraphqlWebClientUtil graphqlWebClientUtil = new GraphqlWebClientUtil(webClient);

        String document = """
                        query test($productCode : Long!) {
                          productByCode(productCode : $productCode) {
                            productCode
                            productName
                            price
                          }
                        }
                """;
        String name = "productByCode";
        Map<String, Object> param = new HashMap<>();
        param.put("productCode", 1);
        Product result = graphqlWebClientUtil.postSync(ApiEndPoint.GET_API_PATH, document, name, param, Product.class);
        log.info(String.valueOf(result));
    }

}
