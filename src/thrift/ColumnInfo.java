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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-11-3")
public class ColumnInfo implements org.apache.thrift.TBase<ColumnInfo, ColumnInfo._Fields>, java.io.Serializable, Cloneable, Comparable<ColumnInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ColumnInfo");

  private static final org.apache.thrift.protocol.TField M_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("mName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField M_DTYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("mDType", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ColumnInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ColumnInfoTupleSchemeFactory());
  }

  public String mName; // required
  public DType mDType; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    M_NAME((short)1, "mName"),
    M_DTYPE((short)2, "mDType");

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
        case 1: // M_NAME
          return M_NAME;
        case 2: // M_DTYPE
          return M_DTYPE;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.M_NAME, new org.apache.thrift.meta_data.FieldMetaData("mName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.M_DTYPE, new org.apache.thrift.meta_data.FieldMetaData("mDType", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, DType.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ColumnInfo.class, metaDataMap);
  }

  public ColumnInfo() {
  }

  public ColumnInfo(
    String mName,
    DType mDType)
  {
    this();
    this.mName = mName;
    this.mDType = mDType;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ColumnInfo(ColumnInfo other) {
    if (other.isSetMName()) {
      this.mName = other.mName;
    }
    if (other.isSetMDType()) {
      this.mDType = new DType(other.mDType);
    }
  }

  public ColumnInfo deepCopy() {
    return new ColumnInfo(this);
  }

  @Override
  public void clear() {
    this.mName = null;
    this.mDType = null;
  }

  public String getMName() {
    return this.mName;
  }

  public ColumnInfo setMName(String mName) {
    this.mName = mName;
    return this;
  }

  public void unsetMName() {
    this.mName = null;
  }

  /** Returns true if field mName is set (has been assigned a value) and false otherwise */
  public boolean isSetMName() {
    return this.mName != null;
  }

  public void setMNameIsSet(boolean value) {
    if (!value) {
      this.mName = null;
    }
  }

  public DType getMDType() {
    return this.mDType;
  }

  public ColumnInfo setMDType(DType mDType) {
    this.mDType = mDType;
    return this;
  }

  public void unsetMDType() {
    this.mDType = null;
  }

  /** Returns true if field mDType is set (has been assigned a value) and false otherwise */
  public boolean isSetMDType() {
    return this.mDType != null;
  }

  public void setMDTypeIsSet(boolean value) {
    if (!value) {
      this.mDType = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case M_NAME:
      if (value == null) {
        unsetMName();
      } else {
        setMName((String)value);
      }
      break;

    case M_DTYPE:
      if (value == null) {
        unsetMDType();
      } else {
        setMDType((DType)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_NAME:
      return getMName();

    case M_DTYPE:
      return getMDType();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case M_NAME:
      return isSetMName();
    case M_DTYPE:
      return isSetMDType();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ColumnInfo)
      return this.equals((ColumnInfo)that);
    return false;
  }

  public boolean equals(ColumnInfo that) {
    if (that == null)
      return false;

    boolean this_present_mName = true && this.isSetMName();
    boolean that_present_mName = true && that.isSetMName();
    if (this_present_mName || that_present_mName) {
      if (!(this_present_mName && that_present_mName))
        return false;
      if (!this.mName.equals(that.mName))
        return false;
    }

    boolean this_present_mDType = true && this.isSetMDType();
    boolean that_present_mDType = true && that.isSetMDType();
    if (this_present_mDType || that_present_mDType) {
      if (!(this_present_mDType && that_present_mDType))
        return false;
      if (!this.mDType.equals(that.mDType))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_mName = true && (isSetMName());
    list.add(present_mName);
    if (present_mName)
      list.add(mName);

    boolean present_mDType = true && (isSetMDType());
    list.add(present_mDType);
    if (present_mDType)
      list.add(mDType);

    return list.hashCode();
  }

  @Override
  public int compareTo(ColumnInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMName()).compareTo(other.isSetMName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mName, other.mName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMDType()).compareTo(other.isSetMDType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMDType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mDType, other.mDType);
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
    StringBuilder sb = new StringBuilder("ColumnInfo(");
    boolean first = true;

    sb.append("mName:");
    if (this.mName == null) {
      sb.append("null");
    } else {
      sb.append(this.mName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mDType:");
    if (this.mDType == null) {
      sb.append("null");
    } else {
      sb.append(this.mDType);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (mDType != null) {
      mDType.validate();
    }
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ColumnInfoStandardSchemeFactory implements SchemeFactory {
    public ColumnInfoStandardScheme getScheme() {
      return new ColumnInfoStandardScheme();
    }
  }

  private static class ColumnInfoStandardScheme extends StandardScheme<ColumnInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ColumnInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // M_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.mName = iprot.readString();
              struct.setMNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // M_DTYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.mDType = new DType();
              struct.mDType.read(iprot);
              struct.setMDTypeIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ColumnInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.mName != null) {
        oprot.writeFieldBegin(M_NAME_FIELD_DESC);
        oprot.writeString(struct.mName);
        oprot.writeFieldEnd();
      }
      if (struct.mDType != null) {
        oprot.writeFieldBegin(M_DTYPE_FIELD_DESC);
        struct.mDType.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ColumnInfoTupleSchemeFactory implements SchemeFactory {
    public ColumnInfoTupleScheme getScheme() {
      return new ColumnInfoTupleScheme();
    }
  }

  private static class ColumnInfoTupleScheme extends TupleScheme<ColumnInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ColumnInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMName()) {
        optionals.set(0);
      }
      if (struct.isSetMDType()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetMName()) {
        oprot.writeString(struct.mName);
      }
      if (struct.isSetMDType()) {
        struct.mDType.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ColumnInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.mName = iprot.readString();
        struct.setMNameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.mDType = new DType();
        struct.mDType.read(iprot);
        struct.setMDTypeIsSet(true);
      }
    }
  }

}

