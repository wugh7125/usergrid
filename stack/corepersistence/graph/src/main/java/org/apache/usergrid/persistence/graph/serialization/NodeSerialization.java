/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.usergrid.persistence.graph.serialization;


import java.util.Iterator;
import java.util.UUID;

import org.apache.usergrid.persistence.collection.OrganizationScope;
import org.apache.usergrid.persistence.graph.Edge;
import org.apache.usergrid.persistence.graph.MarkedEdge;
import org.apache.usergrid.persistence.graph.SearchByEdge;
import org.apache.usergrid.persistence.graph.SearchByEdgeType;
import org.apache.usergrid.persistence.graph.SearchByIdType;
import org.apache.usergrid.persistence.model.entity.Id;

import com.google.common.base.Optional;
import com.netflix.astyanax.MutationBatch;


/**
 * Simple interface for serializing node information for mark/sweep
 */
public interface NodeSerialization {


    /**
     * Mark the node as a pending delete.
     *
     * @param scope The org scope of the graph
     * @param node The node to mark
     * @param version The version to mark for deletion
     */
    MutationBatch mark( OrganizationScope scope, Id node, UUID version );


    /**
     * Delete the mark entry, signaling a delete is complete
     * @param scope
     * @param node
     * @return
     */
    MutationBatch delete( OrganizationScope scope, Id node, UUID version );

    /**
     * Get the maximum version of a node marked for deletion.  If the node has no mark
     * the optional will return empty
     * @param scope The scope to search in
     * @param nodeId The node id
     * @return The optional uuid.  If none is present, the node is not currently marked
     */
    Optional<UUID> getMaxVersion(OrganizationScope scope, Id nodeId);
}