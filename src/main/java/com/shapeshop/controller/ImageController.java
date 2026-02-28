package com.shapeshop.controller;

import com.shapeshop.ErrorUtil;
import com.shapeshop.ShapeShopException;
import com.shapeshop.entity.ProductEntity;
import com.shapeshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

@CrossOrigin
@RestController
public class ImageController {

    @Autowired
    private ProductRepository productRepository;

//    @CrossOrigin
    @RequestMapping(value = "/{companyName}/uploadfile/{productId}", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file,
                           HttpServletResponse responseX,
                           @PathVariable("productId") String productId,
                           @PathVariable("companyName") String companyName
                           ) {

        try {
            writeFileToFileSystem(file, companyName);

            ProductEntity p = productRepository.findById(Long.parseLong(productId));
            p.setImageFilename(file.getOriginalFilename());
            productRepository.save(p);

        } catch (ShapeShopException e) {
            e.printStackTrace();

            ResponseEntity response = ErrorUtil.getResponseEntity(e);
            return response;

        } catch (Throwable t) {
            t.printStackTrace();
            return new ResponseEntity<>("internal server error 3", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>( HttpStatus.OK);
    }

    /**
     * Write a file onto the server file system
     *
     * @param file file send over REST
     * @param companyName mandate
     * @return java File object created on the local system
     * @throws ShapeShopException
     */
    private static File writeFileToFileSystem(MultipartFile file, String companyName) throws ShapeShopException {

        File createdFile;
        try {
            File folderPath = new ClassPathResource("static\\images\\" + companyName + "\\").getFile();

            if (!folderPath.exists()) {
                throw new ShapeShopException("Cannot find image src folder 'static\\images\\" + companyName + "\\ '", ShapeShopException.ErrorType.IMAGE_SRC_FOLDER_NOT_FOUND);
            }

            createdFile = new File(folderPath.getPath() + "\\" + file.getOriginalFilename());
            createdFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(createdFile);

            fos.write(file.getBytes());
            fos.close();

        }catch(Exception e) {
            e.printStackTrace();

            throw new ShapeShopException("Error saving image file " + file.getOriginalFilename(), ShapeShopException.ErrorType.IMAGE_SAVE_ERROR, e);
        }
        return createdFile;
    }
}
