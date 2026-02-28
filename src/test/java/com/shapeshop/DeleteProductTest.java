package com.shapeshop;


import com.shapeshop.config.ShapeShopTest;
import org.json.JSONArray;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.Test;

public class DeleteProductTest extends ShapeShopTest {

    @Test
    public void deleteProduct() throws Exception {

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/carlscafe/products")).andExpect(matcher.isOk());
        String token = authenticate("admin", "admin");

        JSONArray recievedArray = extractJSONArrayFromResponse(resultActions);
        JSONArray expectedArray = extractJSONArrayFromFileName("src/test/resources/carl_productList.json");

        System.out.println("recievedArray " + recievedArray);
        System.out.println("expectedArray " + expectedArray);

        deleteProductOverHTTP(token, "4");

        JSONAssert.assertEquals(
                expectedArray, recievedArray, JSONCompareMode.LENIENT);
    }



}
