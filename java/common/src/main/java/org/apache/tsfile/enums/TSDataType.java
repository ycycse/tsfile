/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.tsfile.enums;

import org.apache.tsfile.write.UnSupportedDataTypeException;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public enum TSDataType {
  /** BOOLEAN. */
  BOOLEAN((byte) 0),

  /** INT32. */
  INT32((byte) 1),

  /** INT64. */
  INT64((byte) 2),

  /** FLOAT. */
  FLOAT((byte) 3),

  /** DOUBLE. */
  DOUBLE((byte) 4),

  /** TEXT. */
  TEXT((byte) 5),

  /** VECTOR. */
  VECTOR((byte) 6),

  /** UNKNOWN. */
  UNKNOWN((byte) 7),

  /** TIMESTAMP. */
  TIMESTAMP((byte) 8),

  /** DATE. */
  DATE((byte) 9),

  /** BLOB. */
  BLOB((byte) 10),

  /** STRING */
  STRING((byte) 11);
  ;

  private final byte type;

  TSDataType(byte type) {
    this.type = type;
  }

  /**
   * give an integer to return a data type.
   *
   * @param type -param to judge enum type
   * @return -enum type
   */
  public static TSDataType deserialize(byte type) {
    return getTsDataType(type);
  }

  public byte getType() {
    return type;
  }

  public static TSDataType getTsDataType(byte type) {
    switch (type) {
      case 0:
        return TSDataType.BOOLEAN;
      case 1:
        return TSDataType.INT32;
      case 2:
        return TSDataType.INT64;
      case 3:
        return TSDataType.FLOAT;
      case 4:
        return TSDataType.DOUBLE;
      case 5:
        return TSDataType.TEXT;
      case 6:
        return TSDataType.VECTOR;
      case 7:
        return TSDataType.UNKNOWN;
      case 8:
        return TSDataType.TIMESTAMP;
      case 9:
        return TSDataType.DATE;
      case 10:
        return TSDataType.BLOB;
      case 11:
        return TSDataType.STRING;
      default:
        throw new IllegalArgumentException("Invalid input: " + type);
    }
  }

  public static TSDataType deserializeFrom(ByteBuffer buffer) {
    return deserialize(buffer.get());
  }

  public static TSDataType deserializeFrom(InputStream stream) throws IOException {
    return deserialize((byte) stream.read());
  }

  public static int getSerializedSize() {
    return Byte.BYTES;
  }

  public void serializeTo(ByteBuffer byteBuffer) {
    byteBuffer.put(serialize());
  }

  public void serializeTo(DataOutputStream outputStream) throws IOException {
    outputStream.write(serialize());
  }

  public void serializeTo(FileOutputStream outputStream) throws IOException {
    outputStream.write(serialize());
  }

  public int getDataTypeSize() {
    switch (this) {
      case BOOLEAN:
        return 1;
      case INT32:
      case FLOAT:
      case DATE:
        return 4;
        // For text: return the size of reference here
      case TEXT:
      case INT64:
      case DOUBLE:
      case VECTOR:
      case BLOB:
      case STRING:
      case TIMESTAMP:
        return 8;
      default:
        throw new UnSupportedDataTypeException(this.toString());
    }
  }

  /**
   * get type byte.
   *
   * @return byte number
   */
  public byte serialize() {
    return type;
  }

  /**
   * numeric datatype judgement.
   *
   * @return whether it is a numeric datatype
   * @throws UnSupportedDataTypeException when meets unSupported DataType
   */
  public boolean isNumeric() {
    switch (this) {
      case INT32:
      case INT64:
      case FLOAT:
      case DOUBLE:
        return true;
        // For text: return the size of reference here
      case BLOB:
      case TIMESTAMP:
      case DATE:
      case STRING:
      case BOOLEAN:
      case TEXT:
      case VECTOR:
        return false;
      default:
        throw new UnSupportedDataTypeException(this.toString());
    }
  }

  /**
   * comparable datatype judgement.
   *
   * @return whether it is a comparable datatype
   * @throws UnSupportedDataTypeException when meets unSupported DataType
   */
  public boolean isComparable() {
    switch (this) {
      case INT32:
      case INT64:
      case FLOAT:
      case DOUBLE:
      case TEXT:
      case BOOLEAN:
      case TIMESTAMP:
      case DATE:
      case STRING:
        return true;
      case VECTOR:
      case BLOB:
        return false;
      default:
        throw new UnSupportedDataTypeException(this.toString());
    }
  }
}
