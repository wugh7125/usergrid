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
import java.util.concurrent.TimeUnit;

import org.apache.usergrid.persistence.locks.Lock;
import org.apache.usergrid.persistence.locks.LockId;
import org.apache.usergrid.persistence.model.util.UUIDGenerator;

import com.google.common.base.Preconditions;


/**
 * Lock that uses cassandra for multiple regions
 */
public class CassandraMultiRegionLock implements Lock {

    private final LockId lockId;
    private final NodeShardProposalSerialization nodeShardProposalSerialization;
    private final UUID lockUUID;


    public CassandraMultiRegionLock(final LockId lockId,
                          final NodeShardProposalSerialization nodeShardProposalSerialization ) {
        this.lockId = lockId;
        this.nodeShardProposalSerialization = nodeShardProposalSerialization;
        this.lockUUID = UUIDGenerator.newTimeUUID();
    }


    @Override
    public boolean tryLock( final long timeToLive, final TimeUnit timeUnit ) {

        final long expiration = timeUnit.toSeconds( timeToLive );

        Preconditions.checkArgument(expiration > Integer.MAX_VALUE, "Expiration cannot be longer than "  + Integer.MAX_VALUE);

        this.nodeShardProposalSerialization.writeNewValue( lockId, lockUUID, ( int ) expiration );

        //now read back our proposal

        final LockCandidate lockCandidate = nodeShardProposalSerialization.getProposedLock( lockId );






        return false;
    }


    @Override
    public void unlock() {

    }
}
