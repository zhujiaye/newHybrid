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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-10-29")
public class DbmsInfo implements org.apache.thrift.TBase<DbmsInfo, DbmsInfo._Fields>, java.io.Serializable, Cloneable, Comparable<DbmsInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DbmsInfo");

  private static final org.apache.thrift.protocol.TField M_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("mType", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField M_COMPLETE_CONNECTION_STRING_FIELD_DESC = new org.apache.thrift.protocol.TField("mCompleteConnectionString", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField M_MYSQL_USERNAME_FIELD_DESC = new org.apache.thrift.protocol.TField("mMysqlUsername", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField M_MYSQL_PASSWORD_FIELD_DESC = new org.apache.thrift.protocol.TField("mMysqlPassword", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField M_VOLTDB_CAPACITY_MB_FIELD_DESC = new org.apache.thrift.protocol.TField("mVoltdbCapacityMB", org.apache.thrift.protocol.TType.I32, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new DbmsInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new DbmsInfoTupleSchemeFactory());
  }

  /**
   * 
   * @see DbmsType
   */
  public DbmsType mType; // required
  public String mCompleteConnectionString; // required
  public String mMysqlUsername; // required
  public String mMysqlPassword; // required
  public int mVoltdbCapacityMB; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see DbmsType
     */
    M_TYPE((short)1, "mType"),
    M_COMPLETE_CONNECTION_STRING((short)2, "mCompleteConnectionString"),
    M_MYSQL_USERNAME((short)3, "mMysqlUsername"),
    M_MYSQL_PASSWORD((short)4, "mMysqlPassword"),
    M_VOLTDB_CAPACITY_MB((short)5, "mVoltdbCapacityMB");

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
        case 1: // M_TYPE
          return M_TYPE;
        case 2: // M_COMPLETE_CONNECTION_STRING
          return M_COMPLETE_CONNECTION_STRING;
        case 3: // M_MYSQL_USERNAME
          return M_MYSQL_USERNAME;
        case 4: // M_MYSQL_PASSWORD
          return M_MYSQL_PASSWORD;
        case 5: // M_VOLTDB_CAPACITY_MB
          return M_VOLTDB_CAPACITY_MB;
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
  private static final int __MVOLTDBCAPACITYMB_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.M_TYPE, new org.apache.thrift.meta_data.FieldMetaData("mType", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, DbmsType.class)));
    tmpMap.put(_Fields.M_COMPLETE_CONNECTION_STRING, new org.apache.thrift.meta_data.FieldMetaData("mCompleteConnectionString", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.M_MYSQL_USERNAME, new org.apache.thrift.meta_data.FieldMetaData("mMysqlUsername", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.M_MYSQL_PASSWORD, new org.apache.thrift.meta_data.FieldMetaData("mMysqlPassword", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.M_VOLTDB_CAPACITY_MB, new org.apache.thrift.meta_data.FieldMetaData("mVoltdbCapacityMB", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DbmsInfo.class, metaDataMap);
  }

  public DbmsInfo() {
  }

  public DbmsInfo(
    DbmsType mType,
    String mCompleteConnectionString,
    String mMysqlUsername,
    String mMysqlPassword,
    int mVoltdbCapacityMB)
  {
    this();
    this.mType = mType;
    this.mCompleteConnectionString = mCompleteConnectionString;
    this.mMysqlUsername = mMysqlUsername;
    this.mMysqlPassword = mMysqlPassword;
    this.mVoltdbCapacityMB = mVoltdbCapacityMB;
    setMVoltdbCapacityMBIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DbmsInfo(DbmsInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetMType()) {
      this.mType = other.mType;
    }
    if (other.isSetMCompleteConnectionString()) {
      this.mCompleteConnectionString = other.mCompleteConnectionString;
    }
    if (other.isSetMMysqlUsername()) {
      this.mMysqlUsername = other.mMysqlUsername;
    }
    if (other.isSetMMysqlPassword()) {
      this.mMysqlPassword = other.mMysqlPassword;
    }
    this.mVoltdbCapacityMB = other.mVoltdbCapacityMB;
  }

  public DbmsInfo deepCopy() {
    return new DbmsInfo(this);
  }

  @Override
  public void clear() {
    this.mType = null;
    this.mCompleteConnectionString = null;
    this.mMysqlUsername = null;
    this.mMysqlPassword = null;
    setMVoltdbCapacityMBIsSet(false);
    this.mVoltdbCapacityMB = 0;
  }

  /**
   * 
   * @see DbmsType
   */
  public DbmsType getMType() {
    return this.mType;
  }

  /**
   * 
   * @see DbmsType
   */
  public DbmsInfo setMType(DbmsType mType) {
    this.mType = mType;
    return this;
  }

  public void unsetMType() {
    this.mType = null;
  }

  /** Returns true if field mType is set (has been assigned a value) and false otherwise */
  public boolean isSetMType() {
    return this.mType != null;
  }

  public void setMTypeIsSet(boolean value) {
    if (!value) {
      this.mType = null;
    }
  }

  public String getMCompleteConnectionString() {
    return this.mCompleteConnectionString;
  }

  public DbmsInfo setMCompleteConnectionString(String mCompleteConnectionString) {
    this.mCompleteConnectionString = mCompleteConnectionString;
    return this;
  }

  public void unsetMCompleteConnectionString() {
    this.mCompleteConnectionString = null;
  }

  /** Returns true if field mCompleteConnectionString is set (has been assigned a value) and false otherwise */
  public boolean isSetMCompleteConnectionString() {
    return this.mCompleteConnectionString != null;
  }

  public void setMCompleteConnectionStringIsSet(boolean value) {
    if (!value) {
      this.mCompleteConnectionString = null;
    }
  }

  public String getMMysqlUsername() {
    return this.mMysqlUsername;
  }

  public DbmsInfo setMMysqlUsername(String mMysqlUsername) {
    this.mMysqlUsername = mMysqlUsername;
    return this;
  }

  public void unsetMMysqlUsername() {
    this.mMysqlUsername = null;
  }

  /** Returns true if field mMysqlUsername is set (has been assigned a value) and false otherwise */
  public boolean isSetMMysqlUsername() {
    return this.mMysqlUsername != null;
  }

  public void setMMysqlUsernameIsSet(boolean value) {
    if (!value) {
      this.mMysqlUsername = null;
    }
  }

  public String getMMysqlPassword() {
    return this.mMysqlPassword;
  }

  public DbmsInfo setMMysqlPassword(String mMysqlPassword) {
    this.mMysqlPassword = mMysqlPassword;
    return this;
  }

  public void unsetMMysqlPassword() {
    this.mMysqlPassword = null;
  }

  /** Returns true if field mMysqlPassword is set (has been assigned a value) and false otherwise */
  public boolean isSetMMysqlPassword() {
    return this.mMysqlPassword != null;
  }

  public void setMMysqlPasswordIsSet(boolean value) {
    if (!value) {
      this.mMysqlPassword = null;
    }
  }

  public int getMVoltdbCapacityMB() {
    return this.mVoltdbCapacityMB;
  }

  public DbmsInfo setMVoltdbCapacityMB(int mVoltdbCapacityMB) {
    this.mVoltdbCapacityMB = mVoltdbCapacityMB;
    setMVoltdbCapacityMBIsSet(true);
    return this;
  }

  public void unsetMVoltdbCapacityMB() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MVOLTDBCAPACITYMB_ISSET_ID);
  }

  /** Returns true if field mVoltdbCapacityMB is set (has been assigned a value) and false otherwise */
  public boolean isSetMVoltdbCapacityMB() {
    return EncodingUtils.testBit(__isset_bitfield, __MVOLTDBCAPACITYMB_ISSET_ID);
  }

  public void setMVoltdbCapacityMBIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MVOLTDBCAPACITYMB_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case M_TYPE:
      if (value == null) {
        unsetMType();
      } else {
        setMType((DbmsType)value);
      }
      break;

    case M_COMPLETE_CONNECTION_STRING:
      if (value == null) {
        unsetMCompleteConnectionString();
      } else {
        setMCompleteConnectionString((String)value);
      }
      break;

    case M_MYSQL_USERNAME:
      if (value == null) {
        unsetMMysqlUsername();
      } else {
        setMMysqlUsername((String)value);
      }
      break;

    case M_MYSQL_PASSWORD:
      if (value == null) {
        unsetMMysqlPassword();
      } else {
        setMMysqlPassword((String)value);
      }
      break;

    case M_VOLTDB_CAPACITY_MB:
      if (value == null) {
        unsetMVoltdbCapacityMB();
      } else {
        setMVoltdbCapacityMB((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_TYPE:
      return getMType();

    case M_COMPLETE_CONNECTION_STRING:
      return getMCompleteConnectionString();

    case M_MYSQL_USERNAME:
      return getMMysqlUsername();

    case M_MYSQL_PASSWORD:
      return getMMysqlPassword();

    case M_VOLTDB_CAPACITY_MB:
      return Integer.valueOf(getMVoltdbCapacityMB());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case M_TYPE:
      return isSetMType();
    case M_COMPLETE_CONNECTION_STRING:
      return isSetMCompleteConnectionString();
    case M_MYSQL_USERNAME:
      return isSetMMysqlUsername();
    case M_MYSQL_PASSWORD:
      return isSetMMysqlPassword();
    case M_VOLTDB_CAPACITY_MB:
      return isSetMVoltdbCapacityMB();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof DbmsInfo)
      return this.equals((DbmsInfo)that);
    return false;
  }

  public boolean equals(DbmsInfo that) {
    if (that == null)
      return false;

    boolean this_present_mType = true && this.isSetMType();
    boolean that_present_mType = true && that.isSetMType();
    if (this_present_mType || that_present_mType) {
      if (!(this_present_mType && that_present_mType))
        return false;
      if (!this.mType.equals(that.mType))
        return false;
    }

    boolean this_present_mCompleteConnectionString = true && this.isSetMCompleteConnectionString();
    boolean that_present_mCompleteConnectionString = true && that.isSetMCompleteConnectionString();
    if (this_present_mCompleteConnectionString || that_present_mCompleteConnectionString) {
      if (!(this_present_mCompleteConnectionString && that_present_mCompleteConnectionString))
        return false;
      if (!this.mCompleteConnectionString.equals(that.mCompleteConnectionString))
        return false;
    }

    boolean this_present_mMysqlUsername = true && this.isSetMMysqlUsername();
    boolean that_present_mMysqlUsername = true && that.isSetMMysqlUsername();
    if (this_present_mMysqlUsername || that_present_mMysqlUsername) {
      if (!(this_present_mMysqlUsername && that_present_mMysqlUsername))
        return false;
      if (!this.mMysqlUsername.equals(that.mMysqlUsername))
        return false;
    }

    boolean this_present_mMysqlPassword = true && this.isSetMMysqlPassword();
    boolean that_present_mMysqlPassword = true && that.isSetMMysqlPassword();
    if (this_present_mMysqlPassword || that_present_mMysqlPassword) {
      if (!(this_present_mMysqlPassword && that_present_mMysqlPassword))
        return false;
      if (!this.mMysqlPassword.equals(that.mMysqlPassword))
        return false;
    }

    boolean this_present_mVoltdbCapacityMB = true;
    boolean that_present_mVoltdbCapacityMB = true;
    if (this_present_mVoltdbCapacityMB || that_present_mVoltdbCapacityMB) {
      if (!(this_present_mVoltdbCapacityMB && that_present_mVoltdbCapacityMB))
        return false;
      if (this.mVoltdbCapacityMB != that.mVoltdbCapacityMB)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_mType = true && (isSetMType());
    list.add(present_mType);
    if (present_mType)
      list.add(mType.getValue());

    boolean present_mCompleteConnectionString = true && (isSetMCompleteConnectionString());
    list.add(present_mCompleteConnectionString);
    if (present_mCompleteConnectionString)
      list.add(mCompleteConnectionString);

    boolean present_mMysqlUsername = true && (isSetMMysqlUsername());
    list.add(present_mMysqlUsername);
    if (present_mMysqlUsername)
      list.add(mMysqlUsername);

    boolean present_mMysqlPassword = true && (isSetMMysqlPassword());
    list.add(present_mMysqlPassword);
    if (present_mMysqlPassword)
      list.add(mMysqlPassword);

    boolean present_mVoltdbCapacityMB = true;
    list.add(present_mVoltdbCapacityMB);
    if (present_mVoltdbCapacityMB)
      list.add(mVoltdbCapacityMB);

    return list.hashCode();
  }

  @Override
  public int compareTo(DbmsInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMType()).compareTo(other.isSetMType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mType, other.mType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMCompleteConnectionString()).compareTo(other.isSetMCompleteConnectionString());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMCompleteConnectionString()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mCompleteConnectionString, other.mCompleteConnectionString);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMMysqlUsername()).compareTo(other.isSetMMysqlUsername());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMMysqlUsername()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mMysqlUsername, other.mMysqlUsername);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMMysqlPassword()).compareTo(other.isSetMMysqlPassword());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMMysqlPassword()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mMysqlPassword, other.mMysqlPassword);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMVoltdbCapacityMB()).compareTo(other.isSetMVoltdbCapacityMB());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMVoltdbCapacityMB()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mVoltdbCapacityMB, other.mVoltdbCapacityMB);
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
    StringBuilder sb = new StringBuilder("DbmsInfo(");
    boolean first = true;

    sb.append("mType:");
    if (this.mType == null) {
      sb.append("null");
    } else {
      sb.append(this.mType);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mCompleteConnectionString:");
    if (this.mCompleteConnectionString == null) {
      sb.append("null");
    } else {
      sb.append(this.mCompleteConnectionString);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mMysqlUsername:");
    if (this.mMysqlUsername == null) {
      sb.append("null");
    } else {
      sb.append(this.mMysqlUsername);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mMysqlPassword:");
    if (this.mMysqlPassword == null) {
      sb.append("null");
    } else {
      sb.append(this.mMysqlPassword);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mVoltdbCapacityMB:");
    sb.append(this.mVoltdbCapacityMB);
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

  private static class DbmsInfoStandardSchemeFactory implements SchemeFactory {
    public DbmsInfoStandardScheme getScheme() {
      return new DbmsInfoStandardScheme();
    }
  }

  private static class DbmsInfoStandardScheme extends StandardScheme<DbmsInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DbmsInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // M_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.mType = thrift.DbmsType.findByValue(iprot.readI32());
              struct.setMTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // M_COMPLETE_CONNECTION_STRING
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.mCompleteConnectionString = iprot.readString();
              struct.setMCompleteConnectionStringIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // M_MYSQL_USERNAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.mMysqlUsername = iprot.readString();
              struct.setMMysqlUsernameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // M_MYSQL_PASSWORD
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.mMysqlPassword = iprot.readString();
              struct.setMMysqlPasswordIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // M_VOLTDB_CAPACITY_MB
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.mVoltdbCapacityMB = iprot.readI32();
              struct.setMVoltdbCapacityMBIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, DbmsInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.mType != null) {
        oprot.writeFieldBegin(M_TYPE_FIELD_DESC);
        oprot.writeI32(struct.mType.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.mCompleteConnectionString != null) {
        oprot.writeFieldBegin(M_COMPLETE_CONNECTION_STRING_FIELD_DESC);
        oprot.writeString(struct.mCompleteConnectionString);
        oprot.writeFieldEnd();
      }
      if (struct.mMysqlUsername != null) {
        oprot.writeFieldBegin(M_MYSQL_USERNAME_FIELD_DESC);
        oprot.writeString(struct.mMysqlUsername);
        oprot.writeFieldEnd();
      }
      if (struct.mMysqlPassword != null) {
        oprot.writeFieldBegin(M_MYSQL_PASSWORD_FIELD_DESC);
        oprot.writeString(struct.mMysqlPassword);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(M_VOLTDB_CAPACITY_MB_FIELD_DESC);
      oprot.writeI32(struct.mVoltdbCapacityMB);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DbmsInfoTupleSchemeFactory implements SchemeFactory {
    public DbmsInfoTupleScheme getScheme() {
      return new DbmsInfoTupleScheme();
    }
  }

  private static class DbmsInfoTupleScheme extends TupleScheme<DbmsInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DbmsInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMType()) {
        optionals.set(0);
      }
      if (struct.isSetMCompleteConnectionString()) {
        optionals.set(1);
      }
      if (struct.isSetMMysqlUsername()) {
        optionals.set(2);
      }
      if (struct.isSetMMysqlPassword()) {
        optionals.set(3);
      }
      if (struct.isSetMVoltdbCapacityMB()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetMType()) {
        oprot.writeI32(struct.mType.getValue());
      }
      if (struct.isSetMCompleteConnectionString()) {
        oprot.writeString(struct.mCompleteConnectionString);
      }
      if (struct.isSetMMysqlUsername()) {
        oprot.writeString(struct.mMysqlUsername);
      }
      if (struct.isSetMMysqlPassword()) {
        oprot.writeString(struct.mMysqlPassword);
      }
      if (struct.isSetMVoltdbCapacityMB()) {
        oprot.writeI32(struct.mVoltdbCapacityMB);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DbmsInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.mType = thrift.DbmsType.findByValue(iprot.readI32());
        struct.setMTypeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.mCompleteConnectionString = iprot.readString();
        struct.setMCompleteConnectionStringIsSet(true);
      }
      if (incoming.get(2)) {
        struct.mMysqlUsername = iprot.readString();
        struct.setMMysqlUsernameIsSet(true);
      }
      if (incoming.get(3)) {
        struct.mMysqlPassword = iprot.readString();
        struct.setMMysqlPasswordIsSet(true);
      }
      if (incoming.get(4)) {
        struct.mVoltdbCapacityMB = iprot.readI32();
        struct.setMVoltdbCapacityMBIsSet(true);
      }
    }
  }

}

