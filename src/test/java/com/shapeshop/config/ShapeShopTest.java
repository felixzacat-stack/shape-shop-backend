package com.shapeshop.config;

import com.shapeshop.config.mockdata.AnniesArtSupplies;
import com.shapeshop.config.mockdata.Carlscafe;
import com.shapeshop.entity.AddressEntity;
import com.shapeshop.entity.CompanyEntity;
import com.shapeshop.entity.CreditCardEntity;
import com.shapeshop.entity.UserEntity;
import com.shapeshop.model.UserRole;
import com.shapeshop.repository.*;
import com.shapeshop.security.PasswordUtils;
import com.shapeshop.service.CategoryService;
import com.shapeshop.service.ProductService;
import org.aspectj.lang.annotation.Before;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.junit.Before;
//import org.junit.runner.RunWith;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import jakarta.persistence.EntityManager;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//import static org.junit.jupiter.assertNotNull;


@EnableWebMvc
//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class ShapeShopTest {

    String defaultCompany = "carlscafe";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private PasswordUtils passwordValidationService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    protected MockMvc mvc;

    protected StatusResultMatchers matcher = MockMvcResultMatchers.status();

    @BeforeEach
    public void before() throws Exception{

        clearDatabase();

        System.out.println("*************************");
        System.out.println("");
        System.out.println("");
        System.out.println("is this before each test?? or each class??");
        System.out.println("");
        System.out.println("");
        System.out.println("*************************");
        System.out.println("-->>> create some companies ! ");

        companyRepository.save(new CompanyEntity("carlscafe"));
        companyRepository.save(new CompanyEntity("anniesart"));

        AddressEntity a = new AddressEntity("Bob", "Bobby street 12", "41412", "+(09)928423444", "bob@gmail.com");
        addressRepository.save(a);
        AddressEntity a2 = new AddressEntity("Jane", "1 Baker st", "62344", "+(09)34534444", "jane@gmail.com");
        addressRepository.save(a2);

        CreditCardEntity cc = new CreditCardEntity("xxxx-xxxx-xxxx-1234", "22/22", "Bob", "VISA");
        creditCardRepository.save(cc);
        CreditCardEntity cc2 = new CreditCardEntity("xxx-xxx-xxxx-6789", "12/24", "Jane", "MASTERCARD");
        creditCardRepository.save(cc2);

        AnniesArtSupplies.createCategories(categoryRepository, companyRepository);
        Carlscafe.createCategories(categoryRepository, companyRepository, categoryService);
        Carlscafe.createProducts(productRepository, companyRepository, categoryRepository, productService);
        AnniesArtSupplies.createProducts(productRepository, companyRepository, categoryRepository, productService);
        Carlscafe.createOrders(orderRepository, companyRepository, productRepository, creditCardRepository, addressRepository, orderItemRepository);
        AnniesArtSupplies.createOrders(orderRepository, companyRepository, productRepository, creditCardRepository, addressRepository, orderItemRepository);

        String fpasss = passwordValidationService.encryptPassword("foo");
        String apasss = passwordValidationService.encryptPassword("admin");
        String upasss = passwordValidationService.encryptPassword("user");

        System.out.println("-->>> create some users ! admin password = " + apasss);
        System.out.println("-->>> create some users ! user password = " + upasss);
        System.out.println("-->>> create some users ! foo password = " + fpasss);

//			TODO users should be bound to company
        userRepository.save(new UserEntity(UserRole.ROLE_ADMIN, "admin", apasss));
        userRepository.save(new UserEntity(UserRole.ROLE_USER, "user", upasss));
        userRepository.save(new UserEntity(UserRole.ROLE_USER, "foo", fpasss));
    }


    @Autowired
    EntityManager entitymanager;

    @Transactional
    public void clearDatabase() throws SQLException {

        //clear out data
        orderItemRepository.deleteAll();
        userRepository.deleteAll();
        orderRepository.deleteAll();
        productCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        productRepository.deleteAll();
        creditCardRepository.deleteAll();
        addressRepository.deleteAll();
        companyRepository.deleteAll();

        //reset sequences
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        try (Connection dbConnection = dataSource.getConnection()) {
            try (Statement statement = dbConnection.createStatement()) {
                String s = "ALTER TABLE category ALTER COLUMN ID RESTART WITH 1";
                statement.execute(s);
                String s2 = "ALTER TABLE product ALTER COLUMN ID RESTART WITH 1";
                statement.execute(s2);
                String s3 = "ALTER TABLE credit_card ALTER COLUMN cc_id RESTART WITH 1";
                statement.execute(s3);
                String s4 = "ALTER TABLE company ALTER COLUMN id RESTART WITH 1";
                statement.execute(s4);
                String s5 = "ALTER TABLE orders ALTER COLUMN id RESTART WITH 1";
                statement.execute(s5);
                //add as needed for other tables
            }
        }

        System.out.println("****************************");
        System.out.println("");
        System.out.println("cleared database");
        System.out.println("");
        System.out.println("****************************");
    }

    /**
     * UTILITY METHODS :
     */
    protected JSONArray extractJSONArrayFromResponse(ResultActions resultActions) throws UnsupportedEncodingException {
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JSONArray recievedArray = new JSONArray(contentAsString);
        return recievedArray;
    }

    protected JSONObject extractJSONObjectFromResponse(ResultActions resultActions) throws UnsupportedEncodingException {
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JSONObject recievedObj = new JSONObject(contentAsString);
        return recievedObj;
    }

    protected JSONArray extractJSONArrayFromFileName(String filePath) throws FileNotFoundException {
        String newOrderJSON = getString(filePath);

        JSONArray obj = new JSONArray(newOrderJSON);
        return obj;
    }

    protected JSONObject extractJSONObjectFromFileName(String filePath) throws FileNotFoundException {
        String newOrderJSON = getString(filePath);
        JSONObject obj = new JSONObject(newOrderJSON);
        return obj;
    }

    private String getString(String filePath) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        while (in.hasNextLine()) {
            String s = in.nextLine();
            sb.append(s);
        }
        in.close();
        String json = sb.toString();
        return json;
    }

    public String authenticate(String name, String password) throws Exception {
        String requestJson = "{\"username\":\"" + name + "\",\"password\": \"" + password + "\"}";

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType("application/json").content(requestJson)).andExpect(matcher.isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        System.out.println("contentAsString " + contentAsString);

        String token = contentAsString.substring(8, contentAsString.length() - 2);

        assertNotNull(token);
        return token;
    }

