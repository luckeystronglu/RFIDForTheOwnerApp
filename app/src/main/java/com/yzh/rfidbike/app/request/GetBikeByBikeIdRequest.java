// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: GetBikeByBikeIdRequest.proto

package com.yzh.rfidbike.app.request;

public final class GetBikeByBikeIdRequest {
  private GetBikeByBikeIdRequest() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface GetBikeByBikeIdRequestMessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:GetBikeByBikeIdRequestMessage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional int64 bikeId = 1;</code>
     */
    long getBikeId();

    /**
     * <code>optional string session = 2;</code>
     */
    String getSession();
    /**
     * <code>optional string session = 2;</code>
     */
    com.google.protobuf.ByteString
        getSessionBytes();
  }
  /**
   * Protobuf type {@code GetBikeByBikeIdRequestMessage}
   */
  public  static final class GetBikeByBikeIdRequestMessage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:GetBikeByBikeIdRequestMessage)
      GetBikeByBikeIdRequestMessageOrBuilder {
    // Use GetBikeByBikeIdRequestMessage.newBuilder() to construct.
    private GetBikeByBikeIdRequestMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private GetBikeByBikeIdRequestMessage() {
      bikeId_ = 0L;
      session_ = "";
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private GetBikeByBikeIdRequestMessage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              bikeId_ = input.readInt64();
              break;
            }
            case 18: {
              String s = input.readStringRequireUtf8();

              session_ = s;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return GetBikeByBikeIdRequest.internal_static_GetBikeByBikeIdRequestMessage_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return GetBikeByBikeIdRequest.internal_static_GetBikeByBikeIdRequestMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              GetBikeByBikeIdRequestMessage.class, Builder.class);
    }

    public static final int BIKEID_FIELD_NUMBER = 1;
    private long bikeId_;
    /**
     * <code>optional int64 bikeId = 1;</code>
     */
    public long getBikeId() {
      return bikeId_;
    }

    public static final int SESSION_FIELD_NUMBER = 2;
    private volatile Object session_;
    /**
     * <code>optional string session = 2;</code>
     */
    public String getSession() {
      Object ref = session_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        session_ = s;
        return s;
      }
    }
    /**
     * <code>optional string session = 2;</code>
     */
    public com.google.protobuf.ByteString
        getSessionBytes() {
      Object ref = session_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        session_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (bikeId_ != 0L) {
        output.writeInt64(1, bikeId_);
      }
      if (!getSessionBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, session_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (bikeId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, bikeId_);
      }
      if (!getSessionBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, session_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof GetBikeByBikeIdRequestMessage)) {
        return super.equals(obj);
      }
      GetBikeByBikeIdRequestMessage other = (GetBikeByBikeIdRequestMessage) obj;

      boolean result = true;
      result = result && (getBikeId()
          == other.getBikeId());
      result = result && getSession()
          .equals(other.getSession());
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      hash = (37 * hash) + BIKEID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getBikeId());
      hash = (37 * hash) + SESSION_FIELD_NUMBER;
      hash = (53 * hash) + getSession().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static GetBikeByBikeIdRequestMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static GetBikeByBikeIdRequestMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static GetBikeByBikeIdRequestMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static GetBikeByBikeIdRequestMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static GetBikeByBikeIdRequestMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static GetBikeByBikeIdRequestMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static GetBikeByBikeIdRequestMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static GetBikeByBikeIdRequestMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static GetBikeByBikeIdRequestMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static GetBikeByBikeIdRequestMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(GetBikeByBikeIdRequestMessage prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code GetBikeByBikeIdRequestMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:GetBikeByBikeIdRequestMessage)
        GetBikeByBikeIdRequestMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return GetBikeByBikeIdRequest.internal_static_GetBikeByBikeIdRequestMessage_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return GetBikeByBikeIdRequest.internal_static_GetBikeByBikeIdRequestMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                GetBikeByBikeIdRequestMessage.class, Builder.class);
      }

      // Construct using com.yzh.rfidbike.app.request.GetBikeByBikeIdRequest.GetBikeByBikeIdRequestMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        bikeId_ = 0L;

        session_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return GetBikeByBikeIdRequest.internal_static_GetBikeByBikeIdRequestMessage_descriptor;
      }

      public GetBikeByBikeIdRequestMessage getDefaultInstanceForType() {
        return GetBikeByBikeIdRequestMessage.getDefaultInstance();
      }

      public GetBikeByBikeIdRequestMessage build() {
        GetBikeByBikeIdRequestMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public GetBikeByBikeIdRequestMessage buildPartial() {
        GetBikeByBikeIdRequestMessage result = new GetBikeByBikeIdRequestMessage(this);
        result.bikeId_ = bikeId_;
        result.session_ = session_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof GetBikeByBikeIdRequestMessage) {
          return mergeFrom((GetBikeByBikeIdRequestMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(GetBikeByBikeIdRequestMessage other) {
        if (other == GetBikeByBikeIdRequestMessage.getDefaultInstance()) return this;
        if (other.getBikeId() != 0L) {
          setBikeId(other.getBikeId());
        }
        if (!other.getSession().isEmpty()) {
          session_ = other.session_;
          onChanged();
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        GetBikeByBikeIdRequestMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (GetBikeByBikeIdRequestMessage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private long bikeId_ ;
      /**
       * <code>optional int64 bikeId = 1;</code>
       */
      public long getBikeId() {
        return bikeId_;
      }
      /**
       * <code>optional int64 bikeId = 1;</code>
       */
      public Builder setBikeId(long value) {
        
        bikeId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int64 bikeId = 1;</code>
       */
      public Builder clearBikeId() {
        
        bikeId_ = 0L;
        onChanged();
        return this;
      }

      private Object session_ = "";
      /**
       * <code>optional string session = 2;</code>
       */
      public String getSession() {
        Object ref = session_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          session_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string session = 2;</code>
       */
      public com.google.protobuf.ByteString
          getSessionBytes() {
        Object ref = session_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          session_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string session = 2;</code>
       */
      public Builder setSession(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        session_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string session = 2;</code>
       */
      public Builder clearSession() {
        
        session_ = getDefaultInstance().getSession();
        onChanged();
        return this;
      }
      /**
       * <code>optional string session = 2;</code>
       */
      public Builder setSessionBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        session_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:GetBikeByBikeIdRequestMessage)
    }

    // @@protoc_insertion_point(class_scope:GetBikeByBikeIdRequestMessage)
    private static final GetBikeByBikeIdRequestMessage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new GetBikeByBikeIdRequestMessage();
    }

    public static GetBikeByBikeIdRequestMessage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<GetBikeByBikeIdRequestMessage>
        PARSER = new com.google.protobuf.AbstractParser<GetBikeByBikeIdRequestMessage>() {
      public GetBikeByBikeIdRequestMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new GetBikeByBikeIdRequestMessage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<GetBikeByBikeIdRequestMessage> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<GetBikeByBikeIdRequestMessage> getParserForType() {
      return PARSER;
    }

    public GetBikeByBikeIdRequestMessage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GetBikeByBikeIdRequestMessage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_GetBikeByBikeIdRequestMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\034GetBikeByBikeIdRequest.proto\"@\n\035GetBik" +
      "eByBikeIdRequestMessage\022\016\n\006bikeId\030\001 \001(\003\022" +
      "\017\n\007session\030\002 \001(\tB6\n\034com.yzh.rfidbike.app" +
      ".requestB\026GetBikeByBikeIdRequestb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_GetBikeByBikeIdRequestMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_GetBikeByBikeIdRequestMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_GetBikeByBikeIdRequestMessage_descriptor,
        new String[] { "BikeId", "Session", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
