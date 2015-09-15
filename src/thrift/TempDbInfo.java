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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-9-15")
public class TempDbInfo implements org.apache.thrift.TBase<TempDbInfo, TempDbInfo._Fields>, java.io.Serializable, Cloneable, Comparable<TempDbInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TempDbInfo");

  private static final org.apache.thrift.protocol.TField M_TEMP_TABLES_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("mTempTablesInfo", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TempDbInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TempDbInfoTupleSchemeFactory());
  }

  public List<TempTableInfo> mTempTablesInfo; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    M_TEMP_TABLES_INFO((short)1, "mTempTablesInfo");

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
        case 1: // M_TEMP_TABLES_INFO
          return M_TEMP_TABLES_INFO;
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
    tmpMap.put(_Fields.M_TEMP_TABLES_INFO, new org.apache.thrift.meta_data.FieldMetaData("mTempTablesInfo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TempTableInfo.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TempDbInfo.class, metaDataMap);
  }

  public TempDbInfo() {
  }

  public TempDbInfo(
    List<TempTableInfo> mTempTablesInfo)
  {
    this();
    this.mTempTablesInfo = mTempTablesInfo;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TempDbInfo(TempDbInfo other) {
    if (other.isSetMTempTablesInfo()) {
      List<TempTableInfo> __this__mTempTablesInfo = new ArrayList<TempTableInfo>(other.mTempTablesInfo.size());
      for (TempTableInfo other_element : other.mTempTablesInfo) {
        __this__mTempTablesInfo.add(new TempTableInfo(other_element));
      }
      this.mTempTablesInfo = __this__mTempTablesInfo;
    }
  }

  public TempDbInfo deepCopy() {
    return new TempDbInfo(this);
  }

  @Override
  public void clear() {
    this.mTempTablesInfo = null;
  }

  public int getMTempTablesInfoSize() {
    return (this.mTempTablesInfo == null) ? 0 : this.mTempTablesInfo.size();
  }

  public java.util.Iterator<TempTableInfo> getMTempTablesInfoIterator() {
    return (this.mTempTablesInfo == null) ? null : this.mTempTablesInfo.iterator();
  }

  public void addToMTempTablesInfo(TempTableInfo elem) {
    if (this.mTempTablesInfo == null) {
      this.mTempTablesInfo = new ArrayList<TempTableInfo>();
    }
    this.mTempTablesInfo.add(elem);
  }

  public List<TempTableInfo> getMTempTablesInfo() {
    return this.mTempTablesInfo;
  }

  public TempDbInfo setMTempTablesInfo(List<TempTableInfo> mTempTablesInfo) {
    this.mTempTablesInfo = mTempTablesInfo;
    return this;
  }

  public void unsetMTempTablesInfo() {
    this.mTempTablesInfo = null;
  }

  /** Returns true if field mTempTablesInfo is set (has been assigned a value) and false otherwise */
  public boolean isSetMTempTablesInfo() {
    return this.mTempTablesInfo != null;
  }

  public void setMTempTablesInfoIsSet(boolean value) {
    if (!value) {
      this.mTempTablesInfo = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case M_TEMP_TABLES_INFO:
      if (value == null) {
        unsetMTempTablesInfo();
      } else {
        setMTempTablesInfo((List<TempTableInfo>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_TEMP_TABLES_INFO:
      return getMTempTablesInfo();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case M_TEMP_TABLES_INFO:
      return isSetMTempTablesInfo();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TempDbInfo)
      return this.equals((TempDbInfo)that);
    return false;
  }

  public boolean equals(TempDbInfo that) {
    if (that == null)
      return false;

    boolean this_present_mTempTablesInfo = true && this.isSetMTempTablesInfo();
    boolean that_present_mTempTablesInfo = true && that.isSetMTempTablesInfo();
    if (this_present_mTempTablesInfo || that_present_mTempTablesInfo) {
      if (!(this_present_mTempTablesInfo && that_present_mTempTablesInfo))
        return false;
      if (!this.mTempTablesInfo.equals(that.mTempTablesInfo))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_mTempTablesInfo = true && (isSetMTempTablesInfo());
    list.add(present_mTempTablesInfo);
    if (present_mTempTablesInfo)
      list.add(mTempTablesInfo);

    return list.hashCode();
  }

  @Override
  public int compareTo(TempDbInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMTempTablesInfo()).compareTo(other.isSetMTempTablesInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMTempTablesInfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mTempTablesInfo, other.mTempTablesInfo);
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
    StringBuilder sb = new StringBuilder("TempDbInfo(");
    boolean first = true;

    sb.append("mTempTablesInfo:");
    if (this.mTempTablesInfo == null) {
      sb.append("null");
    } else {
      sb.append(this.mTempTablesInfo);
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TempDbInfoStandardSchemeFactory implements SchemeFactory {
    public TempDbInfoStandardScheme getScheme() {
      return new TempDbInfoStandardScheme();
    }
  }

  private static class TempDbInfoStandardScheme extends StandardScheme<TempDbInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TempDbInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // M_TEMP_TABLES_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list32 = iprot.readListBegin();
                struct.mTempTablesInfo = new ArrayList<TempTableInfo>(_list32.size);
                TempTableInfo _elem33;
                for (int _i34 = 0; _i34 < _list32.size; ++_i34)
                {
                  _elem33 = new TempTableInfo();
                  _elem33.read(iprot);
                  struct.mTempTablesInfo.add(_elem33);
                }
                iprot.readListEnd();
              }
              struct.setMTempTablesInfoIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, TempDbInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.mTempTablesInfo != null) {
        oprot.writeFieldBegin(M_TEMP_TABLES_INFO_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.mTempTablesInfo.size()));
          for (TempTableInfo _iter35 : struct.mTempTablesInfo)
          {
            _iter35.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TempDbInfoTupleSchemeFactory implements SchemeFactory {
    public TempDbInfoTupleScheme getScheme() {
      return new TempDbInfoTupleScheme();
    }
  }

  private static class TempDbInfoTupleScheme extends TupleScheme<TempDbInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TempDbInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMTempTablesInfo()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetMTempTablesInfo()) {
        {
          oprot.writeI32(struct.mTempTablesInfo.size());
          for (TempTableInfo _iter36 : struct.mTempTablesInfo)
          {
            _iter36.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TempDbInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list37 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.mTempTablesInfo = new ArrayList<TempTableInfo>(_list37.size);
          TempTableInfo _elem38;
          for (int _i39 = 0; _i39 < _list37.size; ++_i39)
          {
            _elem38 = new TempTableInfo();
            _elem38.read(iprot);
            struct.mTempTablesInfo.add(_elem38);
          }
        }
        struct.setMTempTablesInfoIsSet(true);
      }
    }
  }

}

