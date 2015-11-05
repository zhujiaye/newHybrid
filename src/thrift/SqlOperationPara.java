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
public class SqlOperationPara implements org.apache.thrift.TBase<SqlOperationPara, SqlOperationPara._Fields>, java.io.Serializable, Cloneable, Comparable<SqlOperationPara> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SqlOperationPara");

  private static final org.apache.thrift.protocol.TField M_TENANT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("mTenantID", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField M_SQL_STRING_FIELD_DESC = new org.apache.thrift.protocol.TField("mSqlString", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new SqlOperationParaStandardSchemeFactory());
    schemes.put(TupleScheme.class, new SqlOperationParaTupleSchemeFactory());
  }

  public int mTenantID; // required
  public String mSqlString; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    M_TENANT_ID((short)1, "mTenantID"),
    M_SQL_STRING((short)2, "mSqlString");

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
        case 1: // M_TENANT_ID
          return M_TENANT_ID;
        case 2: // M_SQL_STRING
          return M_SQL_STRING;
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
  private static final int __MTENANTID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.M_TENANT_ID, new org.apache.thrift.meta_data.FieldMetaData("mTenantID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.M_SQL_STRING, new org.apache.thrift.meta_data.FieldMetaData("mSqlString", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SqlOperationPara.class, metaDataMap);
  }

  public SqlOperationPara() {
  }

  public SqlOperationPara(
    int mTenantID,
    String mSqlString)
  {
    this();
    this.mTenantID = mTenantID;
    setMTenantIDIsSet(true);
    this.mSqlString = mSqlString;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SqlOperationPara(SqlOperationPara other) {
    __isset_bitfield = other.__isset_bitfield;
    this.mTenantID = other.mTenantID;
    if (other.isSetMSqlString()) {
      this.mSqlString = other.mSqlString;
    }
  }

  public SqlOperationPara deepCopy() {
    return new SqlOperationPara(this);
  }

  @Override
  public void clear() {
    setMTenantIDIsSet(false);
    this.mTenantID = 0;
    this.mSqlString = null;
  }

  public int getMTenantID() {
    return this.mTenantID;
  }

  public SqlOperationPara setMTenantID(int mTenantID) {
    this.mTenantID = mTenantID;
    setMTenantIDIsSet(true);
    return this;
  }

  public void unsetMTenantID() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MTENANTID_ISSET_ID);
  }

  /** Returns true if field mTenantID is set (has been assigned a value) and false otherwise */
  public boolean isSetMTenantID() {
    return EncodingUtils.testBit(__isset_bitfield, __MTENANTID_ISSET_ID);
  }

  public void setMTenantIDIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MTENANTID_ISSET_ID, value);
  }

  public String getMSqlString() {
    return this.mSqlString;
  }

  public SqlOperationPara setMSqlString(String mSqlString) {
    this.mSqlString = mSqlString;
    return this;
  }

  public void unsetMSqlString() {
    this.mSqlString = null;
  }

  /** Returns true if field mSqlString is set (has been assigned a value) and false otherwise */
  public boolean isSetMSqlString() {
    return this.mSqlString != null;
  }

  public void setMSqlStringIsSet(boolean value) {
    if (!value) {
      this.mSqlString = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case M_TENANT_ID:
      if (value == null) {
        unsetMTenantID();
      } else {
        setMTenantID((Integer)value);
      }
      break;

    case M_SQL_STRING:
      if (value == null) {
        unsetMSqlString();
      } else {
        setMSqlString((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_TENANT_ID:
      return Integer.valueOf(getMTenantID());

    case M_SQL_STRING:
      return getMSqlString();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case M_TENANT_ID:
      return isSetMTenantID();
    case M_SQL_STRING:
      return isSetMSqlString();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof SqlOperationPara)
      return this.equals((SqlOperationPara)that);
    return false;
  }

  public boolean equals(SqlOperationPara that) {
    if (that == null)
      return false;

    boolean this_present_mTenantID = true;
    boolean that_present_mTenantID = true;
    if (this_present_mTenantID || that_present_mTenantID) {
      if (!(this_present_mTenantID && that_present_mTenantID))
        return false;
      if (this.mTenantID != that.mTenantID)
        return false;
    }

    boolean this_present_mSqlString = true && this.isSetMSqlString();
    boolean that_present_mSqlString = true && that.isSetMSqlString();
    if (this_present_mSqlString || that_present_mSqlString) {
      if (!(this_present_mSqlString && that_present_mSqlString))
        return false;
      if (!this.mSqlString.equals(that.mSqlString))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_mTenantID = true;
    list.add(present_mTenantID);
    if (present_mTenantID)
      list.add(mTenantID);

    boolean present_mSqlString = true && (isSetMSqlString());
    list.add(present_mSqlString);
    if (present_mSqlString)
      list.add(mSqlString);

    return list.hashCode();
  }

  @Override
  public int compareTo(SqlOperationPara other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMTenantID()).compareTo(other.isSetMTenantID());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMTenantID()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mTenantID, other.mTenantID);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMSqlString()).compareTo(other.isSetMSqlString());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMSqlString()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mSqlString, other.mSqlString);
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
    StringBuilder sb = new StringBuilder("SqlOperationPara(");
    boolean first = true;

    sb.append("mTenantID:");
    sb.append(this.mTenantID);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mSqlString:");
    if (this.mSqlString == null) {
      sb.append("null");
    } else {
      sb.append(this.mSqlString);
    }
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

  private static class SqlOperationParaStandardSchemeFactory implements SchemeFactory {
    public SqlOperationParaStandardScheme getScheme() {
      return new SqlOperationParaStandardScheme();
    }
  }

  private static class SqlOperationParaStandardScheme extends StandardScheme<SqlOperationPara> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SqlOperationPara struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // M_TENANT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.mTenantID = iprot.readI32();
              struct.setMTenantIDIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // M_SQL_STRING
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.mSqlString = iprot.readString();
              struct.setMSqlStringIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, SqlOperationPara struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(M_TENANT_ID_FIELD_DESC);
      oprot.writeI32(struct.mTenantID);
      oprot.writeFieldEnd();
      if (struct.mSqlString != null) {
        oprot.writeFieldBegin(M_SQL_STRING_FIELD_DESC);
        oprot.writeString(struct.mSqlString);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SqlOperationParaTupleSchemeFactory implements SchemeFactory {
    public SqlOperationParaTupleScheme getScheme() {
      return new SqlOperationParaTupleScheme();
    }
  }

  private static class SqlOperationParaTupleScheme extends TupleScheme<SqlOperationPara> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SqlOperationPara struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMTenantID()) {
        optionals.set(0);
      }
      if (struct.isSetMSqlString()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetMTenantID()) {
        oprot.writeI32(struct.mTenantID);
      }
      if (struct.isSetMSqlString()) {
        oprot.writeString(struct.mSqlString);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SqlOperationPara struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.mTenantID = iprot.readI32();
        struct.setMTenantIDIsSet(true);
      }
      if (incoming.get(1)) {
        struct.mSqlString = iprot.readString();
        struct.setMSqlStringIsSet(true);
      }
    }
  }

}
