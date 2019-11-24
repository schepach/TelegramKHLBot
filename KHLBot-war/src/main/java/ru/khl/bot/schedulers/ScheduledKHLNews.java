package ru.khl.bot.schedulers;

import common.vk.model.Item;
import common.vk.model.MessageStructure;
import common.vk.model.WallItem;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.khl.bot.KHLBot;
import ru.khl.bot.constants.Constants;
import ru.khl.bot.utils.Connection;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alexey on 13.12.2016.
 */

public class ScheduledKHLNews extends TimerTask {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private final String chatId = "@KHL_Info";

    @Override
    public void run() {
        try {
            MessageStructure messageStructure = Connection.getKHLNews(Constants.URL_KHL_INFO);
            if (messageStructure != null && messageStructure.getWallItems() != null) {
                for (WallItem wallItem : messageStructure.getWallItems()) {
                    if (wallItem.getItemList() != null && !wallItem.getItemList().isEmpty()) {
                        for (Item item : wallItem.getItemList()) {
                            if (item.getLink() != null && !item.getLink().isEmpty()) {
                                this.logger.log(Level.INFO, "NEWS_KHL URL = " + item.getLink());
                                new KHLBot().execute(new SendMessage().setChatId(chatId).setText(item.getLink()));
                            } else {
                                this.logger.log(Level.INFO, "NEWS_KHL URL is null or is empty");
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            this.logger.log(Level.SEVERE, "ScheduledKHLNews exception: ", ex);
        }
    }
}