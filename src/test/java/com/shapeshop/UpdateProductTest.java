package com.shapeshop;


import com.shapeshop.config.ShapeShopTest;
import org.json.JSONObject;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.junit.jupiter.api.Test;


import java.sql.SQLException;


public class UpdateProductTest extends ShapeShopTest {

    @Test
    public void updateProductPrice() throws Exception {

        //update hotdog price from 7.9 to 5.5
        String updateProductJSON =
                "{ \"price\": \"5.5\" }";

        String token = authenticate("admin", "admin");

        // update product 2
        mvc.perform(MockMvcRequestBuilders.put("/carlscafe/products/2")
                .header("Authorization", "Bearer " + token).contentType("application/json")
                .content(updateProductJSON)).andExpect(matcher.is(200));

        // get product 2
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/carlscafe/products/2").contentType("application/json")
                .content(updateProductJSON)).andExpect(matcher.is(200));

        JSONObject obj = extractJSONObjectFromResponse(resultActions);
        JSONObject expectedObject = extractJSONObjectFromFileName("src/test/resources/updatedProduct.json");

        System.out.println("recievedArray " + obj);
        System.out.println("expectedArray " + expectedObject);

        //compare (ignore dangling entities)
        JSONAssert.assertEquals(expectedObject, obj, JSONCompareMode.LENIENT);
    }

    //TODO this test will fail if ALL tests are run. See super class for teardown TODO
    @Test
    public void updateProductName() throws Exception {

        //update name
        String updateProductJSON =
                "{\"name\": \"big mac\" }";

        String token = authenticate("admin", "admin");

        // update product 2
        mvc.perform(MockMvcRequestBuilders.put("/carlscafe/products/2")
                .header("Authorization", "Bearer " + token).contentType("application/json")
                .content(updateProductJSON)).andExpect(matcher.is(200));

        // get product 2
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/carlscafe/products/2").contentType("application/json")
                .content(updateProductJSON)).andExpect(matcher.is(200));

        JSONObject obj = extractJSONObjectFromResponse(resultActions);
        JSONObject expectedObject = extractJSONObjectFromFileName("src/test/resources/updatedProduct2.json");

        System.out.println("recievedArray " + obj);
        System.out.println("expectedArray " + expectedObject);

        JSONAssert.assertEquals(expectedObject, obj, JSONCompareMode.LENIENT);
    }


    @Test
    public void updateWrongProduct() throws Exception {

        //update product (2) that exists in carls cafe. But updating in annies art.
        String updateProductJSON =
                "{\"name\": \"big mac\" }";

        String token = authenticate("admin", "admin");

        // update
        mvc.perform(MockMvcRequestBuilders.put("/anniesart/products/2")
                .header("Authorization", "Bearer " + token).contentType("application/json")
                .content(updateProductJSON)).andExpect(matcher.is(404));
    }

}
