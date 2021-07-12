package org.starcoin.base;


public final class EventFilter {
    public final java.util.Optional<@com.novi.serde.Unsigned Long> from_block;
    public final java.util.Optional<@com.novi.serde.Unsigned Long> to_block;
    public final java.util.List<EventKey> event_keys;
    public final java.util.Optional<@com.novi.serde.Unsigned Long> limit;

    public EventFilter(java.util.Optional<@com.novi.serde.Unsigned Long> from_block, java.util.Optional<@com.novi.serde.Unsigned Long> to_block, java.util.List<EventKey> event_keys, java.util.Optional<@com.novi.serde.Unsigned Long> limit) {
        java.util.Objects.requireNonNull(from_block, "from_block must not be null");
        java.util.Objects.requireNonNull(to_block, "to_block must not be null");
        java.util.Objects.requireNonNull(event_keys, "event_keys must not be null");
        java.util.Objects.requireNonNull(limit, "limit must not be null");
        this.from_block = from_block;
        this.to_block = to_block;
        this.event_keys = event_keys;
        this.limit = limit;
    }

    public void serialize(com.novi.serde.Serializer serializer) throws com.novi.serde.SerializationError {
        serializer.increase_container_depth();
        TraitHelpers.serialize_option_u64(from_block, serializer);
        TraitHelpers.serialize_option_u64(to_block, serializer);
        TraitHelpers.serialize_vector_EventKey(event_keys, serializer);
        TraitHelpers.serialize_option_u64(limit, serializer);
        serializer.decrease_container_depth();
    }

    public byte[] bcsSerialize() throws com.novi.serde.SerializationError {
        com.novi.serde.Serializer serializer = new com.novi.bcs.BcsSerializer();
        serialize(serializer);
        return serializer.get_bytes();
    }

    public static EventFilter deserialize(com.novi.serde.Deserializer deserializer) throws com.novi.serde.DeserializationError {
        deserializer.increase_container_depth();
        Builder builder = new Builder();
        builder.from_block = TraitHelpers.deserialize_option_u64(deserializer);
        builder.to_block = TraitHelpers.deserialize_option_u64(deserializer);
        builder.event_keys = TraitHelpers.deserialize_vector_EventKey(deserializer);
        builder.limit = TraitHelpers.deserialize_option_u64(deserializer);
        deserializer.decrease_container_depth();
        return builder.build();
    }

    public static EventFilter bcsDeserialize(byte[] input) throws com.novi.serde.DeserializationError {
        if (input == null) {
             throw new com.novi.serde.DeserializationError("Cannot deserialize null array");
        }
        com.novi.serde.Deserializer deserializer = new com.novi.bcs.BcsDeserializer(input);
        EventFilter value = deserialize(deserializer);
        if (deserializer.get_buffer_offset() < input.length) {
             throw new com.novi.serde.DeserializationError("Some input bytes were not read");
        }
        return value;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EventFilter other = (EventFilter) obj;
        if (!java.util.Objects.equals(this.from_block, other.from_block)) { return false; }
        if (!java.util.Objects.equals(this.to_block, other.to_block)) { return false; }
        if (!java.util.Objects.equals(this.event_keys, other.event_keys)) { return false; }
        if (!java.util.Objects.equals(this.limit, other.limit)) { return false; }
        return true;
    }

    public int hashCode() {
        int value = 7;
        value = 31 * value + (this.from_block != null ? this.from_block.hashCode() : 0);
        value = 31 * value + (this.to_block != null ? this.to_block.hashCode() : 0);
        value = 31 * value + (this.event_keys != null ? this.event_keys.hashCode() : 0);
        value = 31 * value + (this.limit != null ? this.limit.hashCode() : 0);
        return value;
    }

    public static final class Builder {
        public java.util.Optional<@com.novi.serde.Unsigned Long> from_block;
        public java.util.Optional<@com.novi.serde.Unsigned Long> to_block;
        public java.util.List<EventKey> event_keys;
        public java.util.Optional<@com.novi.serde.Unsigned Long> limit;

        public EventFilter build() {
            return new EventFilter(
                from_block,
                to_block,
                event_keys,
                limit
            );
        }
    }
}
