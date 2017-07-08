// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: CommonResponse.proto

package com.yzh.rfidbike.app.response;

public final class CommonResponse {
  private CommonResponse() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface CommonResponseMessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:CommonResponseMessage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .ErrorMessage errorMsg = 1;</code>
     */
    boolean hasErrorMsg();
    /**
     * <code>optional .ErrorMessage errorMsg = 1;</code>
     */
    ErrorMessageResponse.ErrorMessage getErrorMsg();
    /**
     * <code>optional .ErrorMessage errorMsg = 1;</code>
     */
    ErrorMessageResponse.ErrorMessageOrBuilder getErrorMsgOrBuilder();
  }
  /**
   * Protobuf type {@code CommonResponseMessage}
   */
  public  static final class CommonResponseMessage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:CommonResponseMessage)
      CommonResponseMessageOrBuilder {
    // Use CommonResponseMessage.newBuilder() to construct.
    private CommonResponseMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private CommonResponseMessage() {
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private CommonResponseMessage(
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
              ErrorMessageResponse.ErrorMessage.Builder subBuilder = null;
              if (errorMsg_ != null) {
                subBuilder = errorMsg_.toBuilder();
              }
              errorMsg_ = input.readMessage(ErrorMessageResponse.ErrorMessage.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(errorMsg_);
                errorMsg_ = subBuilder.buildPartial();
              }

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
      return CommonResponse.internal_static_CommonResponseMessage_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return CommonResponse.internal_static_CommonResponseMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              CommonResponseMessage.class, Builder.class);
    }

    public static final int ERRORMSG_FIELD_NUMBER = 1;
    private ErrorMessageResponse.ErrorMessage errorMsg_;
    /**
     * <code>optional .ErrorMessage errorMsg = 1;</code>
     */
    public boolean hasErrorMsg() {
      return errorMsg_ != null;
    }
    /**
     * <code>optional .ErrorMessage errorMsg = 1;</code>
     */
    public ErrorMessageResponse.ErrorMessage getErrorMsg() {
      return errorMsg_ == null ? ErrorMessageResponse.ErrorMessage.getDefaultInstance() : errorMsg_;
    }
    /**
     * <code>optional .ErrorMessage errorMsg = 1;</code>
     */
    public ErrorMessageResponse.ErrorMessageOrBuilder getErrorMsgOrBuilder() {
      return getErrorMsg();
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
      if (errorMsg_ != null) {
        output.writeMessage(1, getErrorMsg());
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (errorMsg_ != null) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, getErrorMsg());
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
      if (!(obj instanceof CommonResponseMessage)) {
        return super.equals(obj);
      }
      CommonResponseMessage other = (CommonResponseMessage) obj;

      boolean result = true;
      result = result && (hasErrorMsg() == other.hasErrorMsg());
      if (hasErrorMsg()) {
        result = result && getErrorMsg()
            .equals(other.getErrorMsg());
      }
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      if (hasErrorMsg()) {
        hash = (37 * hash) + ERRORMSG_FIELD_NUMBER;
        hash = (53 * hash) + getErrorMsg().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static CommonResponseMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CommonResponseMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CommonResponseMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static CommonResponseMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static CommonResponseMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static CommonResponseMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static CommonResponseMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static CommonResponseMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static CommonResponseMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static CommonResponseMessage parseFrom(
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
    public static Builder newBuilder(CommonResponseMessage prototype) {
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
     * Protobuf type {@code CommonResponseMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:CommonResponseMessage)
        CommonResponseMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return CommonResponse.internal_static_CommonResponseMessage_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return CommonResponse.internal_static_CommonResponseMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                CommonResponseMessage.class, Builder.class);
      }

      // Construct using com.yzh.rfidbike.app.response.CommonResponse.CommonResponseMessage.newBuilder()
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
        if (errorMsgBuilder_ == null) {
          errorMsg_ = null;
        } else {
          errorMsg_ = null;
          errorMsgBuilder_ = null;
        }
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return CommonResponse.internal_static_CommonResponseMessage_descriptor;
      }

      public CommonResponseMessage getDefaultInstanceForType() {
        return CommonResponseMessage.getDefaultInstance();
      }

      public CommonResponseMessage build() {
        CommonResponseMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public CommonResponseMessage buildPartial() {
        CommonResponseMessage result = new CommonResponseMessage(this);
        if (errorMsgBuilder_ == null) {
          result.errorMsg_ = errorMsg_;
        } else {
          result.errorMsg_ = errorMsgBuilder_.build();
        }
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
        if (other instanceof CommonResponseMessage) {
          return mergeFrom((CommonResponseMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(CommonResponseMessage other) {
        if (other == CommonResponseMessage.getDefaultInstance()) return this;
        if (other.hasErrorMsg()) {
          mergeErrorMsg(other.getErrorMsg());
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
        CommonResponseMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (CommonResponseMessage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private ErrorMessageResponse.ErrorMessage errorMsg_ = null;
      private com.google.protobuf.SingleFieldBuilderV3<
          ErrorMessageResponse.ErrorMessage, ErrorMessageResponse.ErrorMessage.Builder, ErrorMessageResponse.ErrorMessageOrBuilder> errorMsgBuilder_;
      /**
       * <code>optional .ErrorMessage errorMsg = 1;</code>
       */
      public boolean hasErrorMsg() {
        return errorMsgBuilder_ != null || errorMsg_ != null;
      }
      /**
       * <code>optional .ErrorMessage errorMsg = 1;</code>
       */
      public ErrorMessageResponse.ErrorMessage getErrorMsg() {
        if (errorMsgBuilder_ == null) {
          return errorMsg_ == null ? ErrorMessageResponse.ErrorMessage.getDefaultInstance() : errorMsg_;
        } else {
          return errorMsgBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .ErrorMessage errorMsg = 1;</code>
       */
      public Builder setErrorMsg(ErrorMessageResponse.ErrorMessage value) {
        if (errorMsgBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          errorMsg_ = value;
          onChanged();
        } else {
          errorMsgBuilder_.setMessage(value);
        }

        return this;
      }
      /**
       * <code>optional .ErrorMessage errorMsg = 1;</code>
       */
      public Builder setErrorMsg(
          ErrorMessageResponse.ErrorMessage.Builder builderForValue) {
        if (errorMsgBuilder_ == null) {
          errorMsg_ = builderForValue.build();
          onChanged();
        } else {
          errorMsgBuilder_.setMessage(builderForValue.build());
        }

        return this;
      }
      /**
       * <code>optional .ErrorMessage errorMsg = 1;</code>
       */
      public Builder mergeErrorMsg(ErrorMessageResponse.ErrorMessage value) {
        if (errorMsgBuilder_ == null) {
          if (errorMsg_ != null) {
            errorMsg_ =
              ErrorMessageResponse.ErrorMessage.newBuilder(errorMsg_).mergeFrom(value).buildPartial();
          } else {
            errorMsg_ = value;
          }
          onChanged();
        } else {
          errorMsgBuilder_.mergeFrom(value);
        }

        return this;
      }
      /**
       * <code>optional .ErrorMessage errorMsg = 1;</code>
       */
      public Builder clearErrorMsg() {
        if (errorMsgBuilder_ == null) {
          errorMsg_ = null;
          onChanged();
        } else {
          errorMsg_ = null;
          errorMsgBuilder_ = null;
        }

        return this;
      }
      /**
       * <code>optional .ErrorMessage errorMsg = 1;</code>
       */
      public ErrorMessageResponse.ErrorMessage.Builder getErrorMsgBuilder() {
        
        onChanged();
        return getErrorMsgFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .ErrorMessage errorMsg = 1;</code>
       */
      public ErrorMessageResponse.ErrorMessageOrBuilder getErrorMsgOrBuilder() {
        if (errorMsgBuilder_ != null) {
          return errorMsgBuilder_.getMessageOrBuilder();
        } else {
          return errorMsg_ == null ?
              ErrorMessageResponse.ErrorMessage.getDefaultInstance() : errorMsg_;
        }
      }
      /**
       * <code>optional .ErrorMessage errorMsg = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilderV3<
          ErrorMessageResponse.ErrorMessage, ErrorMessageResponse.ErrorMessage.Builder, ErrorMessageResponse.ErrorMessageOrBuilder>
          getErrorMsgFieldBuilder() {
        if (errorMsgBuilder_ == null) {
          errorMsgBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
              ErrorMessageResponse.ErrorMessage, ErrorMessageResponse.ErrorMessage.Builder, ErrorMessageResponse.ErrorMessageOrBuilder>(
                  getErrorMsg(),
                  getParentForChildren(),
                  isClean());
          errorMsg_ = null;
        }
        return errorMsgBuilder_;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:CommonResponseMessage)
    }

    // @@protoc_insertion_point(class_scope:CommonResponseMessage)
    private static final CommonResponseMessage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new CommonResponseMessage();
    }

    public static CommonResponseMessage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<CommonResponseMessage>
        PARSER = new com.google.protobuf.AbstractParser<CommonResponseMessage>() {
      public CommonResponseMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new CommonResponseMessage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<CommonResponseMessage> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<CommonResponseMessage> getParserForType() {
      return PARSER;
    }

    public CommonResponseMessage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_CommonResponseMessage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_CommonResponseMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\024CommonResponse.proto\032\032ErrorMessageResp" +
      "onse.proto\"8\n\025CommonResponseMessage\022\037\n\010e" +
      "rrorMsg\030\001 \001(\0132\r.ErrorMessageB/\n\035com.yzh." +
      "rfidbike.app.responseB\016CommonResponseb\006p" +
      "roto3"
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
          ErrorMessageResponse.getDescriptor(),
        }, assigner);
    internal_static_CommonResponseMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_CommonResponseMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_CommonResponseMessage_descriptor,
        new String[] { "ErrorMsg", });
    ErrorMessageResponse.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
