// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: GetBikeInsuranceCashRequest.proto

package com.yzh.rfidbike.app.request;

public final class GetBikeInsuranceCashRequest {
  private GetBikeInsuranceCashRequest() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface GetBikeInsuranceCashRequestMessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:GetBikeInsuranceCashRequestMessage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string session = 1;</code>
     */
    String getSession();
    /**
     * <code>optional string session = 1;</code>
     */
    com.google.protobuf.ByteString
        getSessionBytes();

    /**
     * <pre>
     *车牌号
     * </pre>
     *
     * <code>optional string plateNumber = 2;</code>
     */
    String getPlateNumber();
    /**
     * <pre>
     *车牌号
     * </pre>
     *
     * <code>optional string plateNumber = 2;</code>
     */
    com.google.protobuf.ByteString
        getPlateNumberBytes();
  }
  /**
   * <pre>
   *通过车牌获取车辆保险提现金额和提现地址
   * </pre>
   *
   * Protobuf type {@code GetBikeInsuranceCashRequestMessage}
   */
  public  static final class GetBikeInsuranceCashRequestMessage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:GetBikeInsuranceCashRequestMessage)
      GetBikeInsuranceCashRequestMessageOrBuilder {
    // Use GetBikeInsuranceCashRequestMessage.newBuilder() to construct.
    private GetBikeInsuranceCashRequestMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private GetBikeInsuranceCashRequestMessage() {
      session_ = "";
      plateNumber_ = "";
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private GetBikeInsuranceCashRequestMessage(
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
            case 10: {
              String s = input.readStringRequireUtf8();

              session_ = s;
              break;
            }
            case 18: {
              String s = input.readStringRequireUtf8();

              plateNumber_ = s;
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
      return GetBikeInsuranceCashRequest.internal_static_GetBikeInsuranceCashRequestMessage_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return GetBikeInsuranceCashRequest.internal_static_GetBikeInsuranceCashRequestMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              GetBikeInsuranceCashRequestMessage.class, Builder.class);
    }

    public static final int SESSION_FIELD_NUMBER = 1;
    private volatile Object session_;
    /**
     * <code>optional string session = 1;</code>
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
     * <code>optional string session = 1;</code>
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

    public static final int PLATENUMBER_FIELD_NUMBER = 2;
    private volatile Object plateNumber_;
    /**
     * <pre>
     *车牌号
     * </pre>
     *
     * <code>optional string plateNumber = 2;</code>
     */
    public String getPlateNumber() {
      Object ref = plateNumber_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        plateNumber_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *车牌号
     * </pre>
     *
     * <code>optional string plateNumber = 2;</code>
     */
    public com.google.protobuf.ByteString
        getPlateNumberBytes() {
      Object ref = plateNumber_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        plateNumber_ = b;
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
      if (!getSessionBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, session_);
      }
      if (!getPlateNumberBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, plateNumber_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getSessionBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, session_);
      }
      if (!getPlateNumberBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, plateNumber_);
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
      if (!(obj instanceof GetBikeInsuranceCashRequestMessage)) {
        return super.equals(obj);
      }
      GetBikeInsuranceCashRequestMessage other = (GetBikeInsuranceCashRequestMessage) obj;

      boolean result = true;
      result = result && getSession()
          .equals(other.getSession());
      result = result && getPlateNumber()
          .equals(other.getPlateNumber());
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      hash = (37 * hash) + SESSION_FIELD_NUMBER;
      hash = (53 * hash) + getSession().hashCode();
      hash = (37 * hash) + PLATENUMBER_FIELD_NUMBER;
      hash = (53 * hash) + getPlateNumber().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static GetBikeInsuranceCashRequestMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static GetBikeInsuranceCashRequestMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static GetBikeInsuranceCashRequestMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static GetBikeInsuranceCashRequestMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static GetBikeInsuranceCashRequestMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static GetBikeInsuranceCashRequestMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static GetBikeInsuranceCashRequestMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static GetBikeInsuranceCashRequestMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static GetBikeInsuranceCashRequestMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static GetBikeInsuranceCashRequestMessage parseFrom(
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
    public static Builder newBuilder(GetBikeInsuranceCashRequestMessage prototype) {
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
     * <pre>
     *通过车牌获取车辆保险提现金额和提现地址
     * </pre>
     *
     * Protobuf type {@code GetBikeInsuranceCashRequestMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:GetBikeInsuranceCashRequestMessage)
        GetBikeInsuranceCashRequestMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return GetBikeInsuranceCashRequest.internal_static_GetBikeInsuranceCashRequestMessage_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return GetBikeInsuranceCashRequest.internal_static_GetBikeInsuranceCashRequestMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                GetBikeInsuranceCashRequestMessage.class, Builder.class);
      }

      // Construct using com.yzh.rfidbike.app.request.GetBikeInsuranceCashRequest.GetBikeInsuranceCashRequestMessage.newBuilder()
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
        session_ = "";

        plateNumber_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return GetBikeInsuranceCashRequest.internal_static_GetBikeInsuranceCashRequestMessage_descriptor;
      }

      public GetBikeInsuranceCashRequestMessage getDefaultInstanceForType() {
        return GetBikeInsuranceCashRequestMessage.getDefaultInstance();
      }

      public GetBikeInsuranceCashRequestMessage build() {
        GetBikeInsuranceCashRequestMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public GetBikeInsuranceCashRequestMessage buildPartial() {
        GetBikeInsuranceCashRequestMessage result = new GetBikeInsuranceCashRequestMessage(this);
        result.session_ = session_;
        result.plateNumber_ = plateNumber_;
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
        if (other instanceof GetBikeInsuranceCashRequestMessage) {
          return mergeFrom((GetBikeInsuranceCashRequestMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(GetBikeInsuranceCashRequestMessage other) {
        if (other == GetBikeInsuranceCashRequestMessage.getDefaultInstance()) return this;
        if (!other.getSession().isEmpty()) {
          session_ = other.session_;
          onChanged();
        }
        if (!other.getPlateNumber().isEmpty()) {
          plateNumber_ = other.plateNumber_;
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
        GetBikeInsuranceCashRequestMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (GetBikeInsuranceCashRequestMessage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private Object session_ = "";
      /**
       * <code>optional string session = 1;</code>
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
       * <code>optional string session = 1;</code>
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
       * <code>optional string session = 1;</code>
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
       * <code>optional string session = 1;</code>
       */
      public Builder clearSession() {
        
        session_ = getDefaultInstance().getSession();
        onChanged();
        return this;
      }
      /**
       * <code>optional string session = 1;</code>
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

      private Object plateNumber_ = "";
      /**
       * <pre>
       *车牌号
       * </pre>
       *
       * <code>optional string plateNumber = 2;</code>
       */
      public String getPlateNumber() {
        Object ref = plateNumber_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          plateNumber_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       *车牌号
       * </pre>
       *
       * <code>optional string plateNumber = 2;</code>
       */
      public com.google.protobuf.ByteString
          getPlateNumberBytes() {
        Object ref = plateNumber_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          plateNumber_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *车牌号
       * </pre>
       *
       * <code>optional string plateNumber = 2;</code>
       */
      public Builder setPlateNumber(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        plateNumber_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *车牌号
       * </pre>
       *
       * <code>optional string plateNumber = 2;</code>
       */
      public Builder clearPlateNumber() {
        
        plateNumber_ = getDefaultInstance().getPlateNumber();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *车牌号
       * </pre>
       *
       * <code>optional string plateNumber = 2;</code>
       */
      public Builder setPlateNumberBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        plateNumber_ = value;
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


      // @@protoc_insertion_point(builder_scope:GetBikeInsuranceCashRequestMessage)
    }

    // @@protoc_insertion_point(class_scope:GetBikeInsuranceCashRequestMessage)
    private static final GetBikeInsuranceCashRequestMessage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new GetBikeInsuranceCashRequestMessage();
    }

    public static GetBikeInsuranceCashRequestMessage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<GetBikeInsuranceCashRequestMessage>
        PARSER = new com.google.protobuf.AbstractParser<GetBikeInsuranceCashRequestMessage>() {
      public GetBikeInsuranceCashRequestMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new GetBikeInsuranceCashRequestMessage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<GetBikeInsuranceCashRequestMessage> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<GetBikeInsuranceCashRequestMessage> getParserForType() {
      return PARSER;
    }

    public GetBikeInsuranceCashRequestMessage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GetBikeInsuranceCashRequestMessage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_GetBikeInsuranceCashRequestMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n!GetBikeInsuranceCashRequest.proto\"J\n\"G" +
      "etBikeInsuranceCashRequestMessage\022\017\n\007ses" +
      "sion\030\001 \001(\t\022\023\n\013plateNumber\030\002 \001(\tB;\n\034com.y" +
      "zh.rfidbike.app.requestB\033GetBikeInsuranc" +
      "eCashRequestb\006proto3"
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
    internal_static_GetBikeInsuranceCashRequestMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_GetBikeInsuranceCashRequestMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_GetBikeInsuranceCashRequestMessage_descriptor,
        new String[] { "Session", "PlateNumber", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
