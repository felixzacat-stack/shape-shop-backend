package com.shapeshop;

import com.shapeshop.config.ShapeShopTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.Test;

public class ProductTest extends ShapeShopTest {

    @Test
    public void getProducts() throws Exception {

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/carlscafe/products")).andExpect(matcher.isOk());

        JSONArray recievedArray = extractJSONArrayFromResponse(resultActions);
        JSONArray expectedArray = extractJSONArrayFromFileName("src/test/resources/carl_productList.json");

        System.out.println("recievedArray " + recievedArray);
        System.out.println("expectedArray " + expectedArray);

        JSONAssert.assertEquals(
                expectedArray, recievedArray, JSONCompareMode.LENIENT);
    }



}
