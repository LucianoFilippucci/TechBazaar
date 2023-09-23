package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.ProductResourceEntity;
import it.lucianofilippucci.university.techbazaar.helpers.*;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Product;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.repositories.ProductRepository;
import it.lucianofilippucci.university.techbazaar.services.mongodb.ProductResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private TelegramSender telegramSender;

    private final DropboxHelper helper;

    private ProductResourcesService productResourcesService;

    public ProductService(ProductRepository productRepository, DropboxHelper dropboxHelper, ProductResourcesService productResourcesService) {
        this.productRepository = productRepository;
        this.helper = dropboxHelper;
        this.productResourcesService = productResourcesService;
    }

    @Transactional(readOnly = true)
    public ProductEntity getById(int id){ return productRepository.findByProductId(id);}

    @Transactional(readOnly = true)
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public ProductEntity newProduct(ProductEntity pe) {
        ProductEntity en = productRepository.save(pe);
        this.productResourcesService.newResource(en.getProductId());
        return en;
    }

    @Transactional
    public ResponseMessage<String> editProduct(int productId, int storeId, float price, String name, String description, String category, int qty) throws NotAuthorizedException {
        Optional<ProductEntity> productEntity = Optional.ofNullable(productRepository.findByProductId(productId));
        if(productEntity.isPresent()) {
            ProductEntity updatedEntity = productEntity.get();
            if(updatedEntity.getStore().getUserId() == storeId) {
                if(price != 0.0)updatedEntity.setProductPrice(price);
                if(name != null)updatedEntity.setProductName(name);
                updatedEntity.setProductQuantity(qty);
                if(category != null)updatedEntity.setProductCategory(category);
                if(description != null)updatedEntity.setProductDescription(description);
                productRepository.save(updatedEntity);
                return new ResponseMessage<>("Product Saved Correctly");
            } else {
                throw new NotAuthorizedException();
            }

        }
        return new ResponseMessage<>("The product Doesn't Exists");
    }

    @Transactional
    public boolean editProductQtyAfterSelling(int productId, int newQty, int soldQty) throws ObjectNotFoundException {
        Optional<ProductEntity> product = Optional.ofNullable(productRepository.findByProductId(productId));
        if(product.isPresent()) {
            ProductEntity entity = product.get();
            entity.setProductQuantity(newQty);
            entity.setProductTotalSelt(entity.getProductTotalSelt() + soldQty);
            return true;
        } else throw new ObjectNotFoundException();
    }

    @Transactional
    public ResponseMessage<String> deleteProduct(int id, int storeId) throws NotAuthorizedException {
        try {
            Optional<ProductEntity> productEntity = Optional.ofNullable(productRepository.findByProductId(id));
            if (productEntity.isPresent()) {
                ProductEntity removed = productEntity.get();
                if (!(removed.getStore().getUserId() == storeId))
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
    public List<Product> getContainingKeywords(String keywords) throws ObjectNotFoundException {
        ArrayList<Product> result;

        List<ProductEntity> entities = productRepository.findByProductNameContaining(keywords);
        result = convertFromJPAToSpecificEntity(entities);

        return result;
    }

    private ArrayList<Product> convertFromJPAToSpecificEntity(List<ProductEntity> productEntities) throws ObjectNotFoundException {
        ArrayList<Product> products = new ArrayList<>();
        for(ProductEntity entity: productEntities) {
            products.add(new Product(entity));
        }
        return products;
    }

    public ResponseMessage<String> uploadFiles(MultipartFile[] files, int productId, int storeId) {
        DropboxResponse response = helper.upload(files, productId, FilePathType.STORE_PRODUCT, storeId);
        if(response.isError()) return new ResponseMessage<>("Error ->" + response.message()).setIsError(true);
        if(this.productResourcesService.newResource(response.message(), productId))
            return new ResponseMessage<>("OK").setIsError(false);
        return new ResponseMessage<>("GenericError -> ProductService.uploadFiles()");
    }

    public List<ProductEntity> getByStoreId(UserEntity store) {
        return this.productRepository.findAllByStore(store);
    }
}
