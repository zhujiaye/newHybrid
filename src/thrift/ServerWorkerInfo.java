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
public class ServerWorkerInfo implements org.apache.thrift.TBase<ServerWorkerInfo, ServerWorkerInfo._Fields>, java.io.Serializable, Cloneable, Comparable<ServerWorkerInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ServerWorkerInfo");

  private static final org.apache.thrift.protocol.TField M_ADDRESS_FIELD_DESC = new org.apache.thrift.protocol.TField("mAddress", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField M_PORT_FIELD_DESC = new org.apache.thrift.protocol.TField("mPort", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField M_DBMS_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("mDbmsInfo", org.apache.thrift.protocol.TType.STRUCT, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ServerWorkerInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ServerWorkerInfoTupleSchemeFactory());
  }

  public String mAddress; // required
  public int mPort; // required
  public DbmsInfo mDbmsInfo; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    M_ADDRESS((short)1, "mAddress"),
    M_PORT((short)2, "mPort"),
    M_DBMS_INFO((short)3, "mDbmsInfo");

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
        case 1: // M_ADDRESS
          return M_ADDRESS;
        case 2: // M_PORT
          return M_PORT;
        case 3: // M_DBMS_INFO
          return M_DBMS_INFO;
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
  private static final int __MPORT_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.M_ADDRESS, new org.apache.thrift.meta_data.FieldMetaData("mAddress", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.M_PORT, new org.apache.thrift.meta_data.FieldMetaData("mPort", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.M_DBMS_INFO, new org.apache.thrift.meta_data.FieldMetaData("mDbmsInfo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, DbmsInfo.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ServerWorkerInfo.class, metaDataMap);
  }

  public ServerWorkerInfo() {
  }

  public ServerWorkerInfo(
    String mAddress,
    int mPort,
    DbmsInfo mDbmsInfo)
  {
    this();
    this.mAddress = mAddress;
    this.mPort = mPort;
    setMPortIsSet(true);
    this.mDbmsInfo = mDbmsInfo;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ServerWorkerInfo(ServerWorkerInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetMAddress()) {
      this.mAddress = other.mAddress;
    }
    this.mPort = other.mPort;
    if (other.isSetMDbmsInfo()) {
      this.mDbmsInfo = new DbmsInfo(other.mDbmsInfo);
    }
  }

  public ServerWorkerInfo deepCopy() {
    return new ServerWorkerInfo(this);
  }

  @Override
  public void clear() {
    this.mAddress = null;
    setMPortIsSet(false);
    this.mPort = 0;
    this.mDbmsInfo = null;
  }

  public String getMAddress() {
    return this.mAddress;
  }

  public ServerWorkerInfo setMAddress(String mAddress) {
    this.mAddress = mAddress;
    return this;
  }

  public void unsetMAddress() {
    this.mAddress = null;
  }

  /** Returns true if field mAddress is set (has been assigned a value) and false otherwise */
  public boolean isSetMAddress() {
    return this.mAddress != null;
  }

  public void setMAddressIsSet(boolean value) {
    if (!value) {
      this.mAddress = null;
    }
  }

  public int getMPort() {
    return this.mPort;
  }

  public ServerWorkerInfo setMPort(int mPort) {
    this.mPort = mPort;
    setMPortIsSet(true);
    return this;
  }

  public void unsetMPort() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MPORT_ISSET_ID);
  }

  /** Returns true if field mPort is set (has been assigned a value) and false otherwise */
  public boolean isSetMPort() {
    return EncodingUtils.testBit(__isset_bitfield, __MPORT_ISSET_ID);
  }

  public void setMPortIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MPORT_ISSET_ID, value);
  }

  public DbmsInfo getMDbmsInfo() {
    return this.mDbmsInfo;
  }

  public ServerWorkerInfo setMDbmsInfo(DbmsInfo mDbmsInfo) {
    this.mDbmsInfo = mDbmsInfo;
    return this;
  }

  public void unsetMDbmsInfo() {
    this.mDbmsInfo = null;
  }

  /** Returns true if field mDbmsInfo is set (has been assigned a value) and false otherwise */
  public boolean isSetMDbmsInfo() {
    return this.mDbmsInfo != null;
  }

  public void setMDbmsInfoIsSet(boolean value) {
    if (!value) {
      this.mDbmsInfo = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case M_ADDRESS:
      if (value == null) {
        unsetMAddress();
      } else {
        setMAddress((String)value);
      }
      break;

    case M_PORT:
      if (value == null) {
        unsetMPort();
      } else {
        setMPort((Integer)value);
      }
      break;

    case M_DBMS_INFO:
      if (value == null) {
        unsetMDbmsInfo();
      } else {
        setMDbmsInfo((DbmsInfo)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_ADDRESS:
      return getMAddress();

    case M_PORT:
      return Integer.valueOf(getMPort());

    case M_DBMS_INFO:
      return getMDbmsInfo();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case M_ADDRESS:
      return isSetMAddress();
    case M_PORT:
      return isSetMPort();
    case M_DBMS_INFO:
      return isSetMDbmsInfo();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ServerWorkerInfo)
      return this.equals((ServerWorkerInfo)that);
    return false;
  }

  public boolean equals(ServerWorkerInfo that) {
    if (that == null)
      return false;

    boolean this_present_mAddress = true && this.isSetMAddress();
    boolean that_present_mAddress = true && that.isSetMAddress();
    if (this_present_mAddress || that_present_mAddress) {
      if (!(this_present_mAddress && that_present_mAddress))
        return false;
      if (!this.mAddress.equals(that.mAddress))
        return false;
    }

    boolean this_present_mPort = true;
    boolean that_present_mPort = true;
    if (this_present_mPort || that_present_mPort) {
      if (!(this_present_mPort && that_present_mPort))
        return false;
      if (this.mPort != that.mPort)
        return false;
    }

    boolean this_present_mDbmsInfo = true && this.isSetMDbmsInfo();
    boolean that_present_mDbmsInfo = true && that.isSetMDbmsInfo();
    if (this_present_mDbmsInfo || that_present_mDbmsInfo) {
      if (!(this_present_mDbmsInfo && that_present_mDbmsInfo))
        return false;
      if (!this.mDbmsInfo.equals(that.mDbmsInfo))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_mAddress = true && (isSetMAddress());
    list.add(present_mAddress);
    if (present_mAddress)
      list.add(mAddress);

    boolean present_mPort = true;
    list.add(present_mPort);
    if (present_mPort)
      list.add(mPort);

    boolean present_mDbmsInfo = true && (isSetMDbmsInfo());
    list.add(present_mDbmsInfo);
    if (present_mDbmsInfo)
      list.add(mDbmsInfo);

    return list.hashCode();
  }

  @Override
  public int compareTo(ServerWorkerInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMAddress()).compareTo(other.isSetMAddress());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMAddress()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mAddress, other.mAddress);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMPort()).compareTo(other.isSetMPort());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMPort()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mPort, other.mPort);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMDbmsInfo()).compareTo(other.isSetMDbmsInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMDbmsInfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mDbmsInfo, other.mDbmsInfo);
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
    StringBuilder sb = new StringBuilder("ServerWorkerInfo(");
    boolean first = true;

    sb.append("mAddress:");
    if (this.mAddress == null) {
      sb.append("null");
    } else {
      sb.append(this.mAddress);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mPort:");
    sb.append(this.mPort);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mDbmsInfo:");
    if (this.mDbmsInfo == null) {
      sb.append("null");
    } else {
      sb.append(this.mDbmsInfo);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (mDbmsInfo != null) {
      mDbmsInfo.validate();
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ServerWorkerInfoStandardSchemeFactory implements SchemeFactory {
    public ServerWorkerInfoStandardScheme getScheme() {
      return new ServerWorkerInfoStandardScheme();
    }
  }

  private static class ServerWorkerInfoStandardScheme extends StandardScheme<ServerWorkerInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ServerWorkerInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // M_ADDRESS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.mAddress = iprot.readString();
              struct.setMAddressIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // M_PORT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.mPort = iprot.readI32();
              struct.setMPortIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // M_DBMS_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.mDbmsInfo = new DbmsInfo();
              struct.mDbmsInfo.read(iprot);
              struct.setMDbmsInfoIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ServerWorkerInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.mAddress != null) {
        oprot.writeFieldBegin(M_ADDRESS_FIELD_DESC);
        oprot.writeString(struct.mAddress);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(M_PORT_FIELD_DESC);
      oprot.writeI32(struct.mPort);
      oprot.writeFieldEnd();
      if (struct.mDbmsInfo != null) {
        oprot.writeFieldBegin(M_DBMS_INFO_FIELD_DESC);
        struct.mDbmsInfo.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ServerWorkerInfoTupleSchemeFactory implements SchemeFactory {
    public ServerWorkerInfoTupleScheme getScheme() {
      return new ServerWorkerInfoTupleScheme();
    }
  }

  private static class ServerWorkerInfoTupleScheme extends TupleScheme<ServerWorkerInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ServerWorkerInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMAddress()) {
        optionals.set(0);
      }
      if (struct.isSetMPort()) {
        optionals.set(1);
      }
      if (struct.isSetMDbmsInfo()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetMAddress()) {
        oprot.writeString(struct.mAddress);
      }
      if (struct.isSetMPort()) {
        oprot.writeI32(struct.mPort);
      }
      if (struct.isSetMDbmsInfo()) {
        struct.mDbmsInfo.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ServerWorkerInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.mAddress = iprot.readString();
        struct.setMAddressIsSet(true);
      }
      if (incoming.get(1)) {
        struct.mPort = iprot.readI32();
        struct.setMPortIsSet(true);
      }
      if (incoming.get(2)) {
        struct.mDbmsInfo = new DbmsInfo();
        struct.mDbmsInfo.read(iprot);
        struct.setMDbmsInfoIsSet(true);
      }
    }
  }

}

