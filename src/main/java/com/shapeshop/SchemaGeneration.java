
//OLD - replace with something like liquibase

//or add this to props :

//spring.jpa.hibernate.ddl-auto=none
//spring.jpa.properties.jakarta.persistence.schema-generation.scripts.action=create
//spring.jpa.properties.jakarta.persistence.schema-generation.scripts.create-target=schema_gen.sql
//spring.jpa.properties.jakarta.persistence.schema-generation.create-source=metadata


//package com.shapeshop;
//
//import com.shapeshop.entity.*;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.service.ServiceRegistry;
//import org.hibernate.tool.hbm2ddl.SchemaExport;
//import org.hibernate.tool.schema.TargetType;
//
//import java.util.EnumSet;
//import java.util.HashMap;
//import java.util.Map;
//
//public class SchemaGeneration {
//
//    /**
//     *
//     * MUST HAVE DB CONTAINER RUNNING FIRST!!!!
//     * ADD NEW ENTITIES TO THE CODE BELOW!!!!
//     *
//     * TODO is generating unnecesary tables :
//     *
//     *  orders_orders_item and product_orders_item;
//     *
//     *  also, alter statements in the schema_gen are breaking the script.
//     *
//     */
//    public static void main(String[] args) {
//
//        Map<String, String> settings = new HashMap<>();
//        settings.put("connection.driver_class", "com.mysql.jdbc.Driver");
//        settings.put("dialect", "org.hibernate.dialect.MySQLDialect");
//        settings.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/shapeshop");
//        settings.put("hibernate.connection.username", "root");
//        settings.put("hibernate.connection.password", "root");
//        settings.put("hibernate.show_sql", "true");
//        settings.put("hibernate.format_sql", "true");
//
//        ServiceRegistry serviceRegistry =
//                new StandardServiceRegistryBuilder().applySettings(settings).build();
//
//        MetadataSources metadata =
//                new MetadataSources(serviceRegistry);
//
//
//        metadata.addAnnotatedClass(CategoryEntity.class);
//        metadata.addAnnotatedClass(ProductCategoryEntity.class);
//        metadata.addAnnotatedClass(AddressEntity.class);
//        metadata.addAnnotatedClass(CompanyEntity.class);
//        metadata.addAnnotatedClass(CreditCardEntity.class);
//        metadata.addAnnotatedClass(OrderItemEntity.class);
//        metadata.addAnnotatedClass(OrderEntity.class);
//        metadata.addAnnotatedClass(ProductEntity.class);
//        metadata.addAnnotatedClass(UserEntity.class);
//
//        EnumSet<TargetType> enumSet = EnumSet.of(TargetType.SCRIPT);
//        SchemaExport schemaExport = new SchemaExport();
//
//        schemaExport.setDelimiter(";");
//
//        schemaExport.setOutputFile("schema_gen.sql");
//        schemaExport.execute(enumSet, SchemaExport.Action.BOTH, metadata.buildMetadata());
//    }
//
//    //??
//    public static void mainX(String[] args) {
//
//        Map<String, String> settings = new HashMap<>();
//        settings.put("connection.driver_class", "com.mysql.jdbc.Driver");
//        settings.put("dialect", "org.hibernate.dialect.MySQLDialect");
//        settings.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/mydatabase");
//        settings.put("hibernate.connection.username", "root");
//        settings.put("hibernate.connection.password", "root");
//        settings.put("hibernate.show_sql", "true");
//        settings.put("hibernate.format_sql", "true");
//
//        ServiceRegistry serviceRegistry =
//                new StandardServiceRegistryBuilder().applySettings(settings).build();
//
//        MetadataSources metadata =
//                new MetadataSources(serviceRegistry);
//        metadata.addAnnotatedClass(AddressEntity.class);
//        metadata.addAnnotatedClass(CompanyEntity.class);
//
//        EnumSet<TargetType> enumSet = EnumSet.of(TargetType.DATABASE);
//        SchemaExport schemaExport = new SchemaExport();
//
//        schemaExport.setOutputFile("myScript.sql");
//        schemaExport.execute(enumSet, SchemaExport.Action.BOTH, metadata.buildMetadata());
//    }
//}
