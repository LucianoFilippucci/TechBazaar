package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Product;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductIdNotFound;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.TelegramSender;
import it.lucianofilippucci.university.techbazaar.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TelegramSender telegramSender;

    @Transactional(readOnly = true)
    public ProductEntity getById(int id){ return productRepository.findProductById(id);}

    @Transactional(readOnly = true)
    public List<ProductEntity> getAllProducts() {return productRepository.findAll(); }

    @Transactional
    public void newProduct(ProductEntity pe) { productRepository.save(pe); }

    @Transactional
    public ResponseMessage<String> editProduct(Product product) {
        Optional<ProductEntity> productEntity = Optional.ofNullable(productRepository.findProductById(product.getId()));
        if(productEntity.isPresent()) {
            ProductEntity updatedEntity = productEntity.get();
            updatedEntity.setProductPrice(product.getPrice());
            updatedEntity.setProductName(product.getName());
            updatedEntity.setProductQuantity(product.getQty());
            updatedEntity.setStoreIdentifier(product.getStoreId());
            updatedEntity.setCategory(product.getCategory());
            updatedEntity.setProductDescription(product.getDescription());

            productRepository.save(updatedEntity);
            return new ResponseMessage<>("Product Saved Correctly");
        }
        return new ResponseMessage<>("Error Saving the product. The product Doesn't Exists");
    }

    @Transactional
    public ResponseMessage deleteProduct(int id, String storeId) throws NotAuthorizedException {
        try {
            Optional<ProductEntity> productEntity = Optional.ofNullable(productRepository.findProductById(id));
            if (productEntity.isPresent()) {
                ProductEntity removed = productEntity.get();
                if (!removed.getStoreIdentifier().equals(storeId))
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
}
