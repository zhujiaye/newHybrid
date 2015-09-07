/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-9-7")
public class TenantInfo implements org.apache.thrift.TBase<TenantInfo, TenantInfo._Fields>, java.io.Serializable, Cloneable, Comparable<TenantInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TenantInfo");

  private static final org.apache.thrift.protocol.TField M_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("mId", org.apache.thrift.protocol.TType.I32, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TenantInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TenantInfoTupleSchemeFactory());
  }

  public int mId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    M_ID((short)1, "mId");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // M_ID
          return M_ID;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __MID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.M_ID, new org.apache.thrift.meta_data.FieldMetaData("mId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TenantInfo.class, metaDataMap);
  }

  public TenantInfo() {
  }

  public TenantInfo(
    int mId)
  {
    this();
    this.mId = mId;
    setMIdIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TenantInfo(TenantInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    this.mId = other.mId;
  }

  public TenantInfo deepCopy() {
    return new TenantInfo(this);
  }

  @Override
  public void clear() {
    setMIdIsSet(false);
    this.mId = 0;
  }

  public int getMId() {
    return this.mId;
  }

  public TenantInfo setMId(int mId) {
    this.mId = mId;
    setMIdIsSet(true);
    return this;
  }

  public void unsetMId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MID_ISSET_ID);
  }

  /** Returns true if field mId is set (has been assigned a value) and false otherwise */
  public boolean isSetMId() {
    return EncodingUtils.testBit(__isset_bitfield, __MID_ISSET_ID);
  }

  public void setMIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case M_ID:
      if (value == null) {
        unsetMId();
      } else {
        setMId((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_ID:
      return Integer.valueOf(getMId());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case M_ID:
      return isSetMId();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TenantInfo)
      return this.equals((TenantInfo)that);
    return false;
  }

  public boolean equals(TenantInfo that) {
    if (that == null)
      return false;

    boolean this_present_mId = true;
    boolean that_present_mId = true;
    if (this_present_mId || that_present_mId) {
      if (!(this_present_mId && that_present_mId))
        return false;
      if (this.mId != that.mId)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_mId = true;
    list.add(present_mId);
    if (present_mId)
      list.add(mId);

    return list.hashCode();
  }

  @Override
  public int compareTo(TenantInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMId()).compareTo(other.isSetMId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mId, other.mId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TenantInfo(");
    boolean first = true;

    sb.append("mId:");
    sb.append(this.mId);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TenantInfoStandardSchemeFactory implements SchemeFactory {
    public TenantInfoStandardScheme getScheme() {
      return new TenantInfoStandardScheme();
    }
  }

  private static class TenantInfoStandardScheme extends StandardScheme<TenantInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TenantInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // M_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.mId = iprot.readI32();
              struct.setMIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TenantInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(M_ID_FIELD_DESC);
      oprot.writeI32(struct.mId);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TenantInfoTupleSchemeFactory implements SchemeFactory {
    public TenantInfoTupleScheme getScheme() {
      return new TenantInfoTupleScheme();
    }
  }

  private static class TenantInfoTupleScheme extends TupleScheme<TenantInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TenantInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMId()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetMId()) {
        oprot.writeI32(struct.mId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TenantInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.mId = iprot.readI32();
        struct.setMIdIsSet(true);
      }
    }
  }

}

