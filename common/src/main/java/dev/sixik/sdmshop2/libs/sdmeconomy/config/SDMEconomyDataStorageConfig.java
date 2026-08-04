package dev.sixik.sdmshop2.libs.sdmeconomy.config;

import ca.spottedleaf.yamlconfig.annotation.Adaptable;
import ca.spottedleaf.yamlconfig.annotation.Serializable;
import dev.sixik.sdmshop2.libs.platform.utils.repository.RepositoryType;

@Adaptable
public class SDMEconomyDataStorageConfig {

    @Serializable(comment = """
            Values:\s
            JSON - The data will be saved in the folder "config/sdm/economy/*",\s
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
        public String database = "sdm_economy";

        @Serializable
        public String accountsCollection = "accounts";

        @Serializable
        public String currenciesCollection = "currencies";

        @Serializable(comment = """
            A unique name for this server (eg: "survival_1", "lobby").
            Used for echo protection (to prevent the server from updating itself).
            If left blank, a random UUID will be generated.
        """)
        public String serverName = "server_1";
    }
}
