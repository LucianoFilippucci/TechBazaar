package it.lucianofilippucci.university.techbazaar.helpers;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import it.lucianofilippucci.university.techbazaar.configurations.DropboxConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

@Component
public class DropboxHelper {
    private final DbxClientV2 dropboxClient;

    private final TelegramSender telegramSender;


    @Autowired
    public DropboxHelper(DropboxConfiguration config, TelegramSender sender) {
        dropboxClient = config.dropboxClient();
        this.telegramSender = sender;
    }

    public ResponseMessage<String> upload(MultipartFile[] files, int productId, FilePathType filePathType , String storeId) {
        try {
            for(MultipartFile file : files) {
                String originalFileName = file.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String newFileName = generateUniqueFileName() + extension;

                dropboxClient.files().uploadBuilder("/src/"+ filePathType + "/" + storeId + "/" + productId + "/" + newFileName)
                        .uploadAndFinish(file.getInputStream());
            }
            telegramSender.sendMessageToUser("OK bruh, file uploaded");

            return new ResponseMessage<>("OK, Everything went fine.").setIsError(false);
        } catch(IOException | DbxException exception) {
            telegramSender.sendMessageToUser("Bro Problema.");
            telegramSender.sendMessageToUser("Dropbox Helper -> upload()");
            telegramSender.sendMessageToUser(Arrays.toString(exception.getStackTrace()));
            return new ResponseMessage<>("Well. it's NOT OK, Something Failed. Check Telegram for More.");
        }
    }

    private String generateUniqueFileName() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS");
        String timestamp = currentTime.format(formatter);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return timestamp + uuid;
    }
}
