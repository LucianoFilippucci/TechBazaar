package it.lucianofilippucci.university.techbazaar.helpers;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.dropbox.core.v2.sharing.SharedLinkSettings;
import it.lucianofilippucci.university.techbazaar.configurations.DropboxConfiguration;
import it.lucianofilippucci.university.techbazaar.services.AuctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static it.lucianofilippucci.university.techbazaar.helpers.FilePathType.*;

@Component
public class DropboxHelper {
    private final DbxClientV2 dropboxClient;

    private final Logger logger = LoggerFactory.getLogger(AuctionService.class);



    @Autowired
    public DropboxHelper(DropboxConfiguration config, TelegramSender sender) {
        dropboxClient = config.dropboxClient();
    }

    public DropboxResponse upload(MultipartFile[] files, int productId, FilePathType filePathType , int storeId) {
        return this.execute(files, filePathType, -1, storeId, productId);
    }

    public DropboxResponse upload(MultipartFile[] file, int userId, FilePathType filePathType) {
        return this.execute(file, filePathType, userId, -1, -1);
    }

    public DropboxResponse upload(MultipartFile[] file, FilePathType filePathType, int userId, int storeId, int productId) {
        return this.execute(file, filePathType, userId, storeId, productId);
    }

    private DropboxResponse execute(MultipartFile[] multipartFiles, FilePathType filePathType, int userId, int storeId, int productId) {
        ArrayList<String> filePaths = new ArrayList<>();
        try {
            logger.info("Connected as: " + dropboxClient.users().getCurrentAccount().getName());

            switch (filePathType) {
                case STORE_PRODUCT -> {
                    // this mean that a Store is trying to upload product Photos.
                    for(MultipartFile file : multipartFiles) {
                        String originalFileName = file.getOriginalFilename();
                        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                        String newFileName = generateUniqueFileName() + extension;
                        String filePath = "/src/"+ filePathType + "/" + storeId + "/" + productId + "/" + newFileName;
                        dropboxClient.files().uploadBuilder(filePath)
                                .uploadAndFinish(file.getInputStream());
                        Metadata metadata = dropboxClient.files().getMetadata(filePath);
                        filePaths.add(dropboxClient.sharing().createSharedLinkWithSettings(metadata.getPathDisplay(), SharedLinkSettings.newBuilder().build()).getUrl());
                    }
                    return new DropboxResponse(filePaths, false);
                }
                case PRODUCT_REVIEW -> {
                    for(MultipartFile file : multipartFiles) {
                        String originalFileName = file.getOriginalFilename();
                        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                        String newFileName = generateUniqueFileName() + extension;
                        String filePath = "/src/"+ filePathType + "/" + storeId + "/" + productId + "/" + userId + "/" + newFileName;
                        dropboxClient.files().uploadBuilder(filePath)
                                .uploadAndFinish(file.getInputStream());
                        Metadata metadata = dropboxClient.files().getMetadata(filePath);
                        filePaths.add(dropboxClient.sharing().createSharedLinkWithSettings(metadata.getPathDisplay(), SharedLinkSettings.newBuilder().build()).getUrl());
                    }
                    return new DropboxResponse(filePaths, false);
                }
                case USER -> {
                    String originalFileName = multipartFiles[0].getOriginalFilename();
                    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String newFileName = generateUniqueFileName() + extension;
                    String filePath = "/src/" + filePathType + "/" + userId + "/thumbnail/" + newFileName;
                    dropboxClient.files().uploadBuilder(filePath).uploadAndFinish(multipartFiles[0].getInputStream());
                    Metadata metadata = dropboxClient.files().getMetadata(filePath);
                    filePaths.add(dropboxClient.sharing().createSharedLinkWithSettings(metadata.getPathDisplay(), SharedLinkSettings.newBuilder().build()).getUrl());
                    return new DropboxResponse(filePaths, false);
                }
                default -> new DropboxResponse(List.of("InvalidFilePathType"), true);
            }
        } catch (UploadErrorException e) {
            return new DropboxResponse(List.of("UploadErrorException"), true);
        } catch (IOException e) {
            return new DropboxResponse(List.of("IOException"), true);
        } catch (GetMetadataErrorException e) {
            return new DropboxResponse(List.of("GetMetadataErrorException"), true);
        } catch (DbxException e) {
            e.printStackTrace();
            return new DropboxResponse(List.of("GenericDropboxException"), true);
        }
        return null;
    }

    private String generateUniqueFileName() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS");
        String timestamp = currentTime.format(formatter);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return timestamp + uuid;
    }

    private boolean checkForViruses() {
        //TODO: implement symantec av
        return false;
    }

}
