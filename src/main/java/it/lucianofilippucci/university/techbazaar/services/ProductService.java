package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.helpers.DropboxHelper;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Product;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductIdNotFound;
import it.lucianofilippucci.university.techbazaar.helpers.FilePathType;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.TelegramSender;
import it.lucianofilippucci.university.techbazaar.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TelegramSender telegramSender;

    @Autowired
    private DropboxHelper helper;

    @Transactional(readOnly = true)
    public ProductEntity getById(int id){ return productRepository.findByProductId(id);}

    @Transactional(readOnly = true)
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public ProductEntity newProduct(ProductEntity pe) { return productRepository.save(pe); }

    @Transactional
    public ResponseMessage<String> editProduct(ProductEntity product) {
        Optional<ProductEntity> productEntity = Optional.ofNullable(productRepository.findByProductId(product.getProductId()));
        if(productEntity.isPresent()) {

            ProductEntity updatedEntity = productEntity.get();
            updatedEntity.setProductPrice(product.getProductPrice());
            updatedEntity.setProductName(product.getProductName());
            updatedEntity.setProductQuantity(product.getProductQuantity());
            updatedEntity.setProductCategory(product.getProductCategory());
            updatedEntity.setProductDescription(product.getProductDescription());

            productRepository.save(updatedEntity);
            return new ResponseMessage<>("Product Saved Correctly");
        }
        return new ResponseMessage<>("Error Saving the product. The product Doesn't Exists");
    }

    @Transactional
    public ResponseMessage<String> deleteProduct(int id, String storeId) throws NotAuthorizedException {
        try {
            Optional<ProductEntity> productEntity = Optional.ofNullable(productRepository.findByProductId(id));
            if (productEntity.isPresent()) {
                ProductEntity removed = productEntity.get();
                if (!removed.getStore().getStoreId().equals(storeId))
                    throw new NotAuthorizedException();
                productRepository.delete(removed);

            }
        } catch (NotAuthorizedException nae) {
            telegramSender.sendMessageToUser("FRA ATTENTO QUALCUNO CERCA DI FARE COSE NON AUTORIZZARE COME ELIMINARE UN PRODOTTO SENZA ESSERNE PROPRIETARIO");
            return new ResponseMessage<>("No Permission to remove the Object.").setIsError(true);
        }
        return new ResponseMessage<>("Object Successfully Removed.").setIsError(false);
    }

    @Transactional
    public List<Product> getContainingKeywords(String keywords) throws ProductIdNotFound {
        ArrayList<Product> result;

        List<ProductEntity> entities = productRepository.findByProductNameContaining(keywords);
        result = convertFromJPAToSpecificEntity(entities);

        return result;
    }

    private ArrayList<Product> convertFromJPAToSpecificEntity(List<ProductEntity> productEntities) throws ProductIdNotFound {
        ArrayList<Product> products = new ArrayList<>();
        for(ProductEntity entity: productEntities) {
            products.add(new Product(entity));
        }
        return products;
    }

    public ResponseMessage<String> uploadFiles(MultipartFile[] files, int productId, String storeId) {
        return helper.upload(files, productId, FilePathType.STORE_PRODUCT, storeId);
    }
}
