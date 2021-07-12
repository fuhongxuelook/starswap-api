package org.starcoin.base;


public final class AccountResource {
    public final com.novi.serde.Bytes authentication_key;
    public final java.util.Optional<WithdrawCapabilityResource> withdrawal_capability;
    public final java.util.Optional<KeyRotationCapabilityResource> key_rotation_capability;
    public final EventHandle received_events;
    public final EventHandle sent_events;
    public final EventHandle accept_token_events;
    public final @com.novi.serde.Unsigned Long sequence_number;

    public AccountResource(com.novi.serde.Bytes authentication_key, java.util.Optional<WithdrawCapabilityResource> withdrawal_capability, java.util.Optional<KeyRotationCapabilityResource> key_rotation_capability, EventHandle received_events, EventHandle sent_events, EventHandle accept_token_events, @com.novi.serde.Unsigned Long sequence_number) {
        java.util.Objects.requireNonNull(authentication_key, "authentication_key must not be null");
        java.util.Objects.requireNonNull(withdrawal_capability, "withdrawal_capability must not be null");
        java.util.Objects.requireNonNull(key_rotation_capability, "key_rotation_capability must not be null");
        java.util.Objects.requireNonNull(received_events, "received_events must not be null");
        java.util.Objects.requireNonNull(sent_events, "sent_events must not be null");
        java.util.Objects.requireNonNull(accept_token_events, "accept_token_events must not be null");
        java.util.Objects.requireNonNull(sequence_number, "sequence_number must not be null");
        this.authentication_key = authentication_key;
        this.withdrawal_capability = withdrawal_capability;
        this.key_rotation_capability = key_rotation_capability;
        this.received_events = received_events;
        this.sent_events = sent_events;
        this.accept_token_events = accept_token_events;
        this.sequence_number = sequence_number;
    }

    public void serialize(com.novi.serde.Serializer serializer) throws com.novi.serde.SerializationError {
        serializer.increase_container_depth();
        serializer.serialize_bytes(authentication_key);
        TraitHelpers.serialize_option_WithdrawCapabilityResource(withdrawal_capability, serializer);
        TraitHelpers.serialize_option_KeyRotationCapabilityResource(key_rotation_capability, serializer);
        received_events.serialize(serializer);
        sent_events.serialize(serializer);
        accept_token_events.serialize(serializer);
        serializer.serialize_u64(sequence_number);
        serializer.decrease_container_depth();
    }

    public byte[] bcsSerialize() throws com.novi.serde.SerializationError {
        com.novi.serde.Serializer serializer = new com.novi.bcs.BcsSerializer();
        serialize(serializer);
        return serializer.get_bytes();
    }

    public static AccountResource deserialize(com.novi.serde.Deserializer deserializer) throws com.novi.serde.DeserializationError {
        deserializer.increase_container_depth();
        Builder builder = new Builder();
        builder.authentication_key = deserializer.deserialize_bytes();
        builder.withdrawal_capability = TraitHelpers.deserialize_option_WithdrawCapabilityResource(deserializer);
        builder.key_rotation_capability = TraitHelpers.deserialize_option_KeyRotationCapabilityResource(deserializer);
        builder.received_events = EventHandle.deserialize(deserializer);
        builder.sent_events = EventHandle.deserialize(deserializer);
        builder.accept_token_events = EventHandle.deserialize(deserializer);
        builder.sequence_number = deserializer.deserialize_u64();
        deserializer.decrease_container_depth();
        return builder.build();
    }

    public static AccountResource bcsDeserialize(byte[] input) throws com.novi.serde.DeserializationError {
        if (input == null) {
             throw new com.novi.serde.DeserializationError("Cannot deserialize null array");
        }
        com.novi.serde.Deserializer deserializer = new com.novi.bcs.BcsDeserializer(input);
        AccountResource value = deserialize(deserializer);
        if (deserializer.get_buffer_offset() < input.length) {
             throw new com.novi.serde.DeserializationError("Some input bytes were not read");
        }
        return value;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AccountResource other = (AccountResource) obj;
        if (!java.util.Objects.equals(this.authentication_key, other.authentication_key)) { return false; }
        if (!java.util.Objects.equals(this.withdrawal_capability, other.withdrawal_capability)) { return false; }
        if (!java.util.Objects.equals(this.key_rotation_capability, other.key_rotation_capability)) { return false; }
        if (!java.util.Objects.equals(this.received_events, other.received_events)) { return false; }
        if (!java.util.Objects.equals(this.sent_events, other.sent_events)) { return false; }
        if (!java.util.Objects.equals(this.accept_token_events, other.accept_token_events)) { return false; }
        if (!java.util.Objects.equals(this.sequence_number, other.sequence_number)) { return false; }
        return true;
    }

    public int hashCode() {
        int value = 7;
        value = 31 * value + (this.authentication_key != null ? this.authentication_key.hashCode() : 0);
        value = 31 * value + (this.withdrawal_capability != null ? this.withdrawal_capability.hashCode() : 0);
        value = 31 * value + (this.key_rotation_capability != null ? this.key_rotation_capability.hashCode() : 0);
        value = 31 * value + (this.received_events != null ? this.received_events.hashCode() : 0);
        value = 31 * value + (this.sent_events != null ? this.sent_events.hashCode() : 0);
        value = 31 * value + (this.accept_token_events != null ? this.accept_token_events.hashCode() : 0);
        value = 31 * value + (this.sequence_number != null ? this.sequence_number.hashCode() : 0);
        return value;
    }

    public static final class Builder {
        public com.novi.serde.Bytes authentication_key;
        public java.util.Optional<WithdrawCapabilityResource> withdrawal_capability;
        public java.util.Optional<KeyRotationCapabilityResource> key_rotation_capability;
        public EventHandle received_events;
        public EventHandle sent_events;
        public EventHandle accept_token_events;
        public @com.novi.serde.Unsigned Long sequence_number;

        public AccountResource build() {
            return new AccountResource(
                authentication_key,
                withdrawal_capability,
                key_rotation_capability,
                received_events,
                sent_events,
                accept_token_events,
                sequence_number
            );
        }
    }
}
