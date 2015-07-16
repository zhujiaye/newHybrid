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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-7-15")
public class TenantResult implements org.apache.thrift.TBase<TenantResult, TenantResult._Fields>, java.io.Serializable, Cloneable, Comparable<TenantResult> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TenantResult");

  private static final org.apache.thrift.protocol.TField M_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("mID", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField M_SLO_FIELD_DESC = new org.apache.thrift.protocol.TField("mSLO", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField M_DATA_SIZE_FIELD_DESC = new org.apache.thrift.protocol.TField("mDataSize", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField M_WH_FIELD_DESC = new org.apache.thrift.protocol.TField("mWH", org.apache.thrift.protocol.TType.I32, (short)4);
  private static final org.apache.thrift.protocol.TField M_SPLIT_RESULTS_FIELD_DESC = new org.apache.thrift.protocol.TField("mSplitResults", org.apache.thrift.protocol.TType.LIST, (short)5);
  private static final org.apache.thrift.protocol.TField M_QUERY_RESULTS_FIELD_DESC = new org.apache.thrift.protocol.TField("mQueryResults", org.apache.thrift.protocol.TType.LIST, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TenantResultStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TenantResultTupleSchemeFactory());
  }

  public int mID; // required
  public int mSLO; // required
  public int mDataSize; // required
  public int mWH; // required
  public List<SplitResult> mSplitResults; // required
  public List<SuccessQueryResult> mQueryResults; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    M_ID((short)1, "mID"),
    M_SLO((short)2, "mSLO"),
    M_DATA_SIZE((short)3, "mDataSize"),
    M_WH((short)4, "mWH"),
    M_SPLIT_RESULTS((short)5, "mSplitResults"),
    M_QUERY_RESULTS((short)6, "mQueryResults");

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
        case 2: // M_SLO
          return M_SLO;
        case 3: // M_DATA_SIZE
          return M_DATA_SIZE;
        case 4: // M_WH
          return M_WH;
        case 5: // M_SPLIT_RESULTS
          return M_SPLIT_RESULTS;
        case 6: // M_QUERY_RESULTS
          return M_QUERY_RESULTS;
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
  private static final int __MSLO_ISSET_ID = 1;
  private static final int __MDATASIZE_ISSET_ID = 2;
  private static final int __MWH_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.M_ID, new org.apache.thrift.meta_data.FieldMetaData("mID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.M_SLO, new org.apache.thrift.meta_data.FieldMetaData("mSLO", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.M_DATA_SIZE, new org.apache.thrift.meta_data.FieldMetaData("mDataSize", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.M_WH, new org.apache.thrift.meta_data.FieldMetaData("mWH", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.M_SPLIT_RESULTS, new org.apache.thrift.meta_data.FieldMetaData("mSplitResults", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, SplitResult.class))));
    tmpMap.put(_Fields.M_QUERY_RESULTS, new org.apache.thrift.meta_data.FieldMetaData("mQueryResults", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, SuccessQueryResult.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TenantResult.class, metaDataMap);
  }

  public TenantResult() {
  }

  public TenantResult(
    int mID,
    int mSLO,
    int mDataSize,
    int mWH,
    List<SplitResult> mSplitResults,
    List<SuccessQueryResult> mQueryResults)
  {
    this();
    this.mID = mID;
    setMIDIsSet(true);
    this.mSLO = mSLO;
    setMSLOIsSet(true);
    this.mDataSize = mDataSize;
    setMDataSizeIsSet(true);
    this.mWH = mWH;
    setMWHIsSet(true);
    this.mSplitResults = mSplitResults;
    this.mQueryResults = mQueryResults;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TenantResult(TenantResult other) {
    __isset_bitfield = other.__isset_bitfield;
    this.mID = other.mID;
    this.mSLO = other.mSLO;
    this.mDataSize = other.mDataSize;
    this.mWH = other.mWH;
    if (other.isSetMSplitResults()) {
      List<SplitResult> __this__mSplitResults = new ArrayList<SplitResult>(other.mSplitResults.size());
      for (SplitResult other_element : other.mSplitResults) {
        __this__mSplitResults.add(new SplitResult(other_element));
      }
      this.mSplitResults = __this__mSplitResults;
    }
    if (other.isSetMQueryResults()) {
      List<SuccessQueryResult> __this__mQueryResults = new ArrayList<SuccessQueryResult>(other.mQueryResults.size());
      for (SuccessQueryResult other_element : other.mQueryResults) {
        __this__mQueryResults.add(new SuccessQueryResult(other_element));
      }
      this.mQueryResults = __this__mQueryResults;
    }
  }

  public TenantResult deepCopy() {
    return new TenantResult(this);
  }

  @Override
  public void clear() {
    setMIDIsSet(false);
    this.mID = 0;
    setMSLOIsSet(false);
    this.mSLO = 0;
    setMDataSizeIsSet(false);
    this.mDataSize = 0;
    setMWHIsSet(false);
    this.mWH = 0;
    this.mSplitResults = null;
    this.mQueryResults = null;
  }

  public int getMID() {
    return this.mID;
  }

  public TenantResult setMID(int mID) {
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

  public int getMSLO() {
    return this.mSLO;
  }

  public TenantResult setMSLO(int mSLO) {
    this.mSLO = mSLO;
    setMSLOIsSet(true);
    return this;
  }

  public void unsetMSLO() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MSLO_ISSET_ID);
  }

  /** Returns true if field mSLO is set (has been assigned a value) and false otherwise */
  public boolean isSetMSLO() {
    return EncodingUtils.testBit(__isset_bitfield, __MSLO_ISSET_ID);
  }

  public void setMSLOIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MSLO_ISSET_ID, value);
  }

  public int getMDataSize() {
    return this.mDataSize;
  }

  public TenantResult setMDataSize(int mDataSize) {
    this.mDataSize = mDataSize;
    setMDataSizeIsSet(true);
    return this;
  }

  public void unsetMDataSize() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MDATASIZE_ISSET_ID);
  }

  /** Returns true if field mDataSize is set (has been assigned a value) and false otherwise */
  public boolean isSetMDataSize() {
    return EncodingUtils.testBit(__isset_bitfield, __MDATASIZE_ISSET_ID);
  }

  public void setMDataSizeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MDATASIZE_ISSET_ID, value);
  }

  public int getMWH() {
    return this.mWH;
  }

  public TenantResult setMWH(int mWH) {
    this.mWH = mWH;
    setMWHIsSet(true);
    return this;
  }

  public void unsetMWH() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MWH_ISSET_ID);
  }

  /** Returns true if field mWH is set (has been assigned a value) and false otherwise */
  public boolean isSetMWH() {
    return EncodingUtils.testBit(__isset_bitfield, __MWH_ISSET_ID);
  }

  public void setMWHIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MWH_ISSET_ID, value);
  }

  public int getMSplitResultsSize() {
    return (this.mSplitResults == null) ? 0 : this.mSplitResults.size();
  }

  public java.util.Iterator<SplitResult> getMSplitResultsIterator() {
    return (this.mSplitResults == null) ? null : this.mSplitResults.iterator();
  }

  public void addToMSplitResults(SplitResult elem) {
    if (this.mSplitResults == null) {
      this.mSplitResults = new ArrayList<SplitResult>();
    }
    this.mSplitResults.add(elem);
  }

  public List<SplitResult> getMSplitResults() {
    return this.mSplitResults;
  }

  public TenantResult setMSplitResults(List<SplitResult> mSplitResults) {
    this.mSplitResults = mSplitResults;
    return this;
  }

  public void unsetMSplitResults() {
    this.mSplitResults = null;
  }

  /** Returns true if field mSplitResults is set (has been assigned a value) and false otherwise */
  public boolean isSetMSplitResults() {
    return this.mSplitResults != null;
  }

  public void setMSplitResultsIsSet(boolean value) {
    if (!value) {
      this.mSplitResults = null;
    }
  }

  public int getMQueryResultsSize() {
    return (this.mQueryResults == null) ? 0 : this.mQueryResults.size();
  }

  public java.util.Iterator<SuccessQueryResult> getMQueryResultsIterator() {
    return (this.mQueryResults == null) ? null : this.mQueryResults.iterator();
  }

  public void addToMQueryResults(SuccessQueryResult elem) {
    if (this.mQueryResults == null) {
      this.mQueryResults = new ArrayList<SuccessQueryResult>();
    }
    this.mQueryResults.add(elem);
  }

  public List<SuccessQueryResult> getMQueryResults() {
    return this.mQueryResults;
  }

  public TenantResult setMQueryResults(List<SuccessQueryResult> mQueryResults) {
    this.mQueryResults = mQueryResults;
    return this;
  }

  public void unsetMQueryResults() {
    this.mQueryResults = null;
  }

  /** Returns true if field mQueryResults is set (has been assigned a value) and false otherwise */
  public boolean isSetMQueryResults() {
    return this.mQueryResults != null;
  }

  public void setMQueryResultsIsSet(boolean value) {
    if (!value) {
      this.mQueryResults = null;
    }
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

    case M_SLO:
      if (value == null) {
        unsetMSLO();
      } else {
        setMSLO((Integer)value);
      }
      break;

    case M_DATA_SIZE:
      if (value == null) {
        unsetMDataSize();
      } else {
        setMDataSize((Integer)value);
      }
      break;

    case M_WH:
      if (value == null) {
        unsetMWH();
      } else {
        setMWH((Integer)value);
      }
      break;

    case M_SPLIT_RESULTS:
      if (value == null) {
        unsetMSplitResults();
      } else {
        setMSplitResults((List<SplitResult>)value);
      }
      break;

    case M_QUERY_RESULTS:
      if (value == null) {
        unsetMQueryResults();
      } else {
        setMQueryResults((List<SuccessQueryResult>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case M_ID:
      return Integer.valueOf(getMID());

    case M_SLO:
      return Integer.valueOf(getMSLO());

    case M_DATA_SIZE:
      return Integer.valueOf(getMDataSize());

    case M_WH:
      return Integer.valueOf(getMWH());

    case M_SPLIT_RESULTS:
      return getMSplitResults();

    case M_QUERY_RESULTS:
      return getMQueryResults();

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
    case M_SLO:
      return isSetMSLO();
    case M_DATA_SIZE:
      return isSetMDataSize();
    case M_WH:
      return isSetMWH();
    case M_SPLIT_RESULTS:
      return isSetMSplitResults();
    case M_QUERY_RESULTS:
      return isSetMQueryResults();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TenantResult)
      return this.equals((TenantResult)that);
    return false;
  }

  public boolean equals(TenantResult that) {
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

    boolean this_present_mSLO = true;
    boolean that_present_mSLO = true;
    if (this_present_mSLO || that_present_mSLO) {
      if (!(this_present_mSLO && that_present_mSLO))
        return false;
      if (this.mSLO != that.mSLO)
        return false;
    }

    boolean this_present_mDataSize = true;
    boolean that_present_mDataSize = true;
    if (this_present_mDataSize || that_present_mDataSize) {
      if (!(this_present_mDataSize && that_present_mDataSize))
        return false;
      if (this.mDataSize != that.mDataSize)
        return false;
    }

    boolean this_present_mWH = true;
    boolean that_present_mWH = true;
    if (this_present_mWH || that_present_mWH) {
      if (!(this_present_mWH && that_present_mWH))
        return false;
      if (this.mWH != that.mWH)
        return false;
    }

    boolean this_present_mSplitResults = true && this.isSetMSplitResults();
    boolean that_present_mSplitResults = true && that.isSetMSplitResults();
    if (this_present_mSplitResults || that_present_mSplitResults) {
      if (!(this_present_mSplitResults && that_present_mSplitResults))
        return false;
      if (!this.mSplitResults.equals(that.mSplitResults))
        return false;
    }

    boolean this_present_mQueryResults = true && this.isSetMQueryResults();
    boolean that_present_mQueryResults = true && that.isSetMQueryResults();
    if (this_present_mQueryResults || that_present_mQueryResults) {
      if (!(this_present_mQueryResults && that_present_mQueryResults))
        return false;
      if (!this.mQueryResults.equals(that.mQueryResults))
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

    boolean present_mSLO = true;
    list.add(present_mSLO);
    if (present_mSLO)
      list.add(mSLO);

    boolean present_mDataSize = true;
    list.add(present_mDataSize);
    if (present_mDataSize)
      list.add(mDataSize);

    boolean present_mWH = true;
    list.add(present_mWH);
    if (present_mWH)
      list.add(mWH);

    boolean present_mSplitResults = true && (isSetMSplitResults());
    list.add(present_mSplitResults);
    if (present_mSplitResults)
      list.add(mSplitResults);

    boolean present_mQueryResults = true && (isSetMQueryResults());
    list.add(present_mQueryResults);
    if (present_mQueryResults)
      list.add(mQueryResults);

    return list.hashCode();
  }

  @Override
  public int compareTo(TenantResult other) {
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
    lastComparison = Boolean.valueOf(isSetMSLO()).compareTo(other.isSetMSLO());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMSLO()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mSLO, other.mSLO);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMDataSize()).compareTo(other.isSetMDataSize());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMDataSize()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mDataSize, other.mDataSize);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMWH()).compareTo(other.isSetMWH());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMWH()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mWH, other.mWH);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMSplitResults()).compareTo(other.isSetMSplitResults());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMSplitResults()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mSplitResults, other.mSplitResults);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMQueryResults()).compareTo(other.isSetMQueryResults());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMQueryResults()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mQueryResults, other.mQueryResults);
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
    StringBuilder sb = new StringBuilder("TenantResult(");
    boolean first = true;

    sb.append("mID:");
    sb.append(this.mID);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mSLO:");
    sb.append(this.mSLO);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mDataSize:");
    sb.append(this.mDataSize);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mWH:");
    sb.append(this.mWH);
    first = false;
    if (!first) sb.append(", ");
    sb.append("mSplitResults:");
    if (this.mSplitResults == null) {
      sb.append("null");
    } else {
      sb.append(this.mSplitResults);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mQueryResults:");
    if (this.mQueryResults == null) {
      sb.append("null");
    } else {
      sb.append(this.mQueryResults);
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

  private static class TenantResultStandardSchemeFactory implements SchemeFactory {
    public TenantResultStandardScheme getScheme() {
      return new TenantResultStandardScheme();
    }
  }

  private static class TenantResultStandardScheme extends StandardScheme<TenantResult> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TenantResult struct) throws org.apache.thrift.TException {
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
          case 2: // M_SLO
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.mSLO = iprot.readI32();
              struct.setMSLOIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // M_DATA_SIZE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.mDataSize = iprot.readI32();
              struct.setMDataSizeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // M_WH
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.mWH = iprot.readI32();
              struct.setMWHIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // M_SPLIT_RESULTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.mSplitResults = new ArrayList<SplitResult>(_list0.size);
                SplitResult _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = new SplitResult();
                  _elem1.read(iprot);
                  struct.mSplitResults.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setMSplitResultsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // M_QUERY_RESULTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list3 = iprot.readListBegin();
                struct.mQueryResults = new ArrayList<SuccessQueryResult>(_list3.size);
                SuccessQueryResult _elem4;
                for (int _i5 = 0; _i5 < _list3.size; ++_i5)
                {
                  _elem4 = new SuccessQueryResult();
                  _elem4.read(iprot);
                  struct.mQueryResults.add(_elem4);
                }
                iprot.readListEnd();
              }
              struct.setMQueryResultsIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, TenantResult struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(M_ID_FIELD_DESC);
      oprot.writeI32(struct.mID);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(M_SLO_FIELD_DESC);
      oprot.writeI32(struct.mSLO);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(M_DATA_SIZE_FIELD_DESC);
      oprot.writeI32(struct.mDataSize);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(M_WH_FIELD_DESC);
      oprot.writeI32(struct.mWH);
      oprot.writeFieldEnd();
      if (struct.mSplitResults != null) {
        oprot.writeFieldBegin(M_SPLIT_RESULTS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.mSplitResults.size()));
          for (SplitResult _iter6 : struct.mSplitResults)
          {
            _iter6.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.mQueryResults != null) {
        oprot.writeFieldBegin(M_QUERY_RESULTS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.mQueryResults.size()));
          for (SuccessQueryResult _iter7 : struct.mQueryResults)
          {
            _iter7.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TenantResultTupleSchemeFactory implements SchemeFactory {
    public TenantResultTupleScheme getScheme() {
      return new TenantResultTupleScheme();
    }
  }

  private static class TenantResultTupleScheme extends TupleScheme<TenantResult> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TenantResult struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetMID()) {
        optionals.set(0);
      }
      if (struct.isSetMSLO()) {
        optionals.set(1);
      }
      if (struct.isSetMDataSize()) {
        optionals.set(2);
      }
      if (struct.isSetMWH()) {
        optionals.set(3);
      }
      if (struct.isSetMSplitResults()) {
        optionals.set(4);
      }
      if (struct.isSetMQueryResults()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetMID()) {
        oprot.writeI32(struct.mID);
      }
      if (struct.isSetMSLO()) {
        oprot.writeI32(struct.mSLO);
      }
      if (struct.isSetMDataSize()) {
        oprot.writeI32(struct.mDataSize);
      }
      if (struct.isSetMWH()) {
        oprot.writeI32(struct.mWH);
      }
      if (struct.isSetMSplitResults()) {
        {
          oprot.writeI32(struct.mSplitResults.size());
          for (SplitResult _iter8 : struct.mSplitResults)
          {
            _iter8.write(oprot);
          }
        }
      }
      if (struct.isSetMQueryResults()) {
        {
          oprot.writeI32(struct.mQueryResults.size());
          for (SuccessQueryResult _iter9 : struct.mQueryResults)
          {
            _iter9.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TenantResult struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.mID = iprot.readI32();
        struct.setMIDIsSet(true);
      }
      if (incoming.get(1)) {
        struct.mSLO = iprot.readI32();
        struct.setMSLOIsSet(true);
      }
      if (incoming.get(2)) {
        struct.mDataSize = iprot.readI32();
        struct.setMDataSizeIsSet(true);
      }
      if (incoming.get(3)) {
        struct.mWH = iprot.readI32();
        struct.setMWHIsSet(true);
      }
      if (incoming.get(4)) {
        {
          org.apache.thrift.protocol.TList _list10 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.mSplitResults = new ArrayList<SplitResult>(_list10.size);
          SplitResult _elem11;
          for (int _i12 = 0; _i12 < _list10.size; ++_i12)
          {
            _elem11 = new SplitResult();
            _elem11.read(iprot);
            struct.mSplitResults.add(_elem11);
          }
        }
        struct.setMSplitResultsIsSet(true);
      }
      if (incoming.get(5)) {
        {
          org.apache.thrift.protocol.TList _list13 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.mQueryResults = new ArrayList<SuccessQueryResult>(_list13.size);
          SuccessQueryResult _elem14;
          for (int _i15 = 0; _i15 < _list13.size; ++_i15)
          {
            _elem14 = new SuccessQueryResult();
            _elem14.read(iprot);
            struct.mQueryResults.add(_elem14);
          }
        }
        struct.setMQueryResultsIsSet(true);
      }
    }
  }

}

