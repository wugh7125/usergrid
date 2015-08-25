/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.usergrid.persistence.graph.serialization.impl.shard;


import com.netflix.astyanax.model.ConsistencyLevel;


/**
 * Interface for shard consistency levels
 */
public interface ShardConsistency {

    /**
     * Get the consistency level for wiring new shards
     * @return
     */
    ConsistencyLevel getShardWriteConsistency();

    /**
     * Get the consistency level for doing reads
     * @return
     */
    ConsistencyLevel getShardReadConsistency();

    /**
     * Get the consistency level for performing a shard audit
     * @return
     */
    ConsistencyLevel getShardAuditConsistency();
}