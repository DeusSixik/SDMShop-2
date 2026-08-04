package dev.sixik.sdmshop2.libs.shop.config;

import ca.spottedleaf.yamlconfig.annotation.Adaptable;
import ca.spottedleaf.yamlconfig.annotation.Serializable;
import dev.sixik.sdmshop2.libs.platform.utils.repository.RepositoryType;

@Adaptable
public class ShopConfig {


    @Serializable(comment = """
            Shop opened by the client "Open Shop" keybind.
            Leave empty to disable the keybind server-side.
            """)
    public String openShopKeybindShopId = "sdm:default";

    @Serializable(comment = """
            Values:\s
            JSON - The data will be saved in the folder "config/sdm/shop/shops/*",\s
            MONGODB - The data will be stored in a database that automatically synchronizes data between multiple servers.,\s
            CUSTOM - Self-written save type Default Value: JSON
    """)
    public RepositoryType storageType = RepositoryType.JSON;

    @Serializable
    public MongoConfig mongodb = new MongoConfig();

    @Adaptable
    public static class MongoConfig {

        @Serializable(comment = """
                Connection string
                Local: mongodb://127.0.0.1:27017/?replicaSet=rs0
                Cloud: mongodb+srv://user:password@cluster.mongodb.net/
                """)
        public String uri = "mongodb://127.0.0.1:27017/?replicaSet=rs0";

        @Serializable(comment = """
                Data base name
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
            A unique name for this server (eg: "survival_1", "lobby").
            Used for echo protection (to prevent the server from updating itself).
            If left blank, a random UUID will be generated.
        """)
        public String serverName = "server_1";
    }
}
