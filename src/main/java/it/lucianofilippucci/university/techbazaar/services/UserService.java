package it.lucianofilippucci.university.techbazaar.services;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserAddressEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.NotificationEntity;
import it.lucianofilippucci.university.techbazaar.helpers.*;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.ProductInPurchase;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.StoreNotFound;
import it.lucianofilippucci.university.techbazaar.repositories.UserAddressRepository;
import it.lucianofilippucci.university.techbazaar.repositories.UserRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    UserAddressRepository userAddressRepository;

    ProductService productService;

    NotificationRepository notificationRepository;

    NotificationService notificationService;

    DropboxHelper dropboxHelper;
    public UserService(DropboxHelper dropboxHelper, NotificationService notificationService, UserRepository userRepository, UserAddressRepository userAddressRepository, ProductService productService, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.productService = productService;
        this.notificationRepository = notificationRepository;
        this.dropboxHelper = dropboxHelper;
        this.notificationService = notificationService;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsernameAndPassword(username, "@Verizon64_");
        if(user == null) {
            throw new UsernameNotFoundException("Utente non trovato: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    @Transactional
    public Optional<UserEntity> getById(int userId) {
       return this.userRepository.findUserEntityByUserId(userId);
    }

    @Transactional
    public UserEntity newUser(UserEntity user) {
        user.setCartId(Helpers.GenerateUID());
        UserEntity userEntity =  userRepository.save(user);
        NotificationEntity entity = new NotificationEntity();
        entity.setNotificationList(new ArrayList<>());
        entity.setUserId(entity.getUserId());
        this.notificationRepository.save(entity);
        return userEntity;
    }

    @Transactional(readOnly = true)
    public UserEntity getUserByCart(String cartId) {
        return userRepository.findUserEntityByCartId(cartId);
    }

    @Transactional(readOnly = true)
    public Optional<UserAddressEntity> getAddressByAddressId(int id) { return userAddressRepository.findById(id); }

    public ResponseEntity<List<ProductEntity>> getStoreProducts(int storeId) throws StoreNotFound {
        Optional<UserEntity> user = this.userRepository.findUserEntityByUserId(storeId);

        if(user.isPresent()) {
            List<ProductEntity> products = this.productService.getByStoreId(user.get());
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            throw new StoreNotFound();
        }
    }


    public void setStoreOrder(int storeId, List<ProductInPurchase> products, String orderId) {

    }

    public int getTotalNotifications(int userId) throws ObjectNotFoundException {
        return this.notificationService.totalUnreadNotifications(userId);
    }

    public ResponseMessage<String> uploadThumbnail(MultipartFile[] multipartfile, int userId) {
       DropboxResponse dropboxResponse = this.dropboxHelper.upload(multipartfile, userId, FilePathType.USER);

       if(!dropboxResponse.isError()) {
           Optional<UserEntity> user = this.userRepository.findUserEntityByUserId(userId);
           if(user.isEmpty()) return new ResponseMessage<>("UserNotFound").setIsError(true);
           user.get().setPath(dropboxResponse.message().get(0));
           this.userRepository.save(user.get());
           return new ResponseMessage<>("thumbnail available at: " + dropboxResponse.message().get(0));
       }
       return new ResponseMessage<>("Error -> " + dropboxResponse.message()).setIsError(true);
    }

}
