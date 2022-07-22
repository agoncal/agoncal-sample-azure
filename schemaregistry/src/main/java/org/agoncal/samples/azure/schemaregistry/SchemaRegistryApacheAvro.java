package org.agoncal.samples.azure.schemaregistry;

import com.azure.core.credential.TokenCredential;
import com.azure.core.models.MessageContent;
import com.azure.core.util.serializer.TypeReference;
import com.azure.data.schemaregistry.SchemaRegistryAsyncClient;
import com.azure.data.schemaregistry.SchemaRegistryClientBuilder;
import com.azure.data.schemaregistry.apacheavro.SchemaRegistryApacheAvroSerializer;
import com.azure.data.schemaregistry.apacheavro.SchemaRegistryApacheAvroSerializerBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import org.agoncal.samples.azure.schemaregistry.generatedtestsources.PlayingCard;
import org.agoncal.samples.azure.schemaregistry.generatedtestsources.PlayingCardSuit;

/**
 * Sample to demonstrate using {@link SchemaRegistryApacheAvroSerializer} for serialization and deserialization of data.
 */
public class SchemaRegistryApacheAvro {
    /**
     * Main method to run this sample.
     *
     * @param args Ignore arguments.
     */
    public static void main(String[] args) {
        // Create AAD token credential
        TokenCredential tokenCredential = new DefaultAzureCredentialBuilder().build();

        // Create the schema registry async client
        SchemaRegistryAsyncClient schemaRegistryAsyncClient = new SchemaRegistryClientBuilder()
            .credential(tokenCredential)
            .fullyQualifiedNamespace("fights-kafka-agoncal.servicebus.windows.net")
            .buildAsyncClient();

        // Create the encoder instance by configuring it with the schema registry client and
        // enabling auto registering of new schemas
        SchemaRegistryApacheAvroSerializer serializer = new SchemaRegistryApacheAvroSerializerBuilder()
            .schemaRegistryClient(schemaRegistryAsyncClient)
            .schemaGroup("FightGroup")
            .avroSpecificReader(true)
            .buildSerializer();

        PlayingCard playingCard = new PlayingCard();
        playingCard.setCardValue(5);
        playingCard.setIsFaceCard(false);
        playingCard.setPlayingCardSuit(PlayingCardSuit.SPADES);

        // Serialize the playing card object and write to the output stream.
        MessageContent message = serializer.serialize(playingCard,
            TypeReference.createInstance(MessageContent.class));
    }
}
