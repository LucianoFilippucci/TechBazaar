package it.lucianofilippucci.university.techbazaar.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramSender extends TelegramLongPollingBot {

    @Value("${telegram.botToken}")
    private String botToken;

    private String chatId = "236886052";

    @Override
    public void onUpdateReceived(Update update) {
        // Questo metodo verrà chiamato automaticamente quando viene ricevuto un aggiornamento da Telegram
        // Puoi inserire qui la logica per gestire gli aggiornamenti, ma in questo caso non invieremo messaggi in risposta
    }

    @Override
    public String getBotUsername() {
        // Restituisci il nome utente del tuo bot
        return "DevHelperBot";
    }

    @Override
    public String getBotToken() {
        // Restituisci il token di accesso al tuo bot
        return botToken;
    }

    public void sendMessageToUser(String messageText) {
        System.out.println(messageText);

        // Crea il messaggio da inviare
        SendMessage message = new SendMessage(chatId, messageText);

        try {
            // Invia il messaggio utilizzando il metodo execute
            execute(message);
        } catch (TelegramApiException e) {
            // Gestisci eventuali errori nell'invio del messaggio
            e.printStackTrace();
        }
    }
}

