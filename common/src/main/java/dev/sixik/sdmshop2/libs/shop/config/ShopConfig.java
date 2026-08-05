package dev.sixik.sdmshop2.libs.shop.config;

import ca.spottedleaf.yamlconfig.annotation.Adaptable;
import ca.spottedleaf.yamlconfig.annotation.Serializable;
import dev.sixik.sdmshop2.libs.platform.utils.repository.RepositoryType;

@Adaptable
public class ShopConfig {


    @Serializable(comment = """
            Магазин, открываемый клиентской клавишей "Open Shop".
            Оставьте пустым, чтобы отключить эту клавишу на стороне сервера.
            """)
    public String openShopKeybindShopId = "sdm:default";

    @Serializable(comment = """
            Значения:\s
            JSON - данные будут сохранены в папке "config/sdm/shop/shops/*",\s
            MONGODB - данные будут храниться в базе данных, которая автоматически синхронизирует данные между несколькими серверами.,\s
            CUSTOM - собственный тип сохранения. Значение по умолчанию: JSON
    """)
    public RepositoryType storageType = RepositoryType.JSON;

    @Serializable
    public MongoConfig mongodb = new MongoConfig();

    @Adaptable
    public static class MongoConfig {

        @Serializable(comment = """
                Строка подключения
                Локально: mongodb://127.0.0.1:27017/?replicaSet=rs0
                Облако: mongodb+srv://user:password@cluster.mongodb.net/
                """)
        public String uri = "mongodb://127.0.0.1:27017/?replicaSet=rs0";

        @Serializable(comment = """
                Имя базы данных
                """)
        public String database = "sdm_shop";

        @Serializable
        public String shopsCollection = "shops";

        @Serializable
        public String limiterOffersCollection = "limiter_offers";

        @Serializable
        public String limiterPlayersCollection = "limiter_players";

        @Serializable
        public String dailyStatsCollection = "daily_stats";

        @Serializable(comment = """
            Уникальное имя этого сервера (например: "survival_1", "lobby").
            Используется для защиты от эха, чтобы сервер не обновлял сам себя.
            Если оставить пустым, будет сгенерирован случайный UUID.
        """)
        public String serverName = "server_1";
    }
}
