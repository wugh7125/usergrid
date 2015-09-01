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

package org.apache.usergrid.persistence.locks.impl;


import java.util.UUID;

import org.apache.usergrid.persistence.locks.LockId;


/**
 * Interface for serializing node shard proposals
 */
public interface NodeShardProposalSerialization {


    /**
     * Propose a new shard and return the UUID of the proposal
     * @param lockId The key for the locks
     * @param proposed The proposed time uuid key
     * @param expirationInSeconds The time to allow the proposal to live.
     */
    void writeNewValue( final LockId lockId, final UUID proposed, final int expirationInSeconds );

    /**
     * Get the proposed locks from the proposed value
     * @param lockId The key for the locks
     * @return
     */
    Proposal getProposal(  final LockId lockId);

    /**
     * Remove all the proposals
     * @param lockId The key for the locks
     * @param proposed The proposed value
     */
    void delete(  final LockId lockId, final UUID proposed );
}