//            mvc.perform(MockMvcRequestBuilders.delete("/carlscafe/products/4")
//                    .header("Authorization", "Bearer " + token).contentType("application/json")).andExpect(matcher.is(200));


    protected void deleteProductOverHTTP(String token, String productId) throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/" + defaultCompany + "/products/" + productId)
                .header("Authorization", "Bearer " + token).contentType("application/json")).andExpect(matcher.is(200));
    }

    protected void deleteCategoryOverHTTP(String token, String id) throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/" + defaultCompany + "/categories/" + id )
                .header("Authorization", "Bearer " + token).contentType("application/json")).andExpect(matcher.is(200));
//                .content(createCategoryJSON))

    }

    protected String getCategoriesOverHTTP() throws Exception {
        ResultActions resultActions2 = mvc.perform(MockMvcRequestBuilders.get("/" + defaultCompany + "/categories").contentType("application/json")
        ).andExpect(matcher.is(200));

        JSONArray jsonArray = extractJSONArrayFromResponse(resultActions2);

        return jsonArray.toString();
    }

    protected String getProductsOverHTTP() throws Exception {
        ResultActions resultActions2 = mvc.perform(MockMvcRequestBuilders.get("/" + defaultCompany + "/products").contentType("application/json")
        ).andExpect(matcher.is(200));

        JSONArray jsonArray = extractJSONArrayFromResponse(resultActions2);

        return jsonArray.toString();
    }

    protected void createCategoryOverHTTP(String token, String createCategoryJSON) throws Exception {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/" + defaultCompany + "/categories")
                .header("Authorization", "Bearer " + token).contentType("application/json")
                .content(createCategoryJSON)).andExpect(matcher.is(200));

        MvcResult mvcResult = resultActions.andReturn();
        resultActions.andExpect(matcher.is(200));
    }

    protected void createProductOverHTTP(String token, String createProductJSON, String cat) throws Exception {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/" + defaultCompany + "/" + cat + "/products")
                .header("Authorization", "Bearer " + token).contentType("application/json")
                .content(createProductJSON));

        MvcResult mvcResult = resultActions.andReturn();
        resultActions.andExpect(matcher.is(200));
    }

    protected void updateCategoryOverHTTP(String token, String updateCategoryJSON, String catId) throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/" + defaultCompany + "/categories/" + catId)
                .header("Authorization", "Bearer " + token).contentType("application/json")
                .content(updateCategoryJSON)).andExpect(matcher.is(200));

    }


}
