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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-8-25")
public class TableInfo implements org.apache.thrift.TBase<TableInfo, TableInfo._Fields>, java.io.Serializable, Cloneable, Comparable<TableInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TableInfo");

  private static final org.apache.thrift.protocol.TField M_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("mName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField M_COLUMNS_FIELD_DESC = new org.apache.thrift.protocol.TField("mColumns", org.apache.thrift.protocol.TType.LIST, (short)2);
  private static final org.apache.thrift.protocol.TField M_PRIMARY_KEY_POS_FIELD_DESC = new org.apache.thrift.protocol.TField("mPrimaryKeyPos", org.apache.thrift.protocol.TType.LIST, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TableInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TableInfoTupleSchemeFactory());
  }

  public String mName; // required
  public List<ColumnInfo> mColumns; // required
  public List<Integer> mPrimaryKeyPos; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    M_NAME((short)1, "mName"),
    M_COLUMNS((short)2, "mColumns"),
    M_PRIMARY_KEY_POS((short)3, "mPrimaryKeyPos");

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
        case 2: // M_COLUMNS
          return M_COLUMNS;
        case 3: // M_PRIMARY_KEY_POS
          return M_PRIMARY_KEY_POS;
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
    tmpMap.put(_Fields.M_COLUMNS, new org.apache.thrift.meta_data.FieldMetaData("mColumns", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ColumnInfo.class))));
    tmpMap.put(_Fields.M_PRIMARY_KEY_POS, new org.apache.thrift.meta_data.FieldMetaData("mPrimaryKeyPos", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TableInfo.class, metaDataMap);
  }

  public TableInfo() {
  }

  public TableInfo(
    String mName,
    List<ColumnInfo> mColumns,
    List<Integer> mPrimaryKeyPos)
  {
    this();
    this.mName = mName;
    this.mColumns = mColumns;
    this.mPrimaryKeyPos = mPrimaryKeyPos;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TableInfo(TableInfo other) {
    if (other.isSetMName()) {
      this.mName = other.mName;
    }
    if (other.isSetMColumns()) {
      List<ColumnInfo> __this__mColumns = new ArrayList<ColumnInfo>(other.mColumns.size());
      for (ColumnInfo other_element : other.mColumns) {
        __this__mColumns.add(new ColumnInfo(other_element));
      }
      this.mColumns = __this__mColumns;
    }
    if (other.isSetMPrimaryKeyPos()) {
      List<Integer> __this__mPrimaryKeyPos = new ArrayList<Integer>(other.mPrimaryKeyPos);
      this.mPrimaryKeyPos = __this__mPrimaryKeyPos;
    }
  }

  public TableInfo deepCopy() {
    return new TableInfo(this);
  }

  @Override
  public void clear() {
    this.mName = null;
    this.mColumns = null;
    this.mPrimaryKeyPos = null;
  }

  public String getMName() {
    return this.mName;
  }

  public TableInfo setMName(String mName) {
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

  public int getMColumnsSize() {
    return (this.mColumns == null) ? 0 : this.mColumns.size();
  }

  public java.util.Iterator<ColumnInfo> getMColumnsIterator() {
    return (this.mColumns == null) ? null : this.mColumns.iterator();
  }

  public void addToMColumns(ColumnInfo elem) {
    if (this.mColumns == null) {
      this.mColumns = new ArrayList<ColumnInfo>();
    }
    this.mColumns.add(elem);
  }

  public List<ColumnInfo> getMColumns() {
    return this.mColumns;
  }

  public TableInfo setMColumns(List<ColumnInfo> mColumns) {
    this.mColumns = mColumns;
    return this;
  }

  public void unsetMColumns() {
    this.mColumns = null;
  }

  /** Returns true if field mColumns is set (has been assigned a value) and false otherwise */
  public boolean isSetMColumns() {
    return this.mColumns != null;
  }

  public void setMColumnsIsSet(boolean value) {
    if (!value) {
      this.mColumns = null;
    }
  }

  public int getMPrimaryKeyPosSize() {
    return (this.mPrimaryKeyPos == null) ? 0 : this.mPrimaryKeyPos.size();
  }

  public java.util.Iterator<Integer> getMPrimaryKeyPosIterator() {
    return (this.mPrimaryKeyPos == null) ? null : this.mPrimaryKeyPos.iterator();
  }

  public void addToMPrimaryKeyPos(int elem) {
    if (this.mPrimaryKeyPos == null) {
      this.mPrimaryKeyPos = new ArrayList<Integer>();
    }
    this.mPrimaryKeyPos.add(elem);
  }

  public List<Integer> getMPrimaryKeyPos() {
    return this.mPrimaryKeyPos;
  }

  public TableInfo setMPrimaryKeyPos(List<Integer> mPrimaryKeyPos) {
    this.mPrimaryKeyPos = mPrimaryKeyPos;
    return this;
  }

  public void unsetMPrimaryKeyPos() {
    this.mPrimaryKeyPos = null;
  }

  /** Returns true if field mPrimaryKeyPos is set (has been assigned a value) and false otherwise */
  public boolean isSetMPrimaryKeyPos() {
    return this.mPrimaryKeyPos != null;
  }

  public void setMPrimaryKeyPosIsSet(boolean value) {
    if (!value) {
      this.mPrimaryKeyPos = null;
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

    case M_COLUMNS:
      if (value == null) {
        unsetMColumns();
      } else {
        setMColumns((List<ColumnInfo>)value);
      }
      break;

    case M_PRIMARY_KEY_POS:
      if (value == null) {
        unsetMPrimaryKeyPos();
      } else {
        setMPrimaryKeyPos((List<Integer>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_NAME:
      return getMName();

    case M_COLUMNS:
      return getMColumns();

    case M_PRIMARY_KEY_POS:
      return getMPrimaryKeyPos();

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
    case M_COLUMNS:
      return isSetMColumns();
    case M_PRIMARY_KEY_POS:
      return isSetMPrimaryKeyPos();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TableInfo)
      return this.equals((TableInfo)that);
    return false;
  }

  public boolean equals(TableInfo that) {
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

    boolean this_present_mColumns = true && this.isSetMColumns();
    boolean that_present_mColumns = true && that.isSetMColumns();
    if (this_present_mColumns || that_present_mColumns) {
      if (!(this_present_mColumns && that_present_mColumns))
        return false;
      if (!this.mColumns.equals(that.mColumns))
        return false;
    }

    boolean this_present_mPrimaryKeyPos = true && this.isSetMPrimaryKeyPos();
    boolean that_present_mPrimaryKeyPos = true && that.isSetMPrimaryKeyPos();
    if (this_present_mPrimaryKeyPos || that_present_mPrimaryKeyPos) {
      if (!(this_present_mPrimaryKeyPos && that_present_mPrimaryKeyPos))
        return false;
      if (!this.mPrimaryKeyPos.equals(that.mPrimaryKeyPos))
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

    boolean present_mColumns = true && (isSetMColumns());
    list.add(present_mColumns);
    if (present_mColumns)
      list.add(mColumns);

    boolean present_mPrimaryKeyPos = true && (isSetMPrimaryKeyPos());
    list.add(present_mPrimaryKeyPos);
    if (present_mPrimaryKeyPos)
      list.add(mPrimaryKeyPos);

    return list.hashCode();
  }

  @Override
  public int compareTo(TableInfo other) {
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
    lastComparison = Boolean.valueOf(isSetMColumns()).compareTo(other.isSetMColumns());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMColumns()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mColumns, other.mColumns);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMPrimaryKeyPos()).compareTo(other.isSetMPrimaryKeyPos());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMPrimaryKeyPos()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mPrimaryKeyPos, other.mPrimaryKeyPos);
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
    StringBuilder sb = new StringBuilder("TableInfo(");
    boolean first = true;

    sb.append("mName:");
    if (this.mName == null) {
      sb.append("null");
    } else {
      sb.append(this.mName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mColumns:");
    if (this.mColumns == null) {
      sb.append("null");
    } else {
      sb.append(this.mColumns);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mPrimaryKeyPos:");
    if (this.mPrimaryKeyPos == null) {
      sb.append("null");
    } else {
      sb.append(this.mPrimaryKeyPos);
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

  private static class TableInfoStandardSchemeFactory implements SchemeFactory {
    public TableInfoStandardScheme getScheme() {
      return new TableInfoStandardScheme();
    }
  }

  private static class TableInfoStandardScheme extends StandardScheme<TableInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TableInfo struct) throws org.apache.thrift.TException {
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
          case 2: // M_COLUMNS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list16 = iprot.readListBegin();
                struct.mColumns = new ArrayList<ColumnInfo>(_list16.size);
                ColumnInfo _elem17;
                for (int _i18 = 0; _i18 < _list16.size; ++_i18)
                {
                  _elem17 = new ColumnInfo();
                  _elem17.read(iprot);
                  struct.mColumns.add(_elem17);
                }
                iprot.readListEnd();
              }
              struct.setMColumnsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // M_PRIMARY_KEY_POS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list19 = iprot.readListBegin();
                struct.mPrimaryKeyPos = new ArrayList<Integer>(_list19.size);
                int _elem20;
                for (int _i21 = 0; _i21 < _list19.size; ++_i21)
                {
                  _elem20 = iprot.readI32();
                  struct.mPrimaryKeyPos.add(_elem20);
                }
                iprot.readListEnd();
              }
              struct.setMPrimaryKeyPosIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, TableInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.mName != null) {
        oprot.writeFieldBegin(M_NAME_FIELD_DESC);
        oprot.writeString(struct.mName);
        oprot.writeFieldEnd();
      }
      if (struct.mColumns != null) {
        oprot.writeFieldBegin(M_COLUMNS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.mColumns.size()));
          for (ColumnInfo _iter22 : struct.mColumns)
          {
            _iter22.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.mPrimaryKeyPos != null) {
        oprot.writeFieldBegin(M_PRIMARY_KEY_POS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, struct.mPrimaryKeyPos.size()));
          for (int _iter23 : struct.mPrimaryKeyPos)
          {
            oprot.writeI32(_iter23);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TableInfoTupleSchemeFactory implements SchemeFactory {
    public TableInfoTupleScheme getScheme() {
      return new TableInfoTupleScheme();
    }
  }

  private static class TableInfoTupleScheme extends TupleScheme<TableInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TableInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMName()) {
        optionals.set(0);
      }
      if (struct.isSetMColumns()) {
        optionals.set(1);
      }
      if (struct.isSetMPrimaryKeyPos()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetMName()) {
        oprot.writeString(struct.mName);
      }
      if (struct.isSetMColumns()) {
        {
          oprot.writeI32(struct.mColumns.size());
          for (ColumnInfo _iter24 : struct.mColumns)
          {
            _iter24.write(oprot);
          }
        }
      }
      if (struct.isSetMPrimaryKeyPos()) {
        {
          oprot.writeI32(struct.mPrimaryKeyPos.size());
          for (int _iter25 : struct.mPrimaryKeyPos)
          {
            oprot.writeI32(_iter25);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TableInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.mName = iprot.readString();
        struct.setMNameIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list26 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.mColumns = new ArrayList<ColumnInfo>(_list26.size);
          ColumnInfo _elem27;
          for (int _i28 = 0; _i28 < _list26.size; ++_i28)
          {
            _elem27 = new ColumnInfo();
            _elem27.read(iprot);
            struct.mColumns.add(_elem27);
          }
        }
        struct.setMColumnsIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TList _list29 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I32, iprot.readI32());
          struct.mPrimaryKeyPos = new ArrayList<Integer>(_list29.size);
          int _elem30;
          for (int _i31 = 0; _i31 < _list29.size; ++_i31)
          {
            _elem30 = iprot.readI32();
            struct.mPrimaryKeyPos.add(_elem30);
          }
        }
        struct.setMPrimaryKeyPosIsSet(true);
      }
    }
  }

}
