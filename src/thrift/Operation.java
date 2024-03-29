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
public class Operation implements org.apache.thrift.TBase<Operation, Operation._Fields>, java.io.Serializable, Cloneable, Comparable<Operation> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Operation");

  private static final org.apache.thrift.protocol.TField M_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("mType", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField M_PARAS_FIELD_DESC = new org.apache.thrift.protocol.TField("mParas", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new OperationStandardSchemeFactory());
    schemes.put(TupleScheme.class, new OperationTupleSchemeFactory());
  }

  /**
   * 
   * @see OperationType
   */
  public OperationType mType; // required
  public OperationPara mParas; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see OperationType
     */
    M_TYPE((short)1, "mType"),
    M_PARAS((short)2, "mParas");

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
        case 2: // M_PARAS
          return M_PARAS;
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
    tmpMap.put(_Fields.M_TYPE, new org.apache.thrift.meta_data.FieldMetaData("mType", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, OperationType.class)));
    tmpMap.put(_Fields.M_PARAS, new org.apache.thrift.meta_data.FieldMetaData("mParas", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, OperationPara.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Operation.class, metaDataMap);
  }

  public Operation() {
  }

  public Operation(
    OperationType mType,
    OperationPara mParas)
  {
    this();
    this.mType = mType;
    this.mParas = mParas;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Operation(Operation other) {
    if (other.isSetMType()) {
      this.mType = other.mType;
    }
    if (other.isSetMParas()) {
      this.mParas = new OperationPara(other.mParas);
    }
  }

  public Operation deepCopy() {
    return new Operation(this);
  }

  @Override
  public void clear() {
    this.mType = null;
    this.mParas = null;
  }

  /**
   * 
   * @see OperationType
   */
  public OperationType getMType() {
    return this.mType;
  }

  /**
   * 
   * @see OperationType
   */
  public Operation setMType(OperationType mType) {
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

  public OperationPara getMParas() {
    return this.mParas;
  }

  public Operation setMParas(OperationPara mParas) {
    this.mParas = mParas;
    return this;
  }

  public void unsetMParas() {
    this.mParas = null;
  }

  /** Returns true if field mParas is set (has been assigned a value) and false otherwise */
  public boolean isSetMParas() {
    return this.mParas != null;
  }

  public void setMParasIsSet(boolean value) {
    if (!value) {
      this.mParas = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case M_TYPE:
      if (value == null) {
        unsetMType();
      } else {
        setMType((OperationType)value);
      }
      break;

    case M_PARAS:
      if (value == null) {
        unsetMParas();
      } else {
        setMParas((OperationPara)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_TYPE:
      return getMType();

    case M_PARAS:
      return getMParas();

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
    case M_PARAS:
      return isSetMParas();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Operation)
      return this.equals((Operation)that);
    return false;
  }

  public boolean equals(Operation that) {
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

    boolean this_present_mParas = true && this.isSetMParas();
    boolean that_present_mParas = true && that.isSetMParas();
    if (this_present_mParas || that_present_mParas) {
      if (!(this_present_mParas && that_present_mParas))
        return false;
      if (!this.mParas.equals(that.mParas))
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

    boolean present_mParas = true && (isSetMParas());
    list.add(present_mParas);
    if (present_mParas)
      list.add(mParas);

    return list.hashCode();
  }

  @Override
  public int compareTo(Operation other) {
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
    lastComparison = Boolean.valueOf(isSetMParas()).compareTo(other.isSetMParas());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMParas()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mParas, other.mParas);
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
    StringBuilder sb = new StringBuilder("Operation(");
    boolean first = true;

    sb.append("mType:");
    if (this.mType == null) {
      sb.append("null");
    } else {
      sb.append(this.mType);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mParas:");
    if (this.mParas == null) {
      sb.append("null");
    } else {
      sb.append(this.mParas);
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

  private static class OperationStandardSchemeFactory implements SchemeFactory {
    public OperationStandardScheme getScheme() {
      return new OperationStandardScheme();
    }
  }

  private static class OperationStandardScheme extends StandardScheme<Operation> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Operation struct) throws org.apache.thrift.TException {
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
              struct.mType = thrift.OperationType.findByValue(iprot.readI32());
              struct.setMTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // M_PARAS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.mParas = new OperationPara();
              struct.mParas.read(iprot);
              struct.setMParasIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Operation struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.mType != null) {
        oprot.writeFieldBegin(M_TYPE_FIELD_DESC);
        oprot.writeI32(struct.mType.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.mParas != null) {
        oprot.writeFieldBegin(M_PARAS_FIELD_DESC);
        struct.mParas.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class OperationTupleSchemeFactory implements SchemeFactory {
    public OperationTupleScheme getScheme() {
      return new OperationTupleScheme();
    }
  }

  private static class OperationTupleScheme extends TupleScheme<Operation> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Operation struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMType()) {
        optionals.set(0);
      }
      if (struct.isSetMParas()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetMType()) {
        oprot.writeI32(struct.mType.getValue());
      }
      if (struct.isSetMParas()) {
        struct.mParas.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Operation struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.mType = thrift.OperationType.findByValue(iprot.readI32());
        struct.setMTypeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.mParas = new OperationPara();
        struct.mParas.read(iprot);
        struct.setMParasIsSet(true);
      }
    }
  }

}

