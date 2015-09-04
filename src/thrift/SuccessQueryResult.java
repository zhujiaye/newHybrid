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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-9-4")
public class SuccessQueryResult implements org.apache.thrift.TBase<SuccessQueryResult, SuccessQueryResult._Fields>, java.io.Serializable, Cloneable, Comparable<SuccessQueryResult> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SuccessQueryResult");

  private static final org.apache.thrift.protocol.TField M_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("mID", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField M_IS_IN_MYSQL_FIELD_DESC = new org.apache.thrift.protocol.TField("mIsInMysql", org.apache.thrift.protocol.TType.BOOL, (short)2);
  private static final org.apache.thrift.protocol.TField M_IS_READ_FIELD_DESC = new org.apache.thrift.protocol.TField("mIsRead", org.apache.thrift.protocol.TType.BOOL, (short)3);
  private static final org.apache.thrift.protocol.TField M_START_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("mStartTime", org.apache.thrift.protocol.TType.I64, (short)4);
  private static final org.apache.thrift.protocol.TField M_END_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("mEndTime", org.apache.thrift.protocol.TType.I64, (short)5);
  private static final org.apache.thrift.protocol.TField M_LATENCY_FIELD_DESC = new org.apache.thrift.protocol.TField("mLatency", org.apache.thrift.protocol.TType.I64, (short)6);
  private static final org.apache.thrift.protocol.TField M_SPLIT_FIELD_DESC = new org.apache.thrift.protocol.TField("mSplit", org.apache.thrift.protocol.TType.I32, (short)7);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new SuccessQueryResultStandardSchemeFactory());
    schemes.put(TupleScheme.class, new SuccessQueryResultTupleSchemeFactory());
  }

  public int mID; // required
  public boolean mIsInMysql; // required
  public boolean mIsRead; // required
  public long mStartTime; // required
  public long mEndTime; // required
  public long mLatency; // required
  public int mSplit; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    M_ID((short)1, "mID"),
    M_IS_IN_MYSQL((short)2, "mIsInMysql"),
    M_IS_READ((short)3, "mIsRead"),
    M_START_TIME((short)4, "mStartTime"),
    M_END_TIME((short)5, "mEndTime"),
    M_LATENCY((short)6, "mLatency"),
    M_SPLIT((short)7, "mSplit");

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
        case 2: // M_IS_IN_MYSQL
          return M_IS_IN_MYSQL;
        case 3: // M_IS_READ
          return M_IS_READ;
        case 4: // M_START_TIME
          return M_START_TIME;
        case 5: // M_END_TIME
          return M_END_TIME;
        case 6: // M_LATENCY
          return M_LATENCY;
        case 7: // M_SPLIT
          return M_SPLIT;
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
  private static final int __MISINMYSQL_ISSET_ID = 1;
  private static final int __MISREAD_ISSET_ID = 2;
  private static final int __MSTARTTIME_ISSET_ID = 3;
  private static final int __MENDTIME_ISSET_ID = 4;
  private static final int __MLATENCY_ISSET_ID = 5;
  private static final int __MSPLIT_ISSET_ID = 6;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.M_ID, new org.apache.thrift.meta_data.FieldMetaData("mID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.M_IS_IN_MYSQL, new org.apache.thrift.meta_data.FieldMetaData("mIsInMysql", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.M_IS_READ, new org.apache.thrift.meta_data.FieldMetaData("mIsRead", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.M_START_TIME, new org.apache.thrift.meta_data.FieldMetaData("mStartTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.M_END_TIME, new org.apache.thrift.meta_data.FieldMetaData("mEndTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.M_LATENCY, new org.apache.thrift.meta_data.FieldMetaData("mLatency", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.M_SPLIT, new org.apache.thrift.meta_data.FieldMetaData("mSplit", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SuccessQueryResult.class, metaDataMap);
  }

  public SuccessQueryResult() {
  }

  public SuccessQueryResult(
    int mID,
    boolean mIsInMysql,
    boolean mIsRead,
    long mStartTime,
    long mEndTime,
    long mLatency,
    int mSplit)
  {
    this();
    this.mID = mID;
    setMIDIsSet(true);
    this.mIsInMysql = mIsInMysql;
    setMIsInMysqlIsSet(true);
    this.mIsRead = mIsRead;
    setMIsReadIsSet(true);
    this.mStartTime = mStartTime;
    setMStartTimeIsSet(true);
    this.mEndTime = mEndTime;
    setMEndTimeIsSet(true);
    this.mLatency = mLatency;
    setMLatencyIsSet(true);
    this.mSplit = mSplit;
    setMSplitIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SuccessQueryResult(SuccessQueryResult other) {
    __isset_bitfield = other.__isset_bitfield;
    this.mID = other.mID;
    this.mIsInMysql = other.mIsInMysql;
    this.mIsRead = other.mIsRead;
    this.mStartTime = other.mStartTime;
    this.mEndTime = other.mEndTime;
    this.mLatency = other.mLatency;
    this.mSplit = other.mSplit;
  }

  public SuccessQueryResult deepCopy() {
    return new SuccessQueryResult(this);
  }

  @Override
  public void clear() {
    setMIDIsSet(false);
    this.mID = 0;
    setMIsInMysqlIsSet(false);
    this.mIsInMysql = false;
    setMIsReadIsSet(false);
    this.mIsRead = false;
    setMStartTimeIsSet(false);
    this.mStartTime = 0;
    setMEndTimeIsSet(false);
    this.mEndTime = 0;
    setMLatencyIsSet(false);
    this.mLatency = 0;
    setMSplitIsSet(false);
    this.mSplit = 0;
  }

  public int getMID() {
    return this.mID;
  }

  public SuccessQueryResult setMID(int mID) {
    this.mID = mID;
    setMIDIsSet(true);
    return this;
  }

  public void unsetMID() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MID_ISSET_ID);
  }

  /** Returns true if field mID is set (has been assigned a value) and false otherwise */
  public boolean isSetMID() {
    return EncodingUtils.testBit(__isset_bitfield, __MID_ISSET_ID);
  }

  public void setMIDIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MID_ISSET_ID, value);
  }

  public boolean isMIsInMysql() {
    return this.mIsInMysql;
  }

  public SuccessQueryResult setMIsInMysql(boolean mIsInMysql) {
    this.mIsInMysql = mIsInMysql;
    setMIsInMysqlIsSet(true);
    return this;
  }

  public void unsetMIsInMysql() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MISINMYSQL_ISSET_ID);
  }

  /** Returns true if field mIsInMysql is set (has been assigned a value) and false otherwise */
  public boolean isSetMIsInMysql() {
    return EncodingUtils.testBit(__isset_bitfield, __MISINMYSQL_ISSET_ID);
  }

  public void setMIsInMysqlIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MISINMYSQL_ISSET_ID, value);
  }

  public boolean isMIsRead() {
    return this.mIsRead;
  }

  public SuccessQueryResult setMIsRead(boolean mIsRead) {
    this.mIsRead = mIsRead;
    setMIsReadIsSet(true);
    return this;
  }

  public void unsetMIsRead() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MISREAD_ISSET_ID);
  }

  /** Returns true if field mIsRead is set (has been assigned a value) and false otherwise */
  public boolean isSetMIsRead() {
    return EncodingUtils.testBit(__isset_bitfield, __MISREAD_ISSET_ID);
  }

  public void setMIsReadIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MISREAD_ISSET_ID, value);
  }

  public long getMStartTime() {
    return this.mStartTime;
  }

  public SuccessQueryResult setMStartTime(long mStartTime) {
    this.mStartTime = mStartTime;
    setMStartTimeIsSet(true);
    return this;
  }

  public void unsetMStartTime() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MSTARTTIME_ISSET_ID);
  }

  /** Returns true if field mStartTime is set (has been assigned a value) and false otherwise */
  public boolean isSetMStartTime() {
    return EncodingUtils.testBit(__isset_bitfield, __MSTARTTIME_ISSET_ID);
  }

  public void setMStartTimeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MSTARTTIME_ISSET_ID, value);
  }

  public long getMEndTime() {
    return this.mEndTime;
  }

  public SuccessQueryResult setMEndTime(long mEndTime) {
    this.mEndTime = mEndTime;
    setMEndTimeIsSet(true);
    return this;
  }

  public void unsetMEndTime() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MENDTIME_ISSET_ID);
  }

  /** Returns true if field mEndTime is set (has been assigned a value) and false otherwise */
  public boolean isSetMEndTime() {
    return EncodingUtils.testBit(__isset_bitfield, __MENDTIME_ISSET_ID);
  }

  public void setMEndTimeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MENDTIME_ISSET_ID, value);
  }

  public long getMLatency() {
    return this.mLatency;
  }

  public SuccessQueryResult setMLatency(long mLatency) {
    this.mLatency = mLatency;
    setMLatencyIsSet(true);
    return this;
  }

  public void unsetMLatency() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MLATENCY_ISSET_ID);
  }

  /** Returns true if field mLatency is set (has been assigned a value) and false otherwise */
  public boolean isSetMLatency() {
    return EncodingUtils.testBit(__isset_bitfield, __MLATENCY_ISSET_ID);
  }

  public void setMLatencyIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MLATENCY_ISSET_ID, value);
  }

  public int getMSplit() {
    return this.mSplit;
  }

  public SuccessQueryResult setMSplit(int mSplit) {
    this.mSplit = mSplit;
    setMSplitIsSet(true);
    return this;
  }

  public void unsetMSplit() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MSPLIT_ISSET_ID);
  }

  /** Returns true if field mSplit is set (has been assigned a value) and false otherwise */
  public boolean isSetMSplit() {
    return EncodingUtils.testBit(__isset_bitfield, __MSPLIT_ISSET_ID);
  }

  public void setMSplitIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MSPLIT_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case M_ID:
      if (value == null) {
        unsetMID();
      } else {
        setMID((Integer)value);
      }
      break;

    case M_IS_IN_MYSQL:
      if (value == null) {
        unsetMIsInMysql();
      } else {
        setMIsInMysql((Boolean)value);
      }
      break;

    case M_IS_READ:
      if (value == null) {
        unsetMIsRead();
      } else {
        setMIsRead((Boolean)value);
      }
      break;

    case M_START_TIME:
      if (value == null) {
        unsetMStartTime();
      } else {
        setMStartTime((Long)value);
      }
      break;

    case M_END_TIME:
      if (value == null) {
        unsetMEndTime();
      } else {
        setMEndTime((Long)value);
      }
      break;

    case M_LATENCY:
      if (value == null) {
        unsetMLatency();
      } else {
        setMLatency((Long)value);
      }
      break;

    case M_SPLIT:
      if (value == null) {
        unsetMSplit();
      } else {
        setMSplit((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_ID:
      return Integer.valueOf(getMID());

    case M_IS_IN_MYSQL:
      return Boolean.valueOf(isMIsInMysql());

    case M_IS_READ:
      return Boolean.valueOf(isMIsRead());

    case M_START_TIME:
      return Long.valueOf(getMStartTime());

    case M_END_TIME:
      return Long.valueOf(getMEndTime());

    case M_LATENCY:
      return Long.valueOf(getMLatency());

    case M_SPLIT:
      return Integer.valueOf(getMSplit());

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
      return isSetMID();
    case M_IS_IN_MYSQL:
      return isSetMIsInMysql();
    case M_IS_READ:
      return isSetMIsRead();
    case M_START_TIME:
      return isSetMStartTime();
    case M_END_TIME:
      return isSetMEndTime();
    case M_LATENCY:
      return isSetMLatency();
    case M_SPLIT:
      return isSetMSplit();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof SuccessQueryResult)
      return this.equals((SuccessQueryResult)that);
    return false;
  }

  public boolean equals(SuccessQueryResult that) {
    if (that == null)
      return false;

    boolean this_present_mID = true;
    boolean that_present_mID = true;
    if (this_present_mID || that_present_mID) {
      if (!(this_present_mID && that_present_mID))
        return false;
      if (this.mID != that.mID)
        return false;
    }

    boolean this_present_mIsInMysql = true;
    boolean that_present_mIsInMysql = true;
    if (this_present_mIsInMysql || that_present_mIsInMysql) {
      if (!(this_present_mIsInMysql && that_present_mIsInMysql))
        return false;
      if (this.mIsInMysql != that.mIsInMysql)
        return false;
    }

    boolean this_present_mIsRead = true;
    boolean that_present_mIsRead = true;
    if (this_present_mIsRead || that_present_mIsRead) {
      if (!(this_present_mIsRead && that_present_mIsRead))
        return false;
      if (this.mIsRead != that.mIsRead)
        return false;
    }

    boolean this_present_mStartTime = true;
    boolean that_present_mStartTime = true;
    if (this_present_mStartTime || that_present_mStartTime) {
      if (!(this_present_mStartTime && that_present_mStartTime))
        return false;
      if (this.mStartTime != that.mStartTime)
        return false;
    }

    boolean this_present_mEndTime = true;
    boolean that_present_mEndTime = true;
    if (this_present_mEndTime || that_present_mEndTime) {
      if (!(this_present_mEndTime && that_present_mEndTime))
        return false;
      if (this.mEndTime != that.mEndTime)
        return false;
    }

    boolean this_present_mLatency = true;
    boolean that_present_mLatency = true;
    if (this_present_mLatency || that_present_mLatency) {
      if (!(this_present_mLatency && that_present_mLatency))
        return false;
      if (this.mLatency != that.mLatency)
        return false;
    }

    boolean this_present_mSplit = true;
    boolean that_present_mSplit = true;
    if (this_present_mSplit || that_present_mSplit) {
      if (!(this_present_mSplit && that_present_mSplit))
        return false;
      if (this.mSplit != that.mSplit)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_mID = true;
    list.add(present_mID);
    if (present_mID)
      list.add(mID);

    boolean present_mIsInMysql = true;
    list.add(present_mIsInMysql);
    if (present_mIsInMysql)
      list.add(mIsInMysql);

    boolean present_mIsRead = true;
    list.add(present_mIsRead);
    if (present_mIsRead)
      list.add(mIsRead);

    boolean present_mStartTime = true;
    list.add(present_mStartTime);
    if (present_mStartTime)
      list.add(mStartTime);

    boolean present_mEndTime = true;
    list.add(present_mEndTime);
    if (present_mEndTime)
      list.add(mEndTime);

    boolean present_mLatency = true;
    list.add(present_mLatency);
    if (present_mLatency)
      list.add(mLatency);

    boolean present_mSplit = true;
    list.add(present_mSplit);
    if (present_mSplit)
      list.add(mSplit);

    return list.hashCode();
  }

  @Override
  public int compareTo(SuccessQueryResult other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMID()).compareTo(other.isSetMID());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMID()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mID, other.mID);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMIsInMysql()).compareTo(other.isSetMIsInMysql());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMIsInMysql()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mIsInMysql, other.mIsInMysql);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMIsRead()).compareTo(other.isSetMIsRead());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMIsRead()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mIsRead, other.mIsRead);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMStartTime()).compareTo(other.isSetMStartTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMStartTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mStartTime, other.mStartTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMEndTime()).compareTo(other.isSetMEndTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMEndTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mEndTime, other.mEndTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMLatency()).compareTo(other.isSetMLatency());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMLatency()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mLatency, other.mLatency);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMSplit()).compareTo(other.isSetMSplit());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMSplit()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mSplit, other.mSplit);
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
    StringBuilder sb = new StringBuilder("SuccessQueryResult(");
    boolean first = true;

    sb.append("mID:");
    sb.append(this.mID);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mIsInMysql:");
    sb.append(this.mIsInMysql);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mIsRead:");
    sb.append(this.mIsRead);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mStartTime:");
    sb.append(this.mStartTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mEndTime:");
    sb.append(this.mEndTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mLatency:");
    sb.append(this.mLatency);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mSplit:");
    sb.append(this.mSplit);
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

  private static class SuccessQueryResultStandardSchemeFactory implements SchemeFactory {
    public SuccessQueryResultStandardScheme getScheme() {
      return new SuccessQueryResultStandardScheme();
    }
  }

  private static class SuccessQueryResultStandardScheme extends StandardScheme<SuccessQueryResult> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SuccessQueryResult struct) throws org.apache.thrift.TException {
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
              struct.mID = iprot.readI32();
              struct.setMIDIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // M_IS_IN_MYSQL
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.mIsInMysql = iprot.readBool();
              struct.setMIsInMysqlIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // M_IS_READ
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.mIsRead = iprot.readBool();
              struct.setMIsReadIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // M_START_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.mStartTime = iprot.readI64();
              struct.setMStartTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // M_END_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.mEndTime = iprot.readI64();
              struct.setMEndTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // M_LATENCY
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.mLatency = iprot.readI64();
              struct.setMLatencyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // M_SPLIT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.mSplit = iprot.readI32();
              struct.setMSplitIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, SuccessQueryResult struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(M_ID_FIELD_DESC);
      oprot.writeI32(struct.mID);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(M_IS_IN_MYSQL_FIELD_DESC);
      oprot.writeBool(struct.mIsInMysql);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(M_IS_READ_FIELD_DESC);
      oprot.writeBool(struct.mIsRead);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(M_START_TIME_FIELD_DESC);
      oprot.writeI64(struct.mStartTime);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(M_END_TIME_FIELD_DESC);
      oprot.writeI64(struct.mEndTime);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(M_LATENCY_FIELD_DESC);
      oprot.writeI64(struct.mLatency);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(M_SPLIT_FIELD_DESC);
      oprot.writeI32(struct.mSplit);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SuccessQueryResultTupleSchemeFactory implements SchemeFactory {
    public SuccessQueryResultTupleScheme getScheme() {
      return new SuccessQueryResultTupleScheme();
    }
  }

  private static class SuccessQueryResultTupleScheme extends TupleScheme<SuccessQueryResult> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SuccessQueryResult struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMID()) {
        optionals.set(0);
      }
      if (struct.isSetMIsInMysql()) {
        optionals.set(1);
      }
      if (struct.isSetMIsRead()) {
        optionals.set(2);
      }
      if (struct.isSetMStartTime()) {
        optionals.set(3);
      }
      if (struct.isSetMEndTime()) {
        optionals.set(4);
      }
      if (struct.isSetMLatency()) {
        optionals.set(5);
      }
      if (struct.isSetMSplit()) {
        optionals.set(6);
      }
      oprot.writeBitSet(optionals, 7);
      if (struct.isSetMID()) {
        oprot.writeI32(struct.mID);
      }
      if (struct.isSetMIsInMysql()) {
        oprot.writeBool(struct.mIsInMysql);
      }
      if (struct.isSetMIsRead()) {
        oprot.writeBool(struct.mIsRead);
      }
      if (struct.isSetMStartTime()) {
        oprot.writeI64(struct.mStartTime);
      }
      if (struct.isSetMEndTime()) {
        oprot.writeI64(struct.mEndTime);
      }
      if (struct.isSetMLatency()) {
        oprot.writeI64(struct.mLatency);
      }
      if (struct.isSetMSplit()) {
        oprot.writeI32(struct.mSplit);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SuccessQueryResult struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(7);
      if (incoming.get(0)) {
        struct.mID = iprot.readI32();
        struct.setMIDIsSet(true);
      }
      if (incoming.get(1)) {
        struct.mIsInMysql = iprot.readBool();
        struct.setMIsInMysqlIsSet(true);
      }
      if (incoming.get(2)) {
        struct.mIsRead = iprot.readBool();
        struct.setMIsReadIsSet(true);
      }
      if (incoming.get(3)) {
        struct.mStartTime = iprot.readI64();
        struct.setMStartTimeIsSet(true);
      }
      if (incoming.get(4)) {
        struct.mEndTime = iprot.readI64();
        struct.setMEndTimeIsSet(true);
      }
      if (incoming.get(5)) {
        struct.mLatency = iprot.readI64();
        struct.setMLatencyIsSet(true);
      }
      if (incoming.get(6)) {
        struct.mSplit = iprot.readI32();
        struct.setMSplitIsSet(true);
      }
    }
  }

}

