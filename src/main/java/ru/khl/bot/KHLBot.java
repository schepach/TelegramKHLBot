package ru.khl.bot;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.khl.bot.constants.Constants;
import ru.khl.bot.utils.BotHelper;

/**
 * Created by alexey on 01.11.16.
 */
public class KHLBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = Logger.getLogger(KHLBot.class.getSimpleName());

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {

            LOGGER.info("FirstName: " + message.getFrom().getFirstName());
            LOGGER.info("LastName: " + message.getFrom().getLastName());
            LOGGER.info("UserName: " + message.getFrom().getUserName());
            LOGGER.info("UserId: " + message.getFrom().getId());
            LOGGER.info("InputCommand: " + message.getText());

            sendMsg(message, BotHelper.checkUserText(message.getText().toUpperCase()));
        }
    }


    public String getBotUsername() {
        return "botname";
    }

    public String getBotToken() {
        return "bottoken";
    }

    private void sendMsg(Message message, String text) {
        if (text != null && !text.isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(text);
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setReplyToMessageId(message.getMessageId());
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException | JSONException ex) {
                LOGGER.info(Constants.UNEXPECTED_ERROR.concat(ex.getMessage() + ex));
            } catch (Exception ex) {
                LOGGER.info("EXCEPTION: " + ex.getMessage() + ex);
            }
        }
    }
}
