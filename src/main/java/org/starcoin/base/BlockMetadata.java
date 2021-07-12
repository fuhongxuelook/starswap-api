package org.starcoin.base;


public final class BlockMetadata {
    public final HashValue parent_hash;
    public final @com.novi.serde.Unsigned Long timestamp;
    public final AccountAddress author;
    public final com.novi.serde.Bytes auth_key_prefix;
    public final @com.novi.serde.Unsigned Long uncles;
    public final @com.novi.serde.Unsigned Long number;

    public BlockMetadata(HashValue parent_hash, @com.novi.serde.Unsigned Long timestamp, AccountAddress author, com.novi.serde.Bytes auth_key_prefix, @com.novi.serde.Unsigned Long uncles, @com.novi.serde.Unsigned Long number) {
        java.util.Objects.requireNonNull(parent_hash, "parent_hash must not be null");
        java.util.Objects.requireNonNull(timestamp, "timestamp must not be null");
        java.util.Objects.requireNonNull(author, "author must not be null");
        java.util.Objects.requireNonNull(auth_key_prefix, "auth_key_prefix must not be null");
        java.util.Objects.requireNonNull(uncles, "uncles must not be null");
        java.util.Objects.requireNonNull(number, "number must not be null");
        this.parent_hash = parent_hash;
        this.timestamp = timestamp;
        this.author = author;
        this.auth_key_prefix = auth_key_prefix;
        this.uncles = uncles;
        this.number = number;
    }

    public void serialize(com.novi.serde.Serializer serializer) throws com.novi.serde.SerializationError {
        serializer.increase_container_depth();
        parent_hash.serialize(serializer);
        serializer.serialize_u64(timestamp);
        author.serialize(serializer);
        serializer.serialize_bytes(auth_key_prefix);
        serializer.serialize_u64(uncles);
        serializer.serialize_u64(number);
        serializer.decrease_container_depth();
    }

    public byte[] bcsSerialize() throws com.novi.serde.SerializationError {
        com.novi.serde.Serializer serializer = new com.novi.bcs.BcsSerializer();
        serialize(serializer);
        return serializer.get_bytes();
    }

    public static BlockMetadata deserialize(com.novi.serde.Deserializer deserializer) throws com.novi.serde.DeserializationError {
        deserializer.increase_container_depth();
        Builder builder = new Builder();
        builder.parent_hash = HashValue.deserialize(deserializer);
        builder.timestamp = deserializer.deserialize_u64();
        builder.author = AccountAddress.deserialize(deserializer);
        builder.auth_key_prefix = deserializer.deserialize_bytes();
        builder.uncles = deserializer.deserialize_u64();
        builder.number = deserializer.deserialize_u64();
        deserializer.decrease_container_depth();
        return builder.build();
    }

    public static BlockMetadata bcsDeserialize(byte[] input) throws com.novi.serde.DeserializationError {
        if (input == null) {
             throw new com.novi.serde.DeserializationError("Cannot deserialize null array");
        }
        com.novi.serde.Deserializer deserializer = new com.novi.bcs.BcsDeserializer(input);
        BlockMetadata value = deserialize(deserializer);
        if (deserializer.get_buffer_offset() < input.length) {
             throw new com.novi.serde.DeserializationError("Some input bytes were not read");
        }
        return value;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BlockMetadata other = (BlockMetadata) obj;
        if (!java.util.Objects.equals(this.parent_hash, other.parent_hash)) { return false; }
        if (!java.util.Objects.equals(this.timestamp, other.timestamp)) { return false; }
        if (!java.util.Objects.equals(this.author, other.author)) { return false; }
        if (!java.util.Objects.equals(this.auth_key_prefix, other.auth_key_prefix)) { return false; }
        if (!java.util.Objects.equals(this.uncles, other.uncles)) { return false; }
        if (!java.util.Objects.equals(this.number, other.number)) { return false; }
        return true;
    }

    public int hashCode() {
        int value = 7;
        value = 31 * value + (this.parent_hash != null ? this.parent_hash.hashCode() : 0);
        value = 31 * value + (this.timestamp != null ? this.timestamp.hashCode() : 0);
        value = 31 * value + (this.author != null ? this.author.hashCode() : 0);
        value = 31 * value + (this.auth_key_prefix != null ? this.auth_key_prefix.hashCode() : 0);
        value = 31 * value + (this.uncles != null ? this.uncles.hashCode() : 0);
        value = 31 * value + (this.number != null ? this.number.hashCode() : 0);
        return value;
    }

    public static final class Builder {
        public HashValue parent_hash;
        public @com.novi.serde.Unsigned Long timestamp;
        public AccountAddress author;
        public com.novi.serde.Bytes auth_key_prefix;
        public @com.novi.serde.Unsigned Long uncles;
        public @com.novi.serde.Unsigned Long number;

        public BlockMetadata build() {
            return new BlockMetadata(
                parent_hash,
                timestamp,
                author,
                auth_key_prefix,
                uncles,
                number
            );
        }
    }
}
