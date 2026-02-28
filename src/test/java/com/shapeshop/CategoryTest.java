package com.shapeshop;

import com.shapeshop.config.ShapeShopTest;
import lombok.val;
import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import org.junit.jupiter.api.Test;

import java.util.logging.Level;


public class CategoryTest extends ShapeShopTest {

    @Test
    public void getCategories() throws Exception {

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/carlscafe/categories")).andExpect(matcher.isOk());

        JSONArray recievedArray = extractJSONArrayFromResponse(resultActions);
        JSONArray expectedArray = extractJSONArrayFromFileName("src/test/resources/categoriesList_carl.json");

        System.out.println("recievedArray " + recievedArray);
        System.out.println("expectedArray " + expectedArray);

        //expect biskits and teas
        JSONAssert.assertEquals(
                expectedArray, recievedArray, JSONCompareMode.LENIENT);
    }


    @Test
    public void getProductsForCategory() throws Exception {

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/carlscafe/main/products")).andExpect(matcher.isOk());

        JSONArray recievedArray = extractJSONArrayFromResponse(resultActions);
        JSONArray expectedArray = extractJSONArrayFromFileName("src/test/resources/carl_products_for_category_main.json");

        System.out.println("recievedArray " + recievedArray);
        System.out.println("expectedArray " + expectedArray);//3

        //expect biskits and teas
        JSONAssert.assertEquals(
                expectedArray, recievedArray, JSONCompareMode.LENIENT);
    }

    @Test
    public void createCategory() throws Exception {

        String token = authenticate("admin", "admin");

        String createCategoryJSON =
            "{\"name\": \"scones\" }";

        //create a cat
        createCategoryOverHTTP(token, createCategoryJSON);

        //get all cats
        String cats = getCategoriesOverHTTP();
        JSONArray recievedArray = new JSONArray(cats);
        JSONArray expectedArray = extractJSONArrayFromFileName("src/test/resources/carl_catList_after_create_cat.json");

        System.out.println("recievedArray " + recievedArray);
        System.out.println("expectedArray " + expectedArray);

        //expect biskits and teas, amd scones
        JSONAssert.assertEquals(
                expectedArray, recievedArray, JSONCompareMode.LENIENT);
    }

    @Test
    public void updateCategory() throws Exception {
        //TODO
        //auth
        String token = authenticate("admin", "admin");

        //create a cat
        String createCategoryJSON =
                "{\"name\": \"scones\" }";
        createCategoryOverHTTP(token, createCategoryJSON);

        //create a prod for new category
        String createProductJSON =
                "{\"name\": \"jam scone\" }";
        createProductOverHTTP(token, createProductJSON, "scones");

        //make sure prod is there
        String productJSON = getProductsOverHTTP();
        JSONObject obj = findProduct(productJSON, "jam scone");
        if (obj == null) {
            throw new RuntimeException("canny find product");
        }

        //make sure prod is there
        String categoriesJSON = getCategoriesOverHTTP();
        JSONObject cat = findCategory(categoriesJSON, "scones");
        if (obj == null) {
            throw new RuntimeException("canny find product");
        }

        //update cat
        String updateCategoryJSON =
                "{\"name\": \"scones2\"}";
        updateCategoryOverHTTP(token, updateCategoryJSON, "7");

        //make sure cat is not there
        String categoryJSON = getCategoriesOverHTTP();
        System.out.println("*** catJSON --> " + categoryJSON);
        obj = findCategory(categoryJSON, "scones2");

        if (obj == null) {
            throw new RuntimeException("scones2 is there");
        }
    }



    @Test
    public void deleteCategory() throws Exception {
        //auth
        String token = authenticate("admin", "admin");

        //create a cat
        String createCategoryJSON =
                "{\"name\": \"scones\" }";
        createCategoryOverHTTP(token, createCategoryJSON);

        //create a prod for new category
        String createProductJSON =
                "{\"name\": \"jam scone\" }";
        createProductOverHTTP(token, createProductJSON, "scones");

        //make sure prod is there
        String productJSON = getProductsOverHTTP();
        JSONObject obj = findProduct(productJSON, "jam scone");
        if (obj == null) {
            throw new RuntimeException("canny find product");
        }

        //make sure prod is there
        String catsJSON = getCategoriesOverHTTP();
        JSONObject objCat = findCategory(catsJSON, "scones");

        //delete cat
        String deleteCategoryJSON =
                "{\"name\": \"scones\" }";
        deleteCategoryOverHTTP(token, "7");

        //make sure cat is not there
        String categoryJSON = getCategoriesOverHTTP();
        System.out.println("*** catJSON --> " + categoryJSON);
        obj = findCategory(categoryJSON, "scones");
        if (obj != null) {
            throw new RuntimeException("category is there when it shouldnt be there");
        }


//        //make sure prod is NOT there
//        productJSON = getProductsOverHTTP();
//        System.out.println("*** productJSON --> " + productJSON);
//        obj = findProduct(productJSON, "jam scone");
//        if (obj != null) {
//            throw new RuntimeException("product is there when it shouldnt be there");
//        }

        //prod should still be there but with no category
        productJSON = getProductsOverHTTP();
        System.out.println("*** productJSON --> " + productJSON);
        obj = findProduct(productJSON, "jam scone");
        if (obj == null) {
            throw new RuntimeException("product is not there when it should be there");
        }
    }


    private JSONObject findCategory(String catJSON, String catToFind) {
        JSONArray array = new JSONArray(catJSON);
        for (int i = 0; i < array.length(); i++){
            String name = array.getJSONObject(i).getString("name");
            if (name.equals(catToFind)) {
                return array.getJSONObject(i);
            }
        }
        return null;
    }

    private JSONObject findProduct(String productJSON, String productToFind) {
        JSONArray array = new JSONArray(productJSON);
        for (int i = 0; i < array.length(); i++){
            String name = array.getJSONObject(i).getString("name");
            if (name.equals(productToFind)) {
                return array.getJSONObject(i);
            }
        }
        return null;
    }
}
