package ru.khl.bot.bean.scheduler;

import common.vk.model.Item;
import common.vk.model.MessageStructure;
import common.vk.model.WallItem;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.khl.bot.KHLBot;
import ru.khl.bot.constants.Constants;
import ru.khl.bot.utils.Connection;

import javax.ejb.Singleton;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alexey on 13.12.2016.
 */
@Singleton
public class NewsScheduler {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private final String chatId = "@khl_unofficial";

    public void run() {
        logger.log(Level.SEVERE, "Start NewsScheduler...");

        try {
            MessageStructure messageStructure = Connection.getKHLNews(Constants.URL_KHL_INFO);

            if (messageStructure == null
                    || messageStructure.getWallItems() == null
                    || messageStructure.getWallItems().isEmpty())
                return;

            for (WallItem wallItem : messageStructure.getWallItems()) {

                if (wallItem.getItemList() == null || wallItem.getItemList().isEmpty())
                    continue;

                for (Item item : wallItem.getItemList()) {
                    if (item.getLink() == null || item.getLink().isEmpty()) {
                        this.logger.log(Level.SEVERE, "KHL news url is null or is empty");
                    }
                    this.logger.log(Level.INFO, "KHL news url - {0}", new Object[]{item.getLink()});
                    new KHLBot().execute(new SendMessage().setChatId(chatId).setText(item.getLink()));
                }
            }

        } catch (Exception ex) {
            this.logger.log(Level.SEVERE, "NewsScheduler exception: ", ex);
        }
    }
}