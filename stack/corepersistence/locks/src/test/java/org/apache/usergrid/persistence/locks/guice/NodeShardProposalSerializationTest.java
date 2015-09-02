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

package org.apache.usergrid.persistence.locks.guice;


import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.usergrid.persistence.core.guice.MigrationManagerRule;
import org.apache.usergrid.persistence.core.scope.ApplicationScope;
import org.apache.usergrid.persistence.core.test.ITRunner;
import org.apache.usergrid.persistence.core.test.UseModules;
import org.apache.usergrid.persistence.locks.LockId;
import org.apache.usergrid.persistence.locks.impl.LockCandidate;
import org.apache.usergrid.persistence.locks.impl.NodeShardProposalSerialization;
import org.apache.usergrid.persistence.locks.impl.TestLockModule;
import org.apache.usergrid.persistence.model.entity.Id;
import org.apache.usergrid.persistence.model.util.UUIDGenerator;

import com.google.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(ITRunner.class)
@UseModules({ TestLockModule.class })
public class NodeShardProposalSerializationTest {

    private static final Logger log = LoggerFactory.getLogger( NodeShardProposalSerializationTest.class );

    @Inject
    @Rule
    public MigrationManagerRule migrationManagerRule;


    @Inject
    protected NodeShardProposalSerialization serialization;


    protected ApplicationScope scope;

    protected final AtomicLong atomicLong = new AtomicLong(  );


    private static final int ONE_HOUR_TTL = 360;

    @Before
    public void setup() {
        scope = mock( ApplicationScope.class );

        Id orgId = mock( Id.class );

        when( orgId.getType() ).thenReturn( "organization" );
        when( orgId.getUuid() ).thenReturn( UUIDGenerator.newTimeUUID() );

        when( scope.getApplication() ).thenReturn( orgId );




    }

    @Test
    public void testOnlyLock(){

        final LockId testId = createLockId();
        final UUID proposed = UUIDGenerator.newTimeUUID();



        final LockCandidate candidate =  serialization.writeNewValue( testId, proposed, ONE_HOUR_TTL );


        assertNotNull(candidate);

        assertTrue(proposed, candidate.isFirst(proposed));
    }

    private LockId createLockId(){

        LockId lockId = mock(LockId.class);
                 //mock up scope
                 when( lockId.getApplicationScope()).thenReturn( scope );

                 when( lockId.generateKey()).thenReturn( atomicLong.incrementAndGet() + "" );

        return lockId;

    }
}
