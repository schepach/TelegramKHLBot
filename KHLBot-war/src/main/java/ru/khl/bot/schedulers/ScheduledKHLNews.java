package ru.khl.bot.schedulers;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.khl.bot.KHLBot;
import ru.khl.bot.constants.Constants;
import ru.khl.bot.model.Item;
import ru.khl.bot.model.MessageStructure;
import ru.khl.bot.model.WallItem;
import ru.khl.bot.utils.Connection;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by Alexey on 13.12.2016.
 */

public class ScheduledKHLNews extends TimerTask {

    private static final Logger LOGGER = Logger.getLogger(ScheduledKHLNews.class.getSimpleName());
    private static final String CHAT_ID = "@KHL_Info";

    @Override
    public void run() {
        try {
            MessageStructure messageStructure = Connection.getKHLNews(Constants.URL_KHL_INFO);
            if (messageStructure != null && messageStructure.getWallItems() != null) {
                for (WallItem wallItem : messageStructure.getWallItems()) {
                    if (wallItem.getItemList() != null && !wallItem.getItemList().isEmpty()) {
                        for (Item item : wallItem.getItemList()) {
                            if (!item.getLink().isEmpty()) {
                                LOGGER.info("NEWS_KHL URL = " + item.getLink());
                                new KHLBot().sendMessage(new SendMessage().setChatId(CHAT_ID).setText(item.getLink()));
                            }
                        }
                    }
                }
            }

        } catch (TelegramApiException | IOException | JSONException e) {
            LOGGER.info(Constants.UNEXPECTED_ERROR.concat(e.getMessage() + e));
        } catch (Exception ex) {
            LOGGER.info("EXCEPTION: " + ex.getMessage() + ex);
        }
    }
}