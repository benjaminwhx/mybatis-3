/**
 *    Copyright 2009-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.executor.statement;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility for {@link java.sql.Statement}.
 *
 * @since 3.4.0
 * @author Kazuki Shimizu
 */
public class StatementUtil {

  private StatementUtil() {
    // NOP
  }

  /**
   * 如果存在事务超时配置，重新设置queryTimeout
   * 1、没有配置查询超时，设置查询超时为事务存活时间
   * 2、如果配置了查询超时，但是事务存活时间 < 查询超时，设置事务存活时间
   *
   * @param statement 表达式
   * @param queryTimeout 查询超时时间
   * @param transactionTimeout 事务超时（注意：在mybatis-spring的事务中，这个参数代表的不是事务的超时时间，而是事务中剩下可存活的时间）
   * @throws SQLException if a database access error occurs, this method is called on a closed <code>Statement</code>
   */
  public static void applyTransactionTimeout(Statement statement, Integer queryTimeout, Integer transactionTimeout) throws SQLException {
    if (transactionTimeout == null){
      return;
    }
    // 剩余查询超时时间
    Integer timeToLiveOfQuery = null;
    if (queryTimeout == null || queryTimeout == 0) {
      timeToLiveOfQuery = transactionTimeout;
    } else if (transactionTimeout < queryTimeout) {
      timeToLiveOfQuery = transactionTimeout;
    }
    if (timeToLiveOfQuery != null) {
      statement.setQueryTimeout(timeToLiveOfQuery);
    }
  }

}
